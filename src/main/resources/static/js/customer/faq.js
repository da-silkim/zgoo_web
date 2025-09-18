$(document).ready(function () {
    let modalCon = false, selectRow, btnMsg = i18n.faq.buttons.add;
    var faqId;

    $('#size').on('change', function () {
        updatePageSize(this, "/faq", ["sectionSearch"]);
    });

    $('#pageList').on('click', 'tr', function () {
        selectRow = $(this);
        faqId = selectRow.find('td').eq(0).attr('id');
        buttonControl($(this), `/faq/btncontrol/${faqId}`);
    });

    $('#addBtn').on('click', function () {
        modalCon = false;
        btnMsg = i18n.faq.buttons.add;
        $('#modalBtn').text(btnMsg);

        $('#section').prop('selectedIndex', 0);
        $('#title').val('');
        $('#content').val('');
    });

    $(document).on('click', '#editBtn', function () {
        modalCon = true;
        btnMsg = i18n.faq.buttons.edit;
        $('#modalBtn').text(btnMsg);

        $.ajax({
            type: 'GET',
            url: `/faq/get/${faqId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {
                $('#section').val(data.section || '');
                $('#title').val(data.title || '');
                $('#content').val(data.content || '');
            }
        });
    });

    $(document).on('click', '#deleteBtn', function () {
        btnMsg = i18n.faq.buttons.delete;

        if (confirmSubmit(btnMsg)) {
            $.ajax({
                type: 'PATCH',
                url: `/faq/delete/${faqId}`,
                success: function (response) {
                    alert(response);
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#modalBtn').on('click', function (event) {
        event.preventDefault();

        if (confirmSubmit(btnMsg)) {
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
                success: function (response) {
                    alert(response);
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });
});