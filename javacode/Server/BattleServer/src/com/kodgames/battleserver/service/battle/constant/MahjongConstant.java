package com.kodgames.battleserver.service.battle.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.battleserver.common.Macro;

public class MahjongConstant
{
	public static final String JSON_PROC = "processer";
	public static final String JSON_FILT = "filters";
	/** 默认房间人数 */
	public static final int DEFAULT_ROOM_MEMBER_COUNT = 4;

	/** 战局状态 */
	public static class BattleState
	{
		/** 战斗中 */
		public static final int INBATTLE = 0;
		/** 结束战斗：黄庄结束 */
		public static final int HUANGZHUANG = 1;
		/** 结束战斗：正常结束 */
		public static final int FINISH = 2;
	}

	public static class BattleConst
	{
		/** 数字牌最大索引：万牌最大索引为9万 */
		public static final byte NUMBER_CARD_COUNT = 9;
		/** 字牌最大索引 */
		public static final byte ZI_CARD_COUNT = 7;
		/** 花牌最大索引 */
		public static final byte HUA_CARD_COUNT = 8;
		/** 花牌（春夏秋冬）最大索引 */
		public static final byte HUA_SEASON_COUNT = 4;
		/** 花牌（梅兰竹菊）最大索引 */
		public static final byte HUA_FLOWER_COUNT = 4;
		/** 风牌最大索引 */
		public static final byte FENG_CARD_COUNT = 4;
		/** 箭牌最大索引 */
		public static final byte JIAN_CARD_COUNT = 3;
		/** 一中牌的最大个数，如1万有4张 */
		public static final int ONE_CARD_MAX = 4;
		public static final int EX_CARD_COUNT_MAX = 8;
		public static final int INVALID_ROLEID = -1;
	}

	/** 玩家状态 */
	public static class PlayerStatus
	{
		/** 默认状态 */
		public static final int DEFAULT = 1;
		/** 是否准备好打牌 */
		public static final int READY = 1 << 1;
		/** 是房主 */
		public static final int HOST = 1 << 2;
		/** 庄家 */
		public static final int ZHUANGJIA = 1 << 8;
		/** 是否在线 */
		public static final int ONLINE = 1 << 13;
		/** 忽略同IP */
		public static final int IGNORE_SAME_IP = 1 << 15;
	}

	/**
	 * 操作类型
	 */
	public static class PlayType
	{
		public static final int UNKNOW = 0;
		/** 手牌(开局发牌/重新加入房间复牌/胡牌后公开手牌) */
		public static final int OPERATE_DEAL_FIRST = 1;
		/** 抓牌 */
		public static final int OPERATE_DEAL = 2;
		/** 过 */
		public static final int OPERATE_CAN_PASS = 3;
		public static final int OPERATE_PASS = 4;
		/** 等待 */
		public static final int OPERATE_WAIT = 5;
		public static final int OPERATE_CANCEL = 6;

		/** 可以出牌 */
		public static final int OPERATE_CAN_PLAY_A_CARD = 100;
		/** 出牌 */
		public static final int OPERATE_PLAY_A_CARD = 101;

		/** 可以自动出牌 */
		public static final int OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD = 102;

		/** 可以出牌，并翻到背面 */
		public static final int OPERATE_CAN_PLAY_A_CARD_HIDE = 103;
		/** 出牌，并翻到背面 */
		public static final int OPERATE_PLAY_A_CARD_HIDE = 104;

		/** 可以吃牌 */
		public static final int OPERATE_CAN_CHI_A_CARD = 110;
		/** 吃牌 */
		public static final int OPERATE_CHI_A_CARD = 111;

		/** 可以碰牌 */
		public static final int OPERATE_CAN_PENG_A_CARD = 120;
		/** 碰牌 */
		public static final int OPERATE_PENG_A_CARD = 121;

		/** 可以杠牌 */
		public static final int OPERATE_CAN_GANG_A_CARD = 130;
		/** 杠牌 */
		public static final int OPERATE_GANG_A_CARD = 131;

		/** 可以补杠 */
		public static final int OPERATE_CAN_BU_GANG_A_CARD = 140;
		/** 补杠 */
		public static final int OPERATE_BU_GANG_A_CARD = 141;

		/** 可以胡 */
		public static final int OPERATE_CAN_HU = 150;
		/** 胡 */
		public static final int OPERATE_HU = 151;
		/** 可以自动胡 */
		public static final int OPERATE_CAN_AUTO_HU = 152;

		/** 可以暗杠 */
		public static final int OPERATE_CAN_AN_GANG = 160;
		/** 暗杠 */
		public static final int OPERATE_AN_GANG = 161;

		/** 换三张开始 */
		public static final int OPERATE_CHANGECARD_START = 170;
		/** 换三张操作 */
		public static final int OPERATE_CHANGECARD = 171;
		/** 换三张结束 */
		public static final int OPERATE_CHANGECARD_FINISH = 172;
		/** 换三张结束的方式，顺时针还是对家换牌 */
		public static final int OPERATE_CHANGECARD_RULE = 173;
		/** 换三张已选择 */
		public static final int OPERATE_CHANGECARD_SELECT = 174;

		/** 定缺开始 */
		public static final int OPERATE_LACK_START = 180;
		/** 定缺操作 */
		public static final int OPERATE_LACK = 181;
		/** 定缺结束 */
		public static final int OPERATE_LACK_FINISH = 182;
		/** 定缺已选择 */
		public static final int OPERATE_LACK_SELECT = 183;

		/** 蹲拉跑开始 （内蒙） */
		public static final int OPERATE_DUN_LA_PAO_START = 190;
		/** 蹲拉跑操作 （内蒙） */
		public static final int OPERATE_DUN_LA_PAO = 191;
		/** 蹲拉跑结束 （内蒙） */
		public static final int OPERATE_DUN_LA_PAO_FINISH = 192;
		/** 蹲拉跑已选择 （内蒙） */
		public static final int OPERATE_DUN_LA_PAO_SELECT = 193;

		/** 明打开始 （聊城） */
		public static final int OPERATE_MING_DA_START = 200;
		/** 明打操作 （聊城） */
		public static final int OPERATE_MING_DA = 201;
		/** 明打拒绝 （聊城） */
		public static final int OPERATE_MING_DA_REFUSED = 202;
		/** 明打 结束（聊城） */
		public static final int OPERATE_MING_DA_END = 203;

		/** 可以听牌 */
		public static final int OPERATE_CAN_TING = 210;
		/** 听牌 */
		public static final int OPERATE_TING = 211;

		/** 可以听牌，需要打牌 */
		public static final int OPERATE_CAN_TING_CARD = 212;
		/** 打牌听牌 */
		public static final int OPERATE_TING_CARD = 213;

		/** 上火选择开始（江西） */
		public static final int OPERATE_SHANG_HUO_START = 230;
		/** 上火选择开始（江西） */
		public static final int OPERATE_SHANG_HUO = 231;
		/** 上火选择开始（江西） */
		public static final int OPERATE_SHANG_HUO_END = 232;

		/** 漂选择开始（江西） */
		public static final int OPERATE_PIAO_START = 235;
		/** 漂选择开始（江西） */
		public static final int OPERATE_PIAO = 236;
		/** 漂选择开始（江西） */
		public static final int OPERATE_PIAO_END = 237;

