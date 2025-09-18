document.addEventListener("DOMContentLoaded", function () {
    let planModaIcon = false;
    let tariffModalIcon = false;
    let planSelectedRow; // 선택한 행, 열 저장
    let selectedTariffId;
    let selected_tinfo_data;



    /*
    * 검색조건 처리=============================================================================
    */


    // // '검색' 버튼 클릭 이벤트 처리
    // document.getElementById("tariffSearchBtn").addEventListener("click", function () {

    //     document.getElementById('searchForm').submit();
    // });


    // // '초기화' 버튼 클릭 이벤트 처리
    // document.getElementById("resetBtn").addEventListener("click", function () {
    //     document.getElementById('companyIdSearch').value = '';
    //     document.getElementById('searchForm').submit();
    // });

    $('#size').on('change', function () {
        updatePageSize(this, "/system/tariff/list", ["companyIdSearch"]);
    });

    /*
    * 요금제(cpplanpolicy) 처리=============================================================================
    */

    function checkboxCheck() {
        let isCheck = false;
        const selectedCheckboxes = document.querySelectorAll('#pageList input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            // 체크된 항목이 하나일 때 
            isCheck = true;

        } else {
            // 체크된 항목이 없거나 두 개 이상일 때 
            isCheck = false;

        }

        return isCheck;
    }

    //테이블 row 클릭시 처리 이벤트
    document.querySelectorAll('#pageList tr').forEach(row => {
        row.addEventListener('click', function () {
            planSelectedRow = row;
            console.log("selected row data:", planSelectedRow)


            if (planSelectedRow) {
                //get tariffinfo
                const tariffId = planSelectedRow.cells[1].innerText;

                fetch(`/system/tariff/tariffinfo/get/${tariffId}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("[GET]Failed to get TariffInfo: " + response.statusText);
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log("TariffInfo result: ", data);
                        //sublist 테이블 구성
                        selected_tinfo_data = data;
                        makeTariffInfoTable(data);
                    })
                    .catch(error => {
                        console.error("Failed get TariffInfo, tariffId:", tariffId, error);
                    });
            }
        });
    });

    function makeTariffInfoTable(data) {
        const tableBody = document.querySelector("#pageListSub");
        tableBody.innerHTML = "";

        if (data.length == 0) {
            const row = document.createElement("tr");

            row.innerHTML = `
            <td colspan="9">${i18n.tariffMgmt.messages.nodata}</td>
        `;

            tableBody.appendChild(row);
        } else {
            data.forEach(tariff => {
                const row = document.createElement("tr");

                row.innerHTML = `
                <td>${tariff.hour}</td>
                <td>${tariff.memSlowUnitCost.toFixed(1)}</td>
                <td>${tariff.nomemSlowUnitCost.toFixed(1)}</td>
                <td>${tariff.memFastUnitCost.toFixed(1)}</td>
                <td>${tariff.nomemFastUnitCost.toFixed(1)}</td>
            `;

                tableBody.appendChild(row);
            });
        }

    }

    function makeTariffInfoModalTable(data) {
        const tableBody = document.querySelector("#modalTariffInfoTable");
        tableBody.innerHTML = "";

        if (data.length == 0) {
            const row = document.createElement("tr");

            row.innerHTML = `
            <td colspan="9">${i18n.tariffMgmt.messages.nodata}</td>
        `;

            tableBody.appendChild(row);
        } else {
            data.forEach(tariff => {
                const row = document.createElement("tr");

                row.innerHTML = `
                <td>${tariff.hour}</td>
                <td><input type="text" value="${tariff.memSlowUnitCost.toFixed(1)}" class="form-control" /></td>
                <td><input type="text" value="${tariff.nomemSlowUnitCost.toFixed(1)}" class="form-control" /></td>
                <td><input type="text" value="${tariff.memFastUnitCost.toFixed(1)}" class="form-control" /></td>
                <td><input type="text" value="${tariff.nomemFastUnitCost.toFixed(1)}" class="form-control" /></td>
            `;

                tableBody.appendChild(row);
            });
        }
    }

    // pagination
    $('#size').on('change', function () {
        // 현재 URL에서 쿼리 파라미터 추출
        const urlParams = new URLSearchParams(window.location.search);
        const selectedSize = document.getElementById("size").value;
        const selectedCompanyId = urlParams.get('companyIdSearch') || '';

        window.location.href = "/system/tariff/list?page=0&size=" + selectedSize +
            "&companyIdSearch=" + (selectedCompanyId);

    });

    //적용상태 구분 옵션 선택에 따른 단가 표시 처리
    document.getElementById("modalTariffStatCode").addEventListener("change", updateTariffInfoTable);

    function updateTariffInfoTable() {
        const selectedStatcode = document.getElementById("modalTariffStatCode").value;
        console.log("stateCode:", selectedStatcode);

        //todo create tariffinfo table >> 
    }

    //요금제 등록/수정시 모달 폼 세팅 함수
    function setPlanModalFormData() {
        let ischeck = checkboxCheck();
        console.log("check status: ", ischeck);

        const applycd = document.getElementById("modalTariffStatCode");
        const modalCompany = document.getElementById("modalCompany");
        const planName = document.getElementById("modalPlanTextbox");
        const applyStart = document.getElementById("applyStartDate");



        if (ischeck && planSelectedRow) {
            //각 엘리먼트 가져오기
            selectedTariffId = planSelectedRow.cells[1].innerText;
            //사업자 정보
            const companyOptions = modalCompany.options;
            let matched = false;
            const companyname = planSelectedRow.cells[2].innerText;


            for (let i = 0; i < companyOptions.length; i++) {
                if (companyOptions[i].text.trim() === companyname) {
                    modalCompany.value = companyOptions[i].value;
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                console.warn("No matching company found in the dropdown for:", companyname);
            }

            // 요금제명
            planName.value = planSelectedRow.cells[3].innerText;

            // 적용상태코드
            const applycodeOptions = applycd.options;
            let appcodeMatched = false;
            const applyCode = planSelectedRow.cells[5].innerText;
            for (let i = 0; i < applycodeOptions.length; i++) {
                if (applycodeOptions[i].text.trim() === applyCode) {
                    applycd.value = applycodeOptions[i].value;
                    appcodeMatched = true;
                    break;
                }
            }

            console.log("appylCode: ", applyCode);

            if (!appcodeMatched) {
                console.warn("No matching applyCode found in the dropdown for:", applyCode);
            }

            if (planModaIcon) {
                //수정일 경우에만 등록된 날짜 가져오기
                const startDateTIme = planSelectedRow.cells[4].innerText;
                const formattedStartDate = formatDate(new Date(startDateTIme));
                applyStart.value = formattedStartDate;
            } else {
                applyStart.value = "";
            }

            //엘리먼트 비활성화
            modalCompany.disabled = true;
            planName.disabled = true;


        } else {
            modalCompany.value = modalCompany.options[0].value;
            applycd.value = applycd.options[0].value;
            planName.value = "";
            applyStart.value = "";
            //엘리먼트 활성화
            modalCompany.disabled = false;
            planName.disabled = false;
            selected_tinfo_data = [];

        }

        // applycd.disabled = planModaIcon ? false : true;
        applycd.disabled = true;

        //tariffInfo=================================
        // 요금제 모달(modalTariffInfoTable) 테이블 row 생성
        console.log("selected tariff_info length: ", selected_tinfo_data.length);

        if (planModaIcon && selected_tinfo_data.length != 0) {
            makeTariffInfoModalTable(selected_tinfo_data);
        } else {
            const modalTariffInfoTableBody = document.getElementById("modalTariffInfoTable");
            modalTariffInfoTableBody.innerHTML = "";
            for (let i = 0; i <= 23; i++) {
                const row = `
                <tr>
                    <td>${i}</td>
                    <td><input type="text" class="form-control" value="200" ></td>
                    <td><input type="text" class="form-control" value="240"></td>
                    <td><input type="text" class="form-control" value="270"></td>
                    <td><input type="text" class="form-control" value="300"></td>
                </tr>
            `;
                modalTariffInfoTableBody.insertAdjacentHTML('beforeend', row);
            }
        }

    }

    //메인폼 요금제 '등록' 버튼 클릭시 모달 등록버튼 메시지 
    document.getElementById("addBtn").addEventListener("click", function () {
        planModaIcon = false;
        btnMsg = i18n.tariffMgmt.buttons.add;
        modaltitle = i18n.tariffMgmt.titles.regPlan;
        $('#planModalBtn').text(btnMsg);
        $('#modalTitle').text(modaltitle);

        setPlanModalFormData();
    });


    //수정버튼 클릭 이벤트 처리
    document.getElementById("editBtn").addEventListener("click", function () {
        planModaIcon = true;
        btnMsg = i18n.tariffMgmt.buttons.edit;
        modaltitle = i18n.tariffMgmt.titles.editPlan;
        $('#planModalBtn').text(btnMsg);
        $('#modalTitle').text(modaltitle);

        setPlanModalFormData();
    });

    // modal > 요금제 등록버튼 클릭시 저장요청
    document.getElementById("planModalBtn").addEventListener("click", function () {

        //tariffInfoList 가져오기
        const tariffInfoList = getTariffInfoList();

        //폼 요소 가져오기
        const bodyData = {
            companyId: document.getElementById("modalCompany").value,
            tariffId: selectedTariffId,
            policyName: document.getElementById("modalPlanTextbox").value,
            applyStartDate: formatToLocalDateTimeString(document.getElementById("applyStartDate").value),
            applyCode: document.getElementById("modalTariffStatCode").value,
            tariffInfo: tariffInfoList
        };
        console.log("input data: ", bodyData);

        //필수항목 체크 (null, undefined, 빈 문자열 모두 체크)
        if (!bodyData.policyName || !bodyData.applyStartDate || !bodyData.applyCode) {
            alert(i18n.tariffMgmt.messages.planRegError);
            return;
        }


        const m_url = planModaIcon ? "/system/tariff/update" : "/system/tariff/new";
        const m_method = planModaIcon ? "PATCH" : "POST";
        fetch(m_url, {
            method: m_method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(bodyData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(planModaIcon ? "Plan updaate failed: " + response.statusText : "Plan registration failed: " + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                console.log("result:", data);
                alert(i18n.tariffMgmt.messages.planRegSuccess);
                window.location.replace('/system/tariff/list');
            })
            .catch(error => {
                if (planModaIcon == false) console.error("Plan registration failed:", error);
                else console.error("Plan update failed:", error);
            });

    });

    //요금제 삭제버튼 클릭 이벤트 처리
    document.getElementById("deleteBtn").addEventListener("click", function () {
        if (planSelectedRow) {
            selectedTariffId = planSelectedRow.cells[1].innerText;
            console.log("DELETE >> Selected TariffId:", selectedTariffId);

            fetch(`/system/tariff/delete/${selectedTariffId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("[DELETE]Plan deletion failed: " + response.statusText);
                    }
                    return response.text();
                })
                .then(data => {
                    console.log("result:", data);
                    window.location.replace('/system/tariff/list');
                })
                .catch(error => {
                    console.error("Plan deletion failed:", error);
                });
        }
    });




    /*
    * 요금정보(TariffInfo) 처리 =============================================================================
    */

    function getTariffInfoList() {
        // Get the table body element
        const tariffTableBody = document.getElementById("modalTariffInfoTable");

        // Initialize an empty array to hold the row data
        const rowDataList = [];

        // Loop through each row in the table body
        tariffTableBody.querySelectorAll("tr").forEach((row) => {
            const rowData = [];
            // Loop through each cell in the current row
            row.querySelectorAll("td").forEach((cell) => {
                // Get the value from an input if present, otherwise get the cell's text content
                const input = cell.querySelector("input");
                rowData.push(input ? input.value : cell.textContent.trim());
            });
            // Add the row data to the list
            rowDataList.push(rowData);
        });

        // Log the list of row data for debugging
        console.log("Row Data List:", rowDataList);

        return rowDataList;
    }
});


// 날짜 형식을 LocalDateTIme 형식으로 변환
function formatToLocalDateTimeString(date) {
    // Ensure the date is non-empty and valid
    if (!date || date.trim() === "") {
        return null; // Return null if no date is provided
    }
    return date.trim() + "T00:00:00"; // Append time only to valid dates
}

function formatDate(dateTime) {
    // Example: "2024-12-02 09:00:00"
    const [datePart] = dateTime.split(" "); // Split by space and take the date part
    return datePart; // Return "yyyy-MM-dd"
}