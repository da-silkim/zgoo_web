$(document).ready(function() {
    let modalCon = false;
    let btnMsg = "등록", selectRow, selectRowSec;

    $('#size').on('change', function() {
        updatePageSize(this, "/system/menu/list", ["companyNameSearch"]);
    });

    $('#pageList').on('click', 'tr', function() {
        selectRow = $(this).find('td').eq(3).text();
    });

    $('#pageList2').on('click', 'tr', function() {
        selectRowSec = $(this).find('td').eq(0).attr('id');
    });

    // 메뉴 - 등록
    $('#addBtn').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);

        $('#parentCode').prop('disabled', true);
        $('#companyId').prop('disabled', false);
        $('#menuLv').prop('selectedIndex', 0);
        $('#parentCode').val('');
        $('#menuName').val('');
        $('#menuCode').val('');
        $('#menuUrl').val('');
        $('#iconClass').val('');
        $('#useYnYes').prop('checked', true);
        $('#parentCode').empty();
    });

    $('#addBtnSec').on('click', function() {
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtnSec').text(btnMsg);

        $('#companyId').prop('disabled', false);
        $.ajax({
            url: '/system/menu/company',
            method: 'GET',
            dataType: 'json',
            success: function(companyMenuList) {
                console.log(companyMenuList);
                renderMenuTable(companyMenuList);
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });
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
                    fetchParentMenuData2('1', data.parentCode);
                } else {
                    parentCodeSelect.prop('disabled', true);
                }

                if (data.useYn === 'Y') {
                    $('#useYnYes').prop('checked', true);
                } else {
                    $('#useYnNo').prop('checked', true);
                }
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });
    });

    // 사업장별 메뉴 권한 - 수정
    $('#editBtnSec').on('click', function() {
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtnSec').text(btnMsg);

        const companyId = selectRowSec;
        $('#companyId').prop('disabled', true);

        $.ajax({
            type: 'GET',
            url: `/system/menu/company/get/${companyId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function(companyMenuList) {
                $('#companyId').val(companyId || '');
                renderMenuTable(companyMenuList);
            },
            error: function(xhr, status, error) {
                console.error(error);
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
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.error(error);
                }
            });
        }
    });

    $('#deleteBtnSec').on('click', function() {
        btnMsg = "삭제";

        if(confirmSubmit(btnMsg)) {
            const companyId = selectRowSec;

            $.ajax({
                type: 'DELETE',
                url: `/system/menu/company/delete/${companyId}`,
                contentType: "application/json",
                success: function(response) {
                    alert(response);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.error(error);
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
                    alert(response);
                    // $('#menuModal').modal('hide');  // 모달 창 닫기
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.error(error);
                }
            });
        }
    });

    $('#modalBtnSec').on('click', function(event) {
        event.preventDefault();

        if(confirmSubmit(btnMsg)) {

            var menuAuthorities = [];
            $('#menuTable tr').each(function(index, tr) {
                var useYn = $('input[name="useYn_' + index + '"]:checked').val();
                var menuCode = $(tr).attr('id');

                menuAuthorities.push({
                    companyId: $('#companyId').val(),
                    menuCode: menuCode,
                    useYn: useYn
                });
            });

            const URL = modalCon ? `/system/menu/company/update` : `/system/menu/company/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(menuAuthorities),
                success: function(response) {
                    if (modalCon) {
                        window.location.reload();
                    } else {
                        window.location.href = "/system/menu/list";
                    }
                    alert(response);
                },
                error: function(xhr, status, error) {
                    console.error(error);
                }
            });
        }
    });

    $('#menuLv').change(function() {
        var selectedMenuLv = $(this).val();
        var parentCodeSelect = $('#parentCode');
        parentCodeSelect.empty();

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
            },
            error: function(xhr, status, error) {
                console.log(error);
            }
        });
    }

    function renderMenuTable(menuList) {
        var menuTable = $('#menuTable');
        menuTable.empty();

        $.each(menuList, function(index, menu) {
            var topMenu = "";
            var midMenu = "";
            var lowMenu = "";

            if (menu.menuLv == '0') {
                topMenu = menu.menuName;
            } else if (menu.menuLv == '1') {
                // 중위메뉴일 경우 상위메뉴 찾기
                var parentMenu = menuList.find(m => m.menuCode === menu.parentCode);
                topMenu = parentMenu ? parentMenu.menuName : "";
                midMenu = menu.menuName;
            } else if (menu.menuLv == '2') {
                // 하위메뉴일 경우 상위 및 중위 메뉴 찾기
                var parentMenuLv1 = menuList.find(m => m.menuCode === menu.parentCode);
                if (parentMenuLv1) {
                    var parentMenuLv0 = menuList.find(m => m.menuCode === parentMenuLv1.parentCode);
                    topMenu = parentMenuLv0 ? parentMenuLv0.menuName : "";
                    midMenu = parentMenuLv1.menuName;
                }
                lowMenu = menu.menuName;
            }

            // 사용 여부 라디오 버튼 생성
            var useYnRadio = `
                <div>
                    <input type="radio" name="useYn_${index}" value="Y" ${menu.useYn === "Y" ? "checked" : ""}>
                    <label>사용</label>
                </div>
                <div class="ms-5">
                    <input type="radio" name="useYn_${index}" value="N" ${menu.useYn === "N" ? "checked" : ""}>
                    <label>사용 안 함</label>
                </div>
            `;

            var row = '';
            var disabledClass = '';
            // menuLv이 0이고 menuUrl이 '/'로 시작하지 않는 경우 hidden
            // menuLv이 1이고 menuUrl이 '/'로 시작하지 않는 경우 hidden
            if ((menu.menuLv == '0' || menu.menuLv == '1') && !menu.menuUrl.startsWith('/') ) {
                var row = `<tr id="${menu.menuCode}" hidden>`;
            } else if (menu.useYn === 'N') {
                disabledClass = 'disabled-row'; // 비활성화 클래스를 추가
                var row = `<tr id="${menu.menuCode}" class="${disabledClass}">`;
            } else {
                row += `<tr id="${menu.menuCode}">`;
            }

            row += `<td class="text-start">${topMenu}</td>
                    <td class="text-start">${midMenu}</td>
                    <td class="text-start">${lowMenu}</td>
                    <td class="radio-wrapper">${useYnRadio}</td>
                </tr>`;

            menuTable.append(row);
        });
    }

    function updateParentMenuUseYn(menuCode, menuList) {
        var menu = menuList.find(m => m.menuCode === menuCode);
        if (menu && menu.menuLv > 0) {  // 부모 메뉴가 있을 경우
            var parentMenu = menuList.find(m => m.menuCode === menu.parentCode);
            if (parentMenu) {
                // 부모 메뉴의 자식들이 모두 'N'이면 부모 메뉴의 useYn을 'N'으로 설정
                var allChildMenusAreN = menuList
                    .filter(m => m.parentCode === parentMenu.menuCode)
                    .every(m => m.useYn === 'N');
    
                parentMenu.useYn = allChildMenusAreN ? 'N' : 'Y';
    
                // 부모 메뉴의 row를 업데이트 (UI에서 반영)
                var parentRow = $('#menuTable tr#' + parentMenu.menuCode);
    
                // 부모 메뉴의 라디오 버튼을 변경
                var parentRadio = parentRow.find('input[type="radio"][value="Y"]');
                if (parentMenu.useYn === 'N') {
                    parentRadio.prop('checked', false);
                    parentRow.find('input[type="radio"][value="N"]').prop('checked', true);
                } else {
                    parentRadio.prop('checked', true);
                    parentRow.find('input[type="radio"][value="N"]').prop('checked', false);
                }
    
                // 부모 메뉴의 비활성화 여부 UI 반영
                if (parentMenu.useYn === 'N') {
                    parentRow.addClass('disabled-row');
                } else {
                    parentRow.removeClass('disabled-row');
                }
            }
        }
    }
    
});