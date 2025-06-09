document.addEventListener("DOMContentLoaded", function () {

    let selectedRow; // 선택한 행, 열 저장

    // '검색' 버튼 클릭 이벤트 처리
    document.getElementById("serachBtn").addEventListener("click", function () {

        if (document.getElementById('companyIdSearch').value == '') {
            alert("사업자를 선택해주세요.");
            return;
        }


        // 현재 선택된 펌웨어 값들을 hidden 필드에 설정
        document.querySelector('input[name="companyIdFw"]').value = document.getElementById('companyIdFw').value;
        document.querySelector('input[name="modelSearch"]').value = document.getElementById('modelSearch').value;
        document.querySelector('input[name="versionSearch"]').value = document.getElementById('versionSearch').value;
        document.querySelector('input[name="urlSearch"]').value = document.getElementById('urlSearch').value;
        document.querySelector('input[name="retries"]').value = document.getElementById('retries').value;
        document.querySelector('input[name="retryInterval"]').value = document.getElementById('retryInterval').value;
        document.querySelector('input[name="retrieveDate"]').value = document.getElementById('retrieveDate').value;


        // 폼 제출
        document.getElementById('searchForm').submit();
    });


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

    // '초기화' 버튼 클릭 이벤트 처리
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 검색 폼 필드만 초기화
        document.getElementById('companyIdSearch').value = '';
        document.getElementById('stationIdSearch').value = '';

        // // 펌웨어 선택 값들은 현재 값으로 유지
        // document.querySelector('input[name="companyIdFw"]').value = document.getElementById('companyIdFw').value;
        // document.querySelector('input[name="modelSearch"]').value = document.getElementById('modelSearch').value;
        // document.querySelector('input[name="versionSearch"]').value = document.getElementById('versionSearch').value;
        // document.querySelector('input[name="urlSearch"]').value = document.getElementById('urlSearch').value;

        // document.getElementById('searchForm').submit();
    });

    // 펌웨어선택 - 사업자 ID선택에 따른 모델명 리스트 조회요청
    $('#companyIdFw').change(function () {
        const companyId = $(this).val();

        // company가 선택되지 않은경우 모델 리스트 초기화
        if (!companyId || companyId === '*{null}') {
            $('#modelSearch').html('<option value="">(선택)</option>');
            $('#versionSearch').html('<option value="">(선택)</option>');
            $('#urlSearch').val('');
            return;
        }

        // 모델명 리스트 조회
        $.ajax({
            url: '/fw/model/list',
            type: 'GET',
            data: { companyId: companyId },
            success: function (models) {
                let options = '<option value="">(선택)</option>';

                //모델리스트 생성
                $.each(models, function (i, model) {
                    options += '<option value="' + model.modelCode + '">' + model.modelCode + '</option>';
                });

                $('#modelSearch').html(options);

                //모델 선택 초기화 시 버전도 초기화
                $('#versionSearch').html('<option value="">(선택)</option>');
                $('#urlSearch').val('');
            },
            error: function (xhr, status, error) {
                console.error('모델 리스트 조회 실패:', error);
                alert('모델 리스트 조회에 실패했습니다.');
            }
        });
    });

    // 모델 선택에 따른 버전 리스트 조회 요청
    $('#modelSearch').change(function () {
        const modelCode = $(this).val();
        const companyId = $('#companyIdFw').val();

        if (!modelCode || !companyId) {
            $('#versionSearch').html('<option value="">(선택)</option>');
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
                let options = '<option value="">(선택)</option>';

                $.each(versions, function (i, version) {
                    options += '<option value="' + version.fwVersion + '">' + version.fwVersion + '</option>';
                });

                $('#versionSearch').html(options);
                $('#urlSearch').val('');
            },
            error: function (xhr, status, error) {
                console.error('버전 리스트 조회 실패:', error);
                alert('버전 리스트 조회에 실패했습니다.');
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
                console.error('url 조회 실패:', error);
                alert('url 조회에 실패했습니다.');
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
                alert("한 번에 최대 10개까지만 선택할 수 있습니다.");
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
                alert("한 번에 최대 10개까지만 선택할 수 있습니다.");
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
                alert("URL 정보가 없습니다.");
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
                alert("업데이트할 충전기를 선택해주세요.");
                return;
            }

            // 10개 초과 선택 체크 (추가 안전장치)
            if (selectedChargers.length > 10) {
                alert("한 번에 최대 10개까지만 선택할 수 있습니다.");
                return;
            }

            if (confirm(`선택한 ${selectedChargers.length}개의 충전기를 업데이트하시겠습니까?`)) {
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

                        alert(`펌웨어 업데이트 요청 결과:\n성공: ${successCount}건\n실패: ${failCount}건`);

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
                        alert('펌웨어 업데이트 요청 중 오류가 발생했습니다.');
                        console.error('업데이트 요청 오류:', error);
                    });
            }
        });
    }

});