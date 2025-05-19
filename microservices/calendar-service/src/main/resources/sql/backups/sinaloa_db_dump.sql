-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table sinaloa_db.activities
CREATE TABLE IF NOT EXISTS `activities` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `status` smallint NOT NULL DEFAULT '0',
  `event_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `activities_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.activities: ~0 rows (approximately)
DELETE FROM `activities`;

-- Dumping structure for table sinaloa_db.events
CREATE TABLE IF NOT EXISTS `events` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `start_date` timestamp NOT NULL,
  `end_date` timestamp NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  `creator_id` bigint unsigned DEFAULT NULL,
  `group_id` bigint unsigned DEFAULT NULL,
  `priority` smallint unsigned NOT NULL DEFAULT '0',
  `category_id` bigint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `creator_id` (`creator_id`),
  KEY `group_id` (`group_id`),
  KEY `events_categories_fk` (`category_id`),
  CONSTRAINT `events_categories_fk` FOREIGN KEY (`category_id`) REFERENCES `event_categories` (`id`) ON DELETE CASCADE,
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`),
  CONSTRAINT `events_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.events: ~0 rows (approximately)
DELETE FROM `events`;

-- Dumping structure for table sinaloa_db.event_categories
CREATE TABLE IF NOT EXISTS `event_categories` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `group_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `event_categories_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.event_categories: ~0 rows (approximately)
DELETE FROM `event_categories`;

