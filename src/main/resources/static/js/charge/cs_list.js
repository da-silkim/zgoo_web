$(document).ready(function() {
    let modalCon = false;
    let isDuplicateCheck = false;
    let selectRow, btnMsg = "등록";
    var stationId;

    $('#resetBtn').on('click', function() {
        window.location.replace('/station/list');
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
        const selectedCompanyId = urlParams.get('companyIdSearch') || '';
        const selectedOpSearch = urlParams.get('opSearch') || '';
        const selectedContentSearch = urlParams.get('contentSearch') || '';

        window.location.href = "/station/list?page=0&size=" + selectedSize +
                               "&companyIdSearch=" + (selectedCompanyId) +
                               "&opSearch=" + (selectedOpSearch) +
                               "&contentSearch=" + (selectedContentSearch);
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

                $('#institutionName').val(data.institutionName || '');
                $('#landType').val(data.landType || '');
                $('#staffName').val(data.staffName || '');
                $('#staffPhone').val(data.staffPhone || '');
                $('#contractDate').val(data.contractDate || '');
                $('#startDate').val(data.startDate || '');
                $('#endDate').val(data.endDate || '');
                $('#landUseRate').val(data.landUseRate || '');
                $('#billDate').val(data.billDate || '');
                $('#kepcoCustNo').val(data.kepcoCustNo || '');
                $('#openingDate').val(data.openingDate || '');
                $('#contPower').val(data.contPower || '');
                $('#rcvCapacityMethod').val(data.rcvCapacityMethod || '');
                $('#rcvCapacity').val(data.rcvCapacity || '');
                $('#voltageType').val(data.voltageType || '');
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
                    alert('충전소가 정상적으로 삭제되었습니다.');
                    window.location.reload();
                },
                error: function(error) {
                    console.log("충전소 삭제 실패: ", error);
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
                landUseRate: $('#landUseRate').val(),
                billDate: $('#billDate').val(),
                kepcoCustNo: $('#kepcoCustNo').val(),
                openingDate: $('#openingDate').val(),
                contPower: $('#contPower').val(),
                rcvCapacityMethod: $('#rcvCapacityMethod').val(),
                rcvCapacity: $('#rcvCapacity').val(),
                voltageType: $('#voltageType').val()
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
                        alert(response.message);
                    } else {
                        window.location.reload();
                    }
                },
                error: function(xhr, status, error) {

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
});