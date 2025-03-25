$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록";
    var faqId;

    $('#resetBtn').on('click', function() {
        window.location.replace('/faq');
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
        const selectedPage = urlParams.get('page') || 0;
        const selectedSize = document.getElementById("size").value;
        const selectedSection = urlParams.get('sectionSearch') || '';

        window.location.href = "/faq?page=" + selectedPage +
                            "&size=" + selectedSize +
                            "&sectionSearch=" + selectedSection;
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        faqId = selectRow.find('td').eq(0).attr('id');

        const cbox = $(this).closest('tr').find('input[type="checkbox"]');
        if (cbox.length > 0 && cbox.is(':checked')) {
            console.log('Checkbox is checked.');

            $.ajax({
                url: `/faq/btncontrol/${faqId}`,
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

        $('#section').prop('selectedIndex', 0);
        $('#title').val('');
        $('#content').val('');
    });

    $(document).on('click', '#editBtn', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        $.ajax({
            type: 'GET',
            url: `/faq/get/${faqId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#section').val(data.section || '');
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
                url: `/faq/delete/${faqId}`,
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
                section: $('#section').val(),
                title: $('#title').val(),
                content: $('#content').val()
            }
            
            const URL = modalCon ? `/faq/update/${faqId}` : `/faq/new`;
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