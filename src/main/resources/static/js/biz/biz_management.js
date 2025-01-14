document.addEventListener("DOMContentLoaded", function () {
    let companySelectedRow; // 선택한 행, 열 저장
    let companyModalCon = false;
    let selectedCompanyId;

    // '검색' 버튼 클릭 이벤트 처리
    document.getElementById("companySearchBtn").addEventListener("click", function () {

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


    // '초기화' 버튼 클릭 이벤트 처리
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 폼 필드 초기화
        document.getElementById("companyIdSearch").selectedIndex = 0; // 사업자명 초기화 (첫 번째 옵션)
        document.getElementById("companyTypeSearch").selectedIndex = 0; // 사업자유형 초기화 (첫 번째 옵션)
        document.getElementById("companyLvSearch").selectedIndex = 0; // 사업자레벨 초기화 (첫 번째 옵션)

        // 로그 출력 (디버깅용)
        console.log("폼 필드가 초기화되었습니다.");
    });

    // pagination
    $('#size').on('change', function () {
        // 현재 URL에서 쿼리 파라미터 추출
        const urlParams = new URLSearchParams(window.location.search);
        const selectedSize = document.getElementById("size").value;
        const selectedCompanyId = urlParams.get('companyIdSearch') || '';
        const selectedCompanyType = urlParams.get('companyTypeSearch') || '';
        const selectedName = urlParams.get('companyLvSearch') || '';

        window.location.href = "/biz/list?page=0&size=" + selectedSize +
            "&companyIdSearch=" + (selectedCompanyId) +
            "&companyTypeSearch=" + encodeURIComponent(selectedCompanyType) +
            "&companyLvSearch=" + encodeURIComponent(selectedName);
    });

    // 등록 모달 열릴 떄 이벤트 설정
    $(`#dataAddModal`).on(`show.bs.modal`, function () {
        consignmentOptionCheck();
    })

    // 사업자 레벨 선택에 따른 상위업체 옵션 처리 함수
    document.getElementById("companyLv").addEventListener("change", updateParentCompanyOptions);

    function updateParentCompanyOptions() {
        const selectedCompanyLv = document.getElementById("companyLv").value;
        const parentCompanySelect = document.getElementById("parentCompanyOptions");

        console.log("업체리스트", companyList);
        console.log("선택된 업체레벨", selectedCompanyLv);

        //기존 옵션 초기화
        parentCompanySelect.innerHTML = '<option value="">없음</option>';

        if (selectedCompanyLv === "MID") {
            companyList.filter(company => company.companyLv === "TOP")
                .forEach(company => {
                    const option = document.createElement("option");
                    option.value = company.companyName;
                    option.textContent = company.companyName;
                    parentCompanySelect.appendChild(option);
                });
        } else if (selectedCompanyLv === "LOW") {
            companyList.filter(company => company.companyLv === "MID")
                .forEach(company => {
                    const option = document.createElement("option");
                    option.value = company.companyName;
                    option.textContent = company.companyName;
                    parentCompanySelect.appendChild(option);
                })
        }

    }



    // PG결제정보 자체/위탁 옵션에 따른 활성/비활성화 처리 함수
    document.getElementById("consignmentState").addEventListener("change", consignmentOptionCheck);
    function consignmentOptionCheck() {
        const selectedOption = document.getElementById("consignmentState").value;

        console.log("결제위탁: ", selectedOption);

        const f_mallid = document.getElementById("mallID");
        const f_mid = document.getElementById("mID");
        const f_merchantkey = document.getElementById("merchantKey");

        //자체 : S, 위탁 : C
        if (selectedOption === "S") {
            //활성화
            f_mallid.disabled = false;
            f_mid.disabled = false;
            f_merchantkey.disabled = false;

        } else {
            //비활성화
            f_mallid.disabled = true;
            f_mid.disabled = true;
            f_merchantkey.disabled = true;
        }
    }

    // 업체등록 - 로밍정보 리스트 가져오는 함수
    function getRoamingInfoList() {
        const roamingRows = document.querySelectorAll("#roamingTable .roaming-row"); // 최신 데이터 조회
        const roamingInfoList = [];

        // 각 행에서 institutionCode, institutionKey, institutionEmail 값 추출
        roamingRows.forEach(row => {
            const inscode = row.querySelector(".institutionCode").value;
            const inskey = row.querySelector(".institutionKey").value;
            const insemail = row.querySelector(".institutionEmail").value;

            // 각 행의 데이터를 객체로 추가
            roamingInfoList.push({
                institutionCode: inscode,
                institutionKey: inskey,
                institutionEmail: insemail
            });
        });

        console.log("로밍정보:", roamingInfoList);
        return roamingInfoList;
    }

    //테이블 row 클릭시 처리 이벤트
    document.querySelectorAll('#pageList tr').forEach(row => {
        row.addEventListener('click', function () {
            companySelectedRow = row;
        })
    });

    // 삭제버튼 클릭 이벤트 처리
    document.getElementById("deleteBtn").addEventListener("click", function () {
        if (companySelectedRow) {
            selectedCompanyId = companySelectedRow.cells[1].innerText;
            console.log("[DELETE] selected ID : ", selectedCompanyId);


            fetch(`/biz_management/biz/delete/${selectedCompanyId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("[DELETE]업제삭제 실패: " + response.statusText);
                    }
                    return response.text();
                })
                .then(data => {
                    console.log("처리결과:", data);
                    window.location.replace('/biz/list');
                })
                .catch(error => {
                    console.error("업체정보 삭제 실패:", error);
                });
        }
    });

    // 수정버튼 클릭 이벤트 처리
    document.getElementById("editBtn").addEventListener("click", function () {
        companyModalCon = true;
        btnMsg = "수정";
        $('#companyModalBtn').text(btnMsg);

        if (companySelectedRow) {
            selectedCompanyId = companySelectedRow.cells[1].innerText;
            console.log("[UPDATE] selected company ID:", selectedCompanyId);

            fetch(`/biz_management/biz/search/${selectedCompanyId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("[UPDATE]업체정보 조회 실패: " + response.statusText);
                    }
                    return response.json();
                })
                .then(data => {
                    // 서버 응답 후 처리 (예: 검색 결과 출력)
                    console.log("검색 결과:", data);

                    // document.getElementById("companyType").value = data.companyType ? data.companyType : "";
                    document.getElementById("companyType").value = data.companyType ? data.companyType : "";
                    document.getElementById("companyLv").value = data.companyLv ? data.companyLv : "";
                    document.getElementById("companyName").value = data.companyName ? data.companyName : "";
                    document.getElementById("bizNum").value = data.bizNum ? data.bizNum : "";
                    document.getElementById("ceoName").value = data.ceoName ? data.ceoName : "";
                    document.getElementById("headPhone").value = data.headPhone ? data.headPhone : "";
                    document.getElementById("bizType").value = data.bizType ? data.bizType : "";
                    document.getElementById("bizKind").value = data.bizKind ? data.bizKind : "";
                    document.getElementById("zipCode").value = data.zipcode ? data.zipcode : "";
                    document.getElementById("address").value = data.address ? data.address : "";
                    document.getElementById("addressDetail").value = data.addressDetail ? data.addressDetail : "";

                    updateParentCompanyOptions();
                    document.getElementById("parentCompanyOptions").value = data.parentCompanyName ? data.parentCompanyName : "";

                    document.getElementById("staffName").value = data.staffName ? data.staffName : "";
                    document.getElementById("staffEmail").value = data.staffEmail ? data.staffEmail : "";
                    document.getElementById("staffTel").value = data.staffTel ? data.staffTel : "";
                    document.getElementById("staffPhone").value = data.staffPhone ? data.staffPhone : "";

                    const tableBody = document.querySelector("#roamingTable tbody");
                    tableBody.innerHTML = "";  // 기존 내용 초기화

                    data.romaing.forEach(roaming => {
                        const row = document.createElement("tr");
                        row.classList.add("roaming-row");

                        row.innerHTML = `
                        <td><input type="checkbox"/></td>
                        <td><input type="text" class="input-add-row institutionCode" value="${roaming.institutionCode ? roaming.institutionCode : ''}"/></td>
                        <td><input type="text" class="input-add-row institutionKey" value="${roaming.institutionKey ? roaming.institutionKey : ''}"/></td>
                        <td><input type="text" class="input-add-row institutionEmail" value="${roaming.institutionEmail ? roaming.institutionEmail : ''}"/></td>
                    `;

                        tableBody.appendChild(row);
                    });

                    document.getElementById("consignmentState").value = data.consignmentPayment ? data.consignmentPayment : "";
                    consignmentOptionCheck();
                    document.getElementById("mID").value = data.mid ? data.mid : "";
                    document.getElementById("merchantKey").value = data.merchantKey ? data.merchantKey : "";
                    document.getElementById("mallID").value = data.sspMallId ? data.sspMallId : "";
                    document.getElementById("contractState").value = data.contractStatus ? data.contractStatus : "";
                    document.getElementById("contractAt").value = data.contractedAt ? formatDateStringToLocalDate(data.contractedAt) : "";
                    document.getElementById("contractStart").value = data.contractStart ? formatDateStringToLocalDate(data.contractStart) : "";
                    document.getElementById("contractEnd").value = data.contractEnd ? formatDateStringToLocalDate(data.contractEnd) : "";
                    document.getElementById("asCompany").value = data.asCompany ? data.asCompany : "";
                    document.getElementById("asNum").value = data.asNum ? data.asNum : "";
                })
                .catch(error => {
                    console.error("[UPDATE]업체정보 조회 실패:", error);
                });
        }
    });

    // 메인폼 '등록' 버튼 클릭시 모달 등록버튼 메시지 수정
    document.getElementById("addBtn").addEventListener("click", function () {
        companyModalCon = false;
        btnMsg = "등록";
        $('#companyModalBtn').text(btnMsg);
    });

    // 'Modal' - '등록/수정' 버튼 클릭 이벤트 처리
    document.getElementById("companyModalBtn").addEventListener("click", function () {

        // 폼 요소 가져오기
        const bodyData = {
            companyId: selectedCompanyId,
            companyType: document.getElementById("companyType").value,
            companyLv: document.getElementById("companyLv").value,
            companyName: document.getElementById("companyName").value,
            bizNum: document.getElementById("bizNum").value,
            ceoName: document.getElementById("ceoName").value,
            headPhone: document.getElementById("headPhone").value,
            bizType: document.getElementById("bizType").value,
            bizKind: document.getElementById("bizKind").value,
            zipcode: document.getElementById("zipCode").value,
            address: document.getElementById("address").value,
            addressDetail: document.getElementById("addressDetail").value,
            parentCompanyName: document.getElementById("parentCompanyOptions").value,
            romaing: getRoamingInfoList(),
            staffName: document.getElementById("staffName").value,
            staffEmail: document.getElementById("staffEmail").value,
            staffTel: document.getElementById("staffTel").value,
            staffPhone: document.getElementById("staffPhone").value,
            consignmentPayment: document.getElementById("consignmentState").value,
            sspMallId: document.getElementById("mallID").value,
            mid: document.getElementById("mID").value,
            merchantKey: document.getElementById("merchantKey").value,
            contractStatus: document.getElementById("contractState").value,
            contractedAt: formatToLocalDateTimeString(document.getElementById("contractAt").value),
            contractStart: formatToLocalDateTimeString(document.getElementById("contractStart").value),
            contractEnd: formatToLocalDateTimeString(document.getElementById("contractEnd").value),
            asCompany: document.getElementById("asCompany").value,
            asNum: document.getElementById("asNum").value
        };

        // 선택된 값 콘솔 출력 (디버깅용)
        console.log("폼 data: ", bodyData);

        // 선택된 값으로 필터링을 위한 요청 처리 (예시: AJAX, Fetch API 등)
        // 예를 들어, Fetch API로 서버에 데이터를 전송하는 방법
        const m_url = companyModalCon ? "/biz_management/biz/update" : "/biz_management/biz/new"
        const m_method = companyModalCon ? "PATCH" : "POST";
        fetch(m_url, {
            method: m_method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bodyData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(companyModalCon ? "업체수정 실패: " + response.statusText : "업체등록 실패: " + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                console.log("처리결과:", data);
                window.location.replace('/biz/list');
            })
            .catch(error => {
                if (companyModalCon == false) console.error("업체정보 등록 실패:", error);
                else console.error("업체정보 수정 실패:", error);
            });

    });

});

// 날짜 형식 변환 함수
function formatDate(dateString) {
    if (!dateString) return "";
    const date = new Date(dateString);

    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 날짜 형식을 LocalDateTIme 형식으로 변환
function formatToLocalDateTimeString(date) {
    // Ensure the date is non-empty and valid
    if (!date || date.trim() === "") {
        return null; // Return null if no date is provided
    }
    return date.trim() + "T00:00:00"; // Append time only to valid dates
}

// 문자열 형식의 날짜를 Date 형식으로 변환
function formatDateStringToLocalDate(datestr) {
    const date = new Date(datestr);
    let formattedDate;
    if (!isNaN(date)) {
        formattedDate = date.toISOString().split('T')[0];
    } else {
        console.warn("Wrong date format:", datestr);
        formattedDate = "";
    }

    return formattedDate;
}