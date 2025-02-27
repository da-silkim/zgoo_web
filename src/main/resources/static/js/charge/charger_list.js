document.addEventListener("DOMContentLoaded", function () {

    let isModalReg = false;
    let dupCheckModemNum = false;
    let dupCheckMModemSn = false;
    /**
     * 검색조건 처리
     */
    // 검색버튼 클릭 이벤트 처리
    document.getElementById("cpSearchBtn").addEventListener("click", function () {
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

    // 검색조건 초기화
    document.getElementById("resetBtn").addEventListener("click", function () {
        document.getElementById("companyIdSearch").selectedIndex = 0;
        document.getElementById("opSearch").selectedIndex = 0;
        document.getElementById("contentSearch").value = "";
        document.getElementById("manfCodeSearch").selectedIndex = 0;
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
        $('modalBtn').text(btnMsg);

    });

    /**
     * 메인 - '수정' 버튼 클릭시 모달버튼 문구 처리부
     */
    document.getElementById('editBtn').addEventListener('click', function () {
        isModalReg = false;
        btnMsg = "수정";
        $('modalBtn').text(btnMsg);
    });

    /**
     * 모달 - '등록/수정' 버튼 클릭에 대한 이벤트 처리리
     */
    document.getElementById('modalBtn').addEventListener('click', function () {

        //중복체크 요소 안했을 경우 return
        if (!dupCheckMModemSn) {
            alert('모뎀시리얼번호 중복체크 확인 필요.');
            return;
        }

        if (!dupCheckModemNum) {
            alert('모뎀번호 중복체크 확인 필요.');
            return;
        }

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

        const m_url = isModalReg ? '/charger/new' : '/charger/update';
        const m_method = isModalReg ? 'POST' : 'PATCH';
        fetch(m_url, {
            method: m_method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bodyData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(isModalReg ? "충전기 등록 실패: " + response.statusText : "충전기 수정 실패: " + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                console.log("처리결과:", data);
                window.location.replace('/charger/list');
            })
            .catch(error => {
                if (planModaIcon == false) console.error("충전기 등록 실패:", error);
                else console.error("충전기정보 수정 실패:", error);
            });
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
});