		/** 上正精 (南昌) */
		public static final int OPERATE_SHANGJING_ZHENG = 240;
		/** 上副精 (南昌) */
		public static final int OPERATE_SHANGJING_FU = 241;
		/** 下正精 (南昌) */
		public static final int OPERATE_XIAJING_ZHENG = 242;
		/** 下副精 (南昌) */
		public static final int OPERATE_XIAJING_FU = 243;

		/** 冷碰(河源) */
		public static final int OPERATE_LENG_PENG_A_CARD = 250;
		/** 可以翻 (河源) */
		public static final int OPERATE_CAN_FAN_A_CARD = 251;
		/** 翻 (河源) */
		public static final int OPERATE_FAN_A_CARD = 252;
		/** Break翻 (河源) */
		public static final int OPERATE_BREAK_FAN_A_CARD = 253;

		/** 平胡 */
		public static final int HU_PING_HU = 1000;
		/** 七对 */
		public static final int HU_QI_DUI = 1001;
		/** 十三幺 */
		public static final int HU_SHI_SAN_YAO = 1002;
		/** 清一色 */
		public static final int HU_QING_YI_SE = 1003;
		/** 一条龙 */
		public static final int HU_YI_TIAO_LONG = 1004;
		/** 根(川麻) */
		public static final int HU_GEN = 1005;
		/** 全带幺(带幺九) */
		public static final int HU_QUAN_DAI_YAO = 1006;
		/** 碰碰和(对对和) */
		public static final int HU_PENG_PENG_HU = 1007;
		/** 将对 全为2,5,8的对对胡 */
		public static final int HU_JIANG_DUI = 1008;
		/** 全小 */
		public static final int HU_QUAN_XIAO = 1009;
		/** 全中 */
		public static final int HU_QUAN_ZHONG = 1010;
		/** 全大 */
		public static final int HU_QUAN_DA = 1011;
		/** 中张, 没有1,9的数牌 */
		public static final int HU_ZHONG_ZHANG = 1012;
		/** 门前清(门清) 没有吃、碰、明杠，四个组合全由自己摸牌组成(可以暗杠) */
		public static final int HU_MEN_QIAN_QING = 1013;
		/** 全求人(全求) 全靠吃牌、碰牌、单钓别人打出的牌和牌。不计单钓将。简称"全求"。 */
		public static final int HU_QUAN_QIU_REN = 1014;
		/** 天和 */
		public static final int HU_TIAN_HU = 1015;
		/** 地和 */
		public static final int HU_DI_HU = 1016;
		/** 杠上花 */
		public static final int HU_GANG_SHANG_HUA = 1017;
		/** 杠上炮 */
		public static final int HU_GANG_SHANG_PAO = 1018;
		/** 杠上胡 */
		public static final int HU_QIANG_GANG_HU = 1019;
		/** 海底捞月(海底胡) */
		public static final int HU_HAI_DI_LAO_YUE = 1020;
		/** 金钩 */
		public static final int HU_JIN_GOU = 1021;
		/** 自摸 */
		public static final int HU_ZI_MO = 1022;
		/** 点炮胡, 对应于自摸 */
		public static final int HU_DIAN_PAO = 1023;
		/** 清幺九 */
		public static final int HU_QING_YAO_JIU = 1025;
		/** 混幺九(幺九胡) */
		public static final int HU_HUN_YAO_JIU = 1026;
		/** 字一色 */
		public static final int HU_ZI_YI_SE = 1027;
		/** 混一色 */
		public static final int HU_HUN_YI_SE = 1028;
		/** 四杠 */
		public static final int HU_SI_GANG = 1029;
		/** 四鬼胡牌 */
		public static final int HU_SI_MASTER_CARD_HU = 1030;
		/** 豪华七对 */
		public static final int HU_HAO_HUA_QI_DUI = 1031;
		/** 缺一门 */
		public static final int HU_QUE_YI_MEN = 1033;
		/** 边张 */
		public static final int HU_BIAN_ZHANG = 1034;
		/** 坎张 */
		public static final int HU_KAN_ZHANG = 1035;
		/** 单钓 */
		public static final int HU_DAN_DIAO = 1036;
		/** 够张 */
		public static final int HU_GOU_ZHANG = 1037;
		/** 庄家 */
		public static final int HU_ZHUANG_JIA = 1038;
		/** 带漂 */
		public static final int HU_DAI_PIAO = 1039;
		/** 双豪华七对 */
		public static final int HU_SHUANG_HAO_HUA_QI_DUI = 1040;
		/** 三豪华七对 */
		public static final int HU_SAN_HAO_HUA_QI_DUI = 1041;
		/** 门清自摸 */
		public static final int HU_MEN_QING_ZI_MO = 1042;
		/** 广州推倒胡的幺九（包含清幺九和混幺九） */
		public static final int HU_YAO_JIU_GDTDH = 1043;
		/** 大胡（广东潮汕的小胡规则下除去平胡外的所有加分，乘分不变） */
		public static final int HU_DA_HU = 1044;
		/** 一般高 */
		public static final int HU_YI_BAN_GAO = 1045;
		/** 小连 */
		public static final int HU_XIAO_LIAN = 1046;
		/** 大连 */
		public static final int HU_DA_LIAN = 1047;
		/** 老少 */
		public static final int HU_LAO_SHAO = 1048;
		/** 刻 */
		public static final int HU_KE = 1049;
		/** 四归一 */
		public static final int HU_SI_GUI_YI = 1050;
		/** 坎五魁 */
		public static final int HU_KAN_WU_KUI = 1051;
		/** 九莲宝灯 */
		public static final int HU_JIU_LIAN_BAO_DENG = 1052;
		/** 13烂 */
		public static final int HU_13LAN = 1053;
		/** 七星13烂 */
		public static final int HU_QIXING13LAN = 1054;
		/** 四对 （三人三房） */
		public static final int HU_SI_DUI = 1055;
		/** 混幺九（潮州麻将） */
		public static final int HU_HUN_YAO_JIU_CHAOZHOU = 1056;
		/** 夹心五 */
		public static final int HU_JIA_XIN_WU = 1057;
		/** 大单吊 */
		public static final int HU_DA_DAN_DIAO = 1058;
		/** 超豪华七对 */
		public static final int HU_CHAO_HAO_HUA_QI_DUI = 1059;

		/** 精钓乘分 */
		public static final int HU_JING_DIAO_MULTIPLY = 1070;
		/** 德国乘分 */
		public static final int HU_DE_GUO_MULTIPLY = 1071;
		/** 德国加分 */
		public static final int HU_DE_GUO_ADD = 1072;
		/** 德中德乘分 */
		public static final int HU_DE_ZHONG_DE_MULTIPLY = 1073;
		/** 德中德加分 */
		public static final int HU_DE_ZHONG_DE_ADD = 1074;
		/** 精钓加分 */
		public static final int HU_JING_DIAO_ADD = 1075;

		/** 门清 */
		public static final int HU_MEN_QING = 1060;
		/** 开门 */
		public static final int HU_KAI_MEN = 1061;
		/** 死卡 */
		public static final int HU_SI_KA = 1062;
		/** 活卡 */
		public static final int HU_HUO_KA = 1063;
		/** 摸宝 */
		public static final int HU_MO_BAO = 1064;
		/** 宝中宝 */
		public static final int HU_BAO_ZHONG_BAO = 1065;
		/** 未上听 */
		public static final int HU_NO_TING_CARD = 1066;
		/** 上听 */
		public static final int HU_TING_CARD = 1067;
		/** 胡牌 客户端显示 */
		public static final int HU_HU_PAI = 1068;
		/** 卡当次数 */
		public static final int HU_KA_DANG = 1069;

