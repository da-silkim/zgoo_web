-- 데이터베이스 초기화 스크립트
-- 주의: 이 스크립트는 기존 데이터를 모두 삭제합니다!
-- 실행 전 반드시 백업을 확인하세요.

SET FOREIGN_KEY_CHECKS = 0;

-- 기존 테이블 데이터 삭제 (순서 주의)
DELETE FROM common_code;
DELETE FROM grp_code;
DELETE FROM menu;
-- 다른 테이블들도 필요에 따라 추가

-- 다국어 지원을 위한 컬럼이 있는 테이블 구조 확인 및 생성
-- menu 테이블
CREATE TABLE IF NOT EXISTS `menu` (
  `menu_url` varchar(200) DEFAULT NULL,
  `menu_name` varchar(100) DEFAULT NULL,
  `menu_name_ko` varchar(100) DEFAULT NULL COMMENT '한국어 메뉴명',
  `menu_name_en` varchar(100) DEFAULT NULL COMMENT '영어 메뉴명',
  `menu_lv` varchar(10) DEFAULT NULL,
  `parent_code` varchar(20) DEFAULT NULL,
  `menu_code` varchar(20) NOT NULL,
  `use_yn` char(1) DEFAULT NULL,
  `icon_class` varchar(200) DEFAULT NULL,
  `reg_dt` datetime(6) DEFAULT NULL,
  `mod_dt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- grp_code 테이블
CREATE TABLE IF NOT EXISTS `grp_code` (
  `grp_code` varchar(20) NOT NULL,
  `grpcd_name` varchar(50) DEFAULT NULL,
  `grpcd_name_ko` varchar(100) DEFAULT NULL COMMENT '한국어 그룹코드명',
  `grpcd_name_en` varchar(100) DEFAULT NULL COMMENT '영어 그룹코드명',
  `reg_user_id` varchar(20) DEFAULT NULL,
  `reg_dt` datetime(6) DEFAULT NULL,
  `mod_user_id` varchar(20) DEFAULT NULL,
  `mod_dt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`grp_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- common_code 테이블
CREATE TABLE IF NOT EXISTS `common_code` (
  `grp_code` varchar(20) NOT NULL,
  `common_code` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `name_ko` varchar(100) DEFAULT NULL COMMENT '한국어 이름',
  `name_en` varchar(100) DEFAULT NULL COMMENT '영어 이름',
  `reserved` varchar(100) DEFAULT NULL,
  `ref_code1` varchar(100) DEFAULT NULL,
  `ref_code2` varchar(100) DEFAULT NULL,
  `ref_code3` varchar(100) DEFAULT NULL,
  `reg_user_id` varchar(20) DEFAULT NULL,
  `reg_dt` datetime(6) DEFAULT NULL,
  `mod_user_id` varchar(20) DEFAULT NULL,
  `mod_dt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`grp_code`,`common_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;



