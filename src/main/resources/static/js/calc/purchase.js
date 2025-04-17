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
});