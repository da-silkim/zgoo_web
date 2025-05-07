$(document).ready(function() {
    let modalCon = false, elecCon = false;
    let selectRow, btnMsg = "등록";
    var id, accountCode;
    const elecContainer = document.getElementById('elecContainer');
    const elecContainerSec = document.getElementById('elecContainerSec');
    const totalContainer = document.getElementById('totalContainer');
    const totalContainerSec = document.getElementById('totalContainerSec');
    const supplyPrice = document.getElementById('supplyPrice');
    const loadBtn = document.getElementById('loadBtn');
    const vat = document.getElementById('vat');
    const totalAmount = document.getElementById('totalAmount');
    const totalAmountSub = document.getElementById('totalAmountSub');
    const charge = document.getElementById('charge');
    const surcharge = document.getElementById('surcharge');
    const cutoffAmount = document.getElementById('cutoffAmount');
    const unpaidAmount = document.getElementById('unpaidAmount');
    const power = document.getElementById('power');
    const setValue = (element, value) => element.value = value;
    const setHidden = (element, hidden) => element.hidden = hidden;

    $('#size').on('change', function() {
        updatePageSize(this, "/calc/purchase", ["opSearch", "contentSearch", "startDate", "endDate"]);
    });

    $('#accountCode').on('change', function() {
        accountCode = $('#accountCode').val();
        
        switch (accountCode) {
            case 'ELCFEE':
                loadBtn.hidden = true;
                elecContainer.hidden = false;
                elecContainerSec.hidden = false;
                totalContainer.hidden = true;
                totalContainerSec.hidden = false;
                elecCon = true;
                console.log("전기료");
                break;
            case 'LANDFEE':
            case 'SMFEE':
                loadBtn.hidden = false;
                elecContainer.hidden = true;
                elecContainerSec.hidden = true;
                totalContainer.hidden = false;
                totalContainerSec.hidden = true;
                elecCon = false;
                break;
            default:
                loadBtn.hidden = true;
                elecContainer.hidden = true;
                elecContainerSec.hidden = true;
                totalContainer.hidden = false;
                totalContainerSec.hidden = true;
                elecCon = false;
                break;
        }
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        id = selectRow.find('td').eq(0).attr('id');
    });

    $('#elecBtn').on('click', function(event) {
        event.preventDefault();
        $('#elecForm')[0].reset();
        $('#elecSaveBtn').prop('disabled', true);
        document.getElementById('elecList').innerHTML = '<tr><td colspan="10">업로드된 데이터가 없습니다.</td></tr>';
    });

    $('#elecSaveBtn').on('click', function(event) {
        event.preventDefault();
        if (confirmSubmit("저장")) {
            const DATA = {
                electricity: getElecList()
            };

            $.ajax({
                url: `/purchase/new/elec`,
                type: 'POST',
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

    function getElecList() {
        const elecInfoList = [];
        const tbody = document.getElementById('elecList');
        const rows = tbody.querySelectorAll('tr');

        rows.forEach(tr => {
            const tds = tr.querySelectorAll('td');
            const kepcoCustNo = tds[0]?.textContent.trim() || "";
            const powerInfo = tds[1]?.textContent.trim() || "";
            const supplyPriceInfo = tds[2]?.textContent.trim() || "";
            const vatInfo = tds[3]?.textContent.trim() || "";
            const chargeInfo = tds[4]?.textContent.trim() || "";
            const surchargeInfo = tds[5]?.textContent.trim() || "";
            const cutoffInfo = tds[6]?.textContent.trim() || "";
            const unpaidInfo = tds[7]?.textContent.trim() || "";
            const totalInfo = tds[8]?.textContent.trim() || "";
            const paymentInfo = tds[9]?.textContent.trim() || "";

            if (!kepcoCustNo && !powerInfo && !supplyPriceInfo && !vatInfo && !chargeInfo &&
                !surchargeInfo && !cutoffInfo && !unpaidInfo && !totalInfo && !paymentInfo) {
                return;
            }

            elecInfoList.push({
                kepcoCustNo: kepcoCustNo,
                power: powerInfo,
                supplyPrice: supplyPriceInfo,
                vat: vatInfo,
                charge: chargeInfo,
                surcharge: surchargeInfo,
                cutoffAmount: cutoffInfo,
                unpaidAmount: unpaidInfo,
                totalAmount: totalInfo,
                paymentMethod: paymentInfo
            });
        });
        // console.log(elecInfoList);
        return elecInfoList;
    }

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
        elecContainerSec.hidden = true;
        totalContainer.hidden = false;
        totalContainerSec.hidden = true;
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

                const isElec = data.accountCode === 'ELCFEE';
                setHidden(elecContainer, !isElec);
                setHidden(elecContainerSec, !isElec);
                setHidden(totalContainer, isElec);
                setHidden(totalContainerSec, !isElec);
                setValue(totalAmount, isElec ? 0 : data.totalAmount);
                setValue(totalAmountSub, isElec ? data.totalAmount : 0);
                setValue(charge, isElec ? data.charge : 0);
                setValue(surcharge, isElec ? data.surcharge : 0);
                setValue(cutoffAmount, isElec ? data.cutoffAmount : 0);
                setValue(unpaidAmount, isElec ? data.unpaidAmount : 0);
                setValue(power, isElec ? data.power : 0);
                elecCon = isElec;

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
            surcharge: elecCon ? surcharge.value : null,
            cutoffAmount: elecCon ? cutoffAmount.value : null,
            unpaidAmount: elecCon ? unpaidAmount.value : null,
            power: elecCon ? power.value : null,
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
        if (elecCon) return;
        const supply = parseInt(supplyPrice.value, 10) || 0;
        const vatVal = parseInt(vat.value, 10) || 0;
        totalAmount.value = supply + vatVal || 0;
    }

    supplyPrice.addEventListener('input', function() {
        vat.value = Math.round(supplyPrice.value * 0.1);
        updateTotalAmount();
    });

    vat.addEventListener('input', updateTotalAmount);

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