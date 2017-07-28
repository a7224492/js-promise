package com.kodgames.corgi.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.CoreServiceConstants;
import com.kodgames.corgi.core.service.TransData.BaseData;

/**
 * 
 * @author XianYue 事务管理类：用来存储在玩家的一次请求中，一个线程在执行过程中的原始数据，用来在产生异常时进行数据的恢复
 *
 */
public class TransactionMgr
{
	private static Logger logger = LoggerFactory.getLogger(TransactionMgr.class);
	private static TransactionMgr instance = new TransactionMgr();
	private Map<Long, TransData> records = new ConcurrentHashMap<Long, TransData>();
	// private Map<Long, BaseLockData> locksMap = new ConcurrentHashMap<>();

	private Set<Integer> invaildProtocolId = new HashSet<>();

	public static TransactionMgr getInstance()
	{
		return instance;
	}

	public void addInvaildProtocolId(int protocolId)
	{
		invaildProtocolId.add(protocolId);
	}

	public void addPlayerData(Long threadId, long playerId, AbstractMessageService service)
	{
		PlayerService playerService = (PlayerService) service;
		TransData transData = records.get(threadId);
		if (transData == null)
		{
			return;
		}
		if (playerId != transData.getOwnerId())
		{
			// logger.error("can't check or change other data,ownerId={},playerId={}",transData.getOwnerId(),playerId);
			return;
		}
		if (playerService.getBean() == null)
		{
			return;
		}
		transData.addPlayerData(playerId, playerService, playerService.getBean());
	}

	/**
	 * 如果获取的是publicService，那么就获取所有该player可能修改的数据集进行备份,这是在获取PublicService的时候自动对数据进行收集
	 * @param threadId
	 *            线程Id
	 * @param backObj
	 *            数据备份对象
	 * @param service
	 *            service对象
	 */
	public  void addPublicData(Long threadId, AbstractMessageService service)
	{
		PublicService publicService = (PublicService) service;
		TransData transData = records.get(threadId);
		if (transData == null)
		{
			return;
		}

		if (transData.getOwnerId() == 0 || publicService.getBean(transData.getOwnerId()) == null)
		{
			return;
		}

		transData.addPublicData(publicService, publicService.getBean(transData.getOwnerId()));
	}
	
	/**
	 * 手动添加备份数据到事物管理类中，
	 * 
	 * @param threadId 线程Id
	 * @param backObj	要备份的数据对象
	 * @param service	操作的数据service
	 */
	public void addPublicDataManual(Long threadId,Object backObj,AbstractMessageService service)
	{
		PublicService publicService = (PublicService) service;
		if(backObj == null) {
			return ;
		}
		
		TransData transData = records.get(threadId);
		if(transData == null) {
			return ;
		}
		
		transData.addPublicData(publicService, backObj);
	}
	

	// 删除本次数据,并在这里释放publicService中数据的锁
	public void removeRecords(long threadId)
	{
		if (records.containsKey(threadId))
		{
			records.remove(threadId);
		}
	}

	/*
	 * 注册这个线程，这个为了防止加载推送数据时，也把serviceData放进这是数据集中
	 * 当然并不是所有的线程都需要去注册，对于一些不必要的逻辑，不用进行事务的恢复
	 *  不对服务器与服务器之间那些playerId为0的协议进行处理
	 * @param threadId
	 */
	public void registerThreadId(long threadId, int protocolId, long playerId)
	{
		if (invaildProtocolId.contains(protocolId))
		{
			return;
		}
		
		if(playerId == 0) {
			return ;
		}
		
		records.put(threadId, new TransData(playerId));
	}

	public void rollback(long threadId, int protocolId)
	{
		//事务没有开启,那么退出 
		if(!CoreServiceConstants.TRANS_OPEN)
			return ;
		
		TransData transData = records.get(threadId);
		if (transData == null)
		{
			logger.error("the record data is null,threadId={}", threadId);
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("playerId : " + transData.getOwnerId() + "\n");
		sb.append("protocolId: " + protocolId + "\n");

		// 恢复玩家数据
		List<BaseData<PlayerService>> playerDatas = transData.getPlayerDatas();
		sb.append("PlayerServiceName : ");
		if (!playerDatas.isEmpty())
		{
			for (BaseData<PlayerService> tmp : playerDatas)
			{
				//事务是否开启
				if(CoreServiceConstants.TRANS_OPEN)
					tmp.getService().recovery(tmp.getObj());
				
				sb.append(" " + tmp.getService().getClass().getName());
			}
		}

		// 恢复public数据
//		List<BaseData<PublicService>> publicDatas = transData.getPublicDatas();
//		sb.append("\n PublicServiceName : ");
//		if (!publicDatas.isEmpty())
//		{
//
//			for (BaseData<PublicService> tmp : publicDatas)
//			{
//				tmp.getService().rollback(0, tmp.getObj());
//				sb.append(" " + tmp.getService().getClass().getName());
//			}
//		}
		logger.debug("rollback threadId={},{}", threadId, sb.toString());
		removeRecords(threadId);
	}
}
