document.addEventListener("DOMContentLoaded", function () {
    // 전역 변수로 선택된 충전기 ID 저장
    let selectedChargerId = '';


    // /**
    //  * 검색버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("cpSearchBtn").addEventListener("click", function () {
    //     // 폼 제출 (size 값은 이미 hidden input에 있음)
    //     document.getElementById('searchForm').submit();
    // });

    // /**
    //  * 초기화 버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("resetBtn").addEventListener("click", function () {
    //     // 모든 입력 필드 초기화
    //     document.getElementById('companyIdSearch').value = '';
    //     document.getElementById('opSearch').value = '';
    //     document.getElementById('contentSearch').value = '';

    //     // 폼 제출 (초기화된 상태로)
    //     document.getElementById('searchForm').submit();
    // });

    $('#size').on('change', function () {
        updatePageSize(this, "/control/charger/list", ["companyIdSearch", "opSearch", "contentSearch"]);
    });

    // 테이블 row 더블클릭 이벤트 처리
    const tableRows = document.querySelectorAll("#dcTable tbody tr");
    tableRows.forEach(function (row) {
        row.addEventListener("dblclick", function () {
            // 선택된 row의 데이터 수집
            const rowData = {
                stationId: row.cells[3].textContent.trim(),
                stationName: row.cells[2].textContent.trim(),
                chargerId: row.cells[5].textContent.trim(),
                protocol: row.cells[6].textContent.trim()
            };

            // 선택된 충전기 ID 저장
            selectedChargerId = rowData.chargerId;

            // 모달 열기
            const modal = new bootstrap.Modal(document.getElementById('dataAddModal'));
            modal.show();

            // 모달 내의 input 필드에 데이터 채우기
            const modalInputs = document.querySelector('#dataAddModal').querySelectorAll('input');
            modalInputs[0].value = rowData.stationId;
            modalInputs[1].value = rowData.stationName;
            modalInputs[2].value = rowData.chargerId;
            modalInputs[3].value = rowData.protocol;

            // OCPP 설정값 조회 테이블 초기화
            const configTableBody = document.getElementById('configurationTable');
            if (configTableBody) {
                configTableBody.innerHTML = '';
            }

            // OCPP 설정값 조회 항목 초기화
            // 전체조회 라디오 버튼 선택
            const allSearchRadio = document.querySelector('input[name="searchType"][value="ALL"]');
            if (allSearchRadio) {
                allSearchRadio.checked = true;
            }

            // select box 비활성화 및 값 초기화
            const configKeySearch = document.getElementById('configKeySearch');
            if (configKeySearch) {
                configKeySearch.disabled = true;
                configKeySearch.value = '';
            }

            // 리셋 타입 초기화
            const resetType = document.getElementById('resetType');
            if (resetType) {
                resetType.selectedIndex = 0;
            }

            // Trigger Message 관련 초기화
            const triggerMessage = document.getElementById('triggerMessage');
            const connectorId = document.getElementById('connectorId');
            if (triggerMessage) {
                triggerMessage.selectedIndex = 0;
            }
            if (connectorId) {
                connectorId.selectedIndex = 0;
            }

            // OCPP 설정 값 제어 관련 초기화
            const configKey = document.getElementById('configKey');
            const configValue = document.getElementById('configValue');
            if (configKey) {
                configKey.selectedIndex = 0;
            }
            if (configValue) {
                configValue.value = '';
            }

            console.log("Selected Row Data:", rowData);
        });
    });

    // OCPP 설정값 로드
    function loadOcppConfigurationValues(chargerId) {
        if (!chargerId) {
            alert('충전기가 선택되지 않았습니다.');
            return;
        }

        // 선택된 조회 타입 가져오기 (전체/개별)
        const searchType = document.querySelector('input[name="searchType"]:checked').value;
        // select 박스에서 선택된 key 값 가져오기
        const selectedKey = document.getElementById('configKeySearch').value;

        console.log("searchType:", searchType);
        console.log("selectedKey:", selectedKey);

        const reqParams = {
            chargerId: chargerId,
            searchOption: searchType,  // 'ALL' 또는 'INDIVIDUAL'
            key: searchType === 'INDIVIDUAL' ? selectedKey : null  // 개별조회일 때만 key 값 전송, 전체조회는 null
        };

        fetch('/control/getConfiguration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reqParams)
        })
            .then(response => response.json())
            .then(data => {
                const tableBody = document.getElementById('configurationTable');
                tableBody.innerHTML = '';

                // configurationKey 배열에 접근하여 순회
                data.configurationKey.forEach(config => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${config.key}</td>
                    <td>${config.value}</td>
                    <td>${config.readonly ? 'Yes' : 'No'}</td>
                    `;
                    tableBody.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Error loading OCPP configuration values:', error);
            });
    }

    // OCPP 설정값 조회 버튼 클릭 이벤트 처리
    const searchOcppConfigBtn = document.getElementById('searchOcppConfigBtn');
    if (searchOcppConfigBtn) {
        searchOcppConfigBtn.addEventListener('click', function (event) {
            event.preventDefault();

            if (!selectedChargerId) {
                alert('충전기가 선택되지 않았습니다.');
                return;
            }

            loadOcppConfigurationValues(selectedChargerId);
        });
    }

    // 라디오 버튼 변경 이벤트 처리 - 개별조회 선택 시 select 박스 활성화/비활성화
    document.querySelectorAll('input[name="searchType"]').forEach(radio => {
        radio.addEventListener('change', function () {
            const configKeySearch = document.getElementById('configKeySearch');
            configKeySearch.disabled = this.value === 'ALL';
        });
    });

    // 초기 로드 시 전체조회가 선택되어 있으면 select 박스 비활성화
    document.addEventListener('DOMContentLoaded', function () {
        const searchType = document.querySelector('input[name="searchType"]:checked').value;
        const configKeySearch = document.getElementById('configKeySearch');
        configKeySearch.disabled = searchType === 'ALL';
    });

    //ChangeConfiguration 전송버튼 클릭 이벤트처리
    const sendConfigBtn = document.getElementById('sendConfigBtn');
    if (sendConfigBtn) {
        sendConfigBtn.addEventListener('click', function (e) {
            e.preventDefault();

            // 충전기가 선택되지 않은 경우
            if (!selectedChargerId) {
                alert('충전기가 선택되지 않았습니다.');
                return;
            }

            // 설정 키 가져오기
            const configKey = document.getElementById('configKey').value;
            selectedConfigKey = configKey;

            // 설정 값 가져오기
            const configValue = document.getElementById('configValue').value;
            selectedConfigValue = configValue;

            if (confirm(`선택한 충전기(${selectedChargerId})에 Key: ${selectedConfigKey} Value: ${selectedConfigValue} 설정값을 전송하시겠습니까?`)) {
                const configData = {
                    chargerId: selectedChargerId,
                    key: selectedConfigKey,
                    value: selectedConfigValue
                };

                fetch('/control/changeConfiguration', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(configData)
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'Accepted') {
                            alert('ChangeConfiguration 요청이 성공적으로 처리되었습니다.');
                        } else {
                            alert('ChangeConfiguration 요청 처리 중 오류가 발생했습니다: ' + (data.message || '알 수 없는 오류'));
                        }
                    })
                    .catch(error => {
                        console.error('ChangeConfiguration request failed:', error);
                        alert('ChangeConfiguration 요청 중 오류가 발생했습니다.');
                    });
            }
        })
    }

    // 트리거 메시지 전송 버튼 클릭 이벤트 처리
    const sendTriggerBtn = document.getElementById('sendTriggerBtn');
    if (sendTriggerBtn) {
        sendTriggerBtn.addEventListener('click', function (e) {
            e.preventDefault();

            // 충전기가 선택되지 않은 경우
            if (!selectedChargerId) {
                alert('충전기가 선택되지 않았습니다.');
                return;
            }

            // 트리거 메시지 가져오기
            const triggerMessage = document.getElementById('triggerMessage').value;
            selectedMessage = triggerMessage;

            // 커넥터 ID 가져오기
            const connectorId = document.getElementById('connectorId').value;
            selectedConnectorId = connectorId;


            if (confirm(`선택한 충전기(${selectedChargerId})에 ${selectedMessage} 메시지를 전송하시겠습니까?`)) {
                const triggerData = {
                    chargerId: selectedChargerId,
                    requestedMessage: selectedMessage,
                    connectorId: selectedConnectorId
                };

                fetch('/control/trigger', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(triggerData)
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'Accepted') {
                            alert('트리거 메시지 전송이 성공적으로 처리되었습니다.');
                        } else {
                            alert('트리거 메시지 전송 처리 중 오류가 발생했습니다: ' + (data.message || '알 수 없는 오류'));
                        }
                    })
                    .catch(error => {
                        console.error('Trigger message request failed:', error);
                        alert('트리거 메시지 전송 중 오류가 발생했습니다.');
                    });
            }

        })
    }

    // 리셋 전송 버튼 클릭 이벤트 처리
    const sendResetBtn = document.getElementById('sendResetBtn');
    if (sendResetBtn) {
        sendResetBtn.addEventListener('click', function (e) {
            e.preventDefault();

            // 충전기가 선택되지 않은 경우
            if (!selectedChargerId) {
                alert('충전기가 선택되지 않았습니다.');
                return;
            }

            // 리셋 타입 가져오기
            const resetType = document.getElementById('resetType').value;

            // 확인 대화상자 표시
            if (confirm(`선택한 충전기(${selectedChargerId})를 ${resetType} 리셋하시겠습니까?`)) {
                // 리셋 요청 데이터 생성
                const resetData = {
                    chargerId: selectedChargerId,
                    resetType: resetType
                };

                // API 호출
                fetch('/control/reset', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(resetData)
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'Accepted') {
                            alert('리셋 요청이 성공적으로 처리되었습니다.');
                        } else {
                            alert('리셋 요청 처리 중 오류가 발생했습니다: ' + (data.message || '알 수 없는 오류'));
                        }
                    })
                    .catch(error => {
                        console.error('Reset request failed:', error);
                        alert('리셋 요청 중 오류가 발생했습니다.');
                    });
            }
        });
    }
});