		/** 大三元 */
		public static final int HU_DA_SAN_YUAN = 1080;
		/** 小三元 */
		public static final int HU_XIAO_SAN_YUAN = 1081;
		/** 大四喜 */
		public static final int HU_DA_SI_XI = 1082;
		/** 小四喜 */
		public static final int HU_XIAO_SI_XI = 1083;

		/** 支牌(安徽-马鞍山) */
		public static final int HU_ZHI_PAI = 1090;
		/** 大吊车(安徽-马鞍山) */
		public static final int HU_DA_DIAO_CHE = 1091;
		/** 压挡(安徽-马鞍山) */
		public static final int HU_YA_DANG = 1092;
		/** 枯枝压(安徽-马鞍山) */
		public static final int HU_KU_ZHI_YA = 1093;
		/** 双扑(安徽-马鞍山) */
		public static final int HU_SHUANG_PU = 1094;
		/** 四核(安徽-马鞍山) */
		public static final int HU_SI_HE = 1095;
		/** 五通(安徽-马鞍山) */
		public static final int HU_WU_TONG = 1096;
		/** 六连(安徽-马鞍山) */
		public static final int HU_LIU_LIAN = 1097;
		/** 十老(安徽-马鞍山) */
		public static final int HU_SHI_LAO = 1098;
		/** 十小(安徽-马鞍山) */
		public static final int HU_SHI_XIAO = 1099;
		/** 挖摸(安徽-马鞍山) */
		public static final int HU_WA_MO = 1100;
		/** 平摸(安徽-马鞍山) */
		public static final int HU_PING_MO = 1101;
		/** 清水大拿(安徽-马鞍山) */
		public static final int HU_QING_SHUI_DA_NA = 1102;
		/** 浑水大拿(安徽-马鞍山) */
		public static final int HU_HUN_SHUI_DA_NA = 1103;
		/** 三张在手(安徽-马鞍山) */
		public static final int HU_SAN_ZHANG_ZAI_SHOU = 1104;
		/** 三张碰出 (安徽-马鞍山) */
		public static final int HU_SAN_ZHANG_PENG_CHU = 1105;

		/** 跑风 (安徽-铜陵) */
		public static final int HU_PAO_FENG = 1106;
		/** 跑风杠 (安徽-铜陵) */
		public static final int HU_PAO_FENG_GANG = 1107;
		/** 跑配 (安徽-铜陵) */
		public static final int HU_PAO_PEI = 1108;
		/** 素牌(安徽-红中) */
		public static final int HU_SU_PAI = 1109;

		/** 坎坎胡（梅州） */
		public static final int HU_KAN_KAN_HU = 1110;
		/** 红中宝（梅州） */
		public static final int HU_MAIN_HONG_ZHONG_BAO = 1111;
		/** 无红中（梅州） */
		public static final int HU_WU_HONG_ZHONG = 1112;
		/** 红中宝（梅州） */
		public static final int HU_SUB_HONG_ZHONG_BAO = 1113;

		/** 吃杠杠爆分（汕头） */
		public static final int HU_CHI_GANG_GANG_BAO = 1120;
		/** 全牌型（汕头） */
		public static final int HU_QUAN_PAI_XING = 1121;

		/** 大哥（河源） */
		public static final int HU_DA_GE = 1130;
		/** 混碰（河源） */
		public static final int HU_HUN_PENG = 1131;
		/** 打翻倍（河源） */
		public static final int HU_DA_FAN_BEI = 1132;
		/** 花吊花（河源） */
		public static final int HU_HUA_DIAO_HUA = 1133;

		/** 杂对（惠州） */
		public static final int HU_ZA_DUI = 1134;
		/** 清碰（惠州） */
		public static final int HU_QING_PENG_HUI_ZHOU = 1135;
		/** 7张花胡（惠州） */
		public static final int HU_MAIN_7_HUA = 1136;
		/** 7张花胡（惠州） */
		public static final int HU_SUB_7_HUA = 1137;
		/** 8张花胡（惠州） */
		public static final int HU_MAIN_8_HUA = 1138;
		/** 8张花胡（惠州） */
		public static final int HU_SUB_8_HUA = 1139;

		/** 龙七对（贵阳） */
		public static final int HU_LONG_QI_DUI = 1140;
		/** 清大对（贵阳） */
		public static final int HU_QING_DA_DUI = 1141;
		/** 清七对（贵阳） */
		public static final int HU_QING_QI_DUI = 1142;
		/** 青龙背（贵阳） */
		public static final int HU_QING_LONG_BEI = 1143;
		/** 硬报（贵阳） */
		public static final int HU_YING_BAO = 1144;
		/** 软报（贵阳） */
		public static final int HU_RUAN_BAO = 1145;

		/** 不动手(安徽-马鞍山) */
		public static final int HU_BU_DONG_SHOU = 1150;
		/** 花开(安徽-池州) */
		public static final int HU_HUA_KAI = 1151;
		/** 大吊车+花开(安徽-池州) */
		public static final int HU_DA_DIAO_CHE_JIA_HUA_KAI = 1152;
		/** 大吊车+杠开(安徽-池州) */
		public static final int HU_DA_DIAO_CHE_JIA_GANG_KAI = 1153;
		/** 小胡(安徽-池州) */
		public static final int HU_XIAO_HU = 1154;
		/** 中胡(安徽-池州) */
		public static final int HU_ZHONG_HU = 1155;
		/** 辣子(安徽-池州) */
		public static final int HU_LA_ZI = 1156;

		/** 压挡自摸(安徽-芜湖) */
		public static final int HU_YA_DANG_ZI_MO = 1157;
		/** 支番(安徽-芜湖) */
		public static final int HU_ZHI_FAN = 1158;
		/** 庄家底番(安徽-芜湖) */
		public static final int HU_ZHUANG_JIA_DI_FAN = 1159;
		/** 自摸嘴子(安徽-芜湖) */
		public static final int HU_ZI_MO_ZUI_ZI = 1160;
		/** 双四核(安徽-芜湖) */
		public static final int HU_SHUANG_SI_HE = 1161;
		/** 双八支(安徽-芜湖) */
		public static final int HU_SHUANG_BA_ZHI = 1162;
		/** 全交(安徽-芜湖) */
		public static final int HU_QUAN_JIAO = 1163;
		/** 四鬼胡牌(安徽-芜湖) */
		public static final int HU_SI_HONG_ZHONG = 1164;
		/** 无花果 （安徽- 安庆） */
		public static final int HU_WU_HUA_GUO = 1165;
		/** 补花的分（安徽-安庆） */
		public static final int HU_BU_HUA_FEN = 1166;
		/** 天胡 （安徽-安庆） */
		public static final int HU_TIAN_HU_AN_QING = 1167;
		/** 对到胡 （安徽-安庆） */
		public static final int HU_DUI_DAO_HU = 1168;
		/** 花（安徽 - 安庆） */
		public static final int HU_HUA_FEN = 1169;
		/** 底（安徽-安庆） */
		public static final int HU_DI_FEN = 1170;
		/** 卡胡（安徽 -安庆） */
		public static final int HU_KA_HU = 1171;
		/** 花开 (安徽-安庆) */
		public static final int HU_HUA_KAI_AN_QING = 1172;

