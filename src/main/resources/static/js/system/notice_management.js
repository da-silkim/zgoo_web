$(document).ready(function() {
    let modalCon = false;   // false: 등록 / true: 수정
    let selectRow, btnMsg = "등록";
    var idx;

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
        idx = selectRow.find('td').eq(0).attr('id');

        // 가장 가까운 checkbox 요소 찾기
        const cbox = $(this).closest('tr').find('input[type="checkbox"]');
        if (cbox.length > 0 && cbox.is(':checked')) {
            console.log('Checkbox is checked.');

            // 해당 데이터에 대한 수정/삭제 권한 여부 확인
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

        $('#type').prop('selectedIndex', 0);
        $('#title').val('');
        $('#content').val('');
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
                content: $('#content').val()
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