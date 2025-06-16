document.addEventListener("DOMContentLoaded", function () {
    // 날짜 선택 제한 로직
    const startMonthInput = document.getElementById('startMonthSearch');
    const endMonthInput = document.getElementById('endMonthSearch');

    // 현재 날짜 구하기
    const today = new Date();
    const currentYear = today.getFullYear();
    const currentMonth = today.getMonth() + 1; // getMonth()는 0부터 시작하므로 +1

    // 현재 년월을 YYYY-MM 형식으로 변환
    const currentYearMonth = `${currentYear}-${currentMonth.toString().padStart(2, '0')}`;

    // max 속성 설정 (현재 월까지만 선택 가능)
    startMonthInput.setAttribute('max', currentYearMonth);
    endMonthInput.setAttribute('max', currentYearMonth);

    // 날짜 변경 이벤트 핸들러
    function validateDateRange() {
        if (!startMonthInput.value || !endMonthInput.value) return;

        const startDate = new Date(startMonthInput.value + '-01');
        const endDate = new Date(endMonthInput.value + '-01');

        // 시작일이 종료일보다 이후인 경우
        if (startDate > endDate) {
            alert('시작 월은 종료 월보다 이전이어야 합니다.');
            startMonthInput.value = endMonthInput.value;
            return;
        }

        // 날짜 차이 계산 (월 단위)
        const monthDiff = (endDate.getFullYear() - startDate.getFullYear()) * 12 +
            (endDate.getMonth() - startDate.getMonth());

        // 2개월 초과 선택 시
        if (monthDiff > 2) {
            alert('조회 기간은 최대 2개월까지만 가능합니다.');

            // 종료일 기준으로 시작일 조정 (종료일로부터 2개월 전)
            const newStartDate = new Date(endDate);
            newStartDate.setMonth(endDate.getMonth() - 2);

            const newStartYear = newStartDate.getFullYear();
            const newStartMonth = (newStartDate.getMonth() + 1).toString().padStart(2, '0');
            startMonthInput.value = `${newStartYear}-${newStartMonth}`;
        }
    }

    // 이벤트 리스너 등록
    startMonthInput.addEventListener('change', validateDateRange);
    endMonthInput.addEventListener('change', validateDateRange);

    // 페이지 로드 시 초기 검증
    validateDateRange();


    $('#size').on('change', function () {
        updatePageSize(this, "/calc/chgpayment", ["opSearch", "contentSearch", "startMonthSearch", "endMonthSearch", "companyIdSearch"]);
    });

    // /**
    //  * 검색버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("searchBtn").addEventListener("click", function () {
    //     // 검색 전 날짜 유효성 검증
    //     validateDateRange();

    //     // 폼 제출 (size 값은 이미 hidden input에 있음)
    //     document.getElementById('searchForm').submit();
    // });

    // /**
    //  * 초기화 버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("resetBtn").addEventListener("click", function () {
    //     // 모든 입력 필드 초기화
    //     document.getElementById('opSearch').value = '';
    //     document.getElementById('contentSearch').value = '';

    //     // 현재 월을 기본값으로 설정
    //     const currentYearMonthFormatted = `${currentYear}-${currentMonth.toString().padStart(2, '0')}`;
    //     document.getElementById('startMonthSearch').value = currentYearMonthFormatted;
    //     document.getElementById('endMonthSearch').value = currentYearMonthFormatted;

    //     // 폼 제출 (초기화된 상태로)
    //     document.getElementById('searchForm').submit();
    // });

});