		/** 闲金（福建-宁德） */
		public static final int HU_XIAN_JIN = 1190;
		/** 三金倒（福建-宁德） */
		public static final int HU_SAN_MASTER_CARD = 1191;
		/** 金雀（福建-宁德） */
		public static final int HU_JIN_QUE = 1192;
		/** 金龙（福建-宁德） */
		public static final int HU_JIN_LONG = 1193;
		/** 金杠（福建-宁德） */
		public static final int HU_JIN_GANG = 1194;
		/** 抢金（福建-宁德） */
		public static final int HU_QIANG_JIN = 1195;

		/** 混对对（江苏-扬州） */
		public static final int HU_HUN_DUI_DUI = 1200;
		/** 清对对（江苏-扬州） */
		public static final int HU_QING_DUI_DUI = 1201;

		/** 清碰(广西-柳州) */
		public static final int HU_QING_PENG = 1300;
		/** 清对(广西-柳州) */
		public static final int HU_QING_DUI = 1301;
		/** 清大对(广西) */
		public static final int HU_QING_QI_DA_DUI = 1302;
		/** 混大对(广西) */
		public static final int HU_HUN_QI_DA_DUI = 1303;
		/** 混一色碰碰胡全求人(广西) */
		public static final int HU_HUN_PENG_QUAN = 1304;
		/** 清一色全求人(广西) */
		public static final int HU_QING_QUAN = 1305;
		/** 清一色碰碰胡全求人(广西) */
		public static final int HU_QING_PENG_QUAN = 1306;
		/** 门清平胡（广西） */
		public static final int HU_PING_HU_MEN_QING = 1307;
		/** 海捞平胡（广西） */
		public static final int HU_PING_HU_HAI_LAO = 1308;
		/** 杠爆平胡（广西） */
		public static final int HU_PING_HU_GANG_BAO = 1309;
		/** 清一色自摸（广西） */
		public static final int HU_ZI_MO_QING_YI_SE = 1310;
		/** 混一色自摸（广西） */
		public static final int HU_ZI_MO_HUN_YI_SE = 1311;
		/** 平胡自摸（广西） */
		public static final int HU_ZI_MO_PING_HU = 1312;
		/** 碰碰胡自摸（广西） */
		public static final int HU_ZI_MO_PENG_PENG_HU = 1313;
		/** 七小对自摸（广西） */
		public static final int HU_ZI_MO_QI_DUI = 1314;
		/** 七大对自摸（广西） */
		public static final int HU_ZI_MO_QI_DA_DUI = 1315;
		/** 自摸正烂（广西） */
		public static final int HU_ZI_MO_ZHENG_LAN = 1316;
		/** 自摸乱烂（广西） */
		public static final int HU_ZI_MO_LUAN_LAN = 1317;
		/** 天胡平胡（广西） */
		public static final int HU_PING_HU_TIAN_HU = 1318;
		/** 正烂（广西） */
		public static final int HU_ZHENG_LAN = 1320;
		/** 乱烂（广西） */
		public static final int HU_LUAN_LAN = 1321;

		/** 全字对对碰（潮汕） */
		public static final int HU_QUAN_ZI_DUI_DUI_PENG = 1400;

		public static final int HU_END = 2000;

		/** 剩余牌池 */
		public static final int DISPLAY_LAST_CARD_COUNT = 2001;
		/** 花牌 */
		public static final int DISPLAY_EX_CARD = 2002;
		/** 正牌（汕尾） */
		public static final int DISPLAY_ZHENG_CARD = 2009;

		/** 被吃 */
		public static final int DISPLAY_BE_CHI = 3000;
		/** 被碰 */
		public static final int DISPLAY_BE_PENG = 3001;
		/** 点杠 */
		public static final int DISPLAY_BE_GANG = 3002;
		/** 自摸加番 */
		public static final int DISPLAY_ZIMO_FAN = 3003;
		/** 自摸加分 */
		public static final int DISPLAY_ZIMO_FEN = 3004;
		/** 花猪 */
		public static final int DISPLAY_HUAZHU = 3005;
		/** 大叫 */
		public static final int DISPLAY_DAJIAO = 3006;
		/** 点炮 */
		public static final int DISPLAY_DIANPAO = 3007;
		/** 未胡牌 */
		public static final int DISPLAY_LOSER = 3008;
		/** 听牌 */
		public static final int DISPLAY_TING = 3009;
		/** */
		public static final int DISPLAY_UN_TING = 3010;
		/** 退税:退分 */
		public static final int DISPLAY_TUI_SHUI = 3011;
		/** 呼叫转移 */
		public static final int DISPLAY_HU_JIAO_ZHUAN_YI = 3012;
		/** 奖马:总牌数(广东) */
		public static final int DISPLAY_DEAL_BETTING_HOUSE = 3013;
		/** 奖马:中马牌(广东) */
		public static final int DISPLAY_BETTING_HOUSE = 3014;
		/** 翻鬼(广东) */
		public static final int DISPLAY_DEAL_MASTER_CARD = 3015;
		/** 显示鬼(广东) */
		public static final int DISPLAY_SHOW_MASTER_CARD = 3016;
		/** 无鬼加倍得分(广东) */
		public static final int DISPLAY_NO_MASTER_CARD = 3017;
		/** 跟庄(广东) */
		public static final int DISPLAY_FOLLOW_BANKER = 3018;
		/** 手牌全部蒙灰 */
		public static final int DISPLAY_MASK_ALL_HAND_CARD = 3019;
		/** 开启自动打牌 */
		public static final int DISPLAY_AUTO_PLAY_LAST_DEALED_CARD = 3020;
		/** 奖马:马牌分值（汕尾，服务器内部使用，为了方便房间结算时计算奖马个数） */
		public static final int DISPLAY_BETTING_HOUSE_VALUE = 3021;
		/** 花牌加番（汕尾） */
		public static final int DISPLAY_HUA_JIA_FAN = 3024;
		/** 字牌分（汕尾） */
		public static final int DISPLAY_ZI_JIA_FAN = 3025;
		/** 风牌分（汕尾） */
		public static final int DISPLAY_FENG_JIA_FAN = 3026;
		/** 买马（潮汕） */
		public static final int DISPLAY_BUY_HORSE = 3027;
		/** 罚马（潮汕） */
		public static final int DISPLAY_PUNISH_HORSE = 3028;
		/** 买马罚马的马牌值（潮汕） */
		public static final int DISPLAY_HORSE_CARD = 3029;
		/** 赢的买马罚马牌（潮汕） */
		public static final int DISPLAY_WIN_HORSE_CARD = 3030;
		/** 输的买马罚马牌（潮汕） */
		public static final int DISPLAY_LOSE_HORSE_CARD = 3031;
		/** 买马罚马结束（潮汕） */
		public static final int DISPLAY_HORSE_END = 3032;
		/** 买马的中马胡牌分值（潮汕） */
		public static final int DISPLAY_HU_BUY_HORSE_SCORE = 3033;
		/** 罚马的中马胡牌分值（潮汕） */
		public static final int DISPLAY_HU_PUNISH_HORSE_SCORE = 3034;
		/** 赢的被买马罚马牌（潮汕） */
		public static final int DISPLAY_BE_HU_BUY_HORSE_SCORE = 3035;
		/** 输的被买马罚马牌（潮汕） */
		public static final int DISPLAY_BE_HU_PUNISH_HORSE_SCORE = 3036;
		/** 买中的马的个数（潮汕） */
		public static final int DISPLAY_BUY_HORSE_CARD_COUNT = 3037;
		/** 最终结算界面所有的马牌的个数（潮汕） */
		public static final int DISPLAY_ALL_HORSE_CARD_COUNT = 3038;
		/** 杠跟底分（潮汕） */
		public static final int DISPLAY_GANG_GEN_DI_FEN = 3039;
		/** 连庄（潮汕） */
		public static final int DISPLAY_LIAN_ZHUANG = 3040;
		/** 奖马:总牌数(潮汕揭阳一炮多响) */
		public static final int DISPLAY_DEAL_BETTING_HOUSE_MULTI = 3041;
		/** 奖马:中马牌(潮汕揭阳一炮多响) */
		public static final int DISPLAY_BETTING_HOUSE_MULTI = 3042;

