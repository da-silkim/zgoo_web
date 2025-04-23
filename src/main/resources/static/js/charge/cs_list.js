$(document).ready(function() {
    let modalCon = false;
    let isDuplicateCheck = false;
    let selectRow, btnMsg = "등록";
    var stationId;

    $('#size').on('change', function() {
        updatePageSize(this, "/station/list", ["companyIdSearch", "opSearch", "contentSearch"]);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        stationId = selectRow.find('td').eq(0).attr('id');
    });

    $('#modalClose').on('click', function() {
        $(".attr-control").removeAttr("disabled");
        window.location.reload();
    });

    $('#openingAllTime').change(function() {
        if ($(this).prop('checked')) {
            $('#openStartTime').val('00:00').prop('disabled', true);
            $('#openEndTime').val('23:59').prop('disabled', true);
        } else {
            $('#openStartTime').prop('disabled', false);
            $('#openEndTime').prop('disabled', false);
        }
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#csForm')[0].reset();
        $('#modalBtn').show();
        $('#modalCancel').show();
        $("#modalClose").attr("hidden", true);
        $('#modalBtn').text(btnMsg);
        isDuplicateCheck = false;

        $('#openStartTime').prop('disabled', false);
        $('#openEndTime').prop('disabled', false);
        $('#openingAllTime').prop('checked', false);
        $('#opStatus').val('OPERATING');
        $('#companyId').prop('disabled', false);
        $('#stationName').prop('disabled', false);
        $('#duplicateBtn').prop('disabled', false);
        updateUnit();
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        isDuplicateCheck = true;
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').show();
        $('#modalCancel').show();
        $("#modalClose").attr("hidden", true);
        $('#modalBtn').text(btnMsg);
        $('#companyId').prop('disabled', true);
        $('#stationName').prop('disabled', true);
        $('#duplicateBtn').prop('disabled', true);

        $.ajax({
            type: 'GET',
            url: `/station/list/get/${stationId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#companyId').val(data.companyId || '');
                $('#stationName').val(data.stationName || '');
                $('#stationId').val(data.stationId || '');
                $('#stationType').val(data.stationType || '');
                $('#facilityType').val(data.facilityType || '');
                $('#asNum').val(data.asNum || '');
                $('#opStatus').val(data.opStatus || '');
                $('#zipCode').val(data.zipcode || '');
                $('#address').val(data.address || '');
                $('#addressDetail').val(data.addressDetail || '');
                $('#latitude').val(data.latitude || '');
                $('#longitude').val(data.longitude || '');
                $('#openStartTime').val(data.openStartTime || '');
                $('#openEndTime').val(data.openEndTime || '');

                console.log("openStartTime: " + data.openStartTime);
                if (data.openStartTime === '00:00:00' && data.openEndTime === '23:59:00') {
                    $('#openingAllTime').prop('checked', true);
                    $('#openStartTime').prop('disabled', true);
                    $('#openEndTime').prop('disabled', true);
                } else {
                    $('#openingAllTime').prop('checked', false);
                    $('#openStartTime').prop('disabled', false);
                    $('#openingAllTime').prop('checked', false);
                }

                
                if (data.parkingFeeYn === 'Y') {
                    $('#parkingFeeYes').prop('checked', true);
                } else {
                    $('#parkingFeeNo').prop('checked', true);
                }

                if (data.landUseType === 'FIX') {
                    $('#FIX').prop('checked', true);
                } else {
                    $('#RATE').prop('checked', true);
                }
                updateUnit();

                $('#institutionName').val(data.institutionName || '');
                $('#landType').val(data.landType || '');
                $('#staffName').val(data.staffName || '');
                $('#staffPhone').val(data.staffPhone || '');
                $('#contractDate').val(data.contractDate || '');
                $('#startDate').val(data.startDate || '');
                $('#endDate').val(data.endDate || '');
                $('#landUseFee').val(data.landUseFee || '');
                $('#kepcoCustNo').val(data.kepcoCustNo || '');
                $('#openingDate').val(data.openingDate || '');
                $('#contPower').val(data.contPower || '');
                $('#rcvCapacityMethod').val(data.rcvCapacityMethod || '');
                $('#rcvCapacity').val(data.rcvCapacity || '');
                $('#voltageType').val(data.voltageType || '');
                $('#sido').val(data.sido || '');
                $('#settlementDate').val(data.settlementDate || '');
                $('#safetyManagementFee').val(data.safetyManagementFee || '');
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {

            $.ajax({
                type: 'DELETE',
                url: `/station/list/delete/${stationId}`,
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    var response = JSON.parse(xhr.responseText);
                    alert(response);
                }
            });
        }
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if(!isDuplicateCheck) {
            alert('충전소 이름 중복체크를 해주세요.');
            return;
        }

        if (confirmSubmit(btnMsg)) {
            const DATA = {
                companyId: $('#companyId').val(),
                stationName: $('#stationName').val(),
                stationId: $('#stationId').val(),
                stationType: $('#stationType').val(),
                facilityType: $('#facilityType').val(),
                asNum: $('#asNum').val(),
                opStatus: $('#opStatus').val(),
                zipcode: $('#zipCode').val(),
                address: $('#address').val(),
                addressDetail: $('#addressDetail').val(),
                latitude: $('#latitude').val(),
                longitude: $('#longitude').val(),
                openStartTime: $('#openStartTime').val(),
                openEndTime: $('#openEndTime').val(),
                parkingFeeYn: $('input[name="parkingFeeYn"]:checked').val(),
                institutionName: $('#institutionName').val(),
                landType: $('#landType').val(),
                staffName: $('#staffName').val(),
                staffPhone: $('#staffPhone').val(),
                contractDate: $('#contractDate').val(),
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val(),
                landUseType: $('input[name="landUseType"]:checked').val(),
                landUseFee: $('#landUseFee').val(),
                kepcoCustNo: $('#kepcoCustNo').val(),
                openingDate: $('#openingDate').val(),
                contPower: $('#contPower').val(),
                rcvCapacityMethod: $('#rcvCapacityMethod').val(),
                rcvCapacity: $('#rcvCapacity').val(),
                voltageType: $('#voltageType').val(),
                sido: $('#sido').val(),
                settlementDate: $('#settlementDate').val(),
                safetyManagementFee: $('#safetyManagementFee').val()
            }

            const URL = modalCon ? `/station/list/update` : `/station/list/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
                success: function(response) {
                    if (!modalCon) {
                        if (response.stationId) {
                            // 등록성공
                            $('#stationId').val(response.stationId || '');
                            $('#modalBtn').hide();
                            $('#modalCancel').hide();
                            $("#modalClose").attr("hidden", false);
                            $(".attr-control").attr("disabled", true);
                        }
                    }
                    alert(response.message);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    var response = JSON.parse(xhr.responseText);
                    alert(response.message);
                }
            });
        }
    });

    $('#stationName').change(function() {
        isDuplicateCheck = false;
    });

    $('#duplicateBtn').on('click', function() {
        var stationName = $('#stationName').val();
        if(stationName.trim() === '') {
            alert('충전소 이름을 입력해주세요');
            return;
        }

        $.ajax({
            url: '/station/list/checkStationName',
            type: 'GET',
            data: {stationName: stationName},
            success: function(isDuplicate) {
                if (isDuplicate) {
                    alert('이미 사용 중인 충전소 이름입니다.');
                    isDuplicateCheck = false;
                } else {
                    alert('사용 가능한 충전소 이름입니다.');
                    isDuplicateCheck = true;
                }
            },
            error: function() {
                alert('충전소 이름 중복확인 중 오류가 발생으로 다시 시도해주세요');
            }
        });
    });

    // 토지사용구분 제어
    function updateUnit() {
        const selectedValue = document.querySelector('input[name="landUseType"]:checked').value;
        const unitElement = document.getElementById('unit');
        const landUseFeeInput = document.getElementById('landUseFee');
        landUseFeeInput.value = '';

        if (selectedValue === 'FIX') {
            unitElement.innerText = '원';
        } else if (selectedValue === 'RATE') {
            unitElement.innerText = '%';
        }
    }

    document.getElementById('FIX').addEventListener('click', updateUnit);
    document.getElementById('RATE').addEventListener('click', updateUnit);
});