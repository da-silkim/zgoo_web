$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록", chargerIdValid = false;
    var cpmaintainId, chargerId;

    $('#resetBtn').on('click', function() {
        window.location.replace('/maintenance/errlist');
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
        const selectedProcessStatus = urlParams.get('processStatusSearch') || '';
        const selectedStartDate = urlParams.get('startDateSearch')|| '';
        const selectedEndDate = urlParams.get('endDateSearch') || '';

        window.location.href = "/maintenance/errlist?page=0&size=" + selectedSize +
                               "&companyIdSearch=" + (selectedCompanyId) +
                               "&opSearch=" + (selectedOpSearch) +
                               "&contentSearch=" + (selectedContentSearch) +
                               "&processStatusSearch=" + (selectedProcessStatus) +
                               "&startDateSearch=" + (selectedStartDate) +
                               "&endDateSearch=" + (selectedEndDate);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        cpmaintainId = selectRow.find('td').eq(0).attr('id');
    });

    $('#chargerIdSearchBtn').on('click', function() {
        chargerId = $('#chargerId').val();

        $.ajax({
            type: 'GET',
            url: `/errlist/search/${chargerId}`,
            success: function(data) {
                $('#companyName').val(data.cpInfo.companyName || '');
                $('#stationId').val(data.cpInfo.stationId || '');
                $('#stationName').val(data.cpInfo.stationName || '');
                $('#address').val(data.cpInfo.address || '');

                if (data.message) {
                    chargerIdValid = false;
                    alert(data.message);
                } else {
                    chargerIdValid = true;
                }
                
            },
            error: function(error) {
                alert(error.message);
            }
        });
    });

    $('#chargerId').on('input', function() {
        // console.log('Input detected: ' + this.value);
        chargerIdValid = false;
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        $('#maintainForm')[0].reset();
        $('#processStatus').val('FSTATREADY');
    });
});