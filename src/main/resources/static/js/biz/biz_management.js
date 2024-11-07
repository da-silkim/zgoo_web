document.addEventListener("DOMContentLoaded", function() {
    // '검색' 버튼 클릭 이벤트 처리
    document.getElementById("companySearchBtn").addEventListener("click", function() {
        
        // 폼 요소 가져오기
        const business = document.getElementById("business").value;
        const businessType = document.getElementById("businessType").value;
        const businessLv = document.getElementById("businessLv").value;

        // 선택된 값 콘솔 출력 (디버깅용)
        console.log("사업자명:", business);
        console.log("사업자유형:", businessType);
        console.log("사업자레벨:", businessLv);

        // 선택된 값으로 필터링을 위한 요청 처리 (예시: AJAX, Fetch API 등)
        // 예를 들어, Fetch API로 서버에 데이터를 전송하는 방법
        fetch("/biz_management/biz/search", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                companyId: business,
                companyType: businessType,
                companyLv: businessLv
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("업체정보 조회 실패: " + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            // 서버 응답 후 처리 (예: 검색 결과 출력)
            console.log("검색 결과:", data);

            const tableBody = document.getElementById("pageListSub");
            tableBody.innerHTML = "";       //기존내용 초기화

            data.forEach(company => {
                const row = document.createElement("tr");

                row.innerHTML = `
                    <td><input type="checkbox"/></td>
                    <td>${company.companyName || ''}</td>
                    <td>${company.companyLv || ''}</td>
                    <td>${company.companyType || ''}</td>
                    <td>${company.parentId || ''}</td>
                    <td>${formatDate(company.contractedAt) || ''}</td>
                    <td>${formatDate(company.contractEnd) || ''}</td>
                    <td>${company.contractStatus || ''}</td>
                `;

                tableBody.appendChild(row);
                
            });

        })
        .catch(error => {
            console.error("업체정보 조회 실패:", error);
        });
    });


    // '초기화' 버튼 클릭 이벤트 처리
    document.getElementById("resetBtn").addEventListener("click", function() {
        // 폼 필드 초기화
        document.getElementById("business").selectedIndex = 0; // 사업자명 초기화 (첫 번째 옵션)
        document.getElementById("businessType").selectedIndex = 0; // 사업자유형 초기화 (첫 번째 옵션)
        document.getElementById("businessLv").selectedIndex = 0; // 사업자레벨 초기화 (첫 번째 옵션)

        // 로그 출력 (디버깅용)
        console.log("폼 필드가 초기화되었습니다.");
    });

    // 사업자 레벨 선택에 따른 상위업체 옵션 처리 함수
    document.getElementById("companyLv").addEventListener("change", updateParentCompanyOptions);

    function updateParentCompanyOptions(){
        const companyList = /*[[${companyList}]]*/ [];
        const selectedCompanyLv = document.getElementById("companyLv").value;
        const parentCompanySelect = document.getElementById("parentCompanyOptions");

        //기존 옵션 초기화
        parentCompanySelect.innerHTML = "<option value='%'>선택</option>";

        if(selectedCompanyLv === "MID"){
            companyList.filter(company => company.companyLv === "TOP")
            .forEach(company => {
                const option = document.createElement("option");
                option.value = company.companyId;
                option.textContent = company.companyName;
                parentCompanySelect.appendChild(option);
            });
        } else if(selectedCompanyLv === "LOW"){
            companyList.filter(company => company.companyLv === "MID")
            .forEach(company => {
                const option = document.createElement("option");
                option.value = company.companyId;
                option.textContent = company.companyName;
                parentCompanySelect.appendChild(option);
            })
        } 

    }

    // 'Modal' - '등록' 버튼 클릭 이벤트 처리
    
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