$(document).ready(function () {
    let modalCon = false, selectRow, btnMsg = "등록";
    var bizId;

    //===============빌키 발급 요청 시작====
    $('#billkeyBtn').on('click', function () {
        console.log('빌키발급 START >> ');
        if (!validateCardInfo()) {
            alert('카드 정보를 모두 입력해주세요.');
            return;
        }

        //카드 유효기간 포맷(YYMM)
        const expYear = $('#cardExpYear').val();
        const expMonth = $('#cardExpMonth').val();
        const cardExpire = expYear + expMonth;

        //카드번호 조합
        const cardNum = $('#cardNum1').val() + $('#cardNum2').val() + $('#cardNum3').val() + $('#cardNum4').val();

        //빌키 발급 요청 데이터
        const requestData = {
            cardNum: cardNum,
            cardExpire: cardExpire,
            cardPwd: $('#cardPwd').val(),
            bizNo: $('#bizNo').val(),
            bizName: $('#bizName').val()
        };

        //빌키 발급 요청
        $.ajax({
            url: '/corp/billkey',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            success: function (response) {
                console.log('빌키 발급 결과:', response);
                $('#bid').val(response.BID);
                $('#cardCode').val(response.CardCode);
                $('#cardName').val(response.CardName);
                $('#authDate').val(response.AuthDate);
            },
            error: function (error) {
                console.log('빌키 발급 오류:', error);
                alert('빌키 발급 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });


    });

    // 카드 정보 유효성 검사 함수
    function validateCardInfo() {
        const cardNum1 = $('#cardNum1').val();
        const cardNum2 = $('#cardNum2').val();
        const cardNum3 = $('#cardNum3').val();
        const cardNum4 = $('#cardNum4').val();
        const cardExpMonth = $('#cardExpMonth').val();
        const cardExpYear = $('#cardExpYear').val();
        const cardPwd = $('#cardPwd').val();
        const bizName = $('#bizName').val();
        const bizNo = $('#bizNo').val();        //법인사업자번호 혹은 생년월일

        console.log(cardNum1, cardNum2, cardNum3, cardNum4, cardExpMonth, cardExpYear, cardPwd, bizName, bizNo);

        // 모든 필드가 입력되었는지 확인
        if (!cardNum1 || !cardNum2 || !cardNum3 || !cardNum4 ||
            !cardExpMonth || !cardExpYear || !cardPwd ||
            !bizName || !bizNo) {
            return false;
        }

        // 카드번호 길이 확인
        if (cardNum1.length !== 4 || cardNum2.length !== 4 ||
            cardNum3.length !== 4 || cardNum4.length !== 4) {
            return false;
        }

        // 유효기간 형식 확인
        if (cardExpMonth.length !== 2 || cardExpYear.length !== 2 ||
            parseInt(cardExpMonth) < 1 || parseInt(cardExpMonth) > 12) {
            return false;
        }

        // 카드 비밀번호 길이 확인
        if (cardPwd.length !== 2) {
            return false;
        }

        // 생년월일/사업자번호 길이 확인
        if (bizNo.length !== 6) {
            alert('생년월일 입력값 오류: 6자리로 입력해주세요.');
            return false;
        }
        // if (birthDay.length !== 6 || birthDay.length !== 10) {
        //     return false;
        // }

        return true;
    }

    // 카드번호 입력 후 자동으로 다음 입력란으로 이동하는 기능
    $('#cardNum1, #cardNum2, #cardNum3').on('input', function () {
        if (this.value.length === 4) {
            $(this).next().next().focus();
        }
    });

    // 유효기간 월 입력 후 자동으로 연도 입력란으로 이동
    $('#cardExpMonth').on('input', function () {
        if (this.value.length === 2) {
            $('#cardExpYear').focus();
        }
    });


    //===============빌키 발급 요청 끝====

    // toggleInputFunction();

    $('#resetBtn').on('click', function () {
        window.location.replace('/corp/list');
    });

    $('#searchBtn').on('click', function () {
        const selectedSize = document.getElementById('size').value;
        const form = document.getElementById('searchForm');

        const hiddenSizeInput = document.createElement('input');
        hiddenSizeInput.type = 'hidden';
        hiddenSizeInput.name = 'size';
        hiddenSizeInput.value = selectedSize;
        hiddenSizeInput.id = 'hiddenSizeInput';

        form.appendChild(hiddenSizeInput);
        form.submit();
    });

    $('#size').on('change', function () {
        const urlParams = new URLSearchParams(window.location.search);
        const selectedSize = document.getElementById("size").value;
        const selectedManfCd = urlParams.get('manfCdSearch') || '';
        const selectedChgSpeedCd = urlParams.get('chgSpeedCdSearch') || '';

        window.location.href = "/corp/list?page=0&size=" + selectedSize +
            "&manfCdSearch=" + (selectedManfCd) +
            "&chgSpeedCdSearch=" + (selectedChgSpeedCd);
    });

    $('#pageList').on('click', 'tr', function () {
        selectRow = $(this);
        bizId = selectRow.find('td').eq(0).attr('id');
    });

    $('#addBtn').on('click', function (event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#bizName').val('');
        $('#bizNo').val('');
        // deleteCardInfo();
    });

    function deleteCardInfo() {
        $('#fnCode').prop('selectedIndex', 0);
        $('#cardNum1').val('');
        $('#cardNum2').val('');
        $('#cardNum3').val('');
        $('#cardNum4').val('');
        $('#tid').val('');
    }

    $('#editBtn').on('click', function (event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $.ajax({
            type: 'GET',
            url: `/corp/get/${bizId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {
                $('#bizName').val(data.bizName || '');
                $('#bizNo').val(data.bizNo || '');
                $('#fnCode').val(data.fnCode || '');
                $('#cardNum1').val(data.cardNum1 || '');
                $('#cardNum2').val(data.cardNum2 || '');
                $('#cardNum3').val(data.cardNum3 || '');
                $('#cardNum4').val(data.cardNum4 || '');
                $('#tid').val(data.tid || '');
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $('#deleteBtn').on('click', function () {
        btnMsg = "삭제";

        if (confirmSubmit(btnMsg)) {

            $.ajax({
                type: 'DELETE',
                url: `/corp/delete/${bizId}`,
                contentType: "application/json",
                success: function (response) {
                    // console.log(response);
                    alert(response);
                    window.location.reload();
                    // window.location.replace('/corp/list');
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
    });

    $('#modalBtn').on('click', function (event) {
        event.preventDefault();
        console.log('카드번호: ' + updateCardNum());
        if (confirmSubmit(btnMsg)) {
            const DATA = {
                bizNo: $('#bizNo').val(),
                bizName: $('#bizName').val(),
                bid: $('#bid').val(),
                cardNum: updateCardNum(),
                cardCode: $('#cardCode').val(),
                cardName: $('#cardName').val(),
                authDate: $('#authDate').val(),
            };

            const URL = modalCon ? `/corp/update/${bizId}` : `/corp/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
                success: function (response) {
                    window.location.reload();
                },
                error: function (error) {
                    alert(error);
                }
            });
        }
    });

    $('#opSearch').on('change', function () {
        toggleInputFunction();
    });

    function toggleInputFunction() {
        const opSearch = document.getElementById('opSearch').value;
        const contentSearch = document.getElementById('contentSearch');

        if (!opSearch || opSearch === "null") {
            contentSearch.setAttribute('disabled', 'disabled');
            contentSearch.removeAttribute('oninput');
            contentSearch.value = '';
        } else {
            contentSearch.removeAttribute('disabled');

            if (opSearch === "bizNo") {
                contentSearch.setAttribute('oninput', 'addHyphenBizNo(this)');
            } else {
                contentSearch.removeAttribute('oninput');
            }
        }
    }

    function updateCardNum() {
        var cardNum1 = document.getElementById('cardNum1').value;
        var cardNum2 = document.getElementById('cardNum2').value;
        var cardNum3 = document.getElementById('cardNum3').value;
        var cardNum4 = document.getElementById('cardNum4').value;

        return cardNum1 + cardNum2 + cardNum3 + cardNum4;
    }
});