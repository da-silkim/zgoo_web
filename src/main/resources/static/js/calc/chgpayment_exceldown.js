function excelDownload() {

    // 현재 검색 조건 가져오기
    const companyIdSearch = document.getElementById('companyIdSearch')?.value || '';
    const startMonthSearch = document.getElementById('startMonthSearch')?.value || '';
    const endMonthSearch = document.getElementById('endMonthSearch')?.value || '';
    const opSearch = document.getElementById('opSearch')?.value || '';
    const contentSearch = document.getElementById('contentSearch')?.value || '';

    console.log("searchOption:", companyIdSearch, startMonthSearch, endMonthSearch, opSearch, contentSearch);

    // URL 생성
    let url = '/calc/chgpayment/excel?';
    const params = [];

    if (companyIdSearch && companyIdSearch !== '(없음)') {
        params.push('companyIdSearch=' + encodeURIComponent(companyIdSearch));
    }

    if (startMonthSearch) {
        params.push('startMonthSearch=' + encodeURIComponent(startMonthSearch));
    }

    if (endMonthSearch) {
        params.push('endMonthSearch=' + encodeURIComponent(endMonthSearch));
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
}