		/** 蹲 （内蒙） */
		public static final int DISPLAY_DUN = 4000;
		/** 拉 （内蒙） */
		public static final int DISPLAY_LA = 4001;
		/** 跑 （内蒙） */
		public static final int DISPLAY_PAO = 4002;
		/** 赔杠（内蒙） */
		public static final int DISPLAY_PEI_GANG = 4003;

		/** 摊牌 （聊城） */
		public static final int DISPLAY_TANPAI = 4100;

		/** 对火 */
		public static final int DISPLAY_DUI_HUO = 4200;
		/** 被漂 */
		public static final int DISPLAY_BE_PIAO = 4201;

		/** 提宝(哈尔滨) */
		public static final int DISPLAY_LIFT_BAO_CARD = 4300;
		/** 翻宝(哈尔滨) */
		public static final int DISPLAY_DEAL_BAO_CARD = 4301;
		/** 换宝(哈尔滨) */
		public static final int DISPLAY_HUAN_BAO_CARD = 4302;
		/** 显示宝牌(哈尔滨) */
		public static final int DISPLAY_SHOW_BAO_CARD = 4303;

		/** 被明杠(宜昌) */
		public static final int DISPLAY_HU_BE_GANG = 4400;
		/** 被暗杠(宜昌) */
		public static final int DISPLAY_HU_BE_AN_GANG = 4401;
		/** 实时总分数 （宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_TOTAL_POINT = 4402;
		/** 实时当前局分数 （宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_POINT_IN_GAME = 4403;
		/** 实时计分自摸分数 （宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_ZI_MO = 4404;
		/** 实时计分胡牌分数 （宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_HU_PAI = 4405;
		/** 实时计分被自摸分数（宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_BEI_ZI_MO = 4406;
		/** 实时计分点炮分数（宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_DIAN_PAO = 4407;
		/** 实时计分查花猪分数（宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_HUA_ZHU = 4408;
		/** 实时计分查大叫分数（宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_DA_JIAO = 4409;
		/** 实时计分赢家花猪大叫分数（宜昌血流） */
		public static final int DISPLAY_SHI_SHI_JI_FEN_SUM_SCORE = 4410;
		/** 査听功能 打出能胡的牌以及听牌之后能听的牌和数据（宜昌血流） */
		public static final int DISPLAY_CHA_TING_PLAY_CARD_CAN_HU_CARD = 4411;
		/** 査听功能 听牌之后能听的牌以及能听的牌的数据（宜昌血流） */
		public static final int DISPLAY_CHA_TING_PLAY_CARD_CAN_TING_CARD = 4412;
		/** 査听功能 显示査听（宜昌血流） */
		public static final int DISPLAY_XIAN_SHI_CHA_TING = 4413;
		/** 査听功能 取消显示査听（宜昌血流） */
		public static final int DISPLAY_QU_XIAO_CHA_TING = 4414;
		/** 査听功能 用于客户端清除打出去能胡的牌和数据 */
		public static final int DISPLAY_QING_CHU_CAN_HU_CARD = 4415;
		/** 査听功能 用于客户端清除能听的牌的数据 */
		public static final int DISPLAY_QING_CHU_CAN_TING_CARD = 4416;

		/** 抄庄 (南昌) */
		public static final int DISPLAY_CHAOZHUANG = 4500;
		/** 抄庄流局(南昌) */
		public static final int DISPLAY_CHAOZHUANG_LIUJU = 4511;
		/** 回头一笑 (南昌) */
		public static final int DISPLAY_HUI_TOU_YI_XIAO = 4501;
		/** 上精 (南昌) */
		public static final int DISPLAY_SHANG_JING = 4502;
		/** 下翻精 (南昌) */
		public static final int DISPLAY_XIA_FAN_JING = 4503;
		/** 埋地雷 (南昌) */
		public static final int DISPLAY_MAI_DI_LEI = 4504;
		/** 冲关 */
		public static final int DISPLAY_CHONG_GUAN = 4505;
		/** 霸王精 */
		public static final int DISPLAY_BA_WANG_JING = 4506;
		/** 杠精 */
		public static final int DISPLAY_GANG_JING = 4507;
		/** 精（用于房间结算） */
		public static final int DISPLAY_JING = 4508;
		/** 未点炮 (相对于自摸和点炮) */
		public static final int DISPLAY_UN_DIAN_PAO = 4509;
		/** 翻地雷（南昌最后结算翻开地雷） */
		public static final int DISPLAY_FAN_DI_LEI = 4510;
		/** 总胡牌次数（用于房间总结算） */
		public static final int DISPLAY_HU_TOTAL = 4511;
		/** 总杠牌次数（用于房间总结算） */
		public static final int DISPLAY_GANG_TOTAL = 4512;
		/** 回头一笑 牌(南昌，用于客户端显示自己手里的回头一笑牌) */
		public static final int DISPLAY_HUI_TOU_YI_XIAO_CARDS = 4513;
		/** 同一首歌开头(南昌) */
		public static final int DISPLAY_TONG_YI_SHOU_GE_START = 4514;
		/** 同一首歌(南昌) */
		public static final int DISPLAY_TONG_YI_SHOU_GE_END = 4515;

		/** 跑配(安徽-铜陵) */
		public static final int DISPLAY_PAO_PEI = 4600;
		/** 弃牌(安徽-铜陵) */
		public static final int DISPLAY_QI_PAI = 4601;
		/** 鬼牌(安徽-红中) */
		public static final int DISPLAY_SHOW_ZHONG_MASTER_CARD = 4602;
		/** 翻码(安徽-红中) */
		public static final int DISPLAY_FAN_HOUSE = 4603;
		/** 中码(安徽-红中) */
		public static final int DISPLAY_ZHONG_HOUSE = 4604;
		/** 补花（安徽-安庆） */
		public static final int DISPLAY_BU_HUA = 4605;
		/** 安庆的花牌（给客户端发送） */
		public static final int DISPLAY_AN_QING_HUA_PAI = 4606;
		/** 顺包（安徽-安庆） */
		public static final int DISPLAY_SHUN_BAO = 4607;
		/** 反包（安徽-安庆） */
		public static final int DISPLAY_FAN_BAO = 4608;
		/** 底牌（安徽-阜阳） */
		public static final int DISPLAY_DI_CARD = 4609;

