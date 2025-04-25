$(document).ready(function() {
    let modalCon = false, elecCon = false;
    let selectRow, btnMsg = "등록";
    var id, accountCode;
    const elecContainer = document.getElementById('elecContainer');
    const totalContainer = document.getElementById('totalContainer');
    const supplyPrice = document.getElementById('supplyPrice');
    const loadBtn = document.getElementById('loadBtn');
    const vat = document.getElementById('vat');
    const totalAmount = document.getElementById('totalAmount');
    const totalAmountSub = document.getElementById('totalAmountSub');
    const charge = document.getElementById('charge');

    $('#size').on('change', function() {
        updatePageSize(this, "/calc/purchase", ["opSearch", "contentSearch", "startDate", "endDate"]);
    });

    $('#accountCode').on('change', function() {
        accountCode = $('#accountCode').val();
        
        switch (accountCode) {
            case 'ELCFEE':
                loadBtn.hidden = true;
                elecContainer.hidden = false;
                totalContainer.hidden = true;
                elecCon = true;
                console.log("전기료");
                break;
            case 'LANDFEE':
            case 'SMFEE':
                loadBtn.hidden = false;
                elecContainer.hidden = true;
                totalContainer.hidden = false;
                elecCon = false;
                break;
            default:
                loadBtn.hidden = true;
                elecContainer.hidden = true;
                totalContainer.hidden = false;
                elecCon = false;
                break;
        }
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        id = selectRow.find('td').eq(0).attr('id');
        console.log("id: ", id);
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#purchaseForm')[0].reset();
        $('#accountCode').prop('disabled', false);
        $('#stationSearchBtn').prop('disabled', false);
        $('#stationOpSearch').val('');
        loadBtn.hidden = true;
        elecContainer.hidden = true;
        totalContainer.hidden = false;
        document.getElementById('stationSearchList').innerHTML = '<tr><td colspan="3">조회된 데이터가 없습니다.</td></tr>';
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $('#purchaseForm')[0].reset();
        $('#accountCode').prop('disabled', true);
        $('#stationSearchBtn').prop('disabled', true);
        $('#stationOpSearch').val('');
        loadBtn.hidden = true;
        document.getElementById('stationSearchList').innerHTML = '<tr><td colspan="3">조회된 데이터가 없습니다.</td></tr>';

        $.ajax({
            url: `/purchase/get/${id}`,
            type: 'GET',
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#accountCode').val(data.accountCode);

                if (data.accountCode === 'ELCFEE') {
                    elecContainer.hidden = false;
                    totalContainer.hidden = true;
                    totalAmount.value = 0;
                    totalAmountSub.value = data.totalAmount;
                    charge.value = data.charge;
                    elecCon = true;
                } else {
                    elecContainer.hidden = true;
                    totalContainer.hidden = false;
                    totalAmount.value = data.totalAmount;
                    totalAmountSub.value = 0;
                    charge.value = 0;
                    elecCon = false;
                }

                $('#item').val(data.item);
                $('#bizName').val(data.bizName);
                $('#bizNum').val(data.bizNum);
                $('#stationId').val(data.stationId);
                $('#purchaseDate').val(data.purchaseDate);
                $('#paymentMethod').val(data.paymentMethod);
                $('#approvalNo').val(data.approvalNo);
                $('#unitPrice').val(data.unitPrice);
                $('#amount').val(data.amount);
                $('#supplyPrice').val(data.supplyPrice);
                $('#vat').val(data.vat);
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        if (confirmSubmit("삭제")) {
            $.ajax({
                url: `/purchase/delete/${id}`,
                type: 'PATCH',
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

        var form = document.getElementById('purchaseForm');
        if (!form.checkValidity()) { 
            alert('필수 입력 값이 누락되어 있습니다.');
            return;
        }

        if ($('#stationId').val().trim() === '') {
            alert("충전소ID를 조회해 주세요.");
            return;
        }

        // 계정과목:기타
        if ($('#accountCode').val() === 'EFEE' && $('#item').val().trim() === '') {
            alert("품폭명을 입력해 주세요.");
            return;
        }

        // 카드결제: 승인번호 확인
        if ($('#paymentMethod').val() === 'PM01' && $('#approvalNo').val().trim() === '') {
            alert("승인번호를 입력해 주세요.");
            return;
        }

        const DATA = {
            accountCode: $('#accountCode').val(),
            item: $('#item').val(),
            bizName: $('#bizName').val(),
            bizNum: $('#bizNum').val(),
            stationId: $('#stationId').val(),
            purchaseDate: $('#purchaseDate').val(),
            paymentMethod: $('#paymentMethod').val(),
            approvalNo: $('#approvalNo').val(),
            unitPrice: $('#unitPrice').val(),
            amount: $('#amount').val(),
            supplyPrice: $('#supplyPrice').val(),
            vat: $('#vat').val(),
            charge: elecCon ? charge.value : null,
            totalAmount: elecCon ? totalAmountSub.value : totalAmount.value
        };

        if (confirmSubmit(btnMsg)) {
            const URL = modalCon ? `/purchase/update/${id}` : `/purchase/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
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

    $('#stationOpSearchBtn').on('click', function() {
        const searchStation = document.getElementById('searchStation').value;
        const stationOpSearch = document.getElementById('stationOpSearch').value;
        $.ajax({
            url: `/purchase/search/station`,
            method: 'GET',
            data: {searchOp: searchStation, searchContent: stationOpSearch},
            success: function(response) {
                console.log(response.csList);

                var stationSearchList = document.getElementById('stationSearchList');
                stationSearchList.innerHTML = '';
                if (response.csList.length === 0) {
                    stationSearchList.innerHTML = '<tr><td colspan="3">조회된 데이터가 없습니다.</td></tr>';
                    return;
                }

                response.csList.forEach(function (cs) {
                    var row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${cs.stationName}</td>
                        <td>${cs.stationId}</td>
                        <td><button type="button" class="btn btn-outline-grey select-station-btn" data-station-id="${cs.stationId}" data-station-name="${cs.stationName}">선택</button></td>
                    `;
                    stationSearchList.appendChild(row);
                });
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });

    document.getElementById('stationSearchList').addEventListener('click', function(e) {
        if (e.target.classList.contains('select-station-btn')) {
            var stationId = e.target.dataset.stationId;
            document.getElementById('stationId').value = stationId;
            bootstrap.Modal.getInstance(document.getElementById('stationSearchModal')).hide();
        }
    });

    document.getElementById('stationSearchBtn').addEventListener('click', function () {
        var stationSearchModal = new bootstrap.Modal(document.getElementById('stationSearchModal'), {
            backdrop: 'static',
            keyboard: false
        });
        stationSearchModal.show();
    });

    function updateTotalAmount() {
        const supply = parseInt(supplyPrice.value, 10) || 0;
        const vatVal = parseInt(vat.value, 10) || 0;
        const chargeVal = parseInt(charge.value, 10) || 0;

        totalAmount.value = supply + vatVal || 0;
        totalAmountSub.value = supply + vatVal + chargeVal || 0;
    }

    supplyPrice.addEventListener('input', function() {
        vat.value = Math.round(supplyPrice.value * 0.1);
        updateTotalAmount();
    });

    vat.addEventListener('input', updateTotalAmount);
    charge.addEventListener('input', updateTotalAmount);

    $('#loadBtn').on('click', function() {
        if ($('#stationId').val().trim() === '') {
            alert("충전소ID를 조회해 주세요.");
            return;
        }
        
        $.ajax({
            url: `/purchase/search/account`,
            type: 'GET',
            data: {accountCode: accountCode, stationId: $('#stationId').val()},
            success: function(data) {
                $('#unitPrice').val(data.unitPrice);
                $('#supplyPrice').val(data.supplyPrice);
                $('#vat').val(data.vat);
                $('#totalAmount').val(data.totalAmount);
            },
            error: function(xhr, status, error) {
            }
        });
    });
});