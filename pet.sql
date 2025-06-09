/*
 Navicat MySQL Data Transfer

 Source Server         : pet
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : pet

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 04/05/2025 20:13:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `articleId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `articleName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `articleContent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `articlePic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `articleUserName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `articleTag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `articleUserPic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `articleCreateTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`articleId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('1', '救助八只可怜猫猫吧', '它们在寒冷的冬天里，瑟瑟发抖，没有一个栖息的地方。', '2530a1d5ae47ef75ec078c09f4a1ad2.png', '动物委员会', '救助', 'http://127.0.0.1:9000/pet/account_circle.png', '2025-04-02 19:23:56');
INSERT INTO `article` VALUES ('10', '爱宠无忧，健康成长', '高蛋白低敏配方：单一动物蛋白来源（牛肉/鸭肉/三文鱼），肠胃敏感宠物也能放心吃！美毛亮肤：添加Omega-3&6，减少掉毛，皮毛顺滑如绸缎✨权威认证：通过AAFCO标准，兽医联合推荐。', 'ecbb71b342285e0c90bfe84ab246700.png', '爱宠嘉', '广告', 'http://127.0.0.1:9000/pet/icon-accounts.png', '2025-04-03 08:13:09');
INSERT INTO `article` VALUES ('2', '你的一次善举，它的一生幸福', '在这个世界上，有很多无助的流浪动物，他们没有家，缺乏食物和医疗的照顾，每天都在艰难地挣扎求生。你的一份捐款，可能无法改变这个世界，但却能够改变它们的一生。请伸出你温暖的双手，帮助这些生命获得生存的机会，让它们不再孤单无助，感受到这个世界的温暖。', '2b07a176ccdd3fb14f2cecfc808cc63.png', '动物委员会', '救助', 'http://127.0.0.1:9000/pet/account_circle.png', '2025-04-03 08:12:41');
INSERT INTO `article` VALUES ('3', '贴心呵护，宠爱无限', '精选人类食用级原料：澳洲牛肉、三文鱼、走地鸡胸肉，0防腐剂、0诱食剂。 高蛋白低脂肪，呵护宠物关节与心脏健康。', '4c4de3a5917c2eceec0bc1cae67c765.png', '爱宠嘉', '广告', 'http://127.0.0.1:9000/pet/icon-accounts.png', '2025-04-03 08:12:44');
INSERT INTO `article` VALUES ('4', '拯救一只流浪动物，收获一生温暖', '每一只流浪动物都有它自己的故事，可能是曾经幸福的宠物，也可能是从未见过爱的孤儿。它们无法为自己发声，无法为自己争取帮助，而我们是它们唯一的希望。您的捐款将直接用于救助这些无家可归的动物，帮助它们治疗伤病、提供食物和避风的栖身之所，让它们重新拥有一个温暖的家。', '894618ac4bdc911c603330626168af7.png', '动物委员会', '救助', 'http://127.0.0.1:9000/pet/account_circle.png', '2025-04-03 08:12:47');
INSERT INTO `article` VALUES ('5', '改变它的命运，你，就是奇迹！', '每天都有无数流浪动物在街头游荡，忍受饥饿与孤独，承受着生命中的种种磨难。它们没有家，没有亲人，没有人给它们关爱。它们的生命如此脆弱，每一分捐款，都是对它们的帮助与鼓励。你的捐赠不仅能够为它们提供温暖的庇护所、营养丰富的食物和必要的医疗援助，还能为它们带来生存的希望。', '90909945b4ca92d94fa92d3af7c7283.png', '动物委员会', '救助', 'http://127.0.0.1:9000/pet/account_circle.png', '2025-04-03 08:12:51');
INSERT INTO `article` VALUES ('6', '小小善举，大大温暖', '对于很多流浪动物来说，街头是它们唯一的家。每天它们都面临着无尽的饥饿、寒冷和疾病，难以逃脱生命的困境。而你的一点帮助，或许就能改变它们的命运。我们正在努力救助这些无家可归的动物，给它们提供食物、庇护和必要的医疗。', '9e48592fe5cb1f9aeca3860366f8a40.png', '动物委员会', '救助', 'http://127.0.0.1:9000/pet/account_circle.png', '2025-04-03 08:12:55');
INSERT INTO `article` VALUES ('7', '萌宠天堂，一站式宠爱', '🦷 洁齿系列：添加薄荷纤维，减少牙垢，口气清新。🐱 挑食救星：独家低温烘焙工艺，锁住肉香，连“玻璃胃”猫咪也疯狂。', 'a1d46fcdb6647e54499d55b065361d8.png', '爱宠嘉', '广告', 'http://127.0.0.1:9000/pet/icon-accounts.png', '2025-04-03 08:12:58');
INSERT INTO `article` VALUES ('8', '每一个被遗弃的生命，都渴望被爱', '我们身边有很多因为无助而遭遇困境的流浪动物，它们没有地方栖息、没有食物来源，甚至有的还遭受着疾病和伤害。每一只无家可归的动物都是需要帮助的生命，而你的捐款就是它们生活的希望。', 'beee0143e3664d54a8567ca0bac019d.png', '动物委员会', '救助', 'http://127.0.0.1:9000/pet/account_circle.png', '2025-04-03 08:13:02');
INSERT INTO `article` VALUES ('9', '最好的爱，给最忠实的朋友', '对宠物说：“你拆家时的‘恶魔行为’，我忍了；你凌晨5点的叫早服务，我认了；但你的健康，我绝不妥协！——爱宠嘉零食，只为给你最好的。”对主人说：“加班回家，它扑向你时的期待眼神…别再用廉价零食敷衍TA的爱❤️”', 'ca20af830776b611d53e722d9ac1eeb.png', '爱宠嘉', '广告', 'http://127.0.0.1:9000/pet/icon-accounts.png', '2025-04-03 08:13:05');

-- ----------------------------
-- Table structure for dialogue
-- ----------------------------
DROP TABLE IF EXISTS `dialogue`;
CREATE TABLE `dialogue`  (
  `dialogueId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dialogueUserId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dialogueContent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dialoguePid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dialogueTime` datetime NULL DEFAULT NULL,
  `dialogueRead` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`dialogueId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dialogue
-- ----------------------------
INSERT INTO `dialogue` VALUES ('1', '1', '你好啊', '2', '2025-04-04 09:50:59', '1');
INSERT INTO `dialogue` VALUES ('123adbe35b7e2c80b5aa2972e994eff5', '2', '好的', '3', '2025-04-21 20:44:42', '0');
INSERT INTO `dialogue` VALUES ('2', '2', '你好', '1', '2025-04-05 09:51:26', '1');
INSERT INTO `dialogue` VALUES ('3', '1', '你的小猫很可爱', '2', '2025-04-06 09:51:52', '1');
INSERT INTO `dialogue` VALUES ('4', '3', '你好', '2', '2025-04-07 11:00:01', '1');
INSERT INTO `dialogue` VALUES ('5', '3', '你家猫猫在那家医院看病啊？', '2', '2025-04-07 11:00:35', '1');
INSERT INTO `dialogue` VALUES ('6', '3', '我家猫生病了', '2', '2025-04-07 12:01:05', '1');

-- ----------------------------
-- Table structure for dongwu
-- ----------------------------
DROP TABLE IF EXISTS `dongwu`;
CREATE TABLE `dongwu`  (
  `animalId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `animalName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalContent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalTag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalGender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalYang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalYM` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalTime` datetime NULL DEFAULT NULL,
  `reportId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalJue` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalYear` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `animalYuan` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`animalId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dongwu
-- ----------------------------
INSERT INTO `dongwu` VALUES ('1', '猫', '很小，在街上流浪', '1', '不知道', 'fba43096389429b95bd7ef9ea1eb25a.png', '0', '没打疫苗', '2025-04-04 11:30:42', '1', '没绝育', '不知', '流浪');
INSERT INTO `dongwu` VALUES ('2', '猫', '三只，关系挺好的，应该成年了', '1', '不知道', '2b3c6457b4370cea9119450fa8bfcde.png', '0', '没打疫苗', '2025-04-04 11:31:37', '2', '没绝育', '不知', '流浪');
INSERT INTO `dongwu` VALUES ('3', '猫', '应该成年了，很亲人', '1', '不知道', '072ba878c676be249d857acd1fd8455.png', '0', '没打疫苗', '2025-04-04 11:32:50', '3', '没绝育', '不知', '流浪');
INSERT INTO `dongwu` VALUES ('4', '狗', '应该成年了，长得楚楚可怜，很亲人', '2', '男孩子', 'a299cffa14754b6a771920495f9e717.png', '0', '没打疫苗', '2025-04-04 11:59:56', '4', '没绝育', '不知', '流浪');
INSERT INTO `dongwu` VALUES ('5', '狗', '很小一只，身上脏脏的', '2', '不知道', 'a22701ea188ec3c7d180e5cfc49d4a4.png', '0', '没打疫苗', '2025-04-04 12:01:53', '5', '没绝育', '不知', '流浪');
INSERT INTO `dongwu` VALUES ('6', '狗', '应该成年了，身上蛮干净的，亲人', '2', '女孩子', 'c69c1541d5243a3ac720f51c0488b30.png', '0', '没打疫苗', '2025-04-04 12:01:56', '6', '没绝育', '不知', '流浪');
INSERT INTO `dongwu` VALUES ('7', '猫', '应该成年了，长得憨憨的，很可爱', '1', '女孩子', 'f1efc7a66703cd075e7489302a0faa7.png', '0', '没打疫苗', '2025-04-04 12:01:59', '7', '没绝育', '不知', '流浪');

-- ----------------------------
-- Table structure for newsmessage
-- ----------------------------
DROP TABLE IF EXISTS `newsmessage`;
CREATE TABLE `newsmessage`  (
  `newsId` int NOT NULL AUTO_INCREMENT,
  `newsTitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `newsContent` varchar(1500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `newsTime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `newsImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`newsId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of newsmessage
-- ----------------------------
INSERT INTO `newsmessage` VALUES (40, '宠物的饮食管理', '宠物店（pet shop）是专门为宠物提供宠物用品零售、宠物美容、宠物寄养、宠物活体销售的场所。其经营项目一般包括宠物用品超市、活体销售、宠物美容、宠物寄养、宠物医疗、宠物乐园、宠物摄影、待产养护。', '2019-01-21 11:35:35', '-7f7e580c656f747.png');
INSERT INTO `newsmessage` VALUES (41, '宠物睡眠分析', '宠物店（pet shop）是专门为宠物提供宠物用品零售、宠物美容、宠物寄养、宠物活体销售的场所。其经营项目一般包括宠物用品超市、活体销售、宠物美容、宠物寄养、宠物医疗、宠物乐园、宠物摄影、待产养护。', '2019-01-21 11:36:00', '47020002a9cdee957096.jpg');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `reportId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportAddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportContent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportFile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportTagone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportTagtwo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportCover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportCunzai` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportUserId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportUserName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportTime` datetime NULL DEFAULT NULL,
  `reportLo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportLa` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`reportId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES ('1', '在北大街发现一只幼猫', '北京市朝阳区建国路123号', '在北大街的前面绿树旁边发现的，很小，眼睛也有毛病，不能睁开。', 'fba43096389429b95bd7ef9ea1eb25a.png', NULL, '1', '1', 'fba43096389429b95bd7ef9ea1eb25a.png', '存在', '1', '小明', '2025-04-04 11:00:24', '116.397500', '39.906200');
INSERT INTO `report` VALUES ('2', '在校园发现三只猫猫', '沈阳市浑南新区南一环路201号', '在xx校园内发现三只猫猫，已经给它们提供食物，看起来挺健康的', '2b3c6457b4370cea9119450fa8bfcde.png', NULL, '1', '1', '2b3c6457b4370cea9119450fa8bfcde.png', '存在', '1', '小明', '2025-04-04 11:02:41', '123.380000', '41.786500');
INSERT INTO `report` VALUES ('3', '在小区发现一只橘猫', '上海市浦东新区陆家嘴123号', '在xx小区的一号楼二单元附近发现一只流浪猫，很亲人。', '072ba878c676be249d857acd1fd8455.png', NULL, '1', '1', '072ba878c676be249d857acd1fd8455.png', '存在', '1', '小明', '2025-04-04 11:06:23', '121.473700', '31.230400');
INSERT INTO `report` VALUES ('37ef85a9-074e-4db6-9fa0-10db58df9b55', '猫', '中国 河北省 衡水市 桃城区 大庆东路', '请救救他', NULL, 'e90f2737-57c8-416f-9500-9480b388056d-video_20250417_194948.mp4', '2', NULL, '4fb45ff8-c5f1-4090-ba74-78612ac2046e-img-1736858530978c1a96ee40e74b37a0db0b6782f2174e7a816c829c76786887497320d112bf4ff.jpg', '不存在', '2', 'pony', '2025-04-21 20:39:58', '115.688135', '37.74722');
INSERT INTO `report` VALUES ('4', '在街道发现一只小狗', '成都市武侯区人民南路789号', '在xx街路口处发现一只棕色小狗，很可爱，还会摇尾巴。', NULL, '20250404_111127.mp4', '2', '2', 'a299cffa14754b6a771920495f9e717.png', '存在', '1', '小明', '2025-04-04 11:08:08', '104.066800', '30.572600');
INSERT INTO `report` VALUES ('5', '在垃圾桶处发现一只幼狗', '河北省衡水市桃城区河东街道滏东街与和平东路交汇处东北', '在xx街的垃圾堆积处发现一只小狗，大约3，4个月大。', 'a22701ea188ec3c7d180e5cfc49d4a4.png', NULL, '1', '2', 'a22701ea188ec3c7d180e5cfc49d4a4.png', '存在', '1', '小明', '2025-04-04 11:16:07', '115.716500', '​37.738200');
INSERT INTO `report` VALUES ('6', '在街道发现一只小狗', '中国 河北省 衡水市 桃城区 大庆东路', '在xx街道的东侧发现一只橘白色狗狗，很温顺，见到人会摇尾巴', 'c69c1541d5243a3ac720f51c0488b30.png', NULL, '1', '2', 'c69c1541d5243a3ac720f51c0488b30.png', '存在', '1', '小明', '2025-04-04 11:18:20', '115.670000', '​37.738900');
INSERT INTO `report` VALUES ('7', '在校区发现一个猫猫', '河北省衡水市桃城区永兴路与前进大街交叉口东北角安居馨苑小区1号楼1213号门店', '在xx学校的xx校区发现一只小猫，身上灰尘很多，但是外表看起来没什么病', 'f1efc7a66703cd075e7489302a0faa7.png', NULL, '1', '1', 'f1efc7a66703cd075e7489302a0faa7.png', '存在', '1', '小明', '2025-04-04 11:20:13', '115.698000', '37.745000');

-- ----------------------------
-- Table structure for reviewtb
-- ----------------------------
DROP TABLE IF EXISTS `reviewtb`;
CREATE TABLE `reviewtb`  (
  `reviewId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publishId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reviewContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reviewUserId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reviewUserName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reviewTime` datetime NULL DEFAULT NULL,
  `reviewUserImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`reviewId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reviewtb
-- ----------------------------
INSERT INTO `reviewtb` VALUES ('1', '1', '哈哈哈', '1', '小明', '2025-04-06 10:19:42', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('10', '4', '可爱', '1', '小明', '2025-04-06 10:28:04', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('11', '4', '好安稳', '2', 'pony', '2025-04-06 10:28:08', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('12', '4', '赞', '4', '小花', '2025-04-06 10:28:11', 'account_circle.png');
INSERT INTO `reviewtb` VALUES ('13', '5', '好聪明', '1', '小明', '2025-04-06 10:28:14', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('14', '5', '好厉害', '2', 'pony', '2025-04-06 10:28:18', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('15', '5', '哈哈哈哈', '4', '小花', '2025-04-06 10:28:21', 'account_circle.png');
INSERT INTO `reviewtb` VALUES ('16', '6', '快陪它玩', '1', '小明', '2025-04-06 10:28:25', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('17', '6', '可爱', '2', 'pony', '2025-04-06 10:28:32', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('18', '6', '猫猫', '4', '小花', '2025-04-06 10:28:36', 'account_circle.png');
INSERT INTO `reviewtb` VALUES ('19', '7', '哈哈哈哈', '1', '小明', '2025-04-06 10:28:40', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('2', '1', '它真的好爱吃饭', '2', 'pony', '2025-04-06 10:20:19', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('20', '7', '可爱', '2', 'pony', '2025-04-06 10:28:44', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('21', '7', '好活泼', '4', '小花', '2025-04-06 10:28:48', 'account_circle.png');
INSERT INTO `reviewtb` VALUES ('3', '1', '哈哈哈哈哈', '4', '小花', '2025-04-06 10:20:47', 'account_circle.png');
INSERT INTO `reviewtb` VALUES ('4', '2', '好小一只', '1', '小明', '2025-04-06 10:21:25', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('5', '2', '好可爱', '2', 'pony', '2025-04-06 10:21:47', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('6', '2', '亲亲小猫', '4', '小花', '2025-04-06 10:22:20', 'account_circle.png');
INSERT INTO `reviewtb` VALUES ('7', '3', '好漂亮', '1', '小明', '2025-04-06 10:27:56', '875de146c37bd09ac0d16f1cf126c39.png');
INSERT INTO `reviewtb` VALUES ('8', '3', '暖暖的', '2', 'pony', '2025-04-06 10:27:59', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('88a3b359865c35356fa70ce1683fda83', '1', '哈哈哈', '2', 'pony', '2025-04-21 20:41:47', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png');
INSERT INTO `reviewtb` VALUES ('9', '3', '可爱', '4', '小花', '2025-04-06 10:28:02', 'account_circle.png');

-- ----------------------------
-- Table structure for t_pet_dangan
-- ----------------------------
DROP TABLE IF EXISTS `t_pet_dangan`;
CREATE TABLE `t_pet_dangan`  (
  `petId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createTime` datetime NULL DEFAULT NULL,
  `updatetime` datetime NULL DEFAULT NULL,
  `petName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petContext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `petYM` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petTag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petGender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petJue` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petYuan` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `petYear` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`petId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet_dangan
-- ----------------------------
INSERT INTO `t_pet_dangan` VALUES ('1', '2', '2025-04-04 11:20:51', '2025-04-08 11:20:55', '小花', '可爱的猫猫，希望健康成长', 'f5fd280977777cedf36840bab313ae9.png', '已打疫苗', '1', '女', '已绝育', '领养', '1岁');
INSERT INTO `t_pet_dangan` VALUES ('2', '2', '2025-04-04 11:22:06', '2025-04-04 11:22:10', '白白', '很调皮，老是和家里的老大打架', '9a757d7cafce16a76a84e73a73558ae.png', '已打疫苗', '1', '女', '已绝育', '领养', '5个月');
INSERT INTO `t_pet_dangan` VALUES ('3', '3', '2025-04-04 11:23:34', '2025-04-04 11:23:38', '毛毛', '浅黄色小狗，很爱我们一家', '62efb6421a511f645a4af027f8eb063.png', '已打疫苗', '0', '女', '未绝育', '购买', '3岁');
INSERT INTO `t_pet_dangan` VALUES ('4', '6', '2025-04-04 11:25:19', '2025-04-04 11:25:23', '小一', '柴犬，很倔的小狗', '35190daf4b3b6161e70b02b9ce86390.png', '已打疫苗', '0', '男', '未绝育', '购买', '3岁');
INSERT INTO `t_pet_dangan` VALUES ('5', '6', '2025-04-04 11:27:36', '2025-04-04 11:27:39', '小小', '柴犬，和小一是亲兄弟', 'c28fe858fdf61bfce6bf59b76ab9059.png', '已打疫苗', '0', '男', '未绝育', '购买', '3岁');
INSERT INTO `t_pet_dangan` VALUES ('84de40bfedfb5f9f1b848b10df40a870', '2', NULL, NULL, '猫猫', '很可爱', '4c1880c4-91c5-486e-ae89-e485a30e8240-1725008902952.jpg', '已接种', NULL, '女孩子', '已绝育', '流浪', '七岁');
INSERT INTO `t_pet_dangan` VALUES ('89838a572bfd0ca0249d4c155a67e0bc', '2', '2025-04-21 20:41:20', NULL, '狗', '没', 'b06a4c8c-6705-4e7b-bd4e-dde96b5e6302-1724899280710.jpg', '已打', NULL, '女孩子', '已绝育', '没', '两岁');

-- ----------------------------
-- Table structure for topictb
-- ----------------------------
DROP TABLE IF EXISTS `topictb`;
CREATE TABLE `topictb`  (
  `publishId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publishName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishFile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishUserId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishUserName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishTime` datetime NULL DEFAULT NULL,
  `publishContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishTag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishTagtwo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `publishCover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`publishId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of topictb
-- ----------------------------
INSERT INTO `topictb` VALUES ('1', '吃饭', NULL, '9f23f0f2b2b885ae08ebb92add64230.png', '1', '小明', '2025-04-06 09:53:21', '狗粮倒出来了，它飞快地趴地上了，哈哈哈。', '1', '2', '9f23f0f2b2b885ae08ebb92add64230.png');
INSERT INTO `topictb` VALUES ('2', '小猫', NULL, '650a234af371ab7e1fed6cb0d164655.png', '1', '小明', '2025-04-06 09:55:08', '刚出生的小猫，很可爱。', '1', '1', '650a234af371ab7e1fed6cb0d164655.png');
INSERT INTO `topictb` VALUES ('3', '猫猫', NULL, '333203c754ee595effc5bc43cd10156.png', '1', '小明', '2025-04-06 09:56:30', '在阳光下，刚拍的小猫，暖洋洋的。', '1', '1', '333203c754ee595effc5bc43cd10156.png');
INSERT INTO `topictb` VALUES ('4', '睡觉', NULL, 'cbb590bb943612f453d4957928e0d9a.png', '2', 'pony', '2025-04-06 10:06:43', '在阳光下，睡觉的小狗狗们，(●\'◡\'●)。', '1', '2', 'cbb590bb943612f453d4957928e0d9a.png');
INSERT INTO `topictb` VALUES ('5', '击掌', '20250406_094405.mp4', NULL, '2', 'pony', '2025-04-06 10:10:43', '新学会的技能', '2', '1', '20250406_094405.mp4');
INSERT INTO `topictb` VALUES ('6', '她要玩', '20250406_094526.mp4', NULL, '4', '小花', '2025-04-06 10:10:48', '贪玩的小姑娘哈哈哈', '2', '1', '20250406_094526.mp4');
INSERT INTO `topictb` VALUES ('7', '可爱', '20250406_094908.mp4', NULL, '4', '小花', '2025-04-06 10:10:51', '糯米糍飞奔而来', '2', '2', '20250406_094908.mp4');
INSERT INTO `topictb` VALUES ('7ff0d460-c725-4700-bc5b-841ea2b964ac', '可爱小猫', NULL, 'b5c0a224-742f-483e-ac8d-733ef4bebfcd-img-1736858530978c1a96ee40e74b37a0db0b6782f2174e7a816c829c76786887497320d112bf4ff.jpg', '2', 'pony', '2025-04-21 20:43:15', '小猫', '1', NULL, '835bbdb9-70bf-48fd-b345-e03853a6558f-1725008902952.jpg');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userName` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userPhone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userTime` datetime NULL DEFAULT NULL,
  `userId` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `userPic` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `userPwd` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `userTago` int NULL DEFAULT NULL,
  `userTagt` int NULL DEFAULT NULL,
  `userTagtr` int NULL DEFAULT NULL,
  `userDate` datetime NULL DEFAULT NULL,
  `userAddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `userGender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('小明', '15249241234', '2019-01-21 00:00:00', '1', '30744f1c-97f7-46b2-b7d3-9a8942074460-1724899280710.jpg', '123456', 1, 1, 0, '2025-04-05 00:00:00', '河北', '女');
INSERT INTO `user` VALUES ('pony', '15382092261', '2019-01-21 00:00:00', '2', 'a2f6cf7e8c23008ce9aa8fdf2f4c115.png', '123456', 0, 0, 0, '2025-04-07 00:00:00', '辽宁', '男');
INSERT INTO `user` VALUES ('duoduo', '15249246666', '2019-01-21 00:00:00', '3', 'a41f3077e9bb04c117bc3b0364018ec.png', '123456', 0, 0, 0, '2025-04-01 21:27:51', '辽宁', '女');
INSERT INTO `user` VALUES ('小花', '15249247878', '2019-01-21 00:00:00', '4', 'account_circle.png', '123456', 0, 0, 0, '2025-04-07 21:27:56', '河北', '女');
INSERT INTO `user` VALUES ('小丸子', '15249249696', '2019-01-21 00:00:00', '5', 'ea4f7c2db1671043d9373146f73dd7e.png', '123456', 0, 0, 0, '2025-04-07 21:27:59', '天津', '男');
INSERT INTO `user` VALUES ('cat', '15231802653', '2025-04-04 10:53:47', '6', 'icon_user_img.png', '123456', 0, 0, 0, '2025-04-07 21:28:02', '河南', '男');
INSERT INTO `user` VALUES ('poooony', '17333817121', NULL, 'ead610a4d6a01095e0373c5bea54d840', NULL, NULL, 0, 0, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for userb
-- ----------------------------
DROP TABLE IF EXISTS `userb`;
CREATE TABLE `userb`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reportType` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userb
-- ----------------------------
INSERT INTO `userb` VALUES ('032a132b5ff8bbaffe9e20310441b8d6', '2', '4', '视频', '2025-04-17 20:34:06');
INSERT INTO `userb` VALUES ('1', '2', '1', '图文', '2025-04-09 11:11:22');
INSERT INTO `userb` VALUES ('262dda89b19c413885691f648db2c45f', '2', '4', '视频', '2025-04-17 19:53:07');
INSERT INTO `userb` VALUES ('2bcf1f58934224b6fc98995fbb33bcfd', '2', 'dd719d9b-948e-4f29-b6b5-05b7f25f0052', '视频', '2025-04-21 20:23:00');
INSERT INTO `userb` VALUES ('3e93be9fce839fb8e4e4991f4aaed20b', '2', 'ab90eaa6-00dd-4d5c-9f3c-df913bcc4854', '视频', '2025-04-17 20:56:02');
INSERT INTO `userb` VALUES ('60b5e634b37de838575790aa5c841dc7', '2', '37ef85a9-074e-4db6-9fa0-10db58df9b55', '视频', '2025-04-21 20:40:10');
INSERT INTO `userb` VALUES ('7d600b10bf8be006084907650b8a8241', '2', '4', '视频', '2025-04-21 20:38:02');
INSERT INTO `userb` VALUES ('817d33374526f7c763ff88b2b5a7d767', '2', '07e2b57b-6b20-4a5c-91df-1d3faea12a0b', '视频', '2025-04-17 21:05:30');
INSERT INTO `userb` VALUES ('9f4a5fe72a7c6ea78754223a99f504d0', '2', '7', '图文', '2025-04-21 20:37:57');
INSERT INTO `userb` VALUES ('a9ce65bcc4322c2de5c2c20bd1a92fb8', '2', '4', '视频', '2025-04-21 20:20:37');
INSERT INTO `userb` VALUES ('cf4bb0831ef38ed2f44181f0eb56c8ed', '2', '7', '图文', '2025-04-17 20:33:18');
INSERT INTO `userb` VALUES ('dcb528966249ff80796116976f057b15', '2', '7', '图文', '2025-04-17 19:52:46');
INSERT INTO `userb` VALUES ('ee34e42ab8a0d2144d8e9ac2f8d44497', '2', '7', '图文', '2025-04-21 20:20:26');
INSERT INTO `userb` VALUES ('fe1df5ba0d3b72085546f3f070f5c1ac', '2', '7', '图文', '2025-04-17 20:27:21');

SET FOREIGN_KEY_CHECKS = 1;
