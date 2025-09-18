SET FOREIGN_KEY_CHECKS = 0;

-- -- 여기에 모든 테이블 DROP 문 추가
-- DROP TABLE IF EXISTS biz_info,charger_payment_info,charging_hist,chg_commlog,chg_errorcd,common_code,company,company_contract,company_menu_authority,company_pg,company_relation_info,company_roaming,condition_code,condition_version_hist,connector_status,cp_connector,cp_current_tx,cp_info,cp_maintain,cp_model,cp_model_detail,cp_modem,cp_plan_policy,cp_status,cpinfo,cs_info,cs_kepco_contractinfo,cs_land_info,csinfo,cslandinfo,error_hist,faq,grp_code,login_hist,member,member_auth,member_car,member_condition,member_creditcard,menu,menu_authority,notice,ocpp_cp_config,tariff_info,tariff_policy,tlog_message,tx_id_generator,users,voc;

-- 다국어 지원을 위한 컬럼 추가 (프로그램 재시작 시 자동 실행)
-- menu 테이블에 다국어 컬럼 추가 (한국어는 기존 menu_name 사용)
ALTER TABLE `menu`
ADD COLUMN `menu_name_en` varchar(100) DEFAULT NULL COMMENT '영어 메뉴명';

-- grp_code 테이블에 다국어 컬럼 추가 (한국어는 기존 grpcd_name 사용)
ALTER TABLE `grp_code`
ADD COLUMN `grpcd_name_en` varchar(100) DEFAULT NULL COMMENT '영어 그룹코드명';

-- common_code 테이블에 다국어 컬럼 추가 (한국어는 기존 name 사용)
ALTER TABLE `common_code`
ADD COLUMN `name_en` varchar(100) DEFAULT NULL COMMENT '영어 이름';

-- 기존 한글 데이터는 그대로 유지 (기존 컬럼을 한국어용으로 사용)

SET FOREIGN_KEY_CHECKS = 1;