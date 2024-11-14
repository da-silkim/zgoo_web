$(document).ready(function() {
    let modalCon = false;   // false: 등록 / true: 수정
    let selectRow; isDuplicateCheck = false, userIdCheck = false;
    let btnMsg = "등록";

    // 초기화
    $('#resetBtn').on('click', function() {
        window.location.replace('/system/user/list');
    });

    // 검색
    $('#searchBtn').on('click', function() {
        const DATA = {
            companyId : $('#companyIdSearch').val(),
            companyType : $('#companyTypeSearch').val() || null,
            name : $('#nameSearch').val() || null
        }
        // console.log("검색할 데이터: ", DATA);

        $.ajax({
            type: 'GET',
            url: '/system/user/search',
            data: DATA,
            success: function(response) {
                // 테이블 초기화
                $('#pageList').empty();
                // console.log("조회된 데이터: ", response);

                if (response && response.length > 0) {
                    $.each(response, function(index, user) {
                        $('#pageList').append(`
                            <tr>
                                <td><input type="checkbox" /></td>
                                <td>${user.companyName || ''}</td>
                                <td>${user.companyType || ''}</td>
                                <td>${user.userId || ''}</td>
                                <td>${user.name || ''}</td>
                                <td>${user.phone || ''}</td>
                                <td>${user.email || ''}</td>
                                <td>${user.authority || ''}</td>
                                <td>${formatDate(new Date(user.regDt)) || ''}</td>
                            </tr>
                        `);
                    });
                } else {
                    $('#pageList').append(`
                        <tr>
                            <td colspan="9">조회된 데이터가 없습니다.</td>
                        </tr>
                    `);
                }
            }
        });
    });

    // 행 선택
    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
    });

    // 사용자 - 등록
    $('#addBtn').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        // 사업자, 사용자ID, 권한, 중복버튼 => 활성화
        $('#companyId').prop('disabled', false);
        $('#userId').prop('disabled', false);
        $('#authority').prop('disabled', false);
        $('#duplicateBtn').prop('disabled', false);

        // modal form 초기화
        $('#companyId').prop('selectedIndex', 0);
        $('#userId').val('');
        $('#name').val('');
        $('#password').val('');
        $('#email').val('');
        $('#phone').val('');
        $('#authority').prop('selectedIndex', 0);
    });

    // 사용자 - 수정
    $('#editBtn').on('click', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        // 사업자, 사용자ID, 권한, 중복버튼 => 비활성화
        $('#companyId').prop('disabled', true);
        $('#userId').prop('disabled', true);
        $('#authority').prop('disabled', true);
        $('#duplicateBtn').prop('disabled', true);

        const $userId = selectRow.find('td').eq(3).text();
        // console.log('userId: ' + $userId);

        // 선택된 행에 대한 사용자 정보 불러오기
        $.ajax({
            type: 'GET',
            url: `/system/user/get/${$userId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                // console.log("사용자 정보 불러오기: ", data);
                $('#companyId').val(data.companyId || '');
                $('#userId').val(data.userId || '');
                $('#name').val(data.name || '');
                $('#password').val(data.password || '');
                $('#email').val(data.email || '');
                $('#phone').val(data.phone || '');
                $('#authority').val(data.authority || '');
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            const $userId = selectRow.find('td').eq(3).text();

            $.ajax({
                type: 'DELETE',
                url: `/system/user/delete/${$userId}`,
                contentType: "application/json",
                success: function(response) {
                    console.log("사용자 정보 삭제 성공: ", response);
                    window.location.replace('/system/user/list');
                },
                error: function(error) {
                    console.log("사용자 정보 삭제 실패: ", error);
                }
            });
        }
    });

    $('#cancelBtn').on('click', function() {
        isDuplicateCheck = false;
        userIdCheck = false;
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        const userID = $('#userId').val();
        const name = $('#name').val();
        const password = $('#password').val();
        const email = $('#email').val();

        // 유효성 체크
        var form = document.getElementById('userForm');
        if (!form.checkValidity()) { 
            alert('필수 입력 값이 누락되어 있습니다.');
            return;
        }

        if(!userIdCheck) {
            alert('사용자ID를 다시 확인해주세요.');
            return;
        }

        if (!isPasswordSpecial(password)) { return; }

        if (email.trim() != '') {
            if (!isEmail(email)) { return; }
        }

        // 사용자ID 중복 버튼을 통해 중복ID 확인을 했는지
        if(!isDuplicateCheck) {
            alert('사용자ID 중복체크를 해주세요.');
            return;
        }

        if(confirmSubmit(btnMsg)) {
            const DATA = {
                companyId: $('#companyId').val(),
                userId: userID,
                name: name,
                password: password,
                email: email,
                phone: $('#phone').val(),
                authority: $('#authority').val()
            }

            console.log("사용자 데이터 확인: ", DATA);

            const URL = modalCon ? `/system/user/update` : `/system/user/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(DATA),
                contentType: "application/json",
                success: function() {
                    console.log("사용자 정보 처리 성공");
                    window.location.replace('/system/user/list');
                },
                error: function() {
                    console.log("사용자 정보 처리 실패");
                }
            });
        }        
    });

    // 중복검사
    $('#duplicateBtn').on('click', function() {
        isDuplicateCheck = true;

        var userId = $('#userId').val();
        if (userId.trim() === '') {
            alert('사용자ID를 입력해주세요');
            userIdCheck = false;
            return;
        }

        if (!isId(userId)) { 
            userIdCheck = false;
            return;
        }

        $.ajax({
            url: '/system/user/checkUserId',
            type: 'GET',
            data: { userId: userId },
            success: function(isDuplicate) {
                if (isDuplicate) {
                    alert('이미 사용 중인 ID입니다.');
                    userIdCheck = false;
                } else {
                    alert('사용 가능한 ID입니다.');
                    userIdCheck = true;
                }
            },
            error: function() {
                $('#userIdCheckMessage').text('오류 발생: 다시 시도해 주세요.');
            }
        });
    });

    // 비밀번호 보이기
    $('.info-eyes').on('click', function() {
        // 현재 클릭한 요소의 부모인 .input-info-group에서 password input을 찾음
        var inputGroup = $(this).parents('.input-info-group');
        var passwordField = inputGroup.find('#password');
        
        // .input-info-group에 active 클래스 토글
        inputGroup.toggleClass('active');
        
        // active 클래스가 있는 경우 비밀번호를 텍스트로 보여줌, 없는 경우 숨김
        if (inputGroup.hasClass('active')) {
            $(this).find('.bi-eye').attr('class', 'bi bi-eye-slash');
            passwordField.attr('type', 'text');
        } else {
            $(this).find('.bi-eye-slash').attr('class', 'bi bi-eye');
            passwordField.attr('type', 'password');
        }
    });
});


