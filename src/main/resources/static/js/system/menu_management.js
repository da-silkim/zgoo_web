$(document).ready(function() {
    let modalCon = false;   // false: 등록 / true: 수정
    let btnMsg = "등록", selectRow;

    $('#pageList').on('click', 'tr', function() {
        // const currMenuId = $(this).find('td').eq(3).text();
        selectRow = $(this).find('td').eq(3).text();
        // console.log("현재 메뉴: " + currMenuId);
    });

    // 메뉴 - 등록
    $('#addBtn').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        $('#menuLv').prop('selectedIndex', 0);
        $('#parentCode').val('');
        $('#menuName').val('');
        $('#menuCode').val('');
        $('#menuUrl').val('');
        $('#iconClass').val('');
        $('#useYnYes').prop('checked', true);
        $('#parentCode').empty();
    });

    // 메뉴 - 수정
    $('#editBtn').on('click', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        const menuCode = selectRow;
        console.log("수정할 메뉴코드: " + menuCode);

        $.ajax({
            type: 'GET',
            url: `/system/menu/get/${menuCode}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(data) {
                $('#menuLv').val(data.menuLv || '');
                $('#menuName').val(data.menuName || '');
                $('#menuCode').val(data.menuCode || '');
                $('#menuUrl').val(data.menuUrl || '');
                $('#iconClass').val(data.iconClass || '');

                var parentCodeSelect = $('#parentCode');
                parentCodeSelect.empty();

                if (data.menuLv == '1') {
                    fetchParentMenuData2('0', data.parentCode);
                } else if (data.menuLv == '2') {
                    fetchParentMenuData('1', data.parentCode);
                } else {
                    parentCodeSelect.prop('disabled', true);
                }

                console.log("useYn 값:", data.useYn);

                if (data.useYn === 'Y') {
                    $('#useYnYes').prop('checked', true);
                } else {
                    $('#useYnNo').prop('checked', true);
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX error:', status, error);
            }
        });
    });

    $('#deleteBtn').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            const menuCode = selectRow;

            $.ajax({
                type: 'DELETE',
                url: `/system/menu/delete/${menuCode}`,
                contentType: "application/json",
                success: function(response) {
                    console.log("메뉴 삭제 성공", response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.error("메뉴 삭제 중 오류 발생:", error);
                }
            });
        }
    });

    $('#modalBtn').on('click', function(event) {
        event.preventDefault();

        if(confirmSubmit(btnMsg)) {
            const data = {
                menuLv: $('#menuLv').val(),
                parentCode: $('#parentCode').val(),
                menuName: $('#menuName').val(),
                menuCode: $('#menuCode').val(),
                menuUrl: $('#menuUrl').val(),
                iconClass: $('#iconClass').val(),
                useYn: $('input[name="useYn"]:checked').val()
            };
    
            const URL = modalCon ? `/system/menu/update` : `/system/menu/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                type: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(response) {
                    console.log("메뉴 등록/수정 성공", response);
                    // $('#menuModal').modal('hide');  // 모달 창 닫기
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.error("메뉴 등록/수정 중 오류 발생:", error);
                }
            });
        }
    });

    $('#menuLv').change(function() {
        var selectedMenuLv = $(this).val();
        var parentCodeSelect = $('#parentCode');

        parentCodeSelect.empty();
        // parentCodeSelect.append('<option value="null">선택</option>');

        if (selectedMenuLv == '1') {
            fetchParentMenuData('0');
        } else if (selectedMenuLv == '2') {
            fetchParentMenuData('1');
        } else {
            parentCodeSelect.prop('disabled', true);
        }
    });

    function fetchParentMenuData(menuLv) {
        $.ajax({
            url: '/system/menu/parent/' + menuLv,
            method: 'GET',
            success: function(data) {
                data.forEach(function(menu) {
                    $('#parentCode').append('<option value="' + menu.menuCode + '">' + menu.menuName + '</option>');
                });
                $('#parentCode').prop('disabled', false);
            }
        });
    }

    function fetchParentMenuData2(menuLv, parentCode) {
        $.ajax({
            url: '/system/menu/parent/' + menuLv,
            method: 'GET',
            success: function(data) {
                var parentCodeSelect = $('#parentCode');
                parentCodeSelect.empty();
    
                data.forEach(function(menu) {
                    parentCodeSelect.append('<option value="' + menu.menuCode + '">' + menu.menuName + '</option>');
                });
    
                // 부모 메뉴 코드가 있으면 해당 값으로 셀렉트 박스의 값 설정
                if (parentCode) {
                    parentCodeSelect.val(parentCode);
                }
    
                parentCodeSelect.prop('disabled', false);
            }
        });
    }
    

});