$(document).ready(function() {
    let modalCon = false, selectRow;
    var vocId;

    $(document).on('dblclick', '#vocTable tbody tr', function() {
        selectRow = $(this);
        modalCon = true;
        vocId = selectRow.find('td').eq(0).attr('id');
        
        $('#vocForm')[0].reset();
        $('#type').prop('disabled', true);
        $('#title').prop('disabled', true);
        $('#memSearchBtn').hide();
        $('#content').attr('readonly', true);

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
                    $('#type').val(data.vocInfo.type || '');
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

    $('#addBtn').on('click', function(event) {
        event.preventDefault();
        modalCon = false;

        $('#vocForm')[0].reset();
        $('#type').prop('disabled', false);
        $('#title').prop('disabled', false);
        $('#memName').val('');
        $('#memPhone').val('');
        $('#memSearchBtn').show();
        $('#content').removeAttr('readonly');
        document.getElementById('memSearchList').innerHTML = '<tr><td colspan="4">조회된 데이터가 없습니다.</td></tr>';
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();


        const replyContent = $('#replyContent').val();
        if (modalCon) {
            if (!replyContent || replyContent.trim() === "") {
                alert("답변을 입력해주세요.");
                return;
            }
        } else {
            const memberId = $('#memberId').val();
            if (!memberId) {
                alert("회원정보를 검색해주세요.");
                return;
            }
        }
        

        if(confirmSubmit("저장")) {
            const DATA = {
                memberId: $('#memberId').val(),
                type: $('#type').val(),
                title: $('#title').val(),
                content: $('#content').val(),
                replyContent: replyContent
            }

            const URL = modalCon ? `/voc/update/${vocId}` : '/voc/new/call';
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                type: TYPE,
                url: URL,
                data: JSON.stringify(DATA),
                contentType: "application/json",
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
    
    $('#memSearchModal').on('show.bs.modal', function () {
        $('#dataAddModal .modal-content').addClass('blur-background');
    });

    $('#memSearchModal').on('hidden.bs.modal', function () {
        $('#dataAddModal .modal-content').removeClass('blur-background');
    });
    
    $('#memSearchBtn').on('click', function() {
        var memSearchModal = new bootstrap.Modal(document.getElementById('memSearchModal'), {
            backdrop: 'static',
            keyboard: false
        });
        memSearchModal.show();
    });

    $('#memInfoSearchBtn').on('click', function() {
        $.ajax({
            url: `/voc/search/member`,
            method: 'GET',
            data: {
                memName: $('#memName').val(),
                memPhone: $('#memPhone').val()
            },
            success: function(response) {
                var memSearchList = document.getElementById('memSearchList');
                memSearchList.innerHTML = '';
                if (response.memberList.length === 0) {
                    memSearchList.innerHTML = '<tr><td colspan="4">조회된 데이터가 없습니다.</td></tr>';
                    return;
                }

                response.memberList.forEach(function (mem) {
                    var row = document.createElement('tr');
                    row.innerHTML = `
                        <td id='${mem.memberId}'>${mem.name}</td>
                        <td>${mem.phoneNo}</td>
                        <td>${mem.idTag}</td>
                        <td><button type="button" class="btn btn-outline-grey select-mem-btn" data-member-id="${mem.memberId}">선택</button></td>
                    `;
                    memSearchList.appendChild(row);
                });
            },
            error: function(xhr) {
                var response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });

    $('#memSearchList').on('click', function(e) {
        if (e.target.classList.contains('select-mem-btn')) {
            var memberId = e.target.getAttribute('data-member-id');
            $.ajax({
                url: `/member/get/${memberId}`,
                method: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                success: function(response) {
                    $('#memberId').val(response.memberInfo.memberId || '');
                    $('#name').val(response.memberInfo.name || '');
                    $('#phoneNo').val(response.memberInfo.phoneNo || '');
                },
                error: function(xhr, status, error) {
                    alert(JSON.parse(xhr.responseText).message);
                }
            });
            var memSearchModal = bootstrap.Modal.getInstance(document.getElementById('memSearchModal'));
            memSearchModal.hide();
        }
    });
});