		/** 冲锋鸡（贵阳） */
		public static final int DISPLAY_JI_CHONGFENG = 4700;
		/** 冲锋金鸡（贵阳） */
		public static final int DISPLAY_JI_CHONGFENG_GOLD = 4701;
		/** 责任鸡（贵阳） */
		public static final int DISPLAY_JI_ZEREN = 4702;
		/** 责任金鸡（贵阳） */
		public static final int DISPLAY_JI_ZEREN_GOLD = 4703;
		/** 乌骨冲锋鸡（贵阳） */
		public static final int DISPLAY_JI_WUGU_CHONGFENG = 4704;
		/** 乌骨冲锋金鸡（贵阳） */
		public static final int DISPLAY_JI_WUGU_CHONGFENG_GOLD = 4705;
		/** 乌骨责任鸡（贵阳） */
		public static final int DISPLAY_JI_WUGU_ZEREN = 4706;
		/** 乌骨责任金鸡（贵阳） */
		public static final int DISPLAY_JI_WUGU_ZEREN_GOLD = 4707;
		/** 普通鸡（贵阳） */
		public static final int DISPLAY_JI_NORMAL = 4708;
		/** 普通金鸡（贵阳） */
		public static final int DISPLAY_JI_NORMAL_GOLD = 4709;
		/** 普通鸡-乌骨（贵阳） */
		public static final int DISPLAY_JI_WUGU = 4710;
		/** 普通金鸡-乌骨（贵阳） */
		public static final int DISPLAY_JI_WUGU_GOLD = 4711;
		/** 本鸡（贵阳） */
		public static final int DISPLAY_JI_SELF = 4712;
		/** 金本鸡（贵阳） */
		public static final int DISPLAY_JI_SELF_GOLD = 4713;
		/** 翻牌鸡（贵阳） */
		public static final int DISPLAY_JI_FANPAI = 4714;
		/** 翻牌金鸡（贵阳） */
		public static final int DISPLAY_JI_FANPAI_GOLD = 4715;
		/** 标记责任鸡收分方（贵阳） */
		public static final int DISPLAY_JI_ZEREN_TARGET = 4716;

		/** 河源开始 */
		public static final int DISPLAY_HE_YUAN_START = 4800;
		/** 万能花牌(河源) */
		public static final int DISPLAY_HUA_PAI = 4888;
		/** 河源结束 */
		public static final int DISPLAY_HE_YUAN_END = 4899;

		/** 梅州开始 */
		public static final int DISPLAY_MEI_ZHOU_START = 4900;
		/** 万能红中(梅州) */
		public static final int DISPLAY_MASTER_HONG_ZHONG = 4966;
		/** 梅州结束 */
		public static final int DISPLAY_MEI_ZHOU_END = 4999;

		/** 封胡（广西-柳州） */
		public static final int DISPLAY_FENG_HU = 5000;
		/** 吃三笔（广西-柳州） */
		public static final int DISPLAY_CHI_SAN_BI = 5001;
		/** 单圈封（广西-柳州） */
		public static final int DISPLAY_DAN_QUAN_FENG_CARD = 5002;
		/** 单圈解封（广西-柳州） */
		public static final int DISPLAY_DAN_QUAN_JIE_FENG_CARD = 5003;
		/** 被吃三笔（广西-柳州） */
		public static final int DISPLAY_BEI_CHI_SAN_BI = 5004;

		/** 醒牌（广西-河池） */
		public static final int DISPLAY_XING = 5100;
		/** 中的醒牌（广西-河池） */
		public static final int DISPLAY_XING_CARDS = 5101;

		/** 搬子（江苏-扬州） */
		public static final int DISPLAY_BAN_ZI = 5200;
		/** 配子（江苏-扬州） */
		public static final int DISPLAY_PEI_ZI = 5201;

		/** 金牌（福建-宁德） */
		public static final int DISPLAY_JIN_PAI = 5300;

		/** 通用显示类型 */
		/** 用于在玩家头像上显示一个状态标识（例如：扬州玩家进园子标识） */
		public static final int DISPLAY_GENERAL_HEAD_IMAGE_FLAG = 9900;
		/** 通用显示类型 */
		public static final int DISPLAY_GENERAL_END = 9999;

		private static List<Integer> send2TargetRole = new ArrayList<Integer>(); // playType 发送给该玩家，不广播
		private static List<Integer> basicFan = new ArrayList<>(); // 基础番
		static
		{
			send2TargetRole.add(OPERATE_CAN_AN_GANG);
			send2TargetRole.add(OPERATE_CAN_GANG_A_CARD);
			send2TargetRole.add(OPERATE_CAN_BU_GANG_A_CARD);
			send2TargetRole.add(OPERATE_CAN_CHI_A_CARD);
			send2TargetRole.add(OPERATE_CAN_PENG_A_CARD);
			send2TargetRole.add(OPERATE_CAN_TING);
			send2TargetRole.add(OPERATE_CAN_TING_CARD);
			send2TargetRole.add(OPERATE_CAN_HU);
			send2TargetRole.add(OPERATE_CAN_AUTO_HU);
			send2TargetRole.add(OPERATE_CAN_PASS);
			send2TargetRole.add(OPERATE_PASS);
			send2TargetRole.add(OPERATE_CANCEL);
			send2TargetRole.add(OPERATE_WAIT);
			send2TargetRole.add(OPERATE_CHANGECARD);
			send2TargetRole.add(OPERATE_CHANGECARD_RULE);
			send2TargetRole.add(OPERATE_CHANGECARD_START);
			send2TargetRole.add(OPERATE_CHANGECARD_FINISH);
			send2TargetRole.add(OPERATE_LACK);
			send2TargetRole.add(OPERATE_LACK_START);
			send2TargetRole.add(OPERATE_DUN_LA_PAO);
			send2TargetRole.add(OPERATE_DUN_LA_PAO_START);
			send2TargetRole.add(OPERATE_MING_DA_START);
			send2TargetRole.add(OPERATE_SHANG_HUO_START);
			send2TargetRole.add(OPERATE_SHANG_HUO_END);
			send2TargetRole.add(OPERATE_PIAO_START);
			send2TargetRole.add(OPERATE_PIAO_END);
			send2TargetRole.add(OPERATE_CAN_FAN_A_CARD);

			send2TargetRole.add(DISPLAY_DEAL_BAO_CARD);
			send2TargetRole.add(DISPLAY_SHOW_BAO_CARD);
			send2TargetRole.add(OPERATE_SHANGJING_FU);
			send2TargetRole.add(OPERATE_SHANGJING_ZHENG);
			send2TargetRole.add(OPERATE_XIAJING_FU);
			send2TargetRole.add(OPERATE_XIAJING_ZHENG);

			send2TargetRole.add(DISPLAY_DAN_QUAN_FENG_CARD);
			send2TargetRole.add(DISPLAY_DAN_QUAN_JIE_FENG_CARD);

			send2TargetRole.add(DISPLAY_XING);
			send2TargetRole.add(DISPLAY_XING_CARDS);

			send2TargetRole.add(DISPLAY_XING);
			send2TargetRole.add(DISPLAY_XING_CARDS);

			send2TargetRole.add(DISPLAY_MASK_ALL_HAND_CARD);

			basicFan.add(HU_PING_HU);
			basicFan.add(HU_PENG_PENG_HU);
			basicFan.add(HU_JIANG_DUI);
			basicFan.add(HU_QI_DUI);
			basicFan.add(HU_TIAN_HU);
			basicFan.add(HU_DI_HU);

		}

		public static boolean isBasicFan(int playType)
		{
			return basicFan.contains(playType);
		}

		public static boolean isSend2Target(int playType)
		{
			return send2TargetRole.contains(playType);
		}

		public static boolean isChiPengGangHu(int playType)
		{
			return isChiPengGang(playType) || playType == OPERATE_HU;
		}

