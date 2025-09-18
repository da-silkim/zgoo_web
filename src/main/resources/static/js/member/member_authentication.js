document.addEventListener("DOMContentLoaded", function () {

    // /**
    //  * 검색버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("searchBtn").addEventListener("click", function () {
    //     // 폼 제출 (size 값은 이미 hidden input에 있음)
    //     document.getElementById('searchForm').submit();
    // });

    // /**
    //  * 초기화 버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("resetBtn").addEventListener("click", function () {
    //     // 모든 입력 필드 초기화
    //     document.getElementById('fromDate').value = '';
    //     document.getElementById('toDate').value = '';
    //     document.getElementById('opSearch').value = '';
    //     document.getElementById('contentSearch').value = '';

    //     // 폼 제출 (초기화된 상태로)
    //     document.getElementById('searchForm').submit();
    // });

    // 날짜 형식 설정 (로케일에 따라)
    function setDateFormat() {
        const fromDateInput = document.getElementById('fromDate');
        const toDateInput = document.getElementById('toDate');

        if (fromDateInput && toDateInput) {
            // 현재 로케일이 영어인 경우 날짜 형식 힌트 설정
            if (typeof currentLanguage !== 'undefined' && currentLanguage === 'en') {
                fromDateInput.setAttribute('data-format', 'MM/DD/YYYY');
                toDateInput.setAttribute('data-format', 'MM/DD/YYYY');
                fromDateInput.setAttribute('placeholder', 'Month/Day/Year');
                toDateInput.setAttribute('placeholder', 'Month/Day/Year');
            } else {
                fromDateInput.setAttribute('data-format', 'YYYY-MM-DD');
                toDateInput.setAttribute('data-format', 'YYYY-MM-DD');
                fromDateInput.setAttribute('placeholder', '연도-월-일');
                toDateInput.setAttribute('placeholder', '연도-월-일');
            }
        }
    }

    // 언어 변경 감지 및 날짜 필드 업데이트
    function updateDateFormatOnLanguageChange() {
        // 언어 변경 이벤트 리스너 (언어 선택기가 있을 경우)
        const languageSelector = document.querySelector('[data-locale]');
        if (languageSelector) {
            languageSelector.addEventListener('change', function () {
                setTimeout(() => {
                    setDateFormat();
                }, 100); // 언어 변경 후 약간의 지연을 두고 실행
            });
        }

        // MutationObserver를 사용하여 DOM 변화 감지
        const observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                if (mutation.type === 'attributes' && mutation.attributeName === 'lang') {
                    setTimeout(() => {
                        setDateFormat();
                    }, 100);
                }
            });
        });

        // html 태그의 lang 속성 변화 감지
        observer.observe(document.documentElement, {
            attributes: true,
            attributeFilter: ['lang']
        });
    }

    // 페이지 로드 시 날짜 형식 설정
    setDateFormat();

    // 언어 변경 감지 설정
    updateDateFormatOnLanguageChange();

    $('#size').on('change', function () {
        updatePageSize(this, "/member/authentication/list", ["opSearch", "contentSearch", "fromDate", "toDate"]);
    });
});