document.addEventListener("DOMContentLoaded", function () {

    let selectedRow; // 선택한 행, 열 저장
    let isModalReg = false;
    let dupCheckModemNum = false;
    let dupCheckMModemSn = false;
    /**
     * 검색조건 처리
     */
    // 검색버튼 클릭 이벤트 처리
    document.getElementById("cpSearchBtn").addEventListener("click", function () {
        // 폼 제출 (size 값은 이미 hidden input에 있음)
        document.getElementById('searchForm').submit();
    });

    // 초기화 버튼 클릭 이벤트 처리
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 모든 입력 필드 초기화
        document.getElementById('companyIdSearch').value = '';
        document.getElementById('manfCodeSearch').value = '';
        document.getElementById('opSearch').value = '';
        document.getElementById('contentSearch').value = '';

        // 폼 제출 (초기화된 상태로)
        document.getElementById('searchForm').submit();
    });

    //테이블 row 클릭시 처리 이벤트
    document.querySelectorAll('#pageList tr').forEach(row => {
        row.addEventListener('click', function () {
            selectedRow = row;
            console.log('selected row data:', selectedRow);
        });
    });


    /**
     * 모달(등록) - 충전소ID 검색 버튼 이벤트처리
     */
    document.getElementById("btnModalSearchCs").addEventListener('click', function (event) {

        event.preventDefault();     //기본동작 막기

        //입력된 ㅊ우전소 ID가져오기
        const stationId = document.getElementById('modalCsid').value;

        if (!stationId) {
            alert('충전소ID를 입력해주세요.');
            return;
        }

        fetchStationName(stationId);

        fetchChargerId(stationId);

    });

    function fetchChargerId(stationId) {
        return fetch(`/charger/get/create/cpid?stationId=${encodeURIComponent(stationId)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('충전기ID 정보를 가져오지 못했습니다.');
                }
                return response.json();
            })
            .then(data => {
                // console.log("staionId 조회결과: ", data);
                document.getElementById('modalCpID').value = data.cpid || '';
            })
            .catch(error => {
                console.error('Error:', error);
                alert('충전기ID 정보를 가져오는데 실패했습니다.');
            });
    }

    function fetchStationName(stationId) {
        return fetch(`/station/list/get/${stationId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('충전소 정보를 가져오지 못했습니다.');
                }
                return response.json();
            })
            .then(data => {
                // console.log("staionId 조회결과: ", data);
                document.getElementById('modalCsname').value = data.stationName || '';
            })
            .catch(error => {
                console.error('Error:', error);
                alert('충전소 정보가 존재하지 않습니다.');
            });
    }

    function initModalModelInfo() {
        document.getElementById('modalCpType').value = '';
        document.getElementById('modalCpManfCd').value = '';
        document.getElementById('modalCpKw').value = '';
        document.getElementById('modalDualyn').value = '';
    }

    function initModalForm() {
        // 충전소 정보 초기화
        document.getElementById("modalCompanyId").value = "";
        document.getElementById("modalCsid").value = "";
        document.getElementById("modalCsname").value = "";

        // 충전기 정보 초기화
        document.getElementById("modalCpName").value = "";
        document.getElementById("modalCpID").value = "";

        // 충전기 모델 관련 초기화
        const modelSelect = document.getElementById("modalCpModelCd");
        modelSelect.innerHTML = '<option value="%">(선택)</option>';
        document.getElementById("modalCpType").value = "";
        document.getElementById("modalCpManfCd").value = "";
        document.getElementById("modalCpKw").value = "";

        // 충전기 상세 정보 초기화
        document.getElementById("modalCpSn").value = "";
        document.getElementById("modalCpFwver").value = "";
        document.getElementById("modalAnydeskId").value = "";
        document.getElementById("modalCommonType").value = "";
        document.getElementById("installDate").value = "";

        // 요금제 초기화
        const planSelect = document.getElementById("modalPlan");
        planSelect.innerHTML = '<option value="%">(선택)</option>';

        // 사용 여부 및 위치 정보 초기화
        document.getElementById("modalUseYn").value = "Y";
        document.getElementById("modalReason").value = "";
        document.getElementById("cpLocation").value = "";
        document.getElementById("modalDualyn").value = "";

        // 모뎀 정보 초기화
        document.getElementById("modemSn").value = "";
        document.getElementById("modemNum").value = "";
        document.getElementById("fromDate").value = "";
        document.getElementById("toDate").value = "";
        document.getElementById("modemPlan").value = "";
        document.getElementById("modemDataCap").value = "";
        document.getElementById("modemCorp").value = "";
        document.getElementById("modemModelNm").value = "";
        document.getElementById("modemContStat").value = "";

        dupCheckModemNum = false;
        dupCheckMModemSn = false;
    }

    /**
     * 모달(등록) - 사업자ID에 대한 모델LIst get
     */
    function fetchChargeModelData(companyId, chargeModelSelect) {
        return fetch(`/system/model/get/modal/list/${companyId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 404) {
                        // NOT_FOUND 응답일 경우 기본 옵션만 표시
                        console.warn('충전기 모델: 해당 사업자의 모델 정보가 없습니다.');
                        chargeModelSelect.innerHTML = '<option value="%">(선택)</option>'; // 기존 옵션 초기화
                        initModalModelInfo();
                        return null;
                    }
                    throw new Error('서버 오류가 발생했습니다.');
                }
                return response.json();
            })
            .catch(error => {
                console.error('Error fetching charge model data:', error);
                chargeModelSelect.innerHTML = '<option value="%">(선택)</option>'; // 오류 발생 시 기본 옵션으로 초기화
                initModalModelInfo();
                return null;
            });
    }

    /**
     * 모달(등록) - 사업자ID에 대한 요금제 List get
     */
    function fetchChargerPlanData(companyId, chargerPlanSelect) {
        return fetch(`/system/tariff/get/planInfo/${companyId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 404) {
                        // NOT_FOUND 응답일 경우 기본 옵션만 표시
                        console.warn('요금제:등록된 요금제가 없습니다.');
                        chargerPlanSelect.innerHTML = '<option value="%">(선택)</option>'; // 기존 옵션 초기화
                        return null;
                    }
                    throw new Error('서버 오류가 발생했습니다.');
                }
                return response.json();
            })
            .catch(error => {
                console.error('Error fetching charge plan data:', error);
                chargerPlanSelect.innerHTML = '<option value="%">(선택)</option>'; // 오류 발생 시 기본 옵션으로 초기화
                return null;
            });
    }

    /**
     * 모달(등록) - 사업자 선택에 따른 이벤트 처리
     */
    document.getElementById('modalCompanyId').addEventListener('change', function () {
        const selectedCompanyId = this.value;
        console.log("selected companyId:", selectedCompanyId);

        const chargeModelSelect = document.getElementById('modalCpModelCd'); // 대상 select 요소
        const chargerPlanSelect = document.getElementById('modalPlan');

        if (!selectedCompanyId) {
            chargeModelSelect.innerHTML = '<option value="%">(선택)</option>'; // 기본 옵션으로 초기화
            chargerPlanSelect.innerHTML = '<option value="%">(선택)</option>'; // 기본 옵션으로 초기화
            initModalModelInfo();
            return;
        }

        // Fetch 데이터 가져오기(model list)
        fetchChargeModelData(selectedCompanyId, chargeModelSelect).then(data => {
            if (!data) return;

            console.log('모델정보:', data);
            chargeModelSelect.innerHTML = '<option value="%">(선택)</option>'; // 기존 옵션 초기화
            initModalModelInfo();

            // 리스트 데이터를 기반으로 옵션 추가
            data.forEach(model => {
                const option = document.createElement('option');
                option.value = model.modelCode; // modelId를 value로 설정
                option.textContent = model.modelName; // modelName을 표시
                chargeModelSelect.appendChild(option);
            });
        });

        // Fetch 데이터 가져오기(plan list)
        fetchChargerPlanData(selectedCompanyId, chargerPlanSelect).then(data => {
            if (!data) return;

            console.log('요금제정보:', data);
            chargerPlanSelect.innerHTML = '<option value="%">(선택)</option>'; // 기존 옵션 초기화

            // 리스트 데이터를 기반으로 옵션 추가
            data.forEach(plan => {
                const option = document.createElement('option');
                option.value = plan.policyId; // modelId를 value로 설정
                option.textContent = plan.planName; // modelName을 표시
                chargerPlanSelect.appendChild(option);
            });
        });

    });

    /**
     * 모달(등록) - 충전기 모델 선택에 따른 모델정보 부여(등록폼)
     */
    document.getElementById('modalCpModelCd').addEventListener('change', function () {
        const selectedModelCd = this.value;
        console.log("selected modelId:", selectedModelCd);

        if (!selectedModelCd) {
            return;
        }

        fetch(`/system/model/get/modal/one/${selectedModelCd}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('모델 정보를 가져오지 못했습니다.');
                }
                return response.json();
            })
            .then(data => {
                console.log("modelID 조회결과: ", data);
                document.getElementById('modalCpType').value = data.cpTypeName || '';
                document.getElementById('modalCpManfCd').value = data.manufCdName || '';
                document.getElementById('modalCpKw').value = data.powerUnit || '';
                document.getElementById('modalDualyn').value = data.dualYn || '';
            })
            .catch(error => {
                console.error('Error:', error);
                alert('모델 정보가 존재하지 않습니다.');
                initModalModelInfo();
            });

    });

    /**
     * 모달(등록) - 모뎀 SerialNumber 중복체크
     */
    document.getElementById('modemSnDupCheckBtn').addEventListener('click', function () {
        var serialNum = document.getElementById('modemSn').value.trim();

        if (serialNum === '') {
            alert('모뎀 시리얼번호를 입력해주세요');
            return;
        }

        fetch(`/charger/modem/serialnum/dupcheck?serialNum=${encodeURIComponent(serialNum)}`, {
            method: 'GET',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(isDuplicate => {
                if (isDuplicate) {
                    alert('이미 등록된 시리얼번호 입니다.');
                    dupCheckMModemSn = false;
                } else {
                    alert('등록록 가능한 시리얼번호 입니다다.');
                    dupCheckMModemSn = true;
                }
            })
            .catch(error => {
                alert('오류: 모뎀 시리얼번호 중복체크');
                console.error('Error:', error);
            });

    });

    /**
     * 모달(등록) - 모뎀번호 중복체크
     */
    document.getElementById('modemNumDupCheckBtn').addEventListener('click', function () {
        var modemNum = document.getElementById('modemNum').value.trim().replace(/-/g, '');

        if (modemNum === '') {
            alert('모뎀 전화번호를 입력하세요.');
            return;
        }

        fetch(`/charger/modem/modemNum/dupcheck?modemNum=${encodeURIComponent(modemNum)}`, {
            method: 'GET',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(isDuplicate => {
                if (isDuplicate) {
                    alert('이미 등록된 모뎀번호 입니다.');
                    dupCheckModemNum = false;
                } else {
                    alert('등록록 가능한 모뎀뎀번호 입니다다.');
                    dupCheckModemNum = true;
                }
            })
            .catch(error => {
                alert('오류: 모뎀번호 중복체크');
                console.error('Error:', error);
            });
    });

    /**
     * 메인 - '등록' 버튼 클릭시 모달버튼 문구 처리부
     */
    document.getElementById('addBtn').addEventListener('click', function () {
        isModalReg = true;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        initModalForm();

    });

    function registerCharger() {
        //등록로직
        //폼 요소 가져오기
        const bodyData = {
            companyId: document.getElementById('modalCompanyId').value,
            stationId: document.getElementById('modalCsid').value,
            chargerName: document.getElementById('modalCpName').value,
            chargerId: document.getElementById('modalCpID').value,
            modelCode: document.getElementById('modalCpModelCd').value,
            chgSerialNo: document.getElementById('modalCpSn').value,
            fwversion: document.getElementById('modalCpFwver').value,
            anydeskId: document.getElementById('modalAnydeskId').value,
            commonType: document.getElementById('modalCommonType').value,
            installDate: document.getElementById('installDate').value,
            pricePolicyId: document.getElementById('modalPlan').value,
            useYn: document.getElementById('modalUseYn').value,
            reason: document.getElementById('modalReason').value,
            location: document.getElementById('cpLocation').value,
            modemSerialNo: document.getElementById('modemSn').value,
            modemNo: document.getElementById('modemNum').value,
            modemContractStart: document.getElementById('fromDate').value,
            modemContractEnd: document.getElementById('toDate').value,
            modemPricePlan: document.getElementById('modemPlan').value,
            modemdataCapacity: document.getElementById('modemDataCap').value,
            modemTelCorp: document.getElementById('modemCorp').value,
            modemModelName: document.getElementById('modemModelNm').value,
            modemContractStatus: document.getElementById('modemContStat').value
        };
        console.log("입력 data: ", bodyData);

        const m_url = '/charger/new';
        const m_method = 'POST';
        fetch(m_url, {
            method: m_method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bodyData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("충전기 등록 실패: " + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                console.log("처리결과:", data);
                window.location.replace('/charger/list');
            })
            .catch(error => {
                console.error("충전기 등록 실패:", error);
            });
    }

    // 충전기정보 수정함수
    function updateCharger() {
        //폼데이터 수집
        //폼 요소 가져오기
        const bodyData = {
            companyId: document.getElementById('modalCompanyId').value,
            stationId: document.getElementById('modalCsid').value,
            chargerName: document.getElementById('modalCpName').value,
            chargerId: document.getElementById('modalCpID').value,
            modelCode: document.getElementById('modalCpModelCd').value,
            chgSerialNo: document.getElementById('modalCpSn').value,
            fwversion: document.getElementById('modalCpFwver').value,
            anydeskId: document.getElementById('modalAnydeskId').value,
            commonType: document.getElementById('modalCommonType').value,
            installDate: document.getElementById('installDate').value,
            pricePolicyId: document.getElementById('modalPlan').value,
            useYn: document.getElementById('modalUseYn').value,
            reason: document.getElementById('modalReason').value,
            location: document.getElementById('cpLocation').value,
            modemSerialNo: document.getElementById('modemSn').value,
            modemNo: document.getElementById('modemNum').value,
            modemContractStart: document.getElementById('fromDate').value,
            modemContractEnd: document.getElementById('toDate').value,
            modemPricePlan: document.getElementById('modemPlan').value,
            modemdataCapacity: document.getElementById('modemDataCap').value,
            modemTelCorp: document.getElementById('modemCorp').value,
            modemModelName: document.getElementById('modemModelNm').value,
            modemContractStatus: document.getElementById('modemContStat').value
        };

        // 필수 필드 검증
        if (!bodyData.chargerName || !bodyData.stationId) {
            alert("충전기 이름과 충전소 ID는 필수 입력 항목입니다.");
            return;
        }

        // 서버에 수정 요청 보내기
        fetch('/charger/update', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bodyData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.message || "충전기 정보 수정에 실패했습니다.");
                    });
                }
                return response.json();
            })
            .then(data => {
                alert(data.message || "충전기 정보가 성공적으로 수정되었습니다.");
                window.location.replace('/charger/list');
            })
            .catch(error => {
                console.error("충전기 정보 수정 실패:", error);
                alert(error.message || "충전기 정보 수정 중 오류가 발생했습니다.");
            });
    }


    /**
     * 메인 - '수정' 버튼 클릭시 모달버튼 문구 처리부
     */
    document.getElementById('editBtn').addEventListener('click', function () {
        isModalReg = false;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);

        if (selectedRow) {
            const selectedChargerId = selectedRow.cells[5].innerText;
            console.log("[EDIT] selected ID : ", selectedChargerId);

            // 선택된 충전기 정보 가져오기
            fetch(`/charger/list/search/${selectedChargerId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("충전기 정보 조회 실패: " + response.statusText);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("충전기 정보 조회 결과:", data);

                    // 회사 ID 설정
                    const companyId = data.companyId || "";
                    document.getElementById("modalCompanyId").value = companyId;
                    // 모달 폼에 데이터 채우기
                    document.getElementById("modalCsid").value = data.stationId || "";
                    document.getElementById("modalCsname").value = data.stationName || "";

                    document.getElementById("modalCpName").value = data.chargerName || "";
                    document.getElementById("modalCpID").value = data.chargerId || "";
                    document.getElementById("modalCpModelCd").value = data.modelCode || "";
                    document.getElementById("modalCpType").value = data.cpType || "";
                    document.getElementById("modalCpManfCd").value = data.manufCdName || "";
                    document.getElementById("modalCpKw").value = data.chgKw || "";
                    document.getElementById("modalCpSn").value = data.chgSerialNo || "";
                    document.getElementById("modalCpFwver").value = data.fwversion || "";
                    document.getElementById("modalAnydeskId").value = data.anydeskId || "";
                    document.getElementById("modalCommonType").value = data.commonType || "";
                    document.getElementById("installDate").value = data.installDate ? formatDate(data.installDate) : "";
                    document.getElementById("modalPlan").value = data.pricePolicyId || "";
                    document.getElementById("modalUseYn").value = data.useYn || "";
                    document.getElementById("modalReason").value = data.reason || "";
                    document.getElementById("cpLocation").value = data.location || "";

                    document.getElementById("modalDualyn").value = data.dualYn || "";
                    document.getElementById("modemSn").value = data.modemSerialNo || "";
                    document.getElementById("modemNum").value = data.modemNo || "";
                    document.getElementById("fromDate").value = data.modemContractStart ? formatDate(data.modemContractStart) : "";
                    document.getElementById("toDate").value = data.modemContractEnd ? formatDate(data.modemContractEnd) : "";
                    document.getElementById("modemPlan").value = data.modemPricePlan || "";
                    document.getElementById("modemDataCap").value = data.modemdataCapacity || "";
                    document.getElementById("modemCorp").value = data.modemTelCorp || "";
                    document.getElementById("modemModelNm").value = data.modemModelName || "";
                    document.getElementById("modemContStat").value = data.modemContractStatus || "";

                    if (companyId) {
                        const chargeModelSelect = document.getElementById('modalCpModelCd');
                        const chargerPlanSelect = document.getElementById('modalPlan');

                        // 모델 리스트 로드
                        return fetchChargeModelData(companyId, chargeModelSelect)
                            .then(modelData => {
                                if (modelData) {
                                    console.log('모델정보:', modelData);
                                    chargeModelSelect.innerHTML = '<option value="%">(선택)</option>'; // 기존 옵션 초기화
                                    initModalModelInfo();

                                    // 리스트 데이터를 기반으로 옵션 추가
                                    modelData.forEach(model => {
                                        const option = document.createElement('option');
                                        option.value = model.modelCode;
                                        option.textContent = model.modelName;
                                        chargeModelSelect.appendChild(option);
                                    });

                                    // 모델 코드 설정
                                    if (data.modelCode) {
                                        chargeModelSelect.value = data.modelCode;

                                        // 해당 값이 없는 경우 새 옵션 추가
                                        if (chargeModelSelect.value !== data.modelCode) {
                                            const option = document.createElement('option');
                                            option.value = data.modelCode;
                                            option.textContent = data.modelCode;
                                            chargeModelSelect.appendChild(option);
                                            chargeModelSelect.value = data.modelCode;
                                        }

                                        // 모델 정보 업데이트 (모델 선택 시 실행되는 이벤트 트리거)
                                        const event = new Event('change');
                                        chargeModelSelect.dispatchEvent(event);
                                    }

                                    // 요금제 리스트 로드
                                    return fetchChargerPlanData(companyId, chargerPlanSelect);
                                }
                            })
                            .then(planData => {
                                if (planData) {
                                    console.log('요금제정보:', planData);
                                    chargerPlanSelect.innerHTML = '<option value="%">(선택)</option>'; // 기존 옵션 초기화

                                    // 리스트 데이터를 기반으로 옵션 추가
                                    planData.forEach(plan => {
                                        const option = document.createElement('option');
                                        option.value = plan.policyId;
                                        option.textContent = plan.planName;
                                        chargerPlanSelect.appendChild(option);
                                    });

                                    // 요금제 ID 설정
                                    if (data.pricePolicyId) {
                                        chargerPlanSelect.value = data.pricePolicyId;

                                        // 해당 값이 없는 경우 새 옵션 추가
                                        if (chargerPlanSelect.value != data.pricePolicyId) {
                                            const option = document.createElement('option');
                                            option.value = data.pricePolicyId;
                                            option.textContent = "요금제 " + data.pricePolicyId;
                                            chargerPlanSelect.appendChild(option);
                                            chargerPlanSelect.value = data.pricePolicyId;
                                        }
                                    }
                                }
                            });
                    } else {
                        return null;
                    }
                })
                .catch(error => {
                    console.error("충전기 정보 조회 실패:", error);
                    alert("충전기 정보를 불러오는데 실패했습니다.");
                });
        } else {
            alert("수정할 충전기를 선택해주세요.");
        }
    });

    // 날짜 포맷 함수
    function formatDate(dateString) {
        if (!dateString) return '';

        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
    }

    /**
     * 모달 - '등록/수정' 버튼 클릭에 대한 이벤트 처리리
     */
    document.getElementById('modalBtn').addEventListener('click', function () {

        //등록/수정 모드 판단
        if (isModalReg) {

            //등록로직
            registerCharger();
            if (!dupCheckMModemSn) {
                alert('모뎀시리얼번호 중복체크 확인 필요.');
                return;
            }
            if (!dupCheckModemNum) {
                alert('모뎀번호 중복체크 확인 필요.');
                return;
            }
        } else {
            //수정로직
            updateCharger();
        }
    });

    /**
     * 충전기 사용여부에 따른 사유 enable/disable 처리
     */
    document.getElementById('modalUseYn').addEventListener('change', function () {
        const modalReason = document.getElementById('modalReason');

        if (this.value === 'N') {
            modalReason.disabled = false;
        } else {
            modalReason.disabled = true;
            modalReason.selectedIndex = 0; // 첫 번째 옵션 선택
        }
    });

    /**
     * 충전기 정보 삭제
     */
    document.getElementById("deleteBtn").addEventListener("click", function () {
        if (selectedRow) {
            const selectedChargerId = selectedRow.cells[5].innerText;
            console.log("[DELETE] selected ID : ", selectedChargerId);


            fetch(`/charger/delete/${selectedChargerId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("[DELETE]충전기 삭제 실패: " + response.statusText);
                    }
                    return response.text();
                })
                .then(data => {
                    console.log("처리결과:", data);
                    window.location.replace('/charger/list');
                })
                .catch(error => {
                    console.error("충전기 삭제 실패:", error);
                });
        }
    });
});