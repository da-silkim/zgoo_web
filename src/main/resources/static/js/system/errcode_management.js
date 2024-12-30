$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록", errcdId;

    $('#resetBtn').on('click', function() {
        window.location.replace('/system/errcode/list');
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
        const selectedManfCd = urlParams.get('manfCdSearch') || '';
        const selectedSearchOp = urlParams.get('opSearch') || '';
        const selectedSearchContent = urlParams.get('contentSearch') || '';

        window.location.href = "/system/errcode/list?page=0&size=" + selectedSize +
                               "&manfCdSearch=" + (selectedManfCd) +
                               "&opSearch=" + (selectedSearchOp) +
                               "&contentSearch=" + (selectedSearchContent);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        errcdId = selectRow.find('td').eq(0).attr('id');
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        $('#menufCode').prop('selectedIndex', 0);
        $('#errCode').val('');
        $('#errName').val('');
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        $.ajax({
            type: 'GET',
            url: `/system/errcode/get/${errcdId}`,
            success: function(data) {
                $('#menufCode').val(data.menufCode || '');
                $('#errCode').val(data.errCode || '');
                $('#errName').val(data.errName || '');
            },
            error: function(error) {
                console.log(error);
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if (confirmSubmit(btnMsg)) {
            $.ajax({
                type: 'DELETE',
                url: `/system/errcode/delete/${errcdId}`,
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(error) {
                    console.log(error);
                }
            });
        }

    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if (confirmSubmit(btnMsg)) {
            const DATA = {
                menufCode: $('#menufCode').val(),
                errCode: $('#errCode').val(),
                errName: $('#errName').val(),
            }

            const URL = modalCon ? `/system/errcode/update/${errcdId}` : `/system/errcode/new`;
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
                error: function(xhr, status, error) {
                    console.log(error);
                }
            });
        }
    });
});