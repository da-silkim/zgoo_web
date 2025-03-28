/**
 * 충전기 리스트 엑셀 다운로드 기능
 * @author ksi
 * @version 1.0
 */

// 엑셀 다운로드 함수
function cpExcelDownload() {
    // 로딩 표시
    // showLoading('엑셀 파일을 생성하는 중입니다...');

    // 현재 검색 조건 가져오기
    const companyIdSearch = document.getElementById('companyIdSearch')?.value || '';
    const manfCodeSearch = document.getElementById('manfCodeSearch')?.value || '';
    const opSearch = document.getElementById('opSearch')?.value || '';
    const contentSearch = document.getElementById('contentSearch')?.value || '';

    console.log("searchOption:", companyIdSearch, manfCodeSearch, opSearch, contentSearch);

    // URL 생성
    let url = '/charger/excel/download?';
    const params = [];

    if (companyIdSearch && companyIdSearch !== '(없음)') {
        params.push('companyIdSearch=' + encodeURIComponent(companyIdSearch));
    }
    if (manfCodeSearch && manfCodeSearch !== '(없음)') {
        params.push('manfCodeSearch=' + encodeURIComponent(manfCodeSearch));
    }
    if (opSearch && opSearch !== '(선택)') {
        params.push('opSearch=' + encodeURIComponent(opSearch));
    }
    if (contentSearch) {
        params.push('contentSearch=' + encodeURIComponent(contentSearch));
    }

    url += params.join('&');

    // 다운로드 링크 생성 및 클릭
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    document.body.appendChild(a);
    a.click();

    // 링크 제거
    setTimeout(() => {
        document.body.removeChild(a);
    }, 1000);

    // // 5초 후 로딩 숨기기
    // setTimeout(() => {
    //     hideLoading();
    //     showToast('엑셀 다운로드가 완료되었습니다.');
    // }, 5000);
}

// 로딩 표시 함수
function showLoading(message = '처리 중입니다...') {
    // 로딩 오버레이가 없으면 생성
    if (!document.getElementById('loading-overlay')) {
        const overlay = document.createElement('div');
        overlay.id = 'loading-overlay';
        overlay.style.position = 'fixed';
        overlay.style.top = '0';
        overlay.style.left = '0';
        overlay.style.width = '100%';
        overlay.style.height = '100%';
        overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
        overlay.style.display = 'flex';
        overlay.style.justifyContent = 'center';
        overlay.style.alignItems = 'center';
        overlay.style.zIndex = '9999';

        const spinner = document.createElement('div');
        spinner.className = 'spinner-border text-light';
        spinner.setAttribute('role', 'status');

        const text = document.createElement('span');
        text.id = 'loading-text';
        text.className = 'ms-2 text-light';
        text.textContent = message;

        const container = document.createElement('div');
        container.className = 'd-flex align-items-center';
        container.appendChild(spinner);
        container.appendChild(text);

        overlay.appendChild(container);
        document.body.appendChild(overlay);
    } else {
        document.getElementById('loading-text').textContent = message;
        document.getElementById('loading-overlay').style.display = 'flex';
    }
}

// 로딩 숨기기 함수
function hideLoading() {
    const overlay = document.getElementById('loading-overlay');
    if (overlay) {
        overlay.style.display = 'none';
    }
}

// 토스트 메시지 표시 함수
function showToast(message) {
    // 부트스트랩 토스트 사용 (부트스트랩이 포함된 경우)
    if (typeof bootstrap !== 'undefined') {
        const toastEl = document.createElement('div');
        toastEl.className = 'toast align-items-center text-white bg-success border-0 position-fixed bottom-0 end-0 m-3';
        toastEl.setAttribute('role', 'alert');
        toastEl.setAttribute('aria-live', 'assertive');
        toastEl.setAttribute('aria-atomic', 'true');

        toastEl.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;

        document.body.appendChild(toastEl);
        const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
        toast.show();

        // 토스트가 숨겨진 후 요소 제거
        toastEl.addEventListener('hidden.bs.toast', function () {
            document.body.removeChild(toastEl);
        });
    } else {
        // 간단한 알림 대체
        alert(message);
    }
}
