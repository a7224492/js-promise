import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.region.guangdong.common.Rules_GuangDong;
import com.kodgames.battleserver.service.battle.region.meizhou.Rules_MeiZhou;
import com.kodgames.battleserver.service.battle.region.neimeng.common.Rules_NeiMeng;

public class Main
{
	public static void main(String[] args)
	{
		List<RegionData> regions = new ArrayList<>();
		regions.add(new RegionData("chaoshan", "潮汕", Rules_GuangDong.class));
		regions.add(new RegionData("erdosmj", "内蒙", Rules_NeiMeng.class));
		regions.add(new RegionData("meizhou", "梅州", Rules_MeiZhou.class));

		for (RegionData regionData : regions)
		{
			BIRulesGenerator.generateRulesBi(regionData);
			BillingKeyGenerator.signatureBillingKey(regionData);
			System.out.println(regionData);
		}
	}
}
