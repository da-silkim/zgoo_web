$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록";
    var bizId;

    toggleInputFunction();

    $('#resetBtn').on('click', function() {
        window.location.replace('/corp/list');
    });

    $('#searchBtn').on('click', function() {
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

    $('#size').on('change', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const selectedSize = document.getElementById("size").value;
        const selectedCompanyId = urlParams.get('companyIdSearch') || '';
        const selectedManfCd = urlParams.get('manfCdSearch') || '';
        const selectedChgSpeedCd = urlParams.get('chgSpeedCdSearch') || '';

        window.location.href = "/corp/list?page=0&size=" + selectedSize +
                               "&companyIdSearch=" + (selectedCompanyId) +
                               "&manfCdSearch=" + (selectedManfCd) +
                               "&chgSpeedCdSearch=" + (selectedChgSpeedCd);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        bizId = selectRow.find('td').eq(0).attr('id');
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#companyId').prop('disabled', false);
        $('#companyId').prop('selectedIndex', 0);
        $('#bizName').val('');
        $('#bizNo').val('');
        deleteCardInfo();
    });

    function deleteCardInfo() {
        $('#fnCode').prop('selectedIndex', 0);
        $('#cardNum1').val('');
        $('#cardNum2').val('');
        $('#cardNum3').val('');
        $('#cardNum4').val('');
        $('#tid').val('');
    }

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $('#companyId').prop('disabled', true);
        $.ajax({
            type: 'GET',
            url: `/corp/get/${bizId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                console.log('수정할 데이터' + data.companyId);
                $('#companyId').val(data.companyId || '');
                $('#bizName').val(data.bizName || '');
                $('#bizNo').val(data.bizNo || '');
                $('#fnCode').val(data.fnCode || '');
                $('#cardNum1').val(data.cardNum1 || '');
                $('#cardNum2').val(data.cardNum2 || '');
                $('#cardNum3').val(data.cardNum3 || '');
                $('#cardNum4').val(data.cardNum4 || '');
                $('#tid').val(data.tid || '');
            },
            error: function(error) {
                console.log(error);
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {

            $.ajax({
                type: 'DELETE',
                url: `/corp/delete/${bizId}`,
                contentType: "application/json",
                success: function(response) {
                    // console.log(response);
                    alert(response);
                    window.location.reload();
                    // window.location.replace('/corp/list');
                },
                error: function(error) {
                    console.log(error);
                }
            });
        }
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();
        console.log('카드번호: ' + updateCardNum());
        if (confirmSubmit(btnMsg)) {
            const DATA = {
                companyId: $('#companyId').val(),
                bizName: $('#bizName').val(),
                bizNo: $('#bizNo').val(),
                fnCode: $('#fnCode').val(),
                cardNum: updateCardNum(),
                tid: $('#tid').val(),
            };

            const URL = modalCon ? `/corp/update/${bizId}` : `/corp/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';
    
            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
                success: function(response) {
                    window.location.reload();
                },
                error: function(error) {
                    alert(error);
                }
            });
        }
    });

    $('#opSearch').on('change', function() {
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