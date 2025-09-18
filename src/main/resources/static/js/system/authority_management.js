$(document).ready(function () {
    var companyId, currCompany, currAuthority, currAuthorityName;

    $('#companyIdSearch').on('change', function () {
        companyId = $(this).val();
        console.log("companyId: " + companyId);
        var companyName;
        renderAuthorityTable();
        document.getElementById('saveBtn').disabled = true;

        if (companyId === null || companyId.trim() === "") {
            companyName = "";
        } else {
            companyName = $("#companyIdSearch option:selected").text();
        }

        var companyIdAll = document.querySelectorAll('.companyName');
        companyIdAll.forEach(function (element) {
            element.setAttribute('value', companyId);
            element.textContent = companyName;
        });
    });

    $('#pageList').on('click', 'tr', function () {
        companyId = $(this).find('td').eq(0).attr('value');
        currCompany = $(this).find('td').eq(0).text();
        currAuthority = $(this).find('td').eq(1).text();
        currAuthorityName = $(this).find('td').eq(2).text();
        const authority = $(this).find('td').eq(1).attr('value');

        if (!companyId || companyId.trim() === "") {
            renderAuthorityTable();
            return;
        }

        $('#currAuthority').empty();
        $('#authorityTable').empty();
        $('#currAuthority').append(`
            <span>
                ${i18n.menuAuthMgmt.labels.company}: ${currCompany} | ${i18n.menuAuthMgmt.labels.authGrpId}: ${currAuthority} | ${i18n.menuAuthMgmt.labels.authGrpName}: ${currAuthorityName}
            </span>
        `);

        $.ajax({
            type: 'GET',
            url: `/system/authority/get/${companyId}/${authority}`,
            contentType: "application/json",
            dataType: 'json',
            success: function (response) {
                renderMenuTable(response.authorityList);
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });

    function renderAuthorityTable() {
        $('#authorityTable').empty();
        $('#authorityTable').append(`
            <tr>
                <td colspan="6" th:text="#{menuAuthMgmt.messages.menuAuthInfo}"></td>
            </tr>
        `);
    }

    function renderMenuTable(menuList) {
        var authorityTable = $('#authorityTable');

        $.each(menuList, function (index, menu) {
            var topMenu = "";
            var midMenu = "";
            var lowMenu = "";

            if (menu.menuLv == '0') {
                topMenu = menu.menuName;
            } else if (menu.menuLv == '1') {
                var parentMenu = menuList.find(m => m.menuCode === menu.parentCode);
                topMenu = parentMenu ? parentMenu.menuName : "";
                midMenu = menu.menuName;
            } else if (menu.menuLv == '2') {
                var parentMenuLv1 = menuList.find(m => m.menuCode === menu.parentCode);
                if (parentMenuLv1) {
                    var parentMenuLv0 = menuList.find(m => m.menuCode === parentMenuLv1.parentCode);
                    topMenu = parentMenuLv0 ? parentMenuLv0.menuName : "";
                    midMenu = parentMenuLv1.menuName;
                }
                lowMenu = menu.menuName;
            }

            var row = '';
            if ((menu.menuLv == '0' || menu.menuLv == '1') && !menu.menuUrl.startsWith('/')) {
                row += `<tr id="${menu.menuCode}" hidden>`;
            } else {
                row += `<tr id="${menu.menuCode}">`;
            }

            let isEditable = false;
            let isReadOnly = false;

            if (menuAuthority.authority === 'SU') {
                document.getElementById('saveBtn').disabled = false;
                isEditable = true;
            } else if (menuAuthority.authority === 'AD') {
                console.log("admin companyId: " + menuAuthority.companyId);

                if (companyId === String(menuAuthority.companyId)) {
                    console.log("admin, company");

                    if (currAuthority === 'AD') {
                        document.getElementById('saveBtn').disabled = true;
                        isReadOnly = true;
                    } else {
                        document.getElementById('saveBtn').disabled = false;
                        isEditable = true;
                    }
                } else {
                    console.log("admin, company not match");
                    document.getElementById('saveBtn').disabled = true;
                    isReadOnly = true;
                }
            } else {
                isReadOnly = true;
            }

            row += `<td class="text-start">${topMenu}</td>
                    <td class="text-start">${midMenu}</td>
                    <td class="text-start">${lowMenu}</td>`;

            if (isEditable) {
                row += `<td><input type="text" class="text-center" name="readYn_${index}" value="${menu.readYn}"></td>
                        <td><input type="text" class="text-center" name="modYn_${index}" value="${menu.modYn}"></td>
                        <td><input type="text" class="text-center" name="excelYn_${index}" value="${menu.excelYn}"></td>
                    </tr>`;
            } else if (isReadOnly) {
                row += `<td class="text-center" name="readYn_${index}" value="${menu.readYn}">${menu.readYn}</td>
                        <td class="text-center" name="modYn_${index}" value="${menu.modYn}">${menu.modYn}</td>
                        <td class="text-center" name="excelYn_${index}" value="${menu.excelYn}">${menu.excelYn}</td>
                    </tr>`;
            }

            authorityTable.append(row);
        });
    }

    $('#saveBtn').on('click', function (event) {
        event.preventDefault();

        if (confirmSubmit(i18n.menuAuthMgmt.buttons.save)) {

            var menuAuthorities = [];
            var checkedYn = false;

            $('#authorityTable tr').each(function (index, tr) {
                var readYn = $('input[name="readYn_' + index + '"]').val();
                var modYn = $('input[name="modYn_' + index + '"]').val();
                var excelYn = $('input[name="excelYn_' + index + '"]').val();

                if ((readYn != 'Y' && readYn != 'N') || (modYn != 'Y' && modYn != 'N') || (excelYn != 'Y' && excelYn != 'N')) {
                    checkedYn = true;
                }

                menuAuthorities.push({
                    companyId: companyId,
                    menuCode: $(tr).attr('id'),
                    authority: currAuthority,
                    readYn: readYn,
                    modYn: modYn,
                    excelYn: excelYn
                });
            });

            if (checkedYn) {
                alert(i18n.menuAuthMgmt.messages.yNOnly);
                return;
            }

            $.ajax({
                url: `/system/authority/new`,
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(menuAuthorities),
                success: function (response) {
                    alert(response);
                },
                error: function (xhr, status, error) {
                    console.error(error);
                }
            });
        }
    });
});