document.addEventListener("DOMContentLoaded", function () {

    let selectedRow; // 선택한 행, 열 저장

    // '펌웨어 설정 초기화' 버튼 클릭 이벤트 처리
    document.getElementById("fwconfigResetBtn").addEventListener("click", function () {

        document.getElementById('companyIdFw').value = '';
        document.getElementById('modelSearch').value = '';
        document.getElementById('versionSearch').value = '';
        document.getElementById('urlSearch').value = '';
        document.getElementById('retries').value = '';
        document.getElementById('retryInterval').value = '';
        document.getElementById('retrieveDate').value = '';
    });

    $('#size').on('change', function () {
        updatePageSize(this, "/fw/update/get/cplist", ["companyIdSearch", "stationIdSearch", "companyIdFw", "modelSearch", "versionSearch", "urlSearch", "retries", "retryInterval", "retrieveDate"]);
    });

    // // '검색' 버튼 클릭 이벤트 처리
    // document.getElementById("serachBtn").addEventListener("click", function () {

    //     if (document.getElementById('companyIdSearch').value == '') {
    //         alert("사업자를 선택해주세요.");
    //         return;
    //     }


    //     // 현재 선택된 펌웨어 값들을 hidden 필드에 설정
    //     document.querySelector('input[name="companyIdFw"]').value = document.getElementById('companyIdFw').value;
    //     document.querySelector('input[name="modelSearch"]').value = document.getElementById('modelSearch').value;
    //     document.querySelector('input[name="versionSearch"]').value = document.getElementById('versionSearch').value;
    //     document.querySelector('input[name="urlSearch"]').value = document.getElementById('urlSearch').value;
    //     document.querySelector('input[name="retries"]').value = document.getElementById('retries').value;
    //     document.querySelector('input[name="retryInterval"]').value = document.getElementById('retryInterval').value;
    //     document.querySelector('input[name="retrieveDate"]').value = document.getElementById('retrieveDate').value;


    //     // 폼 제출
    //     document.getElementById('searchForm').submit();
    // });




    // '초기화' 버튼 클릭 이벤트 처리
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 검색 폼 필드만 초기화
        document.getElementById('companyIdSearch').value = '';
        document.getElementById('stationIdSearch').value = '';

    });

    // 펌웨어선택 - 사업자 ID선택에 따른 모델명 리스트 조회요청
    $('#companyIdFw').change(function () {
        const companyId = $(this).val();

        // company가 선택되지 않은경우 모델 리스트 초기화
        if (!companyId || companyId === '*{null}') {
            $('#modelSearch').html('<option value="">' + i18n.fw.labels.select + '</option>');
            $('#versionSearch').html('<option value="">' + i18n.fw.labels.select + '</option>');
            $('#urlSearch').val('');
            return;
        }

        // 모델명 리스트 조회
        $.ajax({
            url: '/fw/model/list',
            type: 'GET',
            data: { companyId: companyId },
            success: function (models) {
                let options = '<option value="">' + i18n.fw.labels.select + '</option>';

                //모델리스트 생성
                $.each(models, function (i, model) {
                    options += '<option value="' + model.modelCode + '">' + model.modelCode + '</option>';
                });

                $('#modelSearch').html(options);

                //모델 선택 초기화 시 버전도 초기화
                $('#versionSearch').html('<option value="">' + i18n.fw.labels.select + '</option>');
                $('#urlSearch').val('');
            },
            error: function (xhr, status, error) {
                console.error('model list search failed:', error);
                alert(i18n.fw.messages.modellistsearchfailed);
            }
        });
    });

    // 모델 선택에 따른 버전 리스트 조회 요청
    $('#modelSearch').change(function () {
        const modelCode = $(this).val();
        const companyId = $('#companyIdFw').val();

        if (!modelCode || !companyId) {
            $('#versionSearch').html('<option value="">' + i18n.fw.labels.select + '</option>');
            $('#urlSearch').val('');
            return;
        }

        $.ajax({
            url: '/fw/version/list',
            type: 'GET',
            data: {
                companyId: companyId,
                modelCode: modelCode
            },
            success: function (versions) {
                let options = '<option value="">' + i18n.fw.labels.select + '</option>';

                $.each(versions, function (i, version) {
                    options += '<option value="' + version.fwVersion + '">' + version.fwVersion + '</option>';
                });

                $('#versionSearch').html(options);
                $('#urlSearch').val('');
            },
            error: function (xhr, status, error) {
                console.error('version list search failed:', error);
                alert(i18n.fw.messages.versionlistsearchfailed);
            }
        });
    });

    //버전 선택에 따른 url 조회
    $('#versionSearch').change(function () {
        const version = $(this).val();
        const modelCode = $('#modelSearch').val();
        const companyId = $('#companyIdFw').val();

        if (!version || !modelCode || !companyId) {
            $('#urlSearch').val('');
            return;
        }

        $.ajax({
            url: '/fw/update/url',
            type: 'GET',
            data: {
                companyId: companyId,
                modelCode: modelCode,
                version: version
            },
            success: function (url) {
                $('#urlSearch').val(url);
            },
            error: function (xhr, status, error) {
                console.error('url search failed:', error);
                alert(i18n.fw.messages.urlsearchfailed);
            }
        });
    });

    // 전체 선택 체크박스 이벤트 추가
    const selectAllCheckbox = document.querySelector('#selectAll');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function () {
            const checkboxes = document.querySelectorAll('#pageList input[type="checkbox"]');

            // 전체 체크박스 개수가 10개를 초과하는 경우
            if (checkboxes.length > 10 && this.checked) {
                alert(i18n.fw.messages.maxselect);
                this.checked = false;
                return;
            }

            checkboxes.forEach(checkbox => {
                checkbox.checked = selectAllCheckbox.checked;
                updateRowStyle(checkbox);
            });
        });
    }

    // 개별 체크박스 클릭 이벤트 처리
    const pageList = document.getElementById('pageList');
    if (pageList) {
        pageList.addEventListener('click', function (event) {
            const target = event.target;

            // 링크 클릭 시 이벤트 전파 중지
            if (target.tagName === 'A') {
                event.stopPropagation();
                return;
            }

            let checkbox;
            if (target.type === 'checkbox') {
                checkbox = target;
            } else {
                const row = target.closest('tr');
                if (!row) return;

                checkbox = row.querySelector('input[type="checkbox"]');
                if (!checkbox) return;

                checkbox.checked = !checkbox.checked;
            }

            // 체크된 체크박스 개수 확인
            const checkedCount = document.querySelectorAll('#pageList input[type="checkbox"]:checked').length;

            // 10개 초과 선택 시 체크 해제
            if (checkedCount > 10) {
                alert(i18n.fw.messages.maxselect);
                checkbox.checked = false;
            }

            updateRowStyle(checkbox);
            updateSelectAllCheckboxState();
        });
    }

    // 행 스타일 업데이트 함수
    function updateRowStyle(checkbox) {
        const row = checkbox.closest('tr');
        if (checkbox.checked) {
            row.classList.add('table-selected-active');
        } else {
            row.classList.remove('table-selected-active');
        }
    }

    // 전체 선택 상태 업데이트 함수
    function updateSelectAllCheckboxState() {
        if (!selectAllCheckbox) return;

        const allCheckboxes = document.querySelectorAll('#pageList input[type="checkbox"]');
        const checkedCheckboxes = document.querySelectorAll('#pageList input[type="checkbox"]:checked');

        selectAllCheckbox.checked = allCheckboxes.length === checkedCheckboxes.length && allCheckboxes.length > 0;
    }

    // 업데이트 버튼 이벤트
    const updateBtn = document.getElementById("updateBtn");
    if (updateBtn) {
        updateBtn.addEventListener("click", function () {
            const selectedUrl = document.getElementById('urlSearch').value;
            const retries = document.getElementById('retries').value;
            const retryInterval = document.getElementById('retryInterval').value;
            const retrieveDate = document.getElementById('retrieveDate').value;

            if (!selectedUrl) {
                alert(i18n.fw.messages.nourlinfo);
                return;
            }

            // 선택된 충전기 수집
            const selectedChargers = [];
            document.querySelectorAll('#pageList input[type="checkbox"]:checked').forEach(checkbox => {
                const row = checkbox.closest('tr');
                const chargerId = row.cells[5].innerText;
                selectedChargers.push(chargerId);
            });

            if (selectedChargers.length === 0) {
                alert(i18n.fw.messages.selectcharger);
                return;
            }

            // 10개 초과 선택 체크 (추가 안전장치)
            if (selectedChargers.length > 10) {
                alert(i18n.fw.messages.maxselect);
                return;
            }

            if (confirm(`${i18n.fw.messages.confirmupdate}(${i18n.fw.messages.selectedchargernum} ${selectedChargers.length})`)) {
                // 벌크 업데이트 처리
                const updatePromises = selectedChargers.map(chargerId => {
                    const updateData = {
                        chargerId: chargerId,
                        location: selectedUrl,
                        retries: retries,
                        retrieveDate: retrieveDate || new Date().toISOString(),
                        retryInterval: retryInterval
                    };

                    return fetch('/control/fwupdate', {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(updateData)
                    }).then(response => response.json());
                });

                Promise.all(updatePromises)
                    .then(results => {
                        const successCount = results.filter(result => result.status === 'Accepted').length;
                        const failCount = results.length - successCount;

                        alert(`${i18n.fw.messages.updateresult}\n${i18n.fw.messages.success}: ${successCount}\n${i18n.fw.messages.fail}: ${failCount}`);

                        // 체크박스 초기화
                        if (selectAllCheckbox) {
                            selectAllCheckbox.checked = false;
                        }
                        document.querySelectorAll('#pageList input[type="checkbox"]').forEach(checkbox => {
                            checkbox.checked = false;
                            updateRowStyle(checkbox);
                        });
                    })
                    .catch(error => {
                        alert(i18n.fw.messages.updatefailed);
                        console.error('update request error:', error);
                    });
            }
        });
    }

});