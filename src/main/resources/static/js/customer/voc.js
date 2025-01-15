$(document).ready(function() {
    let selectRow;
    var vocId;

    $(document).on('dblclick', '#vocTable tbody tr', function() {
        selectRow = $(this);
        vocId = selectRow.find('td').eq(0).attr('id');
        
        const modal = new bootstrap.Modal(document.getElementById('dataAddModal'));
        modal.show();

        $.ajax({
            type: 'GET',
            url: `/voc/get/${vocId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                if (data.vocInfo && Object.keys(data.vocInfo).length !== 0) {
                    $('#vocId').val(data.vocInfo.vocId || '');
                    $('#channelName').val(data.vocInfo.channelName || '');
                    $('#typeName').val(data.vocInfo.typeName || '');
                    $('#phoneNo').val(data.vocInfo.phoneNo || '');
                    $('#regDt').val(formatDate(new Date(data.vocInfo.regDt)) || '');
                    $('#name').val(data.vocInfo.name || '');
                    $('#title').val(data.vocInfo.title || '');
                    $('#content').val(data.vocInfo.content || '');
                    $('#replyContent').val(data.vocInfo.replyContent || '');
                } else {
                    alert(data.message);
                }
            }
        });
    });

    $('#resetBtn').on('click', function() {
        window.location.replace('/voc');
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
        const selectedType = urlParams.get('typeSearch') || '';
        const selectedReplyStat = urlParams.get('replyStatSearch') || '';
        const selectedName = urlParams.get('nameSearch') || '';

        window.location.href = "/voc?page=0&size=" + selectedSize +
                               "&typeSearch=" + selectedType +
                               "&replyStatSearch=" + selectedReplyStat +
                               "&nameSearch=" + selectedName;
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        const replyContent = $('#replyContent').val();
        if (!replyContent || replyContent.trim() === "") {
            alert("답변을 입력해주세요.");
            return;
        }

        if(confirmSubmit("저장")) {
            const DATA = {
                replyContent: replyContent
            }

            $.ajax({
                type: 'PATCH',
                url: `/voc/update/${vocId}`,
                data: JSON.stringify(DATA),
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
});