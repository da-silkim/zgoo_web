$(document).ready(function() {
    let modalCon = false;   // false: 등록 / true: 수정
    let selectRow, btnMsg = "등록";

    $('#resetBtn').on('click', function() {
        window.location.replace('/system/notice/list');
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
        const selectedStartDate = urlParams.get('startDateSearch') || '';
        const selectedEndDate = urlParams.get('endDateSearch') || '';

        window.location.href = "/system/notice/list?page=0&size=" + selectedSize +
                               "&companyIdSearch=" + (selectedCompanyId) +
                               "&startDateSearch=" + encodeURIComponent(selectedStartDate) +
                               "&endDateSearch=" + encodeURIComponent(selectedEndDate);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
    });

    $('#addBtn').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        $('#type').prop('selectedIndex', 0);
        $('#title').val('');
        $('#content').val('');
    });

    $('#editBtn').on('click', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        // const idx = selectRow.attr('id');
        const idx = selectRow.find('td').eq(0).attr('id');

        console.log("idx type: " + typeof(idx) );
        console.log("idx: " + idx );

        $.ajax({
            type: 'GET',
            url: `/system/notice/get/${idx}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#type').val(data.type || '');
                $('#title').val(data.title || '');
                $('#content').val(data.content || '');
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            const idx = selectRow.find('td').eq(0).attr('id');

            $.ajax({
                type: 'PATCH',
                url: `/system/notice/delete/${idx}`,
                success: function(response) {
                    console.log("공지 삭제 성공: ", response);
                    window.location.reload();
                },
                error: function(error) {
                    console.log("공지 삭제 실패: ", error);
                }
            });
        }
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if(confirmSubmit(btnMsg)) {
            const DATA = {
                type: $('#type').val(),
                title: $('#title').val(),
                content: $('#content').val()
            }
            let idx;
            if (modalCon) idx = selectRow.find('td').eq(0).attr('id');
            
            const URL = modalCon ? `/system/notice/update/${idx}` : `/system/notice/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(DATA),
                contentType: "application/json",
                success: function() {
                    console.log("공지사항 " + btnMsg +  " 성공");
                    // window.location.replace('/system/notice/list');
                    window.location.reload();
                },
                error: function() {
                    console.log("공지사항 " + btnMsg +  " 실패");
                }
            });
        }
    });
});