# 다국어 지원 시스템 (i18n) 구현 가이드

## 개요
이 프로젝트는 Spring Boot와 Thymeleaf를 사용하여 한국어, 영어, 일본어를 지원하는 다국어 시스템을 구현했습니다.

## 구현된 기능

### 1. 언어 선택 드롭다운
- 대시보드 상단에 언어 선택 드롭다운 추가
- 한국어, 영어, 일본어 지원
- 현재 선택된 언어 표시

### 2. 다국어 리소스 관리
- `src/main/resources/i18n/` 디렉토리에 언어별 리소스 파일 관리
- `messages_ko.properties`: 한국어
- `messages_en.properties`: 영어  
- `messages_ja.properties`: 일본어

### 3. 동적 텍스트 변경
- HTML 페이지의 모든 텍스트를 리소스 키로 관리
- 언어 변경 시 즉시 해당 언어로 텍스트 변경
- 페이지 새로고침 없이 언어 변경 가능

## 파일 구조

```
src/main/resources/
├── i18n/
│   ├── messages_ko.properties    # 한국어 리소스
│   ├── messages_en.properties    # 영어 리소스
│   └── messages_ja.properties    # 일본어 리소스
└── templates/
    ├── fragments/
    │   └── language-selector.html    # 언어 선택 컴포넌트
    └── pages/
        ├── dashboard.html             # 대시보드 (다국어 적용)
        └── test-locale.html          # 테스트 페이지
```

## 주요 클래스

### 1. LocaleConfig.java
- Spring Boot 국제화 설정
- 로케일 리졸버 및 인터셉터 설정
- 기본 언어: 한국어

### 2. LocaleController.java
- 언어 변경 API (`/change-locale`)
- 현재 언어 조회 API (`/get-current-locale`)
- 세션 기반 언어 관리

### 3. TestController.java
- 테스트 페이지 접근 (`/test-locale`)

## 사용 방법

### 1. 언어 변경
1. 대시보드 상단의 언어 선택 드롭다운 클릭
2. 원하는 언어 선택 (한국어/English/日本語)
3. 페이지가 자동으로 새로고침되어 선택된 언어로 표시

### 2. 새로운 텍스트 추가
1. `messages_ko.properties`에 한국어 텍스트 추가
2. `messages_en.properties`에 영어 텍스트 추가
3. `messages_ja.properties`에 일본어 텍스트 추가
4. HTML에서 `th:text="#{리소스키}"` 형태로 사용

### 3. 새로운 페이지에 다국어 적용
1. HTML 템플릿에 언어 선택기 추가:
   ```html
   <div th:replace="~{fragments/language-selector :: language-selector}"></div>
   ```

2. 언어 선택 스크립트 추가:
   ```html
   <div th:replace="~{fragments/language-selector :: language-script}"></div>
   ```

3. 텍스트를 리소스 키로 변경:
   ```html
   <span th:text="#{리소스키}">기본 텍스트</span>
   ```

## 리소스 키 예시

### 대시보드 관련
- `dashboard.charger.status`: 충전기 현황
- `dashboard.sales.status`: 매출 현황
- `dashboard.charge.status`: 충전 현황

### 충전기 타입
- `charger.fast`: 급속
- `charger.slow`: 완속
- `charger.dispenser`: 디스펜서

### 단위
- `unit.count`: 대
- `unit.won`: 원
- `unit.kwh`: kWh

### 상태
- `status.increase`: ▲ 지난달 대비
- `status.decrease`: ▼ 지난달 대비
- `status.no.change`: -

## 테스트

### 1. 테스트 페이지 접근
```
http://localhost:8080/test-locale
```

### 2. 언어 변경 테스트
1. 테스트 페이지에서 언어 선택 드롭다운 클릭
2. 다른 언어 선택
3. 페이지 텍스트가 선택된 언어로 변경되는지 확인

## 주의사항

1. **리소스 키 관리**: 모든 텍스트는 리소스 키로 관리해야 합니다.
2. **기본 텍스트**: `th:text` 속성에 기본 텍스트를 제공하여 리소스 파일이 없을 때도 표시되도록 합니다.
3. **세션 관리**: 언어 설정은 세션에 저장되므로 브라우저를 닫으면 기본 언어로 초기화됩니다.
4. **캐시**: 리소스 파일은 1시간(3600초) 동안 캐시됩니다.

## 확장 방법

### 1. 새로운 언어 추가
1. `messages_언어코드.properties` 파일 생성
2. `LocaleConfig.java`에 언어 코드 추가
3. `language-selector.html`에 언어 옵션 추가

### 2. 데이터베이스 기반 다국어
현재는 파일 기반이지만, 필요시 데이터베이스 기반으로 확장 가능합니다.

### 3. 사용자별 언어 설정
현재는 세션 기반이지만, 사용자 계정과 연동하여 개인별 언어 설정 가능합니다.

## 문제 해결

### 1. 언어 변경이 안 되는 경우
- 브라우저 개발자 도구에서 네트워크 탭 확인
- `/change-locale` API 호출 상태 확인
- 콘솔 에러 메시지 확인

### 2. 텍스트가 표시되지 않는 경우
- 리소스 키가 올바른지 확인
- 리소스 파일의 인코딩이 UTF-8인지 확인
- 기본 텍스트가 제공되었는지 확인

### 3. 스타일이 적용되지 않는 경우
- `common.css` 파일이 올바르게 로드되었는지 확인
- Bootstrap CSS가 로드되었는지 확인
