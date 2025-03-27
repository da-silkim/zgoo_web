$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록", chargerIdValid = false;
    var cpmaintainId, chargerId;

    var uploadImage = document.getElementById('uploadImage');
    var showImage = document.getElementById('showImage');

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
        chargerIdValid = false;
    });

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#modalBtn').show();

        $('#maintainForm')[0].reset();
        $('#processStatus').val('FSTATREADY');
        $('#chargerId').prop('disabled', false);
        $('#chargerIdSearchBtn').prop('disabled', false);
        $('#errorType').prop('disabled', false);
        $('#errorContent').prop('disabled', false);
        $('#processStatus').prop('disabled', false);
        $('#processContent').prop('disabled', true);
        removeImage('pictureLoc1');
        removeImage('pictureLoc2');
        removeImage('pictureLoc3');
        uploadImage.hidden = false;
        showImage.hidden = true;
    });

    $('#editBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $('#modalBtn').show();

        $('#maintainForm')[0].reset();
        $('#chargerId').prop('disabled', true);
        $('#chargerIdSearchBtn').prop('disabled', true);
        $('#errorType').prop('disabled', true);
        $('#errorContent').prop('disabled', true);
        $('#processContent').prop('disabled', true);
        uploadImage.hidden = true;
        showImage.hidden = false;
        showPicture(1);
        showPicture(2);
        showPicture(3);

        $.ajax({
            type: 'GET',
            url: `/errlist/get/${cpmaintainId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#companyName').val(data.cpInfo.companyName || '');
                $('#stationId').val(data.cpInfo.stationId || '');
                $('#stationName').val(data.cpInfo.stationName || '');
                $('#address').val(data.cpInfo.address || '');

                $('#chargerId').val(data.cpMaintain.chargerId || '');
                $('#errorType').val(data.cpMaintain.errorType || '');
                $('#errorContent').val(data.cpMaintain.errorContent || '');
                $('#processStatus').val(data.cpMaintain.processStatus || '');
                $('#processContent').val(data.cpMaintain.processContent || '');

                if (data.cpMaintain.processStatus === 'FSTATFINISH') {
                    $('#processStatus').prop('disabled', true);
                    $('#modalBtn').hide();
                } else {
                    $('#processStatus').prop('disabled', false);
                }

                const pic1 = data.cpMaintain.pictureLoc1;
                const pic2 = data.cpMaintain.pictureLoc2;
                const pic3 = data.cpMaintain.pictureLoc3;

                if (!pic1 || pic1 === '') {
                    document.getElementById('showPictureLoc1').style.display = 'none';
                    document.getElementById('showPicture1').style.border = 'none';
                } else {
                    $('#showPictureLoc1').attr('src', data.cpMaintain.pictureLoc1 || '');
                }
                
                if (!pic2 || pic2 === '') {
                    document.getElementById('showPictureLoc2').style.display = 'none';
                    document.getElementById('showPicture2').style.border = 'none';
                } else {
                    $('#showPictureLoc2').attr('src', data.cpMaintain.pictureLoc2 || '');
                }
                
                if (!pic3 || pic3 === '') {
                    document.getElementById('showPictureLoc3').style.display = 'none';
                    document.getElementById('showPicture3').style.border = 'none';
                } else {
                    $('#showPictureLoc3').attr('src', data.cpMaintain.pictureLoc3 || '');
                }
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        if(confirmSubmit("삭제")) {
            $.ajax({
                type: 'DELETE',
                url: `/errlist/delete/${cpmaintainId}`,
                contentType: "application/json",
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

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if (!modalCon) {
            if(!chargerIdValid) {
                alert('충전소ID 정보를 확인해 주세요.');
                return;
            }
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

            // var fileLoc1 = $('#showPictureLoc1')[0].files[0];
            // var fileLoc2 = $('#showPictureLoc2')[0].files[0];
            // var fileLoc3 = $('#showPictureLoc3')[0].files[0];
            // if (fileLoc1) {
            //     formData.append('fileLoc1', fileLoc1);
            // }
            // if (fileLoc2) {
            //     formData.append('fileLoc2', fileLoc2);
            // }
            // if (fileLoc3) {
            //     formData.append('fileLoc3', fileLoc3);
            // }

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
                // $.ajax({
                //     url: `/errlist/update/${cpmaintainId}`,
                //     method: 'PATCH',
                //     data: formData,
                //     enctype: 'multipart/form-data',
                //     contentType: false,
                //     processData: false,
                //     cache: false,
                //     success: function(response) {
                //         alert(response);
                //         $('#dataAddModal').modal('hide');
                //     },
                //     error: function(error) {
                //         alert(error);
                //     }
                // });
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

function showPicture(imageId) {
    document.getElementById('showPictureLoc' + imageId).style.display = 'block';
    document.getElementById('showPicture' + imageId).style.border = '1px #2e2e2e solid';
}