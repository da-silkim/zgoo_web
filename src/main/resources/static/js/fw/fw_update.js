document.addEventListener("DOMContentLoaded", function () {

    let selectedRow; // 선택한 행, 열 저장



    // '검색' 버튼 클릭 이벤트 처리
    document.getElementById("serachBtn").addEventListener("click", function () {
        // 현재 선택된 펌웨어 값들을 hidden 필드에 설정
        document.querySelector('input[name="companyIdFw"]').value = document.getElementById('companyIdFw').value;
        document.querySelector('input[name="modelSearch"]').value = document.getElementById('modelSearch').value;
        document.querySelector('input[name="versionSearch"]').value = document.getElementById('versionSearch').value;
        document.querySelector('input[name="urlSearch"]').value = document.getElementById('urlSearch').value;

        document.getElementById('searchForm').submit();
    });


    // '초기화' 버튼 클릭 이벤트 처리
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 검색 폼 필드만 초기화
        document.getElementById('companyIdSearch').value = '';
        document.getElementById('stationIdSearch').value = '';

        // 펌웨어 선택 값들은 현재 값으로 유지
        document.querySelector('input[name="companyIdFw"]').value = document.getElementById('companyIdFw').value;
        document.querySelector('input[name="modelSearch"]').value = document.getElementById('modelSearch').value;
        document.querySelector('input[name="versionSearch"]').value = document.getElementById('versionSearch').value;
        document.querySelector('input[name="urlSearch"]').value = document.getElementById('urlSearch').value;

        document.getElementById('searchForm').submit();
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


    //테이블 row 클릭시 처리 이벤트
    document.querySelectorAll('#pageList tr').forEach(row => {
        row.addEventListener('click', function () {
            selectedRow = row;
            console.log('selected row data:', selectedRow);
        });
    });

    /**
     * 선택된 충전기 업데이트 요청
     */
    const updateBtn = document.getElementById("updateBtn");
    if (updateBtn) {
        updateBtn.addEventListener("click", function () {
            const selectedUrl = document.getElementById('urlSearch').value;
            if (!selectedUrl) {
                alert("URL 정보가 없습니다.");
                return;
            }

            if (selectedRow) {
                const selectedChargerId = selectedRow.cells[4].id;
                console.log("selected chargerID : ", selectedChargerId);

                //업덴이트 요청 데이터 생성
                const updateData = {
                    chargerId: selectedChargerId,
                    url: selectedUrl,
                    retries: 3,
                    retrieveDate: new Date().toISOString,
                    retryInterval: 300
                };

                fetch('/control/fwupdate', {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(updateData)
                })
                    .then(response => response.json())
                    .then(data => {
                        alert('요청결과 : ' + data.status);
                    })
                    .catch(error => {
                        alert('펌웨어 업데이트 요청 중 오류가 발생했습니다.');
                        console.error('업데이트 요청 오류:', error);
                    });
            } else {
                alert("업데이트할 충전기를 선택해주세요.");
                return;
            }
        });
    }

});