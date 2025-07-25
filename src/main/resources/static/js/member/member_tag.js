$(document).ready(function () {
    let selectRow, idTag;

    $('#size').on('change', function () {
        updatePageSize(this, "/member/tag/list", ["idTagSearch", "nameSearch"]);
    });

    $('#pageList').on('click', 'tr', function () {
        selectRow = $(this);
        idTag = selectRow.find('td').eq(4).text();
    });

    $('#editBtn').on('click', function (event) {
        event.preventDefault();
        $('#memberAuthForm')[0].reset();

        $.ajax({
            type: 'GET',
            url: `/member/tag/get/${idTag}`,
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {
                if (data.message) {
                    alert(data.message);
                }

                $('#name').val(data.authInfo.name || '');
                $('#status').val(data.authInfo.status || '');
                $('#idTag').val(data.authInfo.idTag || '');
                $('#expireDate').val(data.authInfo.expireDate || '');
                $('#parentIdTag').val(data.authInfo.parentIdTag || '');

                if (data.authInfo.useYn === 'Y') {
                    $('#useYes').prop('checked', true);
                } else {
                    $('#useNo').prop('checked', true);
                }

                const totalChargingPower = parseFloat(data.authInfo.totalChargingPower) || 0.00;
                const formattedPower = totalChargingPower.toLocaleString('en-US', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                });
                $('#totalChargingPower').val(formattedPower);

                const totalChargingPrice = parseInt(data.authInfo.totalChargingPrice) || 0;
                $('#totalChargingPrice').val(totalChargingPrice);
            },
            error: function (xhr, status, error) {
                alert(JSON.parse(xhr.responseText).message);
            }
        });
    });

    $('#deleteBtn').on('click', function () {
        if (confirmSubmit("삭제")) {
            $.ajax({
                type: 'DELETE',
                url: `/member/tag/delete/${idTag}`,
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

    $('#modalBtn').on('click', function (event) {
        event.preventDefault();

        if (confirmSubmit("수정")) {
            const DATA = {
                idTag: $('#idTag').val(),
                status: $('#status').val(),
                expireDate: $('#expireDate').val(),
                parentIdTag: $('#parentIdTag').val(),
                useYn: $('input[name="useYn"]:checked').val(),
            }

            $.ajax({
                url: `/member/tag/update`,
                method: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify(DATA),
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