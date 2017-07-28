package com.kodgames.game.service.mobileBind;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.game.service.gmtools.SetBindMobileDiamondHandler;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.game.GameProtoBuf.GCBindMobileRES;
import com.kodgames.message.proto.game.GameProtoBuf.GCBindValidRES;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;
import com.kodgames.message.proto.game.GameProtoBuf.GCSendBindCodeRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import sun.misc.BASE64Encoder;
import xbean.DiamondMobileBindBean;
import xbean.IdMobileBean;
import xbean.MobileIdBean;
import xbean.RoleInfo;

/**
 * 绑定手机服务类
 * 
 * @author 张斌
 */
public class MobileBindService extends PublicService
{
	private static final long serialVersionUID = 7529910377006446412L;

	private static final Logger logger = LoggerFactory.getLogger(SetBindMobileDiamondHandler.class);

	/**
	 * 平台配置的四个常量
	 */
	private static final String ACCOUNT_SID = "5d1ec2925827135315977b8559461f84";
	private static final String AUTH_TOKEN = "79e3718dbb4f1451f33b4e43e70d7991";
	private static final String APP_ID;
	private static final String TEMPLATE_ID;

	static
	{
		Properties pro = new Properties();
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(MobileBindService.class.getClassLoader().getResource("./mobile.properties").getPath());
			pro.load(in);
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		APP_ID = pro.getProperty("APP_ID");
		TEMPLATE_ID = pro.getProperty("TEMPLATE_ID");
		System.out.println(APP_ID);
	}

	private static final String VERSION = "2014-06-30";

	/**
	 * 发送验证码是否成功
	 */
	private static final int SEND_SUCCESS = 0;
	private static final int SEND_FAIL = 1;

	private static CloseableHttpClient httpClient = null;

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 时间格式化
	 */
	private static SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 回复客户端的验证状态，共四种
	 */
	private static final String[] validStatus = {"SUCCESS", "SEND", "OVER_TIME", "ERROR"};

	/**
	 * 验证码超时时间，设为3分钟
	 */
	private static final int Minute = 3;
	private static final long Over_Time = Minute * 60 * 1000;

	/**
	 * 客户端请求间隔时间为1分钟，云之讯接口请求间隔为30秒
	 */
	private static final long Interal_Time = 30 * 1000;

	/**
	 * 获取成功状态，用于遍历清空Mobile_id_table中status != SUCCESS的数据
	 */
	public static final String getNormalStatus()
	{
		return MobileBindService.validStatus[0];
	}

	/**
	 * 获取id是否绑定手机
	 * 
	 * @param connection 连接
	 * @param roleId 用户id
	 * @callBack 回调
	 */
	public void onGetHasBind(Connection connection, int callBack)
	{
		int roleId = connection.getRemotePeerID();
		// id-phone table
		IdMobileBean idMobileBean = table.Id_mobile_table.select(roleId);

		// 获取绑定可得钻石数
		DiamondMobileBindBean diamondMobileBindBean =
			table.Diamond_mobilebind_table.update(GlobalService.getDiamondKey());

		if (diamondMobileBindBean == null)
		{
			diamondMobileBindBean = table.Diamond_mobilebind_table.insert(GlobalService.getDiamondKey());
			diamondMobileBindBean.setDiamond(GlobalService.getDiamondNum());
		}

		GCBindMobileRES.Builder bindMobileRES = GCBindMobileRES.newBuilder();

		// 表中无数据，未绑定手机号
		if (idMobileBean == null)
		{
			connection.write(callBack,
				bindMobileRES.setResult(PlatformProtocolsConfig.GC_BIND_MOBILE_NO)
					.setPhoneNumber("")
					.setDiamondNum(diamondMobileBindBean.getDiamond())
					.build());
		}
		else
		{
			connection.write(callBack,
				bindMobileRES.setResult(PlatformProtocolsConfig.GC_BIND_MOBILE_YES)
					.setPhoneNumber(idMobileBean.getAccountMobile())
					.setDiamondNum(idMobileBean.getBindAward())
					.build());
		}

		return;
	}