-- Dumping structure for table sinaloa_db.flyway_schema_history
CREATE TABLE IF NOT EXISTS `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.flyway_schema_history: ~0 rows (approximately)
DELETE FROM `flyway_schema_history`;
INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
	(1, '1', 'create users table', 'SQL', 'V1__create_users_table.sql', 1113432830, 'root', '2025-03-27 17:32:32', 869, 1),
	(2, '2', 'create groups table', 'SQL', 'V2__create_groups_table.sql', -703433410, 'root', '2025-03-27 17:32:32', 311, 1),
	(3, '3', 'create memberships table', 'SQL', 'V3__create_memberships_table.sql', 1463477171, 'root', '2025-03-27 17:32:33', 211, 1),
	(4, '4', 'create events table', 'SQL', 'V4__create_events_table.sql', 914574922, 'root', '2025-03-27 17:32:33', 199, 1),
	(5, '5', 'create activities table', 'SQL', 'V5__create_activities_table.sql', 714943603, 'root', '2025-03-27 17:32:33', 321, 1),
	(6, '6', 'create system roles table', 'SQL', 'V6__create_system_roles_table.sql', -216868583, 'root', '2025-03-27 17:32:34', 297, 1),
	(7, '7', 'create member roles table', 'SQL', 'V7__create_member_roles_table.sql', -516964843, 'root', '2025-03-27 17:32:34', 217, 1),
	(8, '8', 'create users system roles table', 'SQL', 'V8__create_users_system_roles_table.sql', -1628160259, 'root', '2025-03-27 17:32:34', 339, 1),
	(9, '9', 'create member roles  memberships table', 'SQL', 'V9__create_member_roles__memberships_table.sql', 1789830806, 'root', '2025-03-27 17:32:35', 392, 1),
	(10, '10', 'create event categories table', 'SQL', 'V10__create_event_categories_table.sql', 56940624, 'root', '2025-03-27 17:32:35', 221, 1),
	(11, '11', 'add priority and category id columns to events table', 'SQL', 'V11__add_priority_and_category_id_columns_to_events_table.sql', -1136277869, 'root', '2025-03-27 17:32:36', 490, 1);

-- Dumping structure for table sinaloa_db.groups
CREATE TABLE IF NOT EXISTS `groups` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  `creator_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `creator_id` (`creator_id`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.groups: ~3 rows (approximately)
DELETE FROM `groups`;
INSERT INTO `groups` (`id`, `name`, `created_at`, `updated_at`, `creator_id`) VALUES
	(1, 'Public Group', '2025-03-27 17:32:45', '2025-03-27 17:32:45', 1),
	(2, 'Grupo Privado', '2025-03-27 17:32:45', '2025-03-27 17:32:45', 1),
	(3, 'Grupo Privado Manolin', '2025-03-27 17:32:45', '2025-03-27 17:32:45', 2);

-- Dumping structure for table sinaloa_db.memberships
CREATE TABLE IF NOT EXISTS `memberships` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `group_id` bigint unsigned NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `memberships_users_fk` (`user_id`),
  KEY `memberships_groups_fk` (`group_id`),
  CONSTRAINT `memberships_groups_fk` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `memberships_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.memberships: ~21 rows (approximately)
DELETE FROM `memberships`;
INSERT INTO `memberships` (`id`, `user_id`, `group_id`, `created_at`, `updated_at`) VALUES
	(1, 1, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(2, 7, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(3, 5, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(4, 6, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(5, 4, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(6, 3, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(7, 10, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(8, 2, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(9, 8, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(10, 9, 1, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(11, 4, 2, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(12, 1, 2, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(13, 5, 2, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(14, 2, 2, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(15, 3, 2, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(16, 5, 3, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(17, 7, 3, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(18, 2, 3, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(19, 6, 3, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(20, 4, 3, '2025-03-27 17:32:45', '2025-03-27 17:32:45'),
	(21, 3, 3, '2025-03-27 17:32:45', '2025-03-27 17:32:45');

-- Dumping structure for table sinaloa_db.member_roles
CREATE TABLE IF NOT EXISTS `member_roles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_roles_roles_name_unique` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.member_roles: ~8 rows (approximately)
DELETE FROM `member_roles`;
INSERT INTO `member_roles` (`id`, `name`) VALUES
	(2, 'Administrador'),
	(3, 'Agregar Miembros'),
	(1, 'Creador'),
	(7, 'Descarga de Archivos'),
	(6, 'Editor General de Eventos'),
	(5, 'Escritura de Eventos'),
	(4, 'Lectura de Eventos'),
	(8, 'Subida de Archivos');

-- Dumping structure for table sinaloa_db.member_roles_memberships
CREATE TABLE IF NOT EXISTS `member_roles_memberships` (
  `membership_id` bigint unsigned NOT NULL,
  `member_role_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`membership_id`,`member_role_id`),
  KEY `member_roles_memberships_pk_member_roles_fk` (`member_role_id`),
  CONSTRAINT `member_roles_memberships_pk_member_roles_fk` FOREIGN KEY (`member_role_id`) REFERENCES `member_roles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `member_roles_memberships_pk_memberships_fk` FOREIGN KEY (`membership_id`) REFERENCES `memberships` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.member_roles_memberships: ~42 rows (approximately)
DELETE FROM `member_roles_memberships`;
INSERT INTO `member_roles_memberships` (`membership_id`, `member_role_id`) VALUES
	(1, 4),
	(2, 4),
	(3, 4),
	(4, 4),
	(5, 4),
	(6, 4),
	(7, 4),
	(8, 4),
	(9, 4),
	(10, 4),
	(11, 4),
	(12, 4),
	(13, 4),
	(14, 4),
	(15, 4),
	(16, 4),
	(17, 4),
	(18, 4),
	(19, 4),
	(20, 4),
	(21, 4),
	(1, 7),
	(2, 7),
	(3, 7),
	(4, 7),
	(5, 7),
	(6, 7),
	(7, 7),
	(8, 7),
	(9, 7),
	(10, 7),
	(11, 7),
	(12, 7),
	(13, 7),
	(14, 7),
	(15, 7),
	(16, 7),
	(17, 7),
	(18, 7),
	(19, 7),
	(20, 7),
	(21, 7);

-- Dumping structure for table sinaloa_db.system_roles
CREATE TABLE IF NOT EXISTS `system_roles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `system_roles_name_unique` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.system_roles: ~2 rows (approximately)
DELETE FROM `system_roles`;
INSERT INTO `system_roles` (`id`, `name`) VALUES
	(2, 'ROLE_ADMIN'),
	(1, 'ROLE_SUPERUSER'),
	(3, 'ROLE_USER');

