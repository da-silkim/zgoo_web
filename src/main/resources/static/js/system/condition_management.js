$(document).ready(function() {
    let selectRow, histSelectRow;
    var conditionCode, conditionName;

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        conditionCode = selectRow.find('td').eq(0).attr('id');
        conditionName = selectRow.find('td').eq(2).text();
        document.getElementById('conditionCodeSub').innerText  = conditionCode;
        document.getElementById('conditionNameSub').innerText  = conditionName;
        renderConditionHist();
    });

    function renderConditionHist() {
        $.ajax({
            type: 'GET',
            url: `/system/condition/hist/search/${conditionCode}`,
            success: function(data) {
                $('#pageListSub').empty();

                if (!data || data.length === 0) {
                    $('#pageListSub').append(`
                        <tr>
                            <td colspan="6">조회된 데이터가 없습니다.</td>
                        </tr>
                    `);
                } else {
                    data.forEach(function(con) {
                        $('#pageListSub').append(`
                            <tr>
                                <td id=${con.conditionVersionHistId}><input type="checkbox"/></td>
                                <td>${con.applyDt ? formatDate(new Date(con.applyDt)) : ''}</td>
                                <td>${con.applyYn || ''}</td>
                                <td>${con.version || ''}</td>
                                <td class="text-left">${con.memo || ''}</td>
                                <td><a href="/system/condition/hist/download?id=${con.conditionVersionHistId}" download>
                                    <i class="bi bi-file-earmark-arrow-down detail-files"></i></a></td>
                            </tr>
                        `);
                    });
                }
            },
            error: function(xhr, status, error) {
            }
        });
    }

    $('#pageListSub').on('click', 'tr', function() {
        histSelectRow = $(this);
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        $('#conditionForm')[0].reset();
    });

    $('#addBtnSub').on('click', function(event) {
        event.preventDefault();
        $('#conHistForm')[0].reset();
    });

    $('#deleteBtn').on('click', function() {
        if(confirmSubmit("삭제")) {
            $.ajax({
                type: 'DELETE',
                url: `/system/condition/delete/${conditionCode}`,
                contentType: "application/json",
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#deleteBtnSub').on('click', function() {
        if(confirmSubmit("삭제")) {
            const conditionVersionHistId = histSelectRow.find('td').eq(0).attr('id');
            $.ajax({
                type: 'DELETE',
                url: `/system/condition/delete/hist/${conditionVersionHistId}`,
                contentType: "application/json",
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if(confirmSubmit("저장")) {
            const DATA = {
                section: $('input[name="section"]:checked').val(),
                conditionCode: $('#conditionCode').val(),
                conditionName: $('#conditionName').val()
            }

            $.ajax({
                type: 'POST',
                url: '/system/condition/new',
                data: JSON.stringify(DATA),
                contentType: "application/json",
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });

    $('#histModalBtn').on('click', function(event) {
        event.preventDefault();

        if(confirmSubmit("저장")) {
            const formData = new FormData();
            var conditionCode = $('#conditionCodeSub').text();
            var conditionName = $('#conditionNameSub').text();
            var applyDtString = $('#applyDtString').val();
            var version = $('#version').val();
            var conditionFile = $('#conditionFile')[0].files[0];
            var memo = $('#memo').val();

            // 필수 값 체크
            if (!conditionFile || !applyDtString || !version) {
                alert("필수 항목을 입력해주세요.");
                return;
            }
            
            formData.append('conditionCode', conditionCode);
            formData.append('conditionName', conditionName);
            formData.append('applyDtString', applyDtString);
            formData.append('version', version);
            formData.append('file', conditionFile);
            formData.append('memo', memo);

            $.ajax({
                url: '/system/condition/hist/new',
                type: 'POST',
                data: formData,
                enctype: 'multipart/form-data',
                contentType: false,
                processData: false,
                cache: false,
                success: function(response) {
                    alert(response);
                    $('#conHistAddModal').modal('hide');
                    renderConditionHist();
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    });
});