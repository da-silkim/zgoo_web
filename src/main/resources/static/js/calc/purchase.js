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

    $('#stationOpSearchBtn').on('click', function() {
        const searchStation = document.getElementById('searchStation').value;
        const stationOpSearch = document.getElementById('stationOpSearch').value;
        $.ajax({
            url: `/purchase/search/station`,
            method: 'GET',
            data: {searchOp: searchStation, searchContent: stationOpSearch},
            success: function(response) {
                var stationSearchList = document.getElementById('stationSearchList');
                stationSearchList.innerHTML = '';
                if (response.csList.length === 0) {
                    stationSearchList.innerHTML = '<tr><td colspan="3">조회된 데이터가 없습니다.</td></tr>';
                    return;
                }

                response.csList.forEach(function (cs) {
                    var row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${cs.statinName}</td>
                        <td>${cs.stationId}</td>
                        <td><button type="button" class="btn btn-outline-grey select-biz-btn" data-station-id="${cs.stationId}" data-station-name="${biz.stationName}">선택</button></td>
                    `;
                    stationSearchList.appendChild(row);
                });
            },
            error: function(xhr) {
                var response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });

    document.getElementById('stationSearchBtn').addEventListener('click', function () {
        var stationSearchModal = new bootstrap.Modal(document.getElementById('stationSearchModal'), {
            backdrop: 'static',
            keyboard: false
        });
        stationSearchModal.show();
    });
});