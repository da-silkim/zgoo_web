$(document).ready(function() {
    let modalCon = false, selectRow, btnMsg = "등록", chargerIdValid = false;
    var cpmaintainId, chargerId;

    $('#size').on('change', function() {
        updatePageSize(this, "/maintenance/errlist", ["companyIdSearch", "opSearch", "contentSearch",
            "processStatusSearch", "startDateSearch", "endDateSearch"]);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this);
        cpmaintainId = selectRow.find('td').eq(0).attr('id');
        buttonControl($(this), `/errlist/btncontrol/${cpmaintainId}`);
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
            error: function(xhr, status, error) {
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
    });

    $(document).on('click', '#editBtn', function(event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $('#modalBtn').show();
        $('#maintainForm')[0].reset();
        $('#chargerId').prop('disabled', true);
        $('#chargerIdSearchBtn').prop('disabled', true);
        $('#processContent').prop('disabled', true);
        removeImage('pictureLoc1');
        removeImage('pictureLoc2');
        removeImage('pictureLoc3');

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

                ['1', '2', '3'].forEach(function(i) {
                    const pic = data.cpMaintain['pictureLoc' + i];
                    if (pic) {
                        $('#pictureLoc' + i).attr('src', pic);
                        $('#existing-pictureLoc' + i).val(pic);
                        editLoadImage('pictureLoc' + i, data.cpMaintain.processStatus);
                    }
                });

                if (data.cpMaintain.processStatus === 'FSTATFINISH') {
                    $('#processStatus').prop('disabled', true);
                    $('#modalBtn').hide();
                    $('#errorType').prop('disabled', true);
                    $('#errorContent').prop('disabled', true);
                } else {
                    $('#processStatus').prop('disabled', false);
                }
            }
        });
    });

    function editLoadImage(imageId, processStatus) {
        document.getElementById(imageId).style.display = 'block';
        document.getElementById('label-' + imageId).style.display = 'none';

        if (processStatus !== 'FSTATFINISH') {
            document.getElementById('remove-' + imageId).style.display = 'block';
        }
    }

    $(document).on('click', '#deleteBtn', function() {
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
            formData.append('chargerId', $('#chargerId').val());
            formData.append('errorType', $('#errorType').val());
            formData.append('errorContent',  $('#errorContent').val());
            formData.append('processStatus', $('#processStatus').val());
            formData.append('processContent', $('#processContent').val());
            formData.append('existingPictureLoc1', $('#existing-pictureLoc1').val());
            formData.append('existingPictureLoc2', $('#existing-pictureLoc2').val());
            formData.append('existingPictureLoc3', $('#existing-pictureLoc3').val());

            var fileLoc1 = $('#input-pictureLoc1')[0].files[0];
            var fileLoc2 = $('#input-pictureLoc2')[0].files[0];
            var fileLoc3 = $('#input-pictureLoc3')[0].files[0];
            
            if (fileLoc1) {
                formData.append('fileLoc1', fileLoc1);
            }
            if (fileLoc2) {
                formData.append('fileLoc2', fileLoc2);
            }
            if (fileLoc3) {
                formData.append('fileLoc3', fileLoc3);
            }

            const URL = modalCon ? `/errlist/update/${cpmaintainId}` : `/errlist/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                method: TYPE,
                data: formData,
                enctype: 'multipart/form-data',
                contentType: false,
                processData: false,
                cache: false,
                success: function(response) {
                    alert(response);
                    $('#dataAddModal').modal('hide');
                    window.location.reload();
                },
                error: function(error) {
                    alert(error);
                }
            });
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

    document.getElementById('input-' + imageId).value = "";
    document.getElementById('remove-' + imageId).style.display = 'none';
    document.getElementById('label-' + imageId).style.display = 'block';
    document.getElementById('existing-' + imageId).value = "";
}