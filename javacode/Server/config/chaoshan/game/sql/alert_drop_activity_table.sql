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

DROP TABLE IF EXISTS activity_rank_bak_cj;
DROP TABLE IF EXISTS activity_history_rank_bak_cj;
DROP TABLE IF EXISTS game_activity_reward_table_bak_cj;
DROP TABLE IF EXISTS games_activity_turntable_reward_bak_cj;
DROP TABLE IF EXISTS last_reward_info_table_bak_cj;
DROP TABLE IF EXISTS turntable_reward_dispatch_table_bak_cj;

CREATE TABLE IF NOT EXISTS `activity_rank` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `activity_history_rank` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `game_activity_reward_table` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `games_activity_turntable_reward` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `last_reward_info_table` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `turntable_reward_dispatch_table` (
  `id` varbinary(767) NOT NULL,
  `value` mediumblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table activity_rank rename activity_rank_bak_cj;
alter table activity_history_rank rename activity_history_rank_bak_cj;
alter table game_activity_reward_table rename game_activity_reward_table_bak_cj;
alter table games_activity_turntable_reward rename games_activity_turntable_reward_bak_cj;
alter table last_reward_info_table rename last_reward_info_table_bak_cj;
alter table turntable_reward_dispatch_table rename turntable_reward_dispatch_table_bak_cj;

DROP TABLE IF EXISTS activity_rank;
DROP TABLE IF EXISTS activity_history_rank;
DROP TABLE IF EXISTS game_activity_reward_table;
DROP TABLE IF EXISTS games_activity_turntable_reward;
DROP TABLE IF EXISTS last_reward_info_table;
DROP TABLE IF EXISTS turntable_reward_dispatch_table;