-- Dumping structure for table sinaloa_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `username` varchar(32) NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_unique` (`email`),
  UNIQUE KEY `users_unique_1` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.users: ~10 rows (approximately)
DELETE FROM `users`;
INSERT INTO `users` (`id`, `email`, `password`, `username`, `created_at`, `updated_at`, `deleted_at`) VALUES
	(1, 'arribamana@gmail.com', '$2a$10$wvTntsrlb4a0tRZAD/4gMOyOqF7ahgjtVFoSM/D8jckcRkCZPmGA2', 'SinaloaAdmin', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(2, 'manolin@gmail.com', '$2a$10$viaT5FSdMblFDGDNKzmwgeDAgpH.HFhIad.pwzhqayhyAAb4WYLSq', 'manolin92', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(3, 'chilinski@gmail.com', '$2a$10$1b79THfZSs7U1yLE/xyZqOiVCOhiyX93p3ROoQM1Bar.OZx6jXtia', 'chilinsk4i', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(4, 'rosalamanguera@outlook.com', '$2a$10$oeEOny2G1epQHLkoI7d.oueEaRormy2Han6cn7Dqq3bHNVGmXTw8m', 'rosameltrozo', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(5, 'romasigloi@gmail.com', '$2a$10$lLEOWrnQV4h5NC6YoujyquvwGpS7qnm9RArJRXSihAHdYW8iOLggC', 'romuloYremo', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(6, 'maclovin12@gmail.com', '$2a$10$A/0dhyTDsV96c5KhZAWWkeAMy4uuaInW4qNHIrf9YGZUpDzHxCPSO', 'maclovin12', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(7, 'kakaroto23@gmail.com', '$2a$10$IJkOEU4YZcLpq8Kd7ncgd.UIcwCsfZU3xBItzM1LnnQbwFLgJso5K', 'kakaroto23', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(8, 'heisenberg97@gmail.com', '$2a$10$UIoAx7lj4Nd.LM/oWUiUNuxW8ea/LP4iyJpThDqSgbVbhoEeLRMAe', 'heisenberg97', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(9, 'fabianruelas1@gmail.com', '$2a$10$1.sTLeiVpcJt0.IrpUMbjO9yv6PVBtqQ8eoAOQGaRU.Ne31YAKJoO', 'fabianruelas1', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL),
	(10, 'rodolfocardenas@gmail.com', '$2a$10$4jIk9GqVOBsoncpQbuvKDePAQQwSuEsvFQrLjsIAj1Q5vQOnZHjJq', 'rodolfocardenas', '2025-03-27 17:32:45', '2025-03-27 17:32:45', NULL);

-- Dumping structure for table sinaloa_db.users_system_roles
CREATE TABLE IF NOT EXISTS `users_system_roles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `system_role_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `users_system_roles_users_fk` (`user_id`),
  KEY `users_system_roles_system_roles_fk` (`system_role_id`),
  CONSTRAINT `users_system_roles_system_roles_fk` FOREIGN KEY (`system_role_id`) REFERENCES `system_roles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `users_system_roles_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table sinaloa_db.users_system_roles: ~12 rows (approximately)
DELETE FROM `users_system_roles`;
INSERT INTO `users_system_roles` (`id`, `user_id`, `system_role_id`) VALUES
	(1, 1, 1),
	(2, 1, 3),
	(3, 1, 2),
	(4, 2, 3),
	(5, 3, 3),
	(6, 4, 3),
	(7, 5, 3),
	(8, 6, 3),
	(9, 7, 3),
	(10, 8, 3),
	(11, 9, 3),
	(12, 10, 3);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
