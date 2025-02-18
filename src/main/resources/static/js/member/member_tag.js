$(document).ready(function() {
    let selectRow, idTag;

    $('#resetBtn').on('click', function() {
        window.location.replace('/member/tag/list');
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
        const selectedIdTag = urlParams.get('idTagSearch') || '';
        const selectedName = urlParams.get('nameSearch') || '';

        window.location.href = "/member/tag/list?page=0&size=" + selectedSize +
                               "&idTagSearch=" + (selectedIdTag) +
                               "&nameSearch=" + (selectedName);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        idTag = selectRow.find('td').eq(5).text();
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        $('#memberAuthForm')[0].reset();

        console.log("idTag: " + idTag);

        $.ajax({
            type: 'GET',
            url: `/member/tag/get/${idTag}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
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

                const totalChargingPower = parseFloat(data.authInfo.totalChargingPower) || 0;
                const totalChargingPrice = parseInt(data.authInfo.totalChargingPrice) || 0;

                $('#totalChargingPower').val(totalChargingPower);
                $('#totalChargingPrice').val(totalChargingPrice);
            },
            error: function(error) {
                alert(error);
            }
        });
    });

    $('#modalBtn').on('click', function(event){
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