	/**
	 * 获取验证码
	 * 
	 * @param connection 连接
	 * @param roleId 用户id
	 * @param phoneNumber 要绑定的手机号
	 * @callBack 回调
	 */
	public void onSendBindCode(Connection connection, String phoneNumber, int callBack)
	{
		int roleId = connection.getRemotePeerID();
		GCSendBindCodeRES.Builder sendBindCodeRES = GCSendBindCodeRES.newBuilder();
		
		//手机号非法
		if(!isPhoneLegal(phoneNumber))
		{
			connection.write(callBack,
				sendBindCodeRES.setResult(PlatformProtocolsConfig.GC_SEND_BIND_CODE_FAIL)
					.setValidTime(Minute)
					.build());
			return;
		}

		// 获取Mobile_id_table对应记录
		MobileIdBean mobileIdBean = table.Mobile_id_table.update(phoneNumber);
		if (mobileIdBean == null)
		{
			mobileIdBean = table.Mobile_id_table.insert(phoneNumber);
			mobileIdBean.setCodeTime(0);
		}

		// 距离上次验证时间必须大于Interal_Time
		if (System.currentTimeMillis() - mobileIdBean.getCodeTime() <= Interal_Time)
		{
			connection.write(callBack,
				sendBindCodeRES.setResult(PlatformProtocolsConfig.GC_SEND_BIND_CODE_LIMIT)
					.setValidTime(Minute)
					.build());

			return;
		}

		// 当前用户状态不能为已验证成功
		if (mobileIdBean.getStatus().equals(validStatus[0]))
		{
			connection.write(callBack,
				sendBindCodeRES.setResult(PlatformProtocolsConfig.GC_SEND_BIND_CODE_BIND_BEFORE)
					.setValidTime(Minute)
					.build());

			return;
		}

		mobileIdBean.setAccountId(roleId);
		
		// 生成验证码,验证码不可相同
		String validCode = "";
		do
		{
			DecimalFormat decimalFormat = new DecimalFormat("000000");
			validCode = decimalFormat.format(Math.random() * 1000000);
		} while (validCode.equals(mobileIdBean.getCode()));

		// 调用云之讯平台接口向手机发送验证码
		// 30秒内不能给同一号码发送相同模板消息; 30秒内不能连续发同样的内容;验证码短信参数长度不能超过10位
		int codeStatus = sendMessage(phoneNumber, validCode, Minute);

		if (codeStatus == SEND_SUCCESS)
		{
			mobileIdBean.setCode(validCode);
			mobileIdBean.setCodeTime(System.currentTimeMillis());
			mobileIdBean.setStatus(validStatus[1]);

			connection.write(callBack,
				sendBindCodeRES.setResult(PlatformProtocolsConfig.GC_SEND_BIND_CODE_SUCCESS)
					.setValidTime(Minute)
					.build());
		}
		else
		{
			connection.write(callBack,
				sendBindCodeRES.setResult(PlatformProtocolsConfig.GC_SEND_BIND_CODE_FAIL).setValidTime(Minute).build());
		}

		return;
	}

	/**
	 * 判断验证码有效性
	 * 
	 * @param connection 连接
	 * @param roleId 用户id
	 * @param phoneNumber 手机号
	 * @param validCode 验证码
	 * @param callBack 回调
	 */
	public void onGetBindVaild(Connection connection, String phoneNumber, String validCode, int callBack)
	{ 
		int roleId = connection.getRemotePeerID();
		MobileIdBean mobileIdBean = table.Mobile_id_table.update(phoneNumber);

		GCBindValidRES.Builder bindValidRES = GCBindValidRES.newBuilder();

		// 获取绑定可得钻石数
		DiamondMobileBindBean diamondMobileBindBean =
			table.Diamond_mobilebind_table.select(GlobalService.getDiamondKey());

		// 当前时间（ms）
		long now = System.currentTimeMillis();
		
		//手机号非法
		if(!isPhoneLegal(phoneNumber))
		{
			connection.write(callBack,
				bindValidRES.setResult(PlatformProtocolsConfig.GC_BIND_VAILD_ERROR).setDiamondNum(diamondMobileBindBean.getDiamond()).build());
			return;
		}
		
		if (mobileIdBean == null)
		{
			connection.write(callBack, bindValidRES.setResult(PlatformProtocolsConfig.GC_NOT_GET_CODE).setDiamondNum(diamondMobileBindBean.getDiamond()).build());
			return;
		}
		
		// 当前用户状态不能为已验证成功,同手机号非法相同处理
		if (mobileIdBean.getStatus().equals(validStatus[0]))
		{
			connection.write(callBack,
				bindValidRES.setResult(PlatformProtocolsConfig.GC_BIND_VAILD_ERROR).setDiamondNum(diamondMobileBindBean.getDiamond()).build());
			return;
		}

		logger.debug("当前存储验证码：" + mobileIdBean.getCode());
		logger.debug("客户端请求验证码：" + validCode);
		logger.debug("验证码获取时间：" + (now - mobileIdBean.getCodeTime()));
		logger.debug("超时时间：" + Over_Time);

		// 判断验证码是否正确
		if (mobileIdBean.getCode().equals(validCode))
		{
			// 判断验证码是否超时
			if (now - mobileIdBean.getCodeTime() > Over_Time)
			{
				mobileIdBean.setStatus(validStatus[2]);

				connection.write(callBack,
					bindValidRES.setResult(PlatformProtocolsConfig.GC_BIND_VAILD_OVERTIME)
						.setDiamondNum(diamondMobileBindBean.getDiamond())
						.build());

				logger.debug("验证码超时：");

				return;
			}
			else
			{
				logger.debug("验证码正确");
				
				// 更新Mobile_id_table的绑定时间及状态
				mobileIdBean.setBindTime(now);
				mobileIdBean.setStatus(validStatus[0]);

				// Id_mobile_table中插入新数据
				IdMobileBean idMobileBean = table.Id_mobile_table.insert(roleId);
				idMobileBean.setAccountMobile(phoneNumber);
				idMobileBean.setBindAward(diamondMobileBindBean.getDiamond());
				idMobileBean.setBindTime(now);

				RoleInfo roleInfo = table.Role_info.update(roleId);

				if (roleInfo == null)
				{
					logger.debug("之前表中无数据");
					roleInfo = table.Role_info.insert(roleId);
				}

				logger.debug("role CardCount before", roleInfo.getCardCount());

				// 增加用户钻石数量
				roleInfo.setCardCount(roleInfo.getCardCount() + diamondMobileBindBean.getDiamond());

				logger.debug("role CardCount after", roleInfo.getCardCount());

				// 返回给client绑定成功消息
				connection.write(callBack,
					bindValidRES.setResult(PlatformProtocolsConfig.GC_BIND_VAILD_OK)
						.setDiamondNum(diamondMobileBindBean.getDiamond())
						.build());
				
				// 通知客户端更新房卡数量
				GCRoomCardModifySYNC.Builder roomCardModifySYNC = GCRoomCardModifySYNC.newBuilder();
				connection.write(callBack,
					roomCardModifySYNC.setRoomCardCount(roleInfo.getCardCount())
						.build());
				
				// BI日志
				KodBiLogHelper.bindPhone(roleId, phoneNumber);

				return;
			}
		}
		else
		{
			mobileIdBean.setStatus(validStatus[3]);

			connection.write(callBack,
				bindValidRES.setResult(PlatformProtocolsConfig.GC_BIND_VAILD_ERROR)
					.setDiamondNum(diamondMobileBindBean.getDiamond())
					.build());

			logger.debug("验证码错误");

			return;
		}
	}

