-- 외래 키 제약 조건 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 데이터만 삭제 (테이블은 유지)
DELETE FROM menu_authority;
DELETE FROM common_code;
DELETE FROM grp_code;
DELETE FROM condition_code;
DELETE FROM users;
DELETE FROM company;
DELETE FROM company_menu_authority;

/* insert default menu */
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code, use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/dashboard', '대시보드', 'Dashboard', '0', null, 'A0000', 'Y', 'fa-solid fa-chart-line font-ico', now(), null);

INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code, use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/dashboard', '대시보드', 'Dashboard', '0', null, 'A0000', 'Y', 'fa-solid fa-chart-line font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/map', '지도', 'Map', '0', null, 'B0000', 'Y', 'fa-solid fa-map font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('MEMBER', '회원관리', 'Member Management', '0', null, 'C0000', 'Y', 'fa-solid fa-user-tie font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/member/list', '회원리스트', 'Member List', '1', 'C0000', 'C0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/member/authentication/list', '회원인증내역', 'Member Authentication History', '1', 'C0000', 'C0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/member/tag/list', '회원카드관리', 'Member Card Management', '1', 'C0000', 'C0300', 'Y', null, now(), null);

INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('STATION', '충전소관리', 'Station Management', '0', null, 'D0000', 'Y', 'fa-solid fa-charging-station font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/station/list', '충전소리스트', 'Station List', '1', 'D0000', 'D0100', 'Y', null, now(), null);

INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CHARGER', '충전기관리', 'Charger Management', '0', null, 'E0000', 'Y', 'fa-solid fa-plug font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/charger/list', '충전기리스트', 'Charger List', '1', 'E0000', 'E0100', 'Y', null, now(), null);


INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CHARGING', '충전현황', 'Charging Status', '0', null, 'F0000', 'Y', 'fa-solid fa-battery-full', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/charging/list', '실시간 충전 리스트', 'Real-time Charging List', '1', 'F0000', 'F0100', 'Y', null, now(), null);


INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('SYSTEM', '시스템', 'System', '0', null, 'G0000', 'Y', 'fa-solid fa-gear font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/model/list', '모델관리', 'Model Management', '1', 'G0000', 'G0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/code/list', '공통코드관리', 'Common Code Management', '1', 'G0000', 'G0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/user/list', '사용자관리', 'User Management', '1', 'G0000', 'G0300', 'Y', null,now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/notice/list', '공지사항 관리', 'Notice Management', '1', 'G0000', 'G0400','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/menu/list', '메뉴관리', 'Menu Management', '1', 'G0000', 'G0500','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/authority/list', '메뉴접근권한 관리', 'Menu Access Authority Management', '1', 'G0000', 'G0600','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/errcode/list', '에러코드 관리', 'Error Code Management', '1', 'G0000', 'G0700','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/tariff/list', '요금제 관리', 'Tariff Management', '1', 'G0000', 'G0800','Y',  null,  now(), null);


INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/condition', '약관관리', 'Terms Management', '1', 'G0000', 'G0900','Y',  null,  now(), null);

INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('MAINTENANCE', '유지보수', 'Maintenance', '0', null, 'H0000', 'Y', 'fa-solid fa-wrench font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/maintenance/errlist', '장애관리', 'Error Management', '1', 'H0000', 'H0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CONTROL', '제어', 'Control', '0', null, 'I0000', 'Y', 'fa-solid fa-satellite-dish', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/control/charger/list', '충전기 제어', 'Charger Control', '1', 'I0000', 'I0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CUSTOMER', '고객센터', 'Customer Service', '0', null, 'J0000', 'Y', 'fa-solid fa-headset', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/voc', '1:1 문의', '1:1 Inquiry', '1', 'J0000', 'J0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/faq', 'FAQ 문의', 'FAQ Inquiry', '1', 'J0000', 'J0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('HISTORY', '이력', 'History', '0', null, 'K0000', 'Y', 'fa-solid fa-clipboard-list font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/charging', '충전이력', 'Charging History', '1', 'K0000', 'K0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/payment', '결제이력', 'Payment History', '1', 'K0000', 'K0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/comm', '통신이력', 'Communication History', '1', 'K0000', 'K0300', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/error', '에러이력', 'Error History', '1', 'K0000', 'K0400', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CALCULATION', '정산', 'Calculation', '0', null, 'L0000', 'Y', 'fa-solid fa-receipt', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/calc/chgpayment', '충전결제정보', 'Charging Payment Information', '1', 'L0000', 'L0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/calc/purchase', '매입관리', 'Purchase Management', '1', 'L0000', 'L0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('STATISTICS', '통계', 'Statistics', '0', null, 'M0000', 'Y', 'fa-solid fa-chart-simple', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/statistics/purchaseandsales', '매입/매출통계', 'Purchase/Sales Statistics', '1', 'M0000', 'M0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/statistics/usage', '이용률 통계', 'Usage Rate Statistics', '1', 'M0000', 'M0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/statistics/totalkw', '충전량 통계', 'Total kWh Statistics', '1', 'M0000', 'M0300', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/statistics/error', '장애율 통계', 'Error Rate Statistics', '1', 'M0000', 'M0400', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('BIZ', '업체관리', 'Business Management', '0', null, 'N0000', 'Y', 'fa-solid fa-building', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/biz/list', '사업자 관리', 'Business Operator Management', '1', 'N0000', 'N0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/corp/list', '법인 관리', 'Corporation Management', '1', 'N0000', 'N0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('FIRMWARE', '펌웨어', 'Firmware', '0', null, 'O0000', 'Y', 'fa-solid fa-microchip font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/fw/version', '펌웨어 버전관리', 'Firmware Version Management', '1', 'O0000', 'O0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_name_en, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/fw/update', '펌웨어 업데이트', 'Firmware Update', '1', 'O0000', 'O0200', 'Y', null, now(), null);


/* insert default menu authority */
DELETE FROM company_menu_authority;
INSERT INTO company_menu_authority (company_id, menu_code, use_yn) 
VALUES
(1,  'A0000', 'Y'),
(1,  'B0000', 'Y'),
(1,  'C0000', 'Y'),
(1,  'C0100', 'Y'),
(1,  'C0200', 'Y'),
(1,  'C0300', 'Y'),
(1,  'D0000', 'Y'),
(1,  'D0100', 'Y'),
(1,  'E0000', 'Y'),
(1,  'E0100', 'Y'),
(1,  'F0000', 'Y'),
(1,  'F0100', 'Y'),
(1,  'G0000', 'Y'),
(1,  'G0100', 'Y'),
(1,  'G0200', 'Y'),
(1,  'G0300', 'Y'),
(1,  'G0400', 'Y'),
(1,  'G0500', 'Y'),
(1,  'G0600', 'Y'),
(1,  'G0700', 'Y'),
(1,  'G0800', 'Y'),
(1,  'G0900', 'Y'),
(1,  'H0000', 'Y'),
(1,  'H0100', 'Y'),
(1,  'I0000', 'Y'),
(1,  'I0100', 'Y'),
(1,  'J0000', 'Y'),
(1,  'J0100', 'Y'),
(1,  'J0200', 'Y'),
(1,  'K0000', 'Y'),
(1,  'K0100', 'Y'),
(1,  'K0200', 'Y'),
(1,  'K0300', 'Y'),
(1,  'K0400', 'Y'),
(1,  'L0000', 'Y'),
(1,  'L0100', 'Y'),
(1,  'L0200', 'Y'),
(1,  'M0000', 'Y'),
(1,  'M0100', 'Y'),
(1,  'M0200', 'Y'),
(1,  'M0300', 'Y'),
(1,  'M0400', 'Y'),
(1,  'N0000', 'Y'),
(1,  'N0100', 'Y'),
(1,  'N0200', 'Y'),
(1,  'O0000', 'Y'),
(1,  'O0100', 'Y'),
(1,  'O0200', 'Y');


/* insert default condition code */
INSERT INTO condition_code (condition_code, condition_name, section, reg_dt)
VALUES
('PI', '개인정보 활용 동의서', 'Y', now()),
('MS', '멤버십 서비스 이용약관', 'Y', now()),
('MK', '마케팅 수신 동의', 'N', now()),
('ES', '이메일 수신 동의', 'N', now()),
('SS', 'SMS 수신 동의', 'N', now());


/* insert admin company */
INSERT INTO company (company_id, address, address_detail, biz_kind, biz_num, biz_type, ceo_name, company_code, company_lv, company_name, company_type, consignment_payment, created_at, head_phone, logo_url, staff_email, staff_name,
staff_phone, staff_tel, updated_at, zipcode, company_contract_id, company_pg_id, company_relation_info_id)
VALUES(
1,
'testaddress',
'testaddressdetail',
'3',
'1234',
'CB',
'홍길동',
'DA',
'TOP',
'동아일렉콤',
'OP',
'S',
NOW(),
'1234',
'',
'staff@mail.co.kr',
'김길동',
'1234',
'1234',
NOW(),
'1234',
null,
null,
1
);

INSERT INTO company_relation_info (company_relation_info_id, parent_company_id, level_path)
VALUES(
    1,
    null,
    '1'
);


/* insert super user */
INSERT INTO users (user_id, authority, email, name, password, phone, reg_dt, company_id)
VALUES (
    'zgoodev',
    'SU',
    'sikim@dongahelecomm.com',
    '슈퍼관리자',
    SHA2('1234', 256),
    '010-1234-5678',
    NOW(),
    1
);

INSERT INTO grp_code (grp_code, grpcd_name, grpcd_name_en, reg_user_id, reg_dt, mod_user_id, mod_dt)
VALUES
('COKIND', '사업자유형', 'Business Type', 'zgoodev', NOW(), NULL, NULL),
('COLV', '사업자레벨', 'Business Level', 'zgoodev', NOW(), NULL, NULL),
('BIZTYPECD', '사업자구분', 'Business Category', 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '계약상태', 'Contract Status', 'zgoodev', NOW(), NULL, NULL),
('MCOMPANY', '유지보수업체', 'Maintenance Company', 'zgoodev', NOW(), NULL, NULL),
('CONSIGNMENTCD', '결제위탁구분', 'Payment Consignment Type', 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', '메뉴권한', 'Menu Access Level', 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '그리드ROW수', 'Grid Row Count', 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'FAQ구분코드', 'FAQ Category Code', 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', '공지유형', 'Notice Type', 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', '부지구분', 'Land Type', 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', '운영단계분류', 'Operation Stage Classification', 'zgoodev', NOW(), NULL, NULL),
('CSFACILITY', '충전소시설유형', 'Charging Station Facility Type', 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', '충전소시설구분', 'Charging Station Facility Category', 'zgoodev', NOW(), NULL, NULL),
('FAUCETTYPE', '수전방식', 'Power Supply Method', 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', '가입요금제', 'Subscription Rate Plan', 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', '업종', 'Industry Type', 'zgoodev', NOW(), NULL, NULL),
('CGMANFCD', '충전기제조사코드', 'Charger Manufacturer Code', 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', '충전기설치유형', 'Charger Installation Type', 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', '커넥터타입', 'Connector Type', 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', '충전기속도구분코드', 'Charger Speed Category Code', 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', '카드사코드', 'Credit Card Company Code', 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', '회원상태코드', 'Member Status Code', 'zgoodev', NOW(), NULL, NULL),
('VOCPATH', '문의경로', 'Inquiry Path', 'zgoodev', NOW(), NULL, NULL),
('VOCSTAT', '조치상태', 'Action Status', 'zgoodev', NOW(), NULL, NULL),
('VOCTYPE', '문의유형', 'Inquiry Type', 'zgoodev', NOW(), NULL, NULL),
('FRCODE', '장애접수유형코드', 'Error Receipt Type Code', 'zgoodev', NOW(), NULL, NULL),
('FSTATCODE', '장애처리상태코드', 'Error Processing Status Code', 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', '계정과목', 'Account Subject', 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', '매입거래지불수단', 'Purchase Transaction Payment Method', 'zgoodev', NOW(), NULL, NULL),
('CGCOMMONCD', '충전기공용구분코드', 'Charger Public Use Category Code', 'zgoodev', NOW(), NULL, NULL),
('NOTUSINGRSN', '충전기미사용사유', 'Charger Non-Use Reason', 'zgoodev', NOW(), NULL, NULL),
('MODEMCORP', '모뎀통신사', 'Modem Carrier', 'zgoodev', NOW(), NULL, NULL),
('COMPANYCD', '사업자코드', 'Business Code', 'zgoodev', NOW(), NULL, NULL),
('TARIFFSTATCD', '단가적용상태코드', 'Tariff Status Code', 'zgoodev', NOW(), NULL, NULL),
('MODEMCTCD', '모뎀계약상태', 'Modem Contract Status', 'zgoodev', NOW(), NULL, NULL);


INSERT INTO common_code (grp_code, common_code, name, name_en, reserved, ref_code1, ref_code2, ref_code3, reg_user_id, reg_dt, mod_user_id, mod_dt)
VALUES
('FSTATCODE', 'FSTATREADY', '대기중', 'Standby', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FSTATCODE', 'FSTATFINISH', '처리완료', 'Completed', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COKIND', 'CO', '위탁운영사', 'Consignment Operator', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COKIND', 'OP', '충전사업자', 'Charging Business Operator', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'ROOT', 'root', 'root', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'TOP', '상위사업자', 'Top Level Business', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'MID', '하위사업자', 'Mid Level Business', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'LOW', '최하위사업자', 'Lowest Level Business', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZTYPECD', 'PB', '개인', 'Personal Business', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZTYPECD', 'CB', '법인', 'Corporate Business', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '1', '계약중', 'Under Contract', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '2', '해지', 'Terminated', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '0', '계약전', 'Before Contract', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONSIGNMENTCD', 'C', '위탁', 'Consignment', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONSIGNMENTCD', 'S', '자체', 'Self', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'AD', '관리자', 'Administrator', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'NO', '일반사용자', 'Normal User', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'AS', 'AS담당자', 'AS Manager', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'SU', '슈퍼관리자', 'Super Administrator', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '10', '10건', '10 items', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '30', '30건', '30 items', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '50', '50건', '50 items', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '100', '100건', '100 items', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'GENRLINFO', '일반정보', 'General Information', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'APPINFO', '앱사용법', 'App Usage Guide', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'PAYMENTINFO', '요금/결제', 'Fees/Payment', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'CHGUSAGE', '충전기사용법', 'Charger Usage Guide', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'ETCINFO', '기타', 'Others', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N1', '전체공지', 'General Notice', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N2', '회원공지', 'Member Notice', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N3', '시스템공지', 'System Notice', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N4', '충전기공지', 'Charger Notice', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N5', '모바일공지', 'Mobile Notice', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTBUILDING', '건축물', 'Building', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTROAD', '도로', 'Road', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTRIVER', '하천', 'River', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTRAIL', '철도', 'Railway', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', 'OPTEST', '시운전', 'Test Operation', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', 'OPSTOP', '운영중지', 'Operation Stopped', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', 'OPERATING', '운영중', 'Operating', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFACILITY', 'CSF000', '공공시설', 'Public Facility', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFACILITY', 'CSF001', '주차장', 'Parking Lot', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF101', '공영주차장', 'Public Parking Lot', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF201', '고속도로휴게소', 'Highway Rest Area', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF301', '공원주차장', 'Park Parking Lot', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF401', '마트', 'Mart', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF501', '정비소', 'Service Center', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF601', '아파트', 'Apartment', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF701', '군부대', 'Military Base', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAUCETTYPE', 'PROCENT', '가공인입', 'Processed Input', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAUCETTYPE', 'UNDERENT', '지중인입', 'Underground Input', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH1', '고압1', 'High Voltage 1', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH2', '고압2', 'High Voltage 2', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH3', '고압3', 'High Voltage 3', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH4', '고압4', 'High Voltage 4', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW1', '저압1', 'Low Voltage 1', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW2', '저압2', 'Low Voltage 2', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW3', '저압3', 'Low Voltage 3', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW4', '저압4', 'Low Voltage 4', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', 'BKAF', '농업,임업 및 어업', 'Agriculture, Forestry and Fisheries', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', 'BKMINE', '광업', 'Mining', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', 'BKMANF', '제조업', 'Manufacturing', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGMANFCD', 'DAE', '동아일렉콤', 'Dong-A Elecomm', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGMANFCD', 'HUM', '휴맥스', 'Humax', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', 'STD', '스탠드형', 'Stand Type', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', 'WAL', '벽부형', 'Wall Mounted', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', 'PRT', '이동형', 'Portable', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', 'SPEEDLOW', '완속', 'Slow Charging', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', 'SPEEDFAST', '급속', 'Fast Charging', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', 'SPEEDDESPN', '디스펜서', 'Dispenser', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPSLOW', '완속', 'Slow', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPAC3', 'AC3상', 'AC 3-Phase', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPCHADEMO', 'DC차데모', 'DC CHAdeMO', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPCOMBO', 'DC콤보(CCS1)', 'DC Combo (CCS1)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPCOMBO2', '콤보2(CCS2)', 'Combo2 (CCS2)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPBGT', 'GBT', 'GBT', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', 'BCCARD', '비씨', 'BC Card', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', 'KBCARD', 'KB국민', 'KB Kookmin', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', 'HANACARD', '하나(구외환)', 'Hana (Ex-Forex)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', 'MSTNORMAL', '정상', 'Normal', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', 'MSTATOUT', '탈퇴', 'Withdrawn', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', 'MSTDORMANT', '휴면', 'Dormant', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCPATH', 'VOCAPP', '모바일앱', 'Mobile App', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCPATH', 'VOCTEL', '전화', 'Phone', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCPATH', 'VOCETC', '기타', 'Others', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCSTAT', 'STANDBY', '대기', 'Standby', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCSTAT', 'COMPLETE', '완료', 'Completed', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCTYPE', 'UINQ', '사용문의', 'Usage Inquiry', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCTYPE', 'FINQ', '고장문의', 'Error Inquiry', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCTYPE', 'BINQ', '요금/결제문의', 'Fee/Payment Inquiry', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCTYPE', 'EINQ', '기타', 'Others', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'ELCFEE', '전기료', 'Electricity Fee', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'COMFEE', '통신비', 'Communication Fee', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'LANDFEE', '토지사용료', 'Land Usage Fee', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'SMFEE', '안전점검관리비', 'Safety Inspection Management Fee', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'REPFEE', '수리비', 'Repair Fee', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'SYSFEE', '시스템사용료', 'System Usage Fee', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('ACCOUNTCD', 'EFEE', '기타', 'Others', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', 'PM01', '신용(체크)카드', 'Credit (Check) Card', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', 'PM02', '현금영수증(계좌이체)', 'Cash Receipt (Account Transfer)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', 'PM03', '현금영수증(현금)', 'Cash Receipt (Cash)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', 'PM04', '현금', 'Cash', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', 'PM05', '계좌이체', 'Account Transfer', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('PURCHASEMTH', 'PM06', '세금계산서(청구)', 'Tax Invoice (Billing)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGCOMMONCD', 'PUB', '공용', 'Public', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGCOMMONCD', 'NOTPUB', '비공용', 'Private', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGCOMMONCD', 'PARTIALPUB', '부분공용', 'Partially Public', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTUSINGRSN', 'NUR01', '철거', 'Demolition', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTUSINGRSN', 'NUR02', '교체', 'Replacement', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTUSINGRSN', 'NUR03', '기타', 'Others', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MODEMCORP', 'SKT', 'SKT', 'SKT', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MODEMCORP', 'KT', 'KT', 'KT', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MODEMCORP', 'LGU+', 'LGU+', 'LGU+', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MODEMCTCD', 'MODEMNC', '미계약', 'No Contract', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MODEMCTCD', 'MODEMUC', '계약중', 'Under Contract', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MODEMCTCD', 'MODEMCF', '계약만료', 'Contract Expired', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COMPANYCD', 'DA', '동아일렉콤', 'Dongah Elecomm', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('TARIFFSTATCD', 'TFPAST', '과거적용', 'PAST', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('TARIFFSTATCD', 'TFCURR', '적용중', 'CURR', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('TARIFFSTATCD', 'TFFUTR', '적용예정', 'FUTURE', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MCOMPANY', 'SF', '자체', 'Self', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MCOMPANY', 'DONGAH', '동아일렉콤위탁', 'Dongah Elecomm Consignment', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FRCODE', 'FRDMAGE', '파손', 'Damage', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FRCODE', 'FRERROR', '에러', 'Error', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FRCODE', 'FRETC', '기타', 'Others', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL);

/* insert AD, AS, NO authority */
INSERT INTO menu_authority(company_id, authority, menu_code, read_yn, mod_yn, excel_yn, reg_at, mod_at, mod_user_id)
values
(1, 'AD', 'A0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'B0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'C0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'C0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'C0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'C0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'D0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'D0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'E0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'E0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'F0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'F0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0400', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0500', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0600', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0700', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0800', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'G0900', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'H0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'H0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'I0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'I0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'J0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'J0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'J0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'K0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'K0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'K0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'K0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'K0400', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'L0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'L0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'L0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'M0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'M0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'M0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'M0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'M0400', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'N0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'N0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'N0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'O0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'O0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AD', 'O0200', 'Y', 'Y', 'Y', now(), null, null);

INSERT INTO menu_authority(company_id, authority, menu_code, read_yn, mod_yn, excel_yn, reg_at, mod_at, mod_user_id)
values
(1, 'AS', 'A0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'B0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'C0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'C0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'C0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'C0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'D0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'D0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'E0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'E0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'F0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'F0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0400', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0500', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0600', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0700', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0800', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'G0900', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'H0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'H0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'I0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'I0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'J0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'J0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'J0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'K0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'K0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'K0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'K0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'K0400', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'L0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'L0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'L0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'M0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'M0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'M0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'M0300', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'M0400', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'N0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'N0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'N0200', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'O0000', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'O0100', 'Y', 'Y', 'Y', now(), null, null),
(1, 'AS', 'O0200', 'Y', 'Y', 'Y', now(), null, null);

INSERT INTO menu_authority(company_id, authority, menu_code, read_yn, mod_yn, excel_yn, reg_at, mod_at, mod_user_id)
values
(1, 'NO', 'A0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'B0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'C0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'C0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'C0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'C0300', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'D0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'D0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'E0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'E0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'F0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'F0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0300', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0400', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0500', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0600', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0700', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0800', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'G0900', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'H0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'H0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'I0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'I0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'J0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'J0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'J0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'K0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'K0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'K0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'K0300', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'K0400', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'L0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'L0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'L0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'M0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'M0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'M0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'M0300', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'M0400', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'N0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'N0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'N0200', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'O0000', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'O0100', 'Y', 'N', 'Y', now(), null, null),
(1, 'NO', 'O0200', 'Y', 'N', 'Y', now(), null, null);


-- 외래 키 제약 조건 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;