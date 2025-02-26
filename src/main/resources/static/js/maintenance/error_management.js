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

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if(!chargerIdValid) {
            alert('충전소ID 정보를 확인해 주세요.');
            return;
        }

        const processStatus = document.getElementById('processStatus').value;
        const processContent = document.getElementById('processContent').value;
        if ((!processContent || processContent.trim() === '') && processStatus === 'FSTATFINISH') {
            alert('처리내용을 작성해 주세요.');
            return;
        }

        if (confirmSubmit(btnMsg)) {
            const formData = new FormData();
            var chargerId =  $('#chargerId').val();
            var errorType = $('#errorType').val();
            var errorContent = $('#errorContent').val();
            var pStatus = $('#processStatus').val();
            var pContent = $('#processContent').val();
            var fileLoc1 = $('#input-pictureLoc1')[0].files[0];
            var fileLoc2 = $('#input-pictureLoc2')[0].files[0];
            var fileLoc3 = $('#input-pictureLoc3')[0].files[0];

            formData.append('chargerId', chargerId);
            formData.append('errorType', errorType);
            formData.append('errorContent', errorContent);
            formData.append('processStatus', pStatus);
            formData.append('processContent', pContent);

            if (fileLoc1) {
                formData.append('fileLoc1', fileLoc1);
            }
            if (fileLoc2) {
                formData.append('fileLoc2', fileLoc2);
            }
            if (fileLoc3) {
                formData.append('fileLoc3', fileLoc3);
            }

            const DATA = {
                chargerId: $('#chargerId').val(),
                errorType: $('#errorType').val(),
                errorContent: $('#errorContent').val(),
                processStatus: $('#processStatus').val(),
                processContent: $('#processContent').val()
            }

            const URL = modalCon ? `/errlist/update/${cpmaintainId}` : `/errlist/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            // 수정
            if (modalCon) {
                $.ajax({
                    url: `/errlist/update/${cpmaintainId}`,
                    method: 'PATCH',
                    contentType: 'application/json',
                    data: JSON.stringify(DATA),
                    success: function(response) {
                        alert(response);
                    },
                    error: function(error) {
                        alert(error);
                    }
                });
            } else {
                $.ajax({
                    url: `/errlist/new`,
                    type: 'POST',
                    data: formData,
                    enctype: 'multipart/form-data',
                    contentType: false,
                    processData: false,
                    cache: false,
                    success: function(response) {
                        alert(response);
                        $('#dataAddModal').modal('hide');
                    },
                    error: function(error) {
                        alert(error);
                    }
                });
            }

        }
    });

    $('#processStatus').on('change', function() {
        const processStatus = document.getElementById('processStatus').value;
        const processContent = document.getElementById('processContent');

        if (processStatus === 'FSTATREADY') {
            processContent.setAttribute('disabled', 'disabled');
            processContent.value = '';
        } else {
            processContent.removeAttribute('disabled');
        }
    });
});

function loadFile(input, imageId) {
    const file = input.files[0];
    const img = document.getElementById(imageId);

    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            img.src = e.target.result;
            img.style.display = 'block';

            const removeBtn = document.getElementById('remove-' + imageId);
            removeBtn.style.display = 'block';

            const label = document.getElementById('label-' + imageId);
            label.style.display = 'none';
        };

        reader.readAsDataURL(file);
    }
}

function removeImage(imageId) {
    const img = document.getElementById(imageId);
    img.src = "";
    img.style.display = 'none';
    img.previousElementSibling.value = "";

    const inputFile = document.getElementById('input-' + imageId);
    inputFile.value = "";

    const removeBtn = document.getElementById('remove-' + imageId);
    removeBtn.style.display = 'none';

    const label = document.getElementById('label-' + imageId);
    label.style.display = 'block';
}