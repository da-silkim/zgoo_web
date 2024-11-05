$(document).ready(function() {
    let grpModalCon = false, comModalCon = false;    // false: 등록 / true: 수정
    let grpSelectRow, comSelectRow; // 선택한 행, 열 저장
    let btnMsg = "등록";            // 등록/수정에 따른 버튼 메시지

    // 그룹코드 테이블 클릭 시 실행
    $('#pageList').on('click', 'tr', function() {
        grpSelectRow = $(this);
        const currGrpCode = grpSelectRow.find('td').eq(1).text();    // 그룹코드
        // const currGrpcdName = grpSelectRow.find('td').eq(2).text();  // 그룹코드명
        console.log('curr grpCode: ' + currGrpCode);    // 현재 클릭한 그룹코드 정보 확인

        // 그룹코드 정보 공통코드 모달창에 자동 입력
        $('.grpCodeInput').val(currGrpCode);
        // var grpcdVal = $('.grpCodeInput').val();
        // console.log("선택한 그룹코드 값: " + grpcdVal);

        // 선택한 그룹코드에 연관된 공통코드 조회
        $.ajax({
            type: 'GET',
            url: `/system/code/commoncd/search/${currGrpCode}`,
            success: function(data) {
                console.info("공통코드: ", data);
                $('#pageListSub').empty();  // 기존 내용 삭제
                data.forEach(function(comcode) {
                    // null 값 체크
                    const comGrpCode = comcode.id.grpCode || '';
                    const comCode = comcode.id.commonCode || '';
                    const comcdName = comcode.name || '';
                    const comRefCode1 = comcode.refCode1 || '';
                    const comRefCode2 = comcode.refCode2 || '';
                    const comRefCode3 = comcode.refCode3 || '';
                    const comcdRegId = comcode.regUserId || '';
                    const comcdModDt = comcode.modDt ? formatDate(new Date(comcode.modDt)) : '';

                    $('#pageListSub').append(`
                        <tr>
                            <td><input type="checkbox"/></td>
                            <td>${comGrpCode}</td>
                            <td>${comCode}</td>
                            <td>${comcdName}</td>
                            <td>${comRefCode1}</td>
                            <td>${comRefCode2}</td>
                            <td>${comRefCode3}</td>
                            <td>${comcdRegId}</td>
                            <td>${comcdModDt}</td>
                        </tr>
                    `);
                });
            },
            error: function(error) {
                console.log("공통코드 조회 실패: ", error);
            }
        });
    });

    $('#pageListSub').on('click', 'tr', function() {
        comSelectRow = $(this);
        // const currComCode = comSelectRow.find('td').eq(2).text();    // 공통코드
        // const currComcdName = comSelectRow.find('td').eq(3).text();  // 공통코드명
    })

    // 그룹코드 - 등록
    $('#addBtn').on('click', function() {
        grpModalCon = false;
        btnMsg = "등록";
        $('#grpModalBtn').text(btnMsg);
        $('#grpCodeM').val('');
        $('#grpcdNameM').val('');
    });

    // 그룹코드 - 수정
    $('#editBtn').on('click', function() {
        grpModalCon = true;
        btnMsg = "수정";
        $('#grpModalBtn').text(btnMsg);

        // 선택된 행이 있는 경우
        if (grpSelectRow) {
            const currGrpCode = grpSelectRow.find('td').eq(1).text();    // 그룹코드
            const currGrpcdName = grpSelectRow.find('td').eq(2).text();  // 그룹코드명

            // 그룹코드 수정 모달창에 데이터 자동 입력
            $('#grpCodeM').val(currGrpCode);
            $('#grpcdNameM').val(currGrpcdName);
        } else {
            console.error("그룹코드 수정 폼 에러 발생");
        }
    });

    // 그룹코드 - 삭제
    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";
        
        if(confirmSubmit(btnMsg)) {
            // 선택된 행이 있는 경우
            if(grpSelectRow) {
                const currGrpCd = grpSelectRow.find('td').eq(1).text();  // 그룹코드

                console.log("삭제 테스트: " + currGrpCd);
                $.ajax({
                    type: 'DELETE',
                    url: `/system/code/grpcode/delete/${currGrpCd}`,
                    contentType: "application/json",
                    success: function(response) {
                        console.log("그룹코드 데이터 삭제 완료: ", response);
                        window.location.replace('/system/code/list');
                    },
                    error: function(error) {
                        console.log("그룹코드 데이터 삭제 실패: ", error);
                    }
                });
            }
        }
    });

    // 공통코드 - 등록
    $('#addBtnSub').on('click', function() {
        comModalCon = false;
        btnMsg = "등록";
        $('#comModalBtn').text(btnMsg);
        $('#commonCode').val('');
        $('#commonCodeName').val('');
        $('#referenceCode1').val('');
        $('#referenceCode2').val('');
        $('#referenceCode3').val('');
    });

    // 공통코드 - 수정
    $('#editBtnSub').on('click', function() {
        comModalCon = true;
        btnMsg = "수정";
        $('#comModalBtn').text(btnMsg);

        // 선택된 행이 있는 경우
        if (comSelectRow) {
            const currGrpCd = comSelectRow.find('td').eq(1).text();  // 그룹코드
            const currComcd = comSelectRow.find('td').eq(2).text();  // 공통코드

            console.log("수정 테스트: " + currGrpCd + ', ' + currComcd);
            $.ajax({
                type: 'GET',
                url: `/system/code/commoncd/get/${currGrpCd}/${currComcd}`,
                contentType: "application/json",
                dataType: 'json',
                success: function(data) {
                    // 조회된 데이터 폼에 입력
                    const refcode1 = data.refCode1 || '';
                    const refcode2 = data.refCode2 || '';
                    const refcode3 = data.refCode3 || '';

                    $('#grpCode').val(data.id.grpCode);
                    $('#commonCode').val(data.id.commonCode);
                    $('#commonCodeName').val(data.name);
                    $('#referenceCode1').val(refcode1);
                    $('#referenceCode2').val(refcode2);
                    $('#referenceCode3').val(refcode3);
                },
                error: function() {
                    console.log('공통코드 데이터 조회 실패');
                }
            });
        }
    });

    // 공통코드 - 삭제
    $('#deleteBtnSub').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            // 선택된 행이 있는 경우
            if(comSelectRow) {
                const currGrpCd = comSelectRow.find('td').eq(1).text();  // 그룹코드
                const currComcd = comSelectRow.find('td').eq(2).text();  // 공통코드

                console.log("삭제 테스트: " + currGrpCd + ', ' + currComcd);
                $.ajax({
                    type: 'DELETE',
                    url: `/system/code/commoncd/delete/${currGrpCd}/${currComcd}`,
                    contentType: "application/json",
                    success: function(response) {
                        console.log("공통코드 데이터 삭제 완료: ", response);
                        window.location.replace('/system/code/list');
                    },
                    error: function(error) {
                        console.log("공통코드 데이터 삭제 실패: ", error);
                    }
                });
            }
        }
    });

    // 그룹코드 - Modal
    $('#grpModalBtn').on('click', function() {
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
                success: function(response) {
                    console.log("그룹코드 데이터 처리 응답:", response);
                    // window.location.href = '/system/code/list';
                    window.location.replace('/system/code/list');

                    // 서버에서 반환한 리다이렉트 URL이 있는지 확인
                    // if (response.redirect) {
                    //     window.location.replace(response.redirect); // 서버에서 제공한 리다이렉트 URL로 이동 (히스토리 기록 X)
                    // } else {
                    //     console.warn("리다이렉트 URL 없음");
                    // }
                },
                error: function(xhr, status, error) {
                    console.error("AJAX 요청 실패:", error);
                }
            });
        }
    });

    // 공통코드 - Modal
    $('#comModalBtn').on('click', function() {
        // console.log("입력한 그룹코드 값: " + $('.grpCodeInput').val());
        // console.log("입력한 공통코드 값: " + $('#commonCode').val());
        
        if (confirmSubmit(btnMsg)) {
            const URL = comModalCon ? '/system/code/commoncd/update' : '/system/code/commoncd/new';
            const TYPE = comModalCon ? 'PATCH' : 'POST';
            const data = {
                id: {
                    grpCode: $('.grpCodeInput').val(),
                    commonCode: $('#commonCode').val()
                },
                name: $('#commonCodeName').val(),
                refCode1: $('#referenceCode1').val(),
                refCode2: $('#referenceCode2').val(),
                refCode3: $('#referenceCode3').val()
            }

            console.log("grpCode: " + $('.grpCodeInput').val());
            console.log("commonCode: " + $('#commonCode').val());
            console.log("commonCodeName: " + $('#commonCodeName').val());
            console.log("refCode1: " + $('#referenceCode1').val());
            console.log("refCode2: " + $('#referenceCode2').val());
            console.log("refCode3: " + $('#referenceCode3').val());

            
            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: 'json', // 서버 응답을 JSON으로 처리
                success: function(response) {
                    console.log("공통코드 데이터 처리 응답:", response);
                    window.location.href = '/system/code/list';
                },
                error: function(xhr, status, error) {
                    console.error("AJAX 요청 실패:", error);
                }
            });
        }        
    });

    // 초기화 버튼 클릭 시 실행
    $('#resetBtn').on('click', function() {
        window.location.href = '/system/code/list';
    });

    // 검색창 Enter 처리
    $('#searchGrpcdName').on('keydown', function(event) {
        if(event.key == 'Enter') {
            event.preventDefault(); // 기본 동작 방지
            searchGrpcdName();
        }
    });

    // 검색 버튼
    $('#grpcdNameSearchBtn').on('click', function() {
        searchGrpcdName();
    });

    // 그룹코드 - 조회
    function searchGrpcdName() {
        const searchGrpcdName = $('#searchGrpcdName').val();
        console.log(searchGrpcdName + ' 그룹코드 조회');
        $.ajax({
            type: 'GET',
            url: '/system/code/grpcode/search',
            data: { grpcdName: searchGrpcdName },
            success: function(data) {
                console.log(data);

                // 그룹, 공통 테이블 내용 삭제
                $('#pageList').empty();
                $('#pageListSub').empty();

                data.searchGrpCode.forEach(function(grp) {
                    // null check
                    const grpCd = grp.grpCode || '';
                    const grpName = grp.grpcdName || '';
                    const grpRegId = grp.regUserId || '';
                    const grpRegDt = grp.regDt ? formatDate(new Date(grp.regDt)) : '';
                    const grpModDt = grp.modDt ? formatDate(new Date(grp.modDt)) : '';

                    console.log('그룹코드 조회: ' + grpCd);

                    $('#pageList').append(`
                        <tr>
                            <td><input type="checkbox"/></td>
                            <td>${grpCd}</td>
                            <td>${grpName}</td>
                            <td>${grpRegId}</td>
                            <td>${grpRegDt}</td>
                        </tr>
                    `);
                });
            }
        });
    }
});

// 날짜 포맷팅 함수(ex. 2024-10-29 12:13:00)
function formatDate(date) {
    // padStart(2, '0'): 2자리 수로 포맷팅
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 + 1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}