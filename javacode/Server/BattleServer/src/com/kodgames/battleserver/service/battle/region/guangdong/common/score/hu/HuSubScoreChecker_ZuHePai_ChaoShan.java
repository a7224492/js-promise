package com.kodgames.battleserver.service.battle.region.guangdong.common.score.hu;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;

/**
 * 组合牌型
 * 
 * @author 毛建伟
 */
public class HuSubScoreChecker_ZuHePai_ChaoShan extends HuSubScoreChecker
{
	public static final String KEY_SCORE_TYPE = "key_scoreType";

	public static final String KEY_PAI_XING_LIST = "key_typeList";

	public static final String KEY_ONLY_THIS_TYPES = "key_onlyThisTypes";

	private int type = 0;

	private List<Integer> typeList = new ArrayList<Integer>();

	/**
	 * 是否只需要这几种类型，如果是就只能有这几种类型
	 */
	private boolean onlyThisTypes = false;

	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		type = CreateContextParser.getInt(context, KEY_SCORE_TYPE);
		for (int index = 0; index < CreateContextParser.getArraySize(context, KEY_PAI_XING_LIST); ++index)
		{
			this.typeList.add(CreateContextParser.getIntInArray(context, KEY_PAI_XING_LIST, index));
		}

		onlyThisTypes = CreateContextParser.getBoolean(context, KEY_ONLY_THIS_TYPES);
	}

	public HuSubScoreChecker_ZuHePai_ChaoShan()
	{
		// 这里无用，会自己新建一个
		super(0);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 如果只能有这几种类型并且typeList和所得的分的长度不相等
		if (onlyThisTypes && typeList.size() != inoutHuContext.scoreData.getPoints().size())
			return false;
		// 循环这个typelist
		for (int checkType : typeList)
		{
			// 判断是否有这个type
			boolean hasThisType = false;
			for (ScorePoint point : inoutHuContext.scoreData.getPoints())
			{
				if (point.getScoreType() == checkType)
					hasThisType = true;
			}
			// 没有这个type证明不能这样组合
			if (hasThisType == false)
				return false;
		}

		// 添加分数
		ScorePoint point = new ScorePoint();
		point.setCalcType(getModifierType().getValue());
		point.setScoreType(this.type);
		point.setScoreValue(getModiferScore());
		inoutHuContext.scoreData.getPoints().add(point);
		return true;
	}

	@Override
	public int getScoreType()
	{
		return this.type;
	}

}