		public static boolean isChiPengGang(int playType)
		{
			return isGangOperator(playType) || playType == OPERATE_PENG_A_CARD || playType == OPERATE_CHI_A_CARD;
		}

		public static boolean isChiPeng(int playType)
		{
			return playType == OPERATE_PENG_A_CARD || playType == OPERATE_CHI_A_CARD;
		}

		public static boolean isGangOperator(int playType)
		{
			return playType == OPERATE_AN_GANG || playType == OPERATE_BU_GANG_A_CARD || playType == OPERATE_GANG_A_CARD;
		}

		public static boolean isPengGang(int playType)
		{
			return isGangOperator(playType) || playType == OPERATE_PENG_A_CARD;
		}

		public static boolean isHuType(int playType)
		{
			return playType >= HU_PING_HU && playType < HU_END;
		}

		public static boolean isHistoryType(int playType)
		{
			return isChiPengGangHu(playType) || playType == PlayType.DISPLAY_SHOW_MASTER_CARD || playType == PlayType.DISPLAY_SHOW_BAO_CARD || playType == PlayType.DISPLAY_EX_CARD
				|| playType == PlayType.OPERATE_SHANGJING_FU || playType == PlayType.OPERATE_SHANGJING_ZHENG || playType == PlayType.DISPLAY_MASTER_HONG_ZHONG
				|| playType == PlayType.DISPLAY_ZHENG_CARD || playType == PlayType.DISPLAY_HUA_PAI;
		}
	}

	/** 等待队列配置信息 */
	public static class WaitSquenceConfig
	{
		// playTye 转化 ：<CanPlayType, PlayType>
		private static ConcurrentHashMap<Integer, Integer> playType_convert = new ConcurrentHashMap<Integer, Integer>();
		// 优先级 : <CanPlayType, priority>
		private static ConcurrentHashMap<Integer, Integer> playType_priority = new ConcurrentHashMap<Integer, Integer>();

		static
		{
			playType_convert.put(PlayType.OPERATE_CAN_PLAY_A_CARD, PlayType.OPERATE_PLAY_A_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_PLAY_A_CARD_HIDE, PlayType.OPERATE_PLAY_A_CARD_HIDE);
			playType_convert.put(PlayType.OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD, PlayType.OPERATE_PLAY_A_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_AN_GANG, PlayType.OPERATE_AN_GANG);
			playType_convert.put(PlayType.OPERATE_CAN_GANG_A_CARD, PlayType.OPERATE_GANG_A_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_BU_GANG_A_CARD, PlayType.OPERATE_BU_GANG_A_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_CHI_A_CARD, PlayType.OPERATE_CHI_A_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_PENG_A_CARD, PlayType.OPERATE_PENG_A_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_AUTO_HU, PlayType.OPERATE_HU);
			playType_convert.put(PlayType.OPERATE_CAN_HU, PlayType.OPERATE_HU);
			playType_convert.put(PlayType.OPERATE_CAN_TING, PlayType.OPERATE_TING);
			playType_convert.put(PlayType.OPERATE_CAN_TING_CARD, PlayType.OPERATE_TING_CARD);
			playType_convert.put(PlayType.OPERATE_CAN_PASS, PlayType.OPERATE_PASS);
			playType_convert.put(PlayType.OPERATE_CAN_FAN_A_CARD, PlayType.OPERATE_FAN_A_CARD);

			playType_priority.put(PlayType.OPERATE_CAN_HU, 10);
			playType_priority.put(PlayType.OPERATE_CAN_AUTO_HU, 10);
			playType_priority.put(PlayType.OPERATE_CAN_TING_CARD, 9);
			playType_priority.put(PlayType.OPERATE_CAN_AN_GANG, 8);
			playType_priority.put(PlayType.OPERATE_CAN_BU_GANG_A_CARD, 7);
			playType_priority.put(PlayType.OPERATE_CAN_GANG_A_CARD, 6);
			playType_priority.put(PlayType.OPERATE_CAN_PENG_A_CARD, 5);
			playType_priority.put(PlayType.OPERATE_CAN_CHI_A_CARD, 4);
			playType_priority.put(PlayType.OPERATE_CAN_TING, 3);
			playType_priority.put(PlayType.OPERATE_CAN_PLAY_A_CARD, 1);
			playType_priority.put(PlayType.OPERATE_CAN_PLAY_A_CARD_HIDE, 1);
			playType_priority.put(PlayType.OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD, 1);
			playType_priority.put(PlayType.OPERATE_CAN_FAN_A_CARD, 1);
			playType_priority.put(PlayType.OPERATE_CAN_PASS, 0);
		}

		/**
		 * 将可以进行的操作类型转化为操作后的类型
		 * 
		 * @param playType
		 * @return
		 */
		public static int convertPlayType(int playType)
		{
			if (playType_convert.containsKey(playType))
				return playType_convert.get(playType);
			else
				return playType;
		}

		/**
		 * 获取优先级
		 * 
		 * @param playType
		 * @return
		 */
		public static int getPriority(int playType)
		{
			if (playType_priority.containsKey(playType))
				return playType_priority.get(playType);
			else if (playType_convert.containsKey(playType))
				return playType_priority.get(convertPlayType(playType));
			else
				return 0;
		}
	}

	public enum CardType
	{
		INVALID(0),

		/** 万 */
		WAN(1 + 10 * 0),

		/** 条 */
		TIAO(1 + 10 * 1),

		/** 筒 */
		TONG(1 + 10 * 2),

		/** 字牌, 东南西北中发白 */
		ZI(1 + 10 * 3),

		/** 风牌， 东南西北 */
		FENG(1 + 10 * 3),

		/** 箭牌， 中发白 */
		JIAN(1 + 10 * 3 + BattleConst.FENG_CARD_COUNT),

		/** 花牌, 春夏秋冬梅兰竹菊 */
		HUA(1 + 10 * 4),

		/** 花牌，春夏秋冬 */
		SEASON(1 + 10 * 4),

		/** 花牌，梅兰竹菊 */
		FLOWER(1 + 10 * 4 + BattleConst.HUA_SEASON_COUNT),

		/** 总数量 */
		TOTAL_COUNT(1 + 10 * 4 + BattleConst.HUA_CARD_COUNT);

		private byte value;

		private CardType(int value)
		{
			this.value = (byte)value;
		}

		/**
		 * 获取这种花色的第一张
		 * 
		 * @return
		 */
		public byte Value()
		{
			return this.value;
		}

		/**
		 * 这种花色的最大值
		 * 
		 * @return
		 */
		public byte MaxValue()
		{
			if (compareTo(CardType.INVALID) == 0)
				return this.value;
			else if (compareTo(ZI) == 0)
				return (byte)(this.value + BattleConst.ZI_CARD_COUNT);
			else if (compareTo(FENG) == 0)
				return (byte)(this.value + BattleConst.FENG_CARD_COUNT);
			else if (compareTo(JIAN) == 0)
				return (byte)(this.value + BattleConst.JIAN_CARD_COUNT);
			else if (compareTo(HUA) == 0)
				return (byte)(this.value + BattleConst.HUA_CARD_COUNT);
			else if (compareTo(SEASON) == 0)
				return (byte)(this.value + BattleConst.HUA_SEASON_COUNT);
			else if (compareTo(FLOWER) == 0)
				return (byte)(this.value + BattleConst.HUA_FLOWER_COUNT);
			else
				return (byte)(this.value + BattleConst.NUMBER_CARD_COUNT);
		}

