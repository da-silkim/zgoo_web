function excelDownload() {

    // 현재 검색 조건 가져오기
    const companyIdSearch = document.getElementById('companyIdSearch')?.value || '';
    const chgStartTimeFrom = document.getElementById('chgStartTimeFrom')?.value || '';
    const chgStartTimeTo = document.getElementById('chgStartTimeTo')?.value || '';
    const opSearch = document.getElementById('opSearch')?.value || '';
    const contentSearch = document.getElementById('contentSearch')?.value || '';

    console.log("searchOption:", companyIdSearch, chgStartTimeFrom, chgStartTimeTo, opSearch, contentSearch);

    // URL 생성
    let url = '/history/charging/excel?';
    const params = [];

    if (companyIdSearch && companyIdSearch !== '(없음)') {
        params.push('companyIdSearch=' + encodeURIComponent(companyIdSearch));
    }

    if (chgStartTimeFrom) {
        params.push('chgStartTimeFrom=' + encodeURIComponent(chgStartTimeFrom));
    }

    if (chgStartTimeTo) {
        params.push('chgStartTimeTo=' + encodeURIComponent(chgStartTimeTo));
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