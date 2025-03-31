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

        const cbox = $(this).closest('tr').find('input[type="checkbox"]');
        if (cbox.length > 0 && cbox.is(':checked')) {
            console.log('Checkbox is checked.');
            $.ajax({
                url: `/system/notice/btncontrol/${idx}`,
                type: 'GET',
                success: function(response) {
                    if (response.btnControl) {
                        $('#buttonContainer').html(`
                            <button class="btn btn-data-edit" id="editBtn"
                                data-bs-toggle="modal" data-bs-target="#dataAddModal">
                                <i class="fa-regular fa-pen-to-square"></i>수정
                            </button>
                            <button class="btn btn-data-delete" id="deleteBtn">
                                <i class="bi bi-trash"></i>삭제
                            </button>
                        `);
                    } else {
                        $('#buttonContainer').empty();
                    }
                },
                error: function(error) {
                    console.log(error);
                }
            });
        } else {
            console.log('Checkbox is not checked.');
            btnControl = false;
            $('#buttonContainer').empty();
        }
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
                error: function(error) {
                    alert(error);
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
                error: function(error) {
                    alert(error);
                }
            });
        }
    });
});