/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.15 : Database - game
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`game` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `game`;

/*Table structure for table `game_activity_reward_table` */

DROP TABLE IF EXISTS `game_activity_reward_table`;

CREATE TABLE `game_activity_reward_table` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `game_activity_reward_table` */

insert  into `game_activity_reward_table`(`id`,`value`) values ('\0��\0\0\0\0\0\0Z#�P\0','\n雀：6钻\n雀：6钻\0\0\0\0\0\0d\0\0\0�	condition'),('\0��\0\0\0\0\0\0Z(��\0','\n雀：6钻\n雀：6钻\0\0\0\0\0\0d\0\0\0�	condition'),('\0��\0\0\0\0\0\0Z-�\0','\n雀：6钻\n雀：6钻\0\0\0\0\0\0d\0\0\0�	condition'),('\0��\0\0\0\0\0Z#�P\0','\n友：1钻\n友：1钻\0\0\0\0\0�\0\0�	condition'),('\0��\0\0\0\0\0Z(��\0','\n友：1钻\n友：1钻\0\0\0\0\0�\0\0�	condition'),('\0��\0\0\0\0\0Z-�\0','\n友：1钻\n友：1钻\0\0\0\0\0�\0\0�	condition'),('\0��\0\0\0\0\0Z#�P\0','\n会：2钻\n会：2钻\0\0\0\0\0,\0\0,	condition'),('\0��\0\0\0\0\0Z(��\0','\n会：2钻\n会：2钻\0\0\0\0\0,\0\0,	condition'),('\0��\0\0\0\0\0Z-�\0','\n会：2钻\n会：2钻\0\0\0\0\0,\0\0,	condition'),('\0��\0\0\0\0\0Z#�P\0','欢：18钻欢：18钻\0\0\0\0\0\0\0\0\0\n	condition'),('\0��\0\0\0\0\0Z(��\0','欢：18钻欢：18钻\0\0\0\0\0\0\0\0\0\n	condition'),('\0��\0\0\0\0\0Z-�\0','欢：18钻欢：18钻\0\0\0\0\0\0\0\0\0\n	condition'),('\0��\0\0\0\0\0Z#�P\0','\n庆：5钻\n庆：5钻\0\0\0\0\0\0\0\0\0	condition'),('\0��\0\0\0\0\0Z(��\0','\n庆：5钻\n庆：5钻\0\0\0\0\0\0\0\0\0	condition'),('\0��\0\0\0\0\0Z-�\0','\n庆：5钻\n庆：5钻\0\0\0\0\0\0\0\0\0	condition'),('\0��\0\0\0\0\0Z#�P\0','\n元：3钻\n元：3钻\0\0\0\0\0\0�\0\0	condition'),('\0��\0\0\0\0\0Z(��\0','\n元：3钻\n元：3钻\0\0\0\0\0\0�\0\0	condition'),('\0��\0\0\0\0\0Z-�\0','\n元：3钻\n元：3钻\0\0\0\0\0\0�\0\0	condition'),('\0��\0\0\0\0\0Z#�P\0','\n宵：8钻\n宵：8钻\0\0\0\0\0\0\0\0\0	condition'),('\0��\0\0\0\0\0Z(��\0','\n宵：8钻\n宵：8钻\0\0\0\0\0\0\0\0\0	condition'),('\0��\0\0\0\0\0Z-�\0','\n宵：8钻\n宵：8钻\0\0\0\0\0\0\0\0\0	condition'),('\0��\0\0\0\0\0Z#�P\0','游戏愉快游戏愉快\0\0\0���\0\0!4	condition\0\0\0'),('\0��\0\0\0\0\0Z(��\0','游戏愉快游戏愉快\0\0\0���\0\0!4	condition\0\0\0'),('\0��\0\0\0\0\0Z-�\0','游戏愉快游戏愉快\0\0\0���\0\0!4	condition\0\0\0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
