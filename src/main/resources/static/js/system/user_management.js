$(document).ready(function () {
    let modalCon = false;   // false: 등록 / true: 수정
    let selectRow; isDuplicateCheck = false, userIdCheck = false;
    let btnMsg = i18n.userManagement.modal.buttons.regist;
    var userId;

    var passwordContainer = document.getElementById('passwordContainer');
    var passwordEditBtn = document.getElementById('passwordEditBtn');

    $('#size').on('change', function () {
        updatePageSize(this, "/system/user/list", ["companyIdSearch", "companyTypeSearch", "nameSearch"]);
    });

    // 행 선택
    $('#pageList').on('click', 'tr', function () {
        selectRow = $(this);
        userId = selectRow.find('td').eq(3).text();
        buttonControl($(this), `/system/user/btncontrol/${userId}`);
    });

    // 사용자 - 등록
    $('#addBtn').on('click', function () {
        modalCon = false;
        btnMsg = i18n.userManagement.modal.buttons.regist;
        $('#modalBtn').text(btnMsg);
        $('#userForm')[0].reset();
        $('#companyId').prop('disabled', false);
        $('#userId').prop('disabled', false);
        $('#authority').prop('disabled', false);
        $('#duplicateBtn').prop('disabled', false);
        $('#companyId').prop('selectedIndex', 0);
        $('#authority').prop('selectedIndex', 0);
        passwordContainer.hidden = false;
        passwordEditBtn.hidden = true;
    });

    // 사용자 - 수정
    $(document).on('click', '#editBtn', function () {
        modalCon = true;
        btnMsg = i18n.userManagement.modal.buttons.edit;
        $('#modalBtn').text(btnMsg);
        $('#userForm')[0].reset();
        $('#companyId').prop('disabled', true);
        $('#userId').prop('disabled', true);
        $('#authority').prop('disabled', true);
        $('#duplicateBtn').prop('disabled', true);
        passwordContainer.hidden = true;
        passwordEditBtn.hidden = false;

        $.ajax({
            type: 'GET',
            url: `/system/user/get/${userId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {
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

    $(document).on('click', '#deleteBtn', function () {
        btnMsg = i18n.userManagement.modal.buttons.delete;

        if (confirmSubmit(btnMsg)) {
            $.ajax({
                type: 'DELETE',
                url: `/system/user/delete/${userId}`,
                contentType: "application/json",
                success: function (response) {
                    alert(response);
                    window.location.replace('/system/user/list');
                },
                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#cancelBtn').on('click', function () {
        isDuplicateCheck = false;
        userIdCheck = false;
    });

    $('#modalBtn').on('click', function (event) {
        event.preventDefault();

        const userID = $('#userId').val();
        const name = $('#name').val();
        const password = $('#password').val();
        const email = $('#email').val();

        // 등록일 경우
        if (!modalCon) {
            // 유효성 체크
            var form = document.getElementById('userForm');
            if (!form.checkValidity()) {
                alert(i18n.userManagement.messages.essentialFieldCheckError);
                return;
            }

            if (!userIdCheck) {
                alert(i18n.userManagement.messages.userIdCheckError);
                return;
            }

            if (!isPasswordSpecial(password)) { return; }

            if (email.trim() != '') {
                if (!isEmail(email)) { return; }
            }

            // 사용자ID 중복 버튼을 통해 중복ID 확인을 했는지
            if (!isDuplicateCheck) {
                alert(i18n.userManagement.messages.duplicateCheckError);
                return;
            }
        }

        if (confirmSubmit(btnMsg)) {
            const DATA = {
                companyId: $('#companyId').val(),
                userId: userID,
                name: name,
                password: password,
                email: email,
                phone: $('#phone').val(),
                authority: $('#authority').val()
            }

            console.log("userdata: ", DATA);

            const URL = modalCon ? `/system/user/update` : `/system/user/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(DATA),
                contentType: "application/json",
                success: function (response) {
                    alert(response);
                    window.location.replace('/system/user/list');
                },
                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    // 중복검사
    $('#duplicateBtn').on('click', function () {
        isDuplicateCheck = true;

        var userId = $('#userId').val();
        if (userId.trim() === '') {
            alert(i18n.userManagement.messages.inputUserId);
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
            success: function (isDuplicate) {
                if (isDuplicate) {
                    alert(i18n.userManagement.messages.alreadyRegisteredUserId);
                    userIdCheck = false;
                } else {
                    alert(i18n.userManagement.messages.availableUserId);
                    userIdCheck = true;
                }
            },
            error: function () {
                $('#userIdCheckMessage').text(i18n.userManagement.messages.error);
            }
        });
    });

    // 비밀번호 보이기
    $('.info-eyes').on('click', function () {
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

    document.getElementById('passwordEditBtn').addEventListener('click', function () {
        $('#passwordForm')[0].reset();
        var editPasswordModal = new bootstrap.Modal(document.getElementById('editPasswordModal'), {
            backdrop: 'static',
            keyboard: false
        });
        editPasswordModal.show();
    });

    document.getElementById('passwordModalBtn').addEventListener('click', function () {
        var editPasswordModal = bootstrap.Modal.getInstance(document.getElementById('editPasswordModal'));
        const existPassword = $('#existPassword').val();
        const newPassword = $('#newPassword').val();
        const newPasswordCheck = $('#newPasswordCheck').val();

        var form = document.getElementById('passwordForm');
        if (!form.checkValidity()) {
            alert(i18n.userManagement.messages.checkAllFields);
            return;
        }

        const DATA = {
            existPassword: existPassword,
            newPassword: newPassword,
            newPasswordCheck: newPasswordCheck
        }

        if (!isPasswordSpecial(newPassword)) { return; }

        var userId = $('#userId').val();
        $.ajax({
            url: `/system/user/update/password/${userId}`,
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(DATA),
            success: function (response) {
                if (response.state === 1) {
                    editPasswordModal.hide();
                }
                alert(response.message);
            },
            error: function (xhr, status, error) {
                alert(JSON.parse(xhr.responseText).message);
            }
        });
    });
});


