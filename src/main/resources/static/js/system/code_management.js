$(document).ready(function () {
    let grpModalCon = false, comModalCon = false;
    let grpSelectRow, comSelectRow;
    let btnMsg = i18n.codeManagement.buttons.modalregist;
    let currGrpCode;

    $('#pageList').on('click', 'tr', function () {
        grpSelectRow = $(this);
        currGrpCode = grpSelectRow.find('td').eq(1).text();    // 그룹코드
        $('.grpCodeInput').val(currGrpCode);
        showCommoncd(currGrpCode);
    });

    $('#pageListSub').on('click', 'tr', function () {
        comSelectRow = $(this);
    });

    // 선택한 그룹코드에 연관된 공통코드 조회
    function showCommoncd(currGrpCode) {
        $.ajax({
            type: 'GET',
            url: `/system/code/commoncd/search/${currGrpCode}`,
            success: function (data) {
                $('#pageListSub').empty();

                if (!data || data.length === 0) {
                    $('#pageListSub').append(`
                        <tr>
                            <td colspan="9"><span th:text="#{codeManagement.messages.noData}"></span></td>
                        </tr>
                    `);
                } else {
                    data.forEach(function (comcode) {
                        // currentLanguage가 'en'일 경우 commonCodeNameEn을, 그 외에는 commonCodeName을 사용
                        const displayName = (typeof currentLanguage !== 'undefined' && currentLanguage === 'en')
                            ? (comcode.commonCodeNameEn || comcode.commonCodeName || '')
                            : (comcode.commonCodeName || '');

                        $('#pageListSub').append(`
                            <tr>
                                <td><input type="checkbox"/></td>
                                <td>${comcode.grpCode || ''}</td>
                                <td>${comcode.commonCode || ''}</td>
                                <td>${displayName}</td>
                                <td>${comcode.refCode1 || ''}</td>
                                <td>${comcode.refCode2 || ''}</td>
                                <td>${comcode.refCode3 || ''}</td>
                                <td>${comcode.regUserId || ''}</td>
                                <td>${comcode.regDt ? formatDate(new Date(comcode.regDt)) : ''}</td>
                            </tr>
                        `);
                    });
                }
            },
            error: function (error) {
                console.error(error);
            }
        });
    }

    // 그룹코드 - 등록
    $('#addBtn').on('click', function () {
        grpModalCon = false;
        btnMsg = i18n.codeManagement.buttons.modalregist;
        $('#grpModalBtn').text(btnMsg);
        $('#grpForm')[0].reset();
        $('#grpCodeM').prop('disabled', false);
    });

    // 그룹코드 - 수정
    $('#editBtn').on('click', function () {
        grpModalCon = true;
        btnMsg = i18n.codeManagement.buttons.modaledit;
        $('#grpModalBtn').text(btnMsg);
        $('#grpCodeM').prop('disabled', true);

        // 선택된 행이 있는 경우
        if (grpSelectRow) {
            const currGrpCode = grpSelectRow.find('td').eq(1).text();    // 그룹코드
            const currGrpcdName = grpSelectRow.find('td').eq(2).text();  // 그룹코드명 (이미 언어별로 표시된 값)
            $('#grpCodeM').val(currGrpCode);
            $('#grpcdNameM').val(currGrpcdName);
        } else {
            console.error("grpcode edit form error!");
        }
    });

    // 그룹코드 - 삭제
    $('#deleteBtn').on('click', function () {
        btnMsg = i18n.codeManagement.buttons.delete;

        if (confirmSubmit(btnMsg)) {
            if (grpSelectRow) {
                const currGrpCd = grpSelectRow.find('td').eq(1).text();  // 그룹코드
                $.ajax({
                    type: 'DELETE',
                    url: `/system/code/grpcode/delete/${currGrpCd}`,
                    contentType: "application/json",
                    success: function (response) {
                        alert(response);
                        window.location.reload();
                    },
                    error: function (error) {
                        console.error(error);
                    }
                });
            }
        }
    });

    // 공통코드 - 등록
    $('#addBtnSub').on('click', function () {
        comModalCon = false;
        btnMsg = i18n.codeManagement.buttons.modalregist;
        $('#comModalBtn').text(btnMsg);
        $('#commForm')[0].reset();
        $('.grpCodeInput').val(currGrpCode);
        $('#commonCode').prop('disabled', false);
    });

    // 공통코드 - 수정
    $('#editBtnSub').on('click', function () {
        comModalCon = true;
        btnMsg = i18n.codeManagement.buttons.modaledit;
        $('#comModalBtn').text(btnMsg);
        $('#commonCode').prop('disabled', true);

        // 선택된 행이 있는 경우
        if (comSelectRow) {
            const currGrpCd = comSelectRow.find('td').eq(1).text();  // 그룹코드
            const currComcd = comSelectRow.find('td').eq(2).text();  // 공통코드
            $.ajax({
                type: 'GET',
                url: `/system/code/commoncd/get/${currGrpCd}/${currComcd}`,
                contentType: "application/json",
                dataType: 'json',
                success: function (data) {
                    $('#grpCode').val(data.grpCode);
                    $('#commonCode').val(data.commonCode);

                    // currentLanguage에 따라 기본 표시할 코드명 설정
                    const defaultCodeName = (typeof currentLanguage !== 'undefined' && currentLanguage === 'en')
                        ? (data.commonCodeNameEn || data.commonCodeName || '')
                        : (data.commonCodeName || '');

                    $('#commonCodeName').val(defaultCodeName);
                    $('#commonCodeNameEn').val(data.commonCodeNameEn);
                    $('#referenceCode1').val(data.refCode1 || '');
                    $('#referenceCode2').val(data.refCode2 || '');
                    $('#referenceCode3').val(data.refCode3 || '');
                },
                error: function (xhr, status, error) {
                    console.error(error);
                }
            });
        }
    });

    // 공통코드 - 삭제
    $('#deleteBtnSub').on('click', function () {
        btnMsg = i18n.codeManagement.buttons.delete;

        if (confirmSubmit(btnMsg)) {
            if (comSelectRow) {
                const currGrpCd = comSelectRow.find('td').eq(1).text();  // 그룹코드
                const currComcd = comSelectRow.find('td').eq(2).text();  // 공통코드
                $.ajax({
                    type: 'DELETE',
                    url: `/system/code/commoncd/delete/${currGrpCd}/${currComcd}`,
                    contentType: "application/json",
                    success: function (response) {
                        alert(response);
                        showCommoncd(currGrpCode);
                    },
                    error: function (xhr, status, error) {
                        console.error(error);
                    }
                });
            }
        }
    });

    // 그룹코드 - Modal
    $('#grpModalBtn').on('click', function () {
        event.preventDefault();

        if (confirmSubmit(btnMsg)) {
            const data = {
                grpCode: $('#grpCodeM').val(),
                grpcdName: $('#grpcdNameM').val()
            }

            const URL = grpModalCon ? '/system/code/grpcode/update' : '/system/code/grpcode/new';
            const TYPE = grpModalCon ? 'PATCH' : 'POST';
            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (response) {
                    alert(response.message);
                    window.location.reload();
                },
                error: function (error) {
                    console.error(error);
                }
            });
        }
    });

    // 공통코드 - Modal
    $('#comModalBtn').on('click', function () {
        // grpCode가 COMPANYCD일 때 commonCode 길이 검증
        const grpCode = $('.grpCodeInput').val();
        const commonCode = $('#commonCode').val();

        if (grpCode === 'COMPANYCD' && commonCode.length > 2) {
            alert(i18n.codeManagement.messages.companycdMaxLength);
            return;
        }

        if (confirmSubmit(btnMsg)) {
            const URL = comModalCon ? '/system/code/commoncd/update' : '/system/code/commoncd/new';
            const TYPE = comModalCon ? 'PATCH' : 'POST';
            const data = {
                grpCode: grpCode,
                commonCode: commonCode,
                commonCodeName: $('#commonCodeName').val(),
                commonCodeNameEn: $('#commonCodeNameEn').val(),
                refCode1: $('#referenceCode1').val(),
                refCode2: $('#referenceCode2').val(),
                refCode3: $('#referenceCode3').val()
            }

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: 'json',
                success: function (response) {
                    alert(response.message);
                    showCommoncd(currGrpCode);
                    $('#commonAddModal').modal('hide');
                },
                error: function (error) {
                    console.error(error);
                }
            });
        }
    });

    // 초기화 버튼 클릭 시 실행
    $('#resetBtn').on('click', function () {
        window.location.href = '/system/code/list';
    });

    // 검색창 Enter 처리
    $('#searchGrpcdName').on('keydown', function (event) {
        if (event.key == 'Enter') {
            event.preventDefault();
            searchGrpcdName();
        }
    });

    // 검색 버튼
    $('#grpcdNameSearchBtn').on('click', function () {
        searchGrpcdName();
    });

    // 그룹코드 - 조회
    function searchGrpcdName() {
        $.ajax({
            type: 'GET',
            url: '/system/code/grpcode/search',
            data: { grpcdName: $('#searchGrpcdName').val() },
            success: function (data) {
                $('#pageList').empty();
                $('#pageListSub').empty();

                if (!data.searchGrpCode || data.searchGrpCode.length === 0) {
                    $('#pageList').append(`
                        <tr>
                            <td colspan="5"><span th:text="#{codeManagement.messages.noData}"></span></td>
                        </tr>
                    `);
                } else {
                    data.searchGrpCode.forEach(function (grp) {
                        // currentLanguage가 'en'일 경우 grpcdNameEn을, 그 외에는 grpcdName을 사용
                        const displayGrpName = (typeof currentLanguage !== 'undefined' && currentLanguage === 'en')
                            ? (grp.grpcdNameEn || grp.grpcdName || '')
                            : (grp.grpcdName || '');

                        $('#pageList').append(`
                            <tr>
                                <td><input type="checkbox"/></td>
                                <td>${grp.grpCode || ''}</td>
                                <td>${displayGrpName}</td>
                                <td>${grp.regUserId || ''}</td>
                                <td>${grp.regDt ? formatDate(new Date(grp.regDt)) : ''}</td>
                            </tr>
                        `);
                    });
                }

                $('#pageListSub').append(`
                    <tr>
                        <td colspan="9"><span th:text="#{codeManagement.messages.showcommoncd}"></span></td>
                    </tr>
                `);
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    }
});

// 날짜 포맷팅 함수(ex. 2024-10-29 12:13:00)
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}