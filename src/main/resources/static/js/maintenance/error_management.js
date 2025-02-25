$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록";
    var cpmaintainId;

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
});