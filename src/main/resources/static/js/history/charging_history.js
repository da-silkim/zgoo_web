document.addEventListener("DOMContentLoaded", function () {

    /**
     * 검색버튼 클릭 이벤트 처리
     */
    document.getElementById("searchBtn").addEventListener("click", function () {
        // 폼 제출 (size 값은 이미 hidden input에 있음)
        document.getElementById('searchForm').submit();
    });

    /**
     * 초기화 버튼 클릭 이벤트 처리
     */
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 모든 입력 필드 초기화
        document.getElementById('companyIdSearch').value = '';
        document.getElementById('chgStartTimeFrom').value = '';
        document.getElementById('chgStartTimeTo').value = '';
        document.getElementById('opSearch').value = '';
        document.getElementById('contentSearch').value = '';

        // 폼 제출 (초기화된 상태로)
        document.getElementById('searchForm').submit();
    });
});