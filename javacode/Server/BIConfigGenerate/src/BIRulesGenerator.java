import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

import com.kodgames.battleserver.service.battle.common.BattleRulesAnnotation;
import com.kodgames.game.common.Constant;
import com.kodgames.game.common.rule.RoomTypeConfig;
import com.kodgames.game.common.rule.RuleManager;

public class BIRulesGenerator
{

	public static void generateRulesBi(RegionData regionData)
	{
		String configPath = System.getProperty("user.dir") + "/../config";
		String rulePath = String.format("%s/%s/rules.xml", configPath, regionData.getName() + "/game/resource");
		try
		{
			if (new File(rulePath).exists() == false)
			{
				System.out.println("GenerateBi Failed : regionName = " + regionData.getName());
				return;
			}

			RuleManager.getInstance().clear();
			RuleManager.getInstance().load(rulePath);
			FileWriter fileWriter = new FileWriter(String.format("%s/%s/bi.properties", configPath, regionData.getName() + "/bi"));

			// 将每一个rule按照 rule:注释的结构输出到文件中
			for (Field field : regionData.getClazz().getDeclaredFields())
			{
				BattleRulesAnnotation annotation = field.getAnnotation(BattleRulesAnnotation.class);
				if (annotation == null)
					continue;

				int rule = field.getInt(null);
				if (annotation.isArea())
					fileWriter.write(String.format("area%d=%s \n", rule, annotation.comment()));
				else
					fileWriter.write(String.format("%d=%s \n", rule, annotation.comment()));
			}

			for (RoomTypeConfig config : RuleManager.getInstance().getRoomConfigs())
			{
				if (config.getGameCount() > 0)
				{
					fileWriter.write(String.format("%d=%s局\n", config.getType(), config.getGameCount()));
				}
				else
				{
					fileWriter.write(String.format("%d=%s圈\n", config.getType(), config.getRoundCount()));
				}
			}

			for (Constant.PayType payType : Constant.PayType.values())
			{
				fileWriter.write(String.format("paytype%d=%s\n", payType.getValue(), payType.getName()));
			}

			fileWriter.flush();
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}