		/**
		 * 获取当前类型在总类型里面的序号
		 * 
		 * 一般用于遍历有Type构成的数组
		 */
		public int getTypeIndex()
		{
			switch (getCardType(this.value))
			{
				case WAN:
					return 0;
				case TIAO:
					return 1;
				case TONG:
					return 2;
				case ZI:
					return 3;
				case HUA:
					return 4;
				default:
					return 5;
			}
		}

		/**
		 * 获取总共有多少类型
		 * 
		 * 一般用于遍历有Type构成的数组
		 */
		public static int getTypeCount()
		{
			return 5;
		}

		/**
		 * 每张牌的最大数量
		 * 
		 * @return
		 */
		public int oneCardMax()
		{
			if (compareTo(HUA) == 0 || compareTo(SEASON) == 0 || compareTo(FLOWER) == 0)
				return 1;
			else
				return BattleConst.ONE_CARD_MAX;
		}

		/**
		 * 判断card是否属于this
		 * 
		 * @param card
		 * @return true：card属于this false：card不属于this
		 */
		public boolean isBelongTo(byte card)
		{
			return card >= this.value && card < MaxValue();
		}

		/**
		 * 获取卡牌对应的CardType
		 * 
		 * @param card
		 * @return 返回卡的类型
		 */
		public static CardType getCardType(byte card)
		{
			if (CardType.WAN.isBelongTo(card))
				return CardType.WAN;
			else if (CardType.TIAO.isBelongTo(card))
				return CardType.TIAO;
			else if (CardType.TONG.isBelongTo(card))
				return CardType.TONG;
			else if (CardType.ZI.isBelongTo(card))
				return CardType.ZI;
			else if (CardType.HUA.isBelongTo(card))
				return CardType.HUA;

			return CardType.INVALID;
		}

		/**
		 * 获取这种花色的第index张牌的值
		 * 
		 * @param cardIndex 卡牌对应的index
		 * @return 这种花色的地index张卡牌对应的byte值
		 */
		public byte convertToCard(int cardIndex)
		{
			return (byte)(this.value + cardIndex);
		}

		/**
		 * 获取某一张卡牌与其所对应花色的第一张卡牌的差值
		 * 
		 * @param card 需要转换的卡牌
		 * @return 返回卡牌所在花色的index
		 */
		public static int convertToCardIndex(byte card)
		{
			CardType cardType = getCardType(card);
			return card - cardType.value;
		}

		/**
		 * 返回cardTypeValue对应的CardType实例
		 */
		public static CardType convertToCardType(byte cardTypeValue)
		{
			if (CardType.WAN.value == cardTypeValue)
				return CardType.WAN;
			if (CardType.TIAO.value == cardTypeValue)
				return CardType.TIAO;
			if (CardType.TONG.value == cardTypeValue)
				return CardType.TONG;
			if (CardType.ZI.value == cardTypeValue)
				return CardType.ZI;
			if (CardType.HUA.value == cardTypeValue)
				return CardType.HUA;
			return CardType.INVALID;
		}

		/**
		 * 这张牌是否是数字牌
		 */
		public static boolean isNumberCard(byte card)
		{
			return isNumberCardType(getCardType(card).Value());
		}

		/**
		 * 这张牌是否是数字牌的第一张
		 */
		public static boolean isNumberCardType(byte cardType)
		{
			return cardType == CardType.WAN.Value() || cardType == CardType.TIAO.Value() || cardType == CardType.TONG.Value();
		}

		/**
		 * @param cardType 判断cardType是否为万条筒中的一种
		 */
		public static boolean isNumberCardType(CardType cardType)
		{
			return cardType == CardType.WAN || cardType == CardType.TIAO || cardType == CardType.TONG;
		}

		/** 获取风箭牌类型 */
		public static CardType getFengJianType(byte card)
		{
			if (FENG.isBelongTo(card))
				return FENG;
			else if (JIAN.isBelongTo(card))
				return JIAN;
			else
				return CardType.INVALID;
		}

		/**
		 * 获取card的下一序列牌
		 * 
		 * @param card
		 * @param checkFengJian 是否检测风箭
		 * @return
		 */
		public static byte getNextCard(byte card, boolean checkFengJian)
		{
			CardType cardType = getCardType(card);
			Macro.AssetFalse(cardType != CardType.INVALID);

			if (cardType == CardType.ZI && checkFengJian)
				cardType = getFengJianType(card);

			if (card < cardType.MaxValue() - 1)
				return (byte)(card + 1);
			else
				return cardType.Value();
		}

		/**
		 * 获取card的上一序列牌
		 * 
		 * @param card
		 * @param checkFengJian 是否检测风箭
		 * @return
		 */
		public static byte getPreCard(byte card, boolean checkFengJian)
		{
			CardType cardType = getCardType(card);
			Macro.AssetFalse(cardType != CardType.INVALID);

			if (cardType == CardType.ZI && checkFengJian)
				cardType = getFengJianType(card);

			if (card > cardType.Value())
				return (byte)(card - 1);
			else
				return (byte)(cardType.MaxValue() - 1);
		}
	}
	
	/** 牌局回放配置 */
	public static class PlaybackConfig
	{
		/** 需要记录的回放数据 */
		private static List<Integer> recordTypes = new ArrayList<Integer>();

		static
		{
			recordTypes.add(PlayType.OPERATE_CAN_HU);
			recordTypes.add(PlayType.OPERATE_CAN_AUTO_HU);
			recordTypes.add(PlayType.OPERATE_CAN_TING_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_AN_GANG);
			recordTypes.add(PlayType.OPERATE_CAN_BU_GANG_A_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_GANG_A_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_PENG_A_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_CHI_A_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_PLAY_A_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_AUTO_PLAY_LAST_DEALED_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_FAN_A_CARD);
			recordTypes.add(PlayType.OPERATE_CAN_PASS);

			recordTypes.add(PlayType.OPERATE_LACK_START);
			recordTypes.add(PlayType.OPERATE_LACK_FINISH);
			recordTypes.add(PlayType.OPERATE_DEAL);
			recordTypes.add(PlayType.OPERATE_PLAY_A_CARD);
			recordTypes.add(PlayType.OPERATE_GANG_A_CARD);
			recordTypes.add(PlayType.OPERATE_AN_GANG);
			recordTypes.add(PlayType.OPERATE_BU_GANG_A_CARD);
			recordTypes.add(PlayType.OPERATE_PENG_A_CARD);
			recordTypes.add(PlayType.OPERATE_CHI_A_CARD);
			recordTypes.add(PlayType.OPERATE_HU);
			recordTypes.add(PlayType.OPERATE_CANCEL);
			recordTypes.add(PlayType.OPERATE_PASS);
			recordTypes.add(PlayType.OPERATE_TING_CARD);
			recordTypes.add(PlayType.DISPLAY_TING);
			recordTypes.add(PlayType.DISPLAY_EX_CARD);
			recordTypes.add(PlayType.DISPLAY_BE_GANG);
			recordTypes.add(PlayType.DISPLAY_BE_PENG);
			recordTypes.add(PlayType.DISPLAY_BE_CHI);

			// 贵阳
			recordTypes.add(PlayType.DISPLAY_JI_FANPAI);
			recordTypes.add(PlayType.DISPLAY_JI_SELF);
		}

		public static boolean enableRecord(int playType)
		{
			return recordTypes.contains(playType);
		}
	}
}
