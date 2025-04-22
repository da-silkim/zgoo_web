$(document).ready(function() {
    let modalCon = false;
    let selectRow, btnMsg = "등록";
    var idx;

    $('#size').on('change', function() {
        updatePageSize(this, "/system/notice/list", ["companyIdSearch", "startDateSearch", "endDateSearch"]);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        idx = selectRow.find('td').eq(0).attr('id');
        buttonControl($(this), `/system/notice/btncontrol/${idx}`);
    });

    $('#addBtn').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#noticeForm')[0].reset();
    });

    $(document).on('click', '#editBtn', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        $.ajax({
            type: 'GET',
            url: `/system/notice/get/${idx}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#type').val(data.type || '');
                $('#title').val(data.title || '');
                $('#content').val(data.content || '');
                $('#startDate').val(data.startDate || '');
                $('#endDate').val(data.endDate || '');
            }
        });
    });

    $(document).on('click', '#deleteBtn', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            $.ajax({
                type: 'PATCH',
                url: `/system/notice/delete/${idx}`,
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
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
                content: $('#content').val(),
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val()
            }
            
            const URL = modalCon ? `/system/notice/update/${idx}` : `/system/notice/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(DATA),
                contentType: "application/json",
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });
});