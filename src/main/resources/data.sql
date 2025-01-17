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
('1', '6', 'D0000', 'Y'),
('1', '7', 'D0100', 'Y'),
('1', '8', 'E0000', 'Y'),
('1', '9', 'E0100', 'Y'),
('1', '10', 'F0000', 'Y'),
('1', '11', 'F0100', 'Y'),
('1', '12', 'G0000', 'Y'),
('1', '13', 'G0100', 'Y'),
('1', '14', 'G0200', 'Y'),
('1', '15', 'G0300', 'Y'),
('1', '16', 'G0400', 'Y'),
('1', '17', 'G0500', 'Y'),
('1', '18', 'G0600', 'Y'),
('1', '19', 'G0700', 'Y'),
('1', '20', 'G0800', 'Y'),
('1', '21', 'H0000', 'Y'),
('1', '22', 'H0100', 'Y'),
('1', '23', 'I0000', 'Y'),
('1', '24', 'I0100', 'Y'),
('1', '25', 'J0000', 'Y'),
('1', '26', 'J0100', 'Y'),
('1', '27', 'J0200', 'Y'),
('1', '28', 'N0000', 'Y'),
('1', '29', 'N0100', 'Y'),
('1', '30', 'N0200', 'Y');
