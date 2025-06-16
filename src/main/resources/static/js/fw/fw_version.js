document.addEventListener("DOMContentLoaded", function () {

    let selectedRow; // 선택한 행, 열 저장

    // /**
    //  * 검색버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("searchBtn").addEventListener("click", function () {
    //     // 폼 제출 (size 값은 이미 hidden input에 있음)
    //     document.getElementById('searchForm').submit();
    // });

    // /**
    //  * 초기화 버튼 클릭 이벤트 처리
    //  */
    // document.getElementById("resetBtn").addEventListener("click", function () {
    //     // 모든 입력 필드 초기화
    //     document.getElementById('companyIdSearch').value = '';
    //     document.getElementById('manfCodeSearch').value = '';
    //     document.getElementById('chgStartTimeFrom').value = '';
    //     document.getElementById('chgStartTimeTo').value = '';
    //     document.getElementById('opSearch').value = '';
    //     document.getElementById('contentSearch').value = '';

    //     // 폼 제출 (초기화된 상태로)
    //     document.getElementById('searchForm').submit();
    // });

    $('#size').on('change', function () {
        updatePageSize(this, "/fw/version", []);
    });

    //테이블 row 클릭시 처리 이벤트
    document.querySelectorAll('#pageList tr').forEach(row => {
        row.addEventListener('click', function () {
            selectedRow = row;
            console.log('selected row data:', selectedRow);
        });
    });

    /**
     * 펌웨어 버전정보 삭제
     */
    const deleteBtn = document.getElementById("deleteBtn");
    if (deleteBtn) {
        deleteBtn.addEventListener("click", function () {
            if (selectedRow) {
                const selectedFwId = selectedRow.cells[0].id;
                console.log("[DELETE] selected ID : ", selectedFwId);


                fetch(`/fw/version/delete/${selectedFwId}`, {
                    method: "DELETE",
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("[DELETE]펌웨어 버전 삭제 실패: " + response.statusText);
                        }
                        return response.text();
                    })
                    .then(data => {
                        console.log("처리결과:", data);
                        window.location.replace('/fw/version');
                    })
                    .catch(error => {
                        console.error("펌웨어 버전 삭제 실패:", error);
                    });
            }
        });
    }

    // 펌웨어 목록 등록 버튼 클릭 이벤트 처리
    document.getElementById('addBtn').addEventListener('click', function () {
        //폼 요소 가져오기
        const bodyData = {
            companyId: document.getElementById('companyId').value,
            cpModelCode: document.getElementById('modelCode').value,
            version: document.getElementById('version').value,
            url: document.getElementById('filePath').value
        };
        console.log("입력 data: ", bodyData);

        if (bodyData.companyId == '') {
            alert("사업자정보를 선택해주세요.");
            return;
        }

        if (bodyData.cpModelCode == '') {
            alert("모델명을 선택해주세요.");
            return;
        }

        if (bodyData.version == '') {
            alert("버전정보를 입력해주세요.");
            return;
        }

        if (bodyData.url == '') {
            alert("파일 경로를 입력해주세요.");
            return;
        }

        const m_url = '/fw/version/new';
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
                    throw new Error("펌웨어 버전 등록 실패: " + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                console.log("처리결과:", data);
                window.location.replace('/fw/version');
            })
            .catch(error => {
                console.error("펌웨어 버전 등록 실패:", error);
            });

    });

    // 사업자 선택에 따른 충전기 모델 리스트 조회
    $('#companyId').change(function () {
        const companyId = $(this).val();

        // company가 선택되지 않은경우 모델 리스트 초기화
        if (!companyId || companyId === '*{null}') {
            $('#modelCode').html('<option value="">(선택)</option>');
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

                $('#modelCode').html(options);

            },
            error: function (xhr, status, error) {
                console.error('모델 리스트 조회 실패:', error);
                alert('모델 리스트 조회에 실패했습니다.');
            }
        });
    });
});