	private CloseableHttpClient getHttpClient()
	{
		if (httpClient == null)
		{
			httpClient = HttpClients.createDefault();
		}
		return httpClient;
	}

	/**
	 * 调用云之讯接口
	 * 
	 * @param 手机号
	 * @param 验证码
	 * @param 短信显示参数，时间
	 * @return 第三方接口是否发送状态，成功或者失败
	 */
	public int sendMessage(String to, String code, int minute)
	{
		String param = code + "," + minute;
		String timestamp = formate.format(System.currentTimeMillis());
		String url = getUrl(timestamp);
		Map<String, String> message = new HashMap<>();
		message.put("appId", APP_ID);
		message.put("templateId", TEMPLATE_ID);
		message.put("to", to);
		message.put("param", param);
		String body;
		try
		{
			body = mapper.writeValueAsString(message);
			body = "{\"templateSMS\":" + body + "}";
			return post(url, timestamp, body);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return SEND_FAIL;
	}

	public String getUrl(String timestamp)
	{
		String signature = MD5(ACCOUNT_SID + AUTH_TOKEN + timestamp);
		StringBuffer sb = new StringBuffer("https://");
		return sb.append("api.ucpaas.com")
			.append("/")
			.append(VERSION)
			.append("/Accounts/")
			.append(ACCOUNT_SID)
			.append("/Messages/templateSMS")
			.append("?sig=")
			.append(signature)
			.toString();
	}

	public int post(String url, String timestamp, String body)
	{
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Content-Type", "application/json;charset=utf-8");
		// Authorization 需base64加密
		String src = ACCOUNT_SID + ":" + timestamp;
		BASE64Encoder encoder = new BASE64Encoder();
		String auth = encoder.encode(src.getBytes());
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
		try
		{
			requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
			requestBody.setContentLength(body.getBytes("UTF-8").length);
			httppost.setEntity(requestBody);
			HttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				String result = EntityUtils.toString(entity, "UTF-8");
				String respCode = result.split("respCode\":\"")[1].split("\",")[0];
				if ("000000".equals(respCode))
				{
					EntityUtils.consume(entity);
					return SEND_SUCCESS;
				}
			}
		}
		catch (Exception e)
		{
			logger.error("sendMessage post error : {}", e);
			e.printStackTrace();
		}

		return SEND_FAIL;
	}

	/**
	 * MD5加密
	 */
	public String MD5(String src)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		byte[] b = md.digest(src.getBytes());
		return byte2HexStr(b);
	}

	private String byte2HexStr(byte[] b)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++)
		{
			String s = Integer.toHexString(b[i] & 0xFF);
			if (s.length() == 1)
			{
				sb.append("0");
			}
			sb.append(s.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 判断是否为手机号
	 */
	public static boolean isPhoneLegal(String str)throws PatternSyntaxException {  
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);  
    } 
	
    /** 
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 
     * 此方法中前三位格式有： 
     * 13+任意数 
     * 15+除4的任意数 
     * 18+除1和4的任意数 
     * 17+除9的任意数 
     * 147 
     */  
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {  
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }  
  
    /** 
     * 香港手机号码8位数，5|6|8|9开头+7位任意数 
     */  
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {  
        String regExp = "^(5|6|8|9)\\d{7}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    } 
	
}
