$(document).ready(function() {
    let modalCon = false;   // false: 등록 / true: 수정
    let selectRow, btnMsg = "등록";

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
    });

    $('#addBtn').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        $('#section').prop('selectedIndex', 0);
        $('#title').val('');
        $('#content').val('');
    });

    $('#editBtn').on('click', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        const id = selectRow.find('td').eq(0).attr('id');

        console.log("id type: " + typeof(id) );
        console.log("id: " + id );

        $.ajax({
            type: 'GET',
            url: `/faq/get/${id}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#section').val(data.section || '');
                $('#title').val(data.title || '');
                $('#content').val(data.content || '');
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            const id = selectRow.find('td').eq(0).attr('id');

            $.ajax({
                type: 'PATCH',
                url: `/faq/delete/${id}`,
                success: function(response) {
                    console.log("FAQ 삭제 성공: ", response);
                    window.location.reload();
                },
                error: function(error) {
                    console.log("FAQ 삭제 실패: ", error);
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
            let id;
            if (modalCon) id = selectRow.find('td').eq(0).attr('id');
            
            const URL = modalCon ? `/faq/update/${id}` : `/faq/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(DATA),
                contentType: "application/json",
                success: function() {
                    console.log("FAQ " + btnMsg +  " 성공");
                    window.location.reload();
                },
                error: function() {
                    console.log("FAQ " + btnMsg +  " 실패");
                }
            });
        }
    });
});