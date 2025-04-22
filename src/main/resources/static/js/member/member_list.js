$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록";
    let isDuplicateCheck = false, memLoginIdCheck = false;
    var memberId;

    var passwordContainer = document.getElementById('passwordContainer');
    var passwordEditBtn = document.getElementById('passwordEditBtn');

    document.getElementById('bizSearchBtn').addEventListener('click', function () {
        var bizSearchModal = new bootstrap.Modal(document.getElementById('bizSearchModal'), {
            backdrop: 'static',
            keyboard: false
        });
        bizSearchModal.show();
    });

    document.getElementById('bizNameSearchBtn').addEventListener('click', function () {
        var bizName = document.getElementById('bizNameSearch').value;
        $.ajax({
            url: `/corp/search/${bizName}`,
            method: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: function(response) {
                var bizSearchList = document.getElementById('bizSearchList');
                bizSearchList.innerHTML = '';
                if (response.bizList.length === 0) {
                    bizSearchList.innerHTML = '<tr><td colspan="3">조회된 데이터가 없습니다.</td></tr>';
                    return;
                }

                response.bizList.forEach(function (biz) {
                    var row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${biz.bizName}</td>
                        <td>${biz.bizNo}</td>
                        <td><button type="button" class="btn btn-outline-grey select-biz-btn" data-biz-id="${biz.bizId}" data-biz-name="${biz.bizName}">선택</button></td>
                    `;
                    bizSearchList.appendChild(row);
                });
            },
            error: function(xhr) {
                var response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });

    document.getElementById('bizSearchList').addEventListener('click', function (e) {
        if (e.target.classList.contains('select-biz-btn')) {
            var bizId = e.target.getAttribute('data-biz-id');
            var bizName = e.target.getAttribute('data-biz-name');
    
            var bizNameInput = document.getElementById('bizNameInput');
            var bizIdInput = document.getElementById('bizId');
            bizNameInput.value = bizName;
            bizIdInput.value = bizId;
    
            $.ajax({
                url: `/corp/get/custom/${bizId}`,
                method: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                success: function(data) {
                    $('#bizName').val(data.bizName || '');
                    $('#bizNo').val(data.bizNo || '');
                    $('#cardYn').val(data.cardYn || '');
                    $('#tidYn').val(data.tidYn || '');
                },
                error: function(error) {
                    alert(error);
                }
            });
            var bizSearchModal = bootstrap.Modal.getInstance(document.getElementById('bizSearchModal'));
            bizSearchModal.hide();
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
                alert('값을 모두 입력해주세요.');
                return;
            }

        const DATA = {
            existPassword: existPassword,
            newPassword: newPassword,
            newPasswordCheck: newPasswordCheck
        }
        
        if (!isPasswordSpecial(newPassword)) { return; }

        $.ajax({
            url: `/member/update/password/${memberId}`,
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(DATA),
            success: function(response) {
                if (response.state === 1) {
                    editPasswordModal.hide();
                }
                alert(response.message);
            },
            error: function(error) {
                alert(error);
            }
        });
    });

    document.getElementById('memLoginId').addEventListener('input', function() {
        isDuplicateCheck = false;
    });

    document.getElementById('bizType').addEventListener('change', handleBizYnChange);

    function handleBizYnChange() {
        var bizTypeValue = document.getElementById('bizType').value;
        var bizSearchBtn = document.getElementById('bizSearchBtn');
        var bizInfoContainer = document.getElementById('bizInfoContainer');
        // var cardInfoContainer = document.getElementById('cardInfoContainer');
    
        if (bizTypeValue === 'PB') {   // 개인
            bizNameInput.value = '';
            bizSearchBtn.disabled = true;
            bizInfoContainer.hidden = true;
            // cardInfoContainer.hidden = false;
        } else if (bizTypeValue === 'CB') { // 법인
            bizSearchBtn.disabled = false;
            bizInfoContainer.hidden = false;
            // cardInfoContainer.hidden = true;
        }
    }

    $('#duplicateMemLoginIdBtn').on('click', function() {
        isDuplicateCheck = true;

        var memLoginId = $('#memLoginId').val();
        if (memLoginId.trim() === '') {
            alert('사용자ID를 입력해주세요');
            memLoginIdCheck = false;
            return;
        }

        if (!isId(memLoginId)) { 
            memLoginIdCheck = false;
            return;
        }

        $.ajax({
            url: '/member/memLoginId',
            type: 'GET',
            data: { memLoginId: memLoginId },
            success: function(isDuplicate) {
                if (isDuplicate) {
                    alert('이미 사용 중인 ID입니다.');
                    memLoginIdCheck = false;
                } else {
                    alert('사용 가능한 ID입니다.');
                    memLoginIdCheck = true;
                }
            },
            error: function(error) {
                console.error(error);
            }
        });
    });

    $('#size').on('change', function() {
        updatePageSize(this, "/system/notice/list", ["companyIdSearch", "idTagSearch", "nameSearch"]);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        memberId = selectRow.find('td').eq(0).attr('id');
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#memberForm')[0].reset();
        handleBizYnChange();
        clearCarTable();
        // clearCardTable();

        $('#companyId').prop('disabled', false);
        $('#bizType').prop('disabled', false);
        $('#bizSearchBtn').prop('disabled', false);
        $('#memLoginId').prop('disabled', false);
        $('#userState').val('MSTNORMAL');
        $('#duplicateMemLoginIdBtn').prop('disabled', false);
        $('#bizNameSearch').val('');
        document.getElementById('bizSearchList').innerHTML = '<tr><td colspan="3">조회된 데이터가 없습니다.</td></tr>';
        passwordContainer.hidden = false;
        passwordEditBtn.hidden = true;
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $('#memberForm')[0].reset();
        $('#companyId').prop('disabled', true);
        $('#bizType').prop('disabled', true);
        $('#memLoginId').prop('disabled', true);
        $('#idTag').prop('disabled', true);
        $('#duplicateMemLoginIdBtn').prop('disabled', true);
        passwordContainer.hidden = true;
        passwordEditBtn.hidden = false;

        $.ajax({
            type: 'GET',
            url: `/member/get/${memberId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#companyId').val(data.memberInfo.companyId || '');
                $('#bizType').val(data.memberInfo.bizType || '');
                handleBizYnChange();
                $('#bizSearchBtn').prop('disabled', true);
                if (data.memberInfo.bizType === 'CB') { // 법인
                    $('#bizName').val(data.bizInfo.bizName || '');
                    $('#bizNo').val(data.bizInfo.bizNo || '');
                    $('#cardYn').val(data.bizInfo.cardYn || '');
                    $('#tidYn').val(data.bizInfo.tidYn || '');
                    var bizNameInput = document.getElementById('bizNameInput');
                    var bizIdInput = document.getElementById('bizId');
                    bizNameInput.value = data.bizInfo.bizName;
                    bizIdInput.value = data.bizInfo.bizId;
                }
                // card table
                // else {
                //     const cardTableBody = document.querySelector("#cardTable tbody");
                //     cardTableBody.innerHTML = "";
                //     data.memberInfo.card.forEach(card => {
                //         const row = document.createElement("tr");
                //         row.classList.add("card-row");
                //         row.innerHTML = `<td><input type="checkbox" class="single-checkbox"></td>`;

                //         const selectElement = document.createElement('select');
                //         selectElement.classList.add('form-control', 'fnCode');
                //         creditCardList.forEach(function(data) {
                //             const option = document.createElement('option');
                //             option.value = data.commonCode;
                //             option.textContent = data.commonCodeName;
                //             if (card.fnCode === data.commonCode) {
                //                 option.selected = true;
                //             }
                //             selectElement.appendChild(option);
                //         });
                //         const tdSelect = document.createElement('td');
                //         tdSelect.appendChild(selectElement);
                //         row.appendChild(tdSelect);

                //         row.innerHTML += `
                //         <td><input type="text" class="input-add-row text-center cardNum" value="${card.carNum ? card.carNum : ''}"/></td>
                //         <td><input type="text" class="input-add-row text-center tid" value="${card.tid ? card.tid : ''}"/></td>
                //         <td><input type="radio" class="mx-3 representativeCard" value="${card.representativeCard}" ${card.representativeCard == "Y" ? "checked" : ""}/></td>
                //         `;
    
                //         cardTableBody.appendChild(row);
                //     });
                // }

                $('#name').val(data.memberInfo.name || '');
                $('#phoneNo').val(data.memberInfo.phoneNo || '');
                $('#memLoginId').val(data.memberInfo.memLoginId || '');
                $('#password').val(data.memberInfo.password || '');
                $('#idTag').val(data.memberInfo.idTag || '');
                $('#userState').val(data.memberInfo.userState || '');
                $('#email').val(data.memberInfo.email || '');
                $('#birth').val(data.memberInfo.birth || '');
                $('#zipCode').val(data.memberInfo.zipCode || '');
                $('#address').val(data.memberInfo.address || '');
                $('#addressDetail').val(data.memberInfo.addressDetail || '');

                const carTableBody = document.querySelector("#carTable tbody");
                carTableBody.innerHTML = "";

                data.memberInfo.car.forEach(car => {
                    const row = document.createElement("tr");
                    row.classList.add("car-row");
                    row.innerHTML = `<td><input type="checkbox" class="single-checkbox"></td>`;
                    const selectElement = document.createElement('select');
                    selectElement.classList.add('form-control', 'carType');
                    bizTypeList.forEach(function(data) {
                        const option = document.createElement('option');
                        option.value = data.commonCode;
                        option.textContent = data.commonCodeName;
                        if (car.carType === data.commonCode) {
                            option.selected = true;
                        }
                        selectElement.appendChild(option);
                    });
                    const tdSelect = document.createElement('td');
                    tdSelect.appendChild(selectElement);
                    row.appendChild(tdSelect);

                    row.innerHTML += `
                    <td><input type="text" class="input-add-row text-center carNum" value="${car.carNum ? car.carNum : ''}"/></td>
                    <td><input type="text" class="input-add-row text-center model" value="${car.model ? car.model : ''}"/></td>
                    `;

                    carTableBody.appendChild(row);
                });

                data.memberInfo.condition.forEach(con => {
                    if (con.conditionCode === 'PI') {
                        if (con.agreeYn === 'Y') {
                            $('#privateY').prop('checked', true);
                        } else {
                            $('#privateN').prop('checked', true);
                        }
                    } else if (con.conditionCode === 'MS') {
                        if (con.agreeYn === 'Y') {
                            $('#memServiceY').prop('checked', true);
                        } else {
                            $('#memServiceN').prop('checked', true);
                        }
                    } else if (con.conditionCode === 'MK') {
                        if (con.agreeYn === 'Y') {
                            $('#marketingY').prop('checked', true);
                        } else {
                            $('#marketingN').prop('checked', true);
                        }
                    } else if (con.conditionCode === 'ES') {
                        if (con.agreeYn === 'Y') {
                            $('#emailY').prop('checked', true);
                        } else {
                            $('#emailN').prop('checked', true);
                        }
                    } else if (con.conditionCode === 'SS') {
                        if (con.agreeYn === 'Y') {
                            $('#smsY').prop('checked', true);
                        } else {
                            $('#smsN').prop('checked', true);
                        }
                    }
                });
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            $.ajax({
                type: 'DELETE',
                url: `/member/delete/${memberId}`,
                contentType: "application/json",
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(error) {
                    alert(error);
                }
            });
        }
    });
    
    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        const carInfoList = getCarList();
        // const cardInfoList = getCreditCardList();

        if (!carInfoList) {
            return;
        }

        const bizTypeValue = $('#bizType').val();
        const bizIdValue = $('#bizId').val();
        if (bizTypeValue === 'CB') {
            if (bizIdValue === null || bizIdValue.trim() === "") {
                alert("법인 정보를 조회해주세요.");
                return;
            }
        }

        // 등록일 경우
        if (!modalCon) {
            const password = $('#password').val();
            const email = $('#email').val();

            var form = document.getElementById('memberForm');
            if (!form.checkValidity()) { 
                alert('필수 입력 값이 누락되어 있습니다.');
                return;
            }

            if(!isDuplicateCheck) {
                alert('사용자ID 중복체크를 해주세요.');
                return;
            }

            if(!memLoginIdCheck) {
                alert('사용자ID를 다시 확인해주세요.');
                return;
            }

            if (!isPasswordSpecial(password)) { return; }

            if (email.trim() != '') {
                if (!isEmail(email)) { return; }
            }
        }

        const DATA = {
            companyId: $('#companyId').val(),
            bizType: $('#bizType').val(),
            name: $('#name').val(),
            phoneNo: $('#phoneNo').val(),
            memLoginId: $('#memLoginId').val(),
            password: $('#password').val(),
            idTag: $('#idTag').val(),
            userState: $('#userState').val(),
            email: $('#email').val(),
            birth: $('#birth').val(),
            zipCode: $('#zipCode').val(),
            address: $('#address').val(),
            addressDetail: $('#addressDetail').val(),
            car: carInfoList,
            condition: getConditionList()
        };

        // 법인
        const CBDATA = {
            bizId: $('#bizId').val()
        };

        // 개인
        // const PBDATA = {
        //     card: cardInfoList
        // };

        if (bizTypeValue === 'CB') {
            Object.assign(DATA, CBDATA);
        }
        // else if (bizTypeValue === 'PB') {
        //     Object.assign(DATA, PBDATA);
        // }
        // console.log("법인ID: " + bizIdValue);
        console.log(DATA);

        if (confirmSubmit(btnMsg)) {
            const URL = modalCon ? `/member/update/${memberId}` : '/member/new';
            const TYPE = modalCon ? 'PATCH' : 'POST';
    
            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(error) {
                    alert(error);
                }
            });
        }
    });

    function getCarList() {
        const carRows = document.querySelectorAll("#carTable .car-row");
        const carInfoList = [];
        var isValid = true;

        carRows.forEach(row => {
            const carType = row.querySelector(".carType").value;
            const carNum = row.querySelector(".carNum").value;
            const model = row.querySelector(".model").value;

            if (!carNum || carNum.trim() === "" || !model || model.trim() === "") {
                isValid = false;
                return;
            }

            carInfoList.push({
                carType: carType,
                carNum: carNum,
                model: model
            });
        });

        if (!isValid) {
            alert("차량번호 또는 차량모델이 누락됐습니다. 값을 입력하거나 행을 삭제해주세요.");
            return null;
        }

        console.log("차량 정보:", carInfoList);
        return carInfoList;
    }

    function getCreditCardList() {
        const cardRows = document.querySelectorAll("#cardTable .card-row");
        const cardInfoList = [];
        var isValid = true;
        var representativeCardCnt = 0;

        cardRows.forEach(row => {
            const fnCode = row.querySelector(".fnCode").value;
            const cardNum1 = row.querySelector(".cardNum1").value;
            const cardNum2 = row.querySelector(".cardNum2").value;
            const cardNum3 = row.querySelector(".cardNum3").value;
            const cardNum4 = row.querySelector(".cardNum4").value;
            const cardNum = cardNum1 + cardNum2 + cardNum3 + cardNum4;
            const tid = row.querySelector(".tid").value;
            const representativeCard = row.querySelector(".representativeCard").checked ? "Y" : "N";

            if (representativeCard === "N") representativeCardCnt += 1;

            if (!cardNum || cardNum.trim() === "") {
                isValid = false;
                return;
            }

            cardInfoList.push({
                fnCode: fnCode,
                cardNum: cardNum,
                tid: tid,
                representativeCard: representativeCard
            });
        });

        // 카드 정보가 1개일 때 representativeCard 값이 "N"이면 "Y"로 변경
        if (cardInfoList.length === 1 && cardInfoList[0].representativeCard === "N") {
            cardInfoList[0].representativeCard = "Y";
        } else if (cardInfoList.length > 1) {   // 2개 이상일 때 처리
            if (cardInfoList.length === representativeCardCnt) {
                alert("대표카드를 선택하셔야 합니다.");
                return null;
            }
        }

        if (!isValid) {
            alert("카드번호가 누락됐습니다. 값을 입력하거나 행을 삭제해주세요.");
            return null;
        }

        console.log("카드 정보:", cardInfoList);
        return cardInfoList;
    }

    function getConditionList() {
        const conditionRows = document.querySelectorAll("#conditionTable .condition-row");
        const conditionInfoList = [];

        conditionRows.forEach(row => {
            // const section = row.querySelector(".condition-section").innerText;
            const conditionCode = row.querySelector(".condition-code").id;
            const agreeYn = row.querySelector('input[type="radio"]:checked').value;

            conditionInfoList.push({
                conditionCode: conditionCode,
                agreeYn: agreeYn,
                // section: section
            });
        });

        console.log("약관 정보:", conditionInfoList);
        return conditionInfoList;
    }

    function clearCarTable() {
        var carTableBody = document.getElementById('carTableBody');
        carTableBody.innerHTML = '';
    }

    function clearCardTable() {
        var cardTableBody = document.getElementById('cardTableBody');
        cardTableBody.innerHTML = '';
    }

    $('.info-eyes').on('click', function() {
        var inputGroup = $(this).parents('.input-info-group');
        var passwordField = inputGroup.find('#password');
        
        inputGroup.toggleClass('active');
        if (inputGroup.hasClass('active')) {
            $(this).find('.bi-eye').attr('class', 'bi bi-eye-slash');
            passwordField.attr('type', 'text');
        } else {
            $(this).find('.bi-eye-slash').attr('class', 'bi bi-eye');
            passwordField.attr('type', 'password');
        }
    });

    $('.condition-detail').on('click', function(event) {
        event.preventDefault();

        var classList = $(this).attr('class').split(/\s+/);
        var itemType = classList[1];

        console.log('classList: ' + classList);
        console.log('itemType: ' + itemType);


        $.ajax({
            type: 'GET',
            url: `/member/file/${itemType}`,
            success: function(data) {
                $('#conditonText').html(data.fileContent);
                $('#conditionName').html(data.conditionName);
                console.log('data: ' + data.fileContent);

                var conditionModal = new bootstrap.Modal(document.getElementById('conditionModal'), {
                    backdrop: 'static',
                    keyboard: false
                });
                conditionModal.show();
            },
            error: function(error) {
                alert(error);
            }
        });
    });
});