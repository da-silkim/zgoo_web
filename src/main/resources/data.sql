-- 외래 키 제약 조건 비활성화
SET FOREIGN_KEY_CHECKS = 0;


/* insert default menu */
DELETE FROM menu;

INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code, use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/dashboard', '대시보드', '0', null, 'A0000', 'Y', 'fa-solid fa-chart-line font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/map', '지도', '0', null, 'B0000', 'Y', 'fa-solid fa-map font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('MEMBER', '회원관리', '0', null, 'C0000', 'Y', 'fa-solid fa-user-tie font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/member/list', '회원리스트', '1', 'C0000', 'C0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/member/authentication/list', '회원인증내역', '1', 'C0000', 'C0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/member/tag/list', '회원카드관리', '1', 'C0000', 'C0300', 'Y', null, now(), null);

INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('STATION', '충전소관리', '0', null, 'D0000', 'Y', 'fa-solid fa-charging-station font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/station/list', '충전소리스트', '1', 'D0000', 'D0100', 'Y', null, now(), null);

INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CHARGER', '충전기관리', '0', null, 'E0000', 'Y', 'fa-solid fa-plug font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/charger/list', '충전기리스트', '1', 'E0000', 'E0100', 'Y', null, now(), null);


INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CHARGING', '충전현황', '0', null, 'F0000', 'Y', 'fa-solid fa-battery-full', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/charging/list', '실시간 충전 리스트', '1', 'F0000', 'F0100', 'Y', null, now(), null);


INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('SYSTEM', '시스템', '0', null, 'G0000', 'Y', 'fa-solid fa-gear font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/model/list', '모델관리', '1', 'G0000', 'G0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/code/list', '공통코드관리', '1', 'G0000', 'G0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/user/list', '사용자관리', '1', 'G0000', 'G0300', 'Y', null,now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/notice/list', '공지사항 관리', '1', 'G0000', 'G0400','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/menu/list', '메뉴관리', '1', 'G0000', 'G0500','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/authority/list', '메뉴접근권한 관리', '1', 'G0000', 'G0600','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/errcode/list', '에러코드 관리', '1', 'G0000', 'G0700','Y',  null,  now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/tariff/list', '요금제 관리', '1', 'G0000', 'G0800','Y',  null,  now(), null);


INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/system/condition', '약관관리', '1', 'G0000', 'G0900','Y',  null,  now(), null);

INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('MAINTENANCE', '유지보수', '0', null, 'H0000', 'Y', 'fa-solid fa-wrench font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/maintenance/errlist', '장애관리', '1', 'H0000', 'H0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CONTROL', '제어', '0', null, 'I0000', 'Y', 'fa-solid fa-satellite-dish', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/control/charger/list', '충전기 제어', '1', 'I0000', 'I0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('CUSTOMER', '고객센터', '0', null, 'J0000', 'Y', 'fa-solid fa-headset', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/voc', '1:1 문의', '1', 'J0000', 'J0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/faq', 'FAQ 문의', '1', 'J0000', 'J0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('HISTORY', '이력', '0', null, 'K0000', 'Y', 'fa-solid fa-clipboard-list font-ico', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/charging', '충전이력', '1', 'K0000', 'K0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/payment', '결제이력', '1', 'K0000', 'K0200', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/comm', '통신이력', '1', 'K0000', 'K0300', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/history/error', '에러이력', '1', 'K0000', 'K0400', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('BIZ', '업체관리', '0', null, 'N0000', 'Y', 'fa-solid fa-building', now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/biz/list', '사업자 관리', '1', 'N0000', 'N0100', 'Y', null, now(), null);
INSERT INTO menu (menu_url, menu_name, menu_lv, parent_code, menu_code,use_yn, icon_class, reg_dt, mod_dt)
VALUES ('/corp/list', '법인 관리', '1', 'N0000', 'N0200', 'Y', null, now(), null);


/* insert default menu authority */
DELETE FROM company_menu_authority;
INSERT INTO company_menu_authority (company_id, company_menu_authority, menu_code, use_yn) 
VALUES
('1', '1', 'A0000', 'Y'),
('1', '2', 'B0000', 'Y'),
('1', '3', 'C0000', 'Y'),
('1', '4', 'C0100', 'Y'),
('1', '5', 'C0200', 'Y'),
('1', '6', 'C0300', 'Y'),
('1', '7', 'D0000', 'Y'),
('1', '8', 'D0100', 'Y'),
('1', '9', 'E0000', 'Y'),
('1', '10', 'E0100', 'Y'),
('1', '11', 'F0000', 'Y'),
('1', '12', 'F0100', 'Y'),
('1', '13', 'G0000', 'Y'),
('1', '14', 'G0100', 'Y'),
('1', '15', 'G0200', 'Y'),
('1', '16', 'G0300', 'Y'),
('1', '17', 'G0400', 'Y'),
('1', '18', 'G0500', 'Y'),
('1', '19', 'G0600', 'Y'),
('1', '20', 'G0700', 'Y'),
('1', '21', 'G0800', 'Y'),
('1', '22', 'G0900', 'Y'),
('1', '23', 'H0000', 'Y'),
('1', '24', 'H0100', 'Y'),
('1', '25', 'I0000', 'Y'),
('1', '26', 'I0100', 'Y'),
('1', '27', 'J0000', 'Y'),
('1', '28', 'J0100', 'Y'),
('1', '29', 'J0200', 'Y'),
('1', '30', 'K0000', 'Y'),
('1', '31', 'K0100', 'Y'),
('1', '32', 'K0200', 'Y'),
('1', '33', 'K0300', 'Y'),
('1', '34', 'K0400', 'Y'),
('1', '35', 'N0000', 'Y'),
('1', '36', 'N0100', 'Y'),
('1', '37', 'N0200', 'Y');


/* insert default condition code */
DELETE FROM condition_code;
INSERT INTO condition_code (condition_code, condition_name, section, reg_dt)
VALUES
('PI', '개인정보 활용 동의서', 'Y', now()),
('MS', '멤버십 서비스 이용약관', 'Y', now()),
('MK', '마케팅 수신 동의', 'N', now()),
('ES', '이메일 수신 동의', 'N', now()),
('SS', 'SMS 수신 동의', 'N', now());


/* insert admin company */
DELETE FROM company;
INSERT INTO company (address, address_detail, biz_kind, biz_num, biz_type, ceo_name, company_code, company_lv, company_name, company_type, consignment_payment, created_at, head_phone, logo_url, staff_email, staff_name,
staff_phone, staff_tel, updated_at, zipcode, company_contract_id, company_pg_id, company_relation_info_id)
VALUES(
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
null
);


/* insert super user */
DELETE FROM users;
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

INSERT INTO grp_code (grp_code, grpcd_name, reg_user_id, reg_dt, mod_user_id, mod_dt)
VALUES
('COKIND', '사업자유형', 'zgoodev', NOW(), NULL, NULL),
('COLV', '사업자레벨', 'zgoodev', NOW(), NULL, NULL),
('BIZTYPECD', '사업자구분', 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '계약상태', 'zgoodev', NOW(), NULL, NULL),
('MCOMPANY', '유지보수업체', 'zgoodev', NOW(), NULL, NULL),
('CONSIGNMENTCD', '결제위탁구분', 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', '메뉴권한', 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '그리드ROW수', 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'FAQ구분코드', 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', '공지유형', 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', '부지구분', 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', '운영단계분류', 'zgoodev', NOW(), NULL, NULL),
('CSFACILITY', '충전소시설유형', 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', '충전소시설구분', 'zgoodev', NOW(), NULL, NULL),
('FAUCETTYPE', '수전방식', 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', '가입요금제', 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', '업종', 'zgoodev', NOW(), NULL, NULL),
('CGMANFCD', '충전기제조사코드', 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', '충전기설치유형', 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', '커넥터타입', 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', '충전기속도구분코드', 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', '카드사코드', 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', '회원상태코드', 'zgoodev', NOW(), NULL, NULL),
('VOCPATH', '문의경로', 'zgoodev', NOW(), NULL, NULL),
('VOCSTAT', '조치상태', 'zgoodev', NOW(), NULL, NULL),
('VOCTYPE', '문의유형', 'zgoodev', NOW(), NULL, NULL),
('FRCODE', '장애접수유형코드', 'zgoodev', NOW(), NULL, NULL),
('FSTATCODE', '장애처리상태코드', 'zgoodev', NOW(), NULL, NULL);


INSERT INTO common_code (grp_code, common_code, name, reserved, ref_code1, ref_code2, ref_code3, reg_user_id, reg_dt, mod_user_id, mod_dt)
VALUES
('FSTATCODE', 'FSTATREADY', '대기중', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FSTATCODE', 'FSTATFINISH', '처리완료', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COKIND', 'CO', '위탁운영사', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COKIND', 'OP', '충전사업자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'ROOT', 'root', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'TOP', '상위사업자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'MID', '하위사업자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('COLV', 'LOW', '최하위사업자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZTYPECD', 'PB', '개인', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZTYPECD', 'CB', '법인', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '1', '계약중', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '2', '해지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONTSTAT', '0', '계약전', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL), 
('CONSIGNMENTCD', 'C', '위탁', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONSIGNMENTCD', 'S', '자체', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'AD', '관리자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'NO', '일반사용자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'AS', 'AS담당자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MENUACCLV', 'SU', '슈퍼관리자', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '10', '10건', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '30', '30건', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '50', '50건', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('SHOWLISTCNT', '100', '100건', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'GENRLINFO', '일반정보', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'APPINFO', '앱사용법', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'PAYMENTINFO', '요금/결제', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'CHGUSAGE', '충전기사용법', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAQKIND', 'ETCINFO', '기타', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N1', '전체공지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N2', '회원공지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N3', '시스템공지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N4', '충전기공지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('NOTICETYPECD', 'N5', '모바일공지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTBUILDING', '건축물', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTROAD', '도로', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTRIVER', '하천', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('LANDTYPE', 'LTRAIL', '철도', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', 'OPTEST', '시운전', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', 'OPSTOP', '운영중지', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('OPSTEPCD', 'OPERATING', '운영중', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFACILITY', 'CSF000', '공공시설', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFACILITY', 'CSF001', '주차장', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF101', '공영주차장', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF201', '고속도로휴게소', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF301', '공원주차장', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF401', '마트', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF501', '정비소', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF601', '아파트', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CSFSUB', 'CSF701', '군부대', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAUCETTYPE', 'PROCENT', '가공인입', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('FAUCETTYPE', 'UNDERENT', '지중인입', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH1', '고압1', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH2', '고압2', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH3', '고압3', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PHIGH4', '고압4', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW1', '저압1', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW2', '저압2', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW3', '저압3', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('POWERTYPE', 'PLOW4', '저압4', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', 'BKAF', '농업,임업 및 어업', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', 'BKMINE', '광업', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('BIZKIND', 'BKMANF', '제조업', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGMANFCD', 'DAE', '동아일렉콤', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CGMANFCD', 'HUM', '휴맥스', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', 'STD', '스탠드형', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', 'WAL', '벽부형', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGINTTYPECD', 'PRT', '이동형', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', 'SPEEDLOW', '완속', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', 'SPEEDFAST', '급속', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CHGSPEEDCD', 'SPEEDDESPN', '디스펜서', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPSLOW', '완속', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPAC3', 'AC3상', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPCHADEMO', 'DC차데모', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPCOMBO', 'DC콤보(CCS1)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPCOMBO2', '콤보2(CCS2)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CONNTYPE', 'CTYPBGT', 'GBT', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', 'BCCARD', '비씨', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', 'KBCARD', 'KB국민', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('CREDITCARDCD', 'HANACARD', '하나(구외환)', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', 'MSTNORMAL', '정상', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', 'MSTATOUT', '탈퇴', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('MEMSTATCD', 'MSTDORMANT', '휴면', NULL, NULL, NULL, NULL, 'zgoodev', NOW(), NULL, NULL),
('VOCPATH', 'VOCAPP', '모바일앱', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCPATH', 'VOCTEL', '전화', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCPATH', 'VOCETC', '기타', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCSTAT', 'STANDBY', '대기', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCSTAT', 'COMPLETE', '완료', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCTYPE', 'UINQ', '사용문의', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCTYPE', 'FINQ', '고장문의', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCTYPE', 'BINQ', '요금/결제문의', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL),
('VOCTYPE', 'EINQ', '기타', NULL, NULL, NULL, NULL, 'daadmin', NOW(), NULL, NULL);

-- 외래 키 제약 조건 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;