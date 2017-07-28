package com.kodgames.club.service.gmtools;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.server.ServerService;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.message.proto.club.ClubProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ninlgde on 2017/3/18.
 */
public class ClubGmToolsCheckCreateService extends PublicService {

    private static final Logger logger = LoggerFactory.getLogger(ClubGmToolsCheckCreateService.class);
    private static final long serialVersionUID = 5179254773164786153L;
    private AtomicInteger messageFlag = new AtomicInteger(1);
    private Map<Integer, Map<String, Object>> uniqueMessageMap = new ConcurrentHashMap<>();

    public void sendCheckRoleRequest(Map<String, Object>  json, KodHttpMessage message)
    {
        int seqId = messageFlag.getAndIncrement();
        Map<String, Object> data = new HashMap<>();
        data.put("json", json);
        data.put("msg", message);
        uniqueMessageMap.put(seqId, data);

        int roleId = (int)json.get("roleId");

        ClubProtoBuf.CLGCheckRoleInfoREQ.Builder builder = ClubProtoBuf.CLGCheckRoleInfoREQ.newBuilder();
        builder.setSeqId(seqId);
        builder.setRoleId(roleId);

        ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
        serverService.getGameConnection().write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
    }

    public void doCheckRoleResponse(ClubProtoBuf.GCLCheckRoleInfoRES message){
        int seqId = message.getSeqId();
        String result = null;
        // 唯一序号不存在，返回
        if (!uniqueMessageMap.containsKey(seqId))
        {
            logger.error("check role error, seqId", seqId);
            return;
        }

        KodHttpMessage kodHttpMessage = (KodHttpMessage)uniqueMessageMap.get(seqId).get("msg");
        Map<String, Object>  json = (HashMap<String, Object>)uniqueMessageMap.get(seqId).get("json");
        uniqueMessageMap.remove(seqId);
        ClubGmToolsService gmtService= ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);

        do{
            int roleId = message.getRoleId();
            if (roleId <= 0) {
                // 用户不存在
                result = createJsonObj(ClubConstants.CLUB_GMT_ERROR.CREATE_CLUB_ERROR_ROLEID_INVALID);
                break;
            }
            int managerId = roleId;
            int managerValid = gmtService.checkManagerValid(managerId);
            if (managerValid < ClubConstants.CLUB_GMT_ERROR.SUCCESS) {
                result = createJsonObj(managerValid);
                break;
            }
            String handler = (String)json.get("handler");
            if (handler.equals("ClubGmtCheckCreateHandler")) {
                result = createJsonObj(ClubConstants.CLUB_GMT_ERROR.SUCCESS);
            } else if (handler.equals("ClubGmtCreateNewClubHandler")) {
                int agentId = (int)json.get("agentId");
                String nickName = message.getName();
                String clubName = (String)json.get("clubName");
                int data = gmtService.createNewClub(agentId, managerId, nickName, clubName);
                result = createJsonObj(data);
            }
        } while (false);

        kodHttpMessage.sendResponseAndClose(result);
    }


    /**
     * 生成返回给gmt的结果（JSON串）
     *
     * @param content
     * @return
     */
    private String createJsonObj(int content)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"result\":1,\"data\":");
        sb.append(content);
        sb.append("}");
        return sb.toString();
    }
}
