$(document).ready(function() {
    let modalCon = false;
    let selectRow, btnMsg = "등록";
    var id;

    $('#size').on('change', function() {
        updatePageSize(this, "/calc/purchase", ["opSearch", "contentSearch", "startDate", "endDate"]);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        id = selectRow.find('td').eq(0).attr('id');
        console.log("id: ", id);
    });

    $('#deleteBtn').on('click', function() {
        if (confirmSubmit("삭제")) {
            $.ajax({
                type: 'PATCH',
                url: `/purchase/delete/${id}`,
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