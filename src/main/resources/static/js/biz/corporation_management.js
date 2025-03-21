$(document).ready(function () {
    let modalCon = false, selectRow, btnMsg = "등록";
    var bizId;

    // 전체 동의 체크박스 기능
    const termsAll = document.getElementById('termsAll');
    const termsCheckboxes = document.querySelectorAll('.terms-item input[type="checkbox"]');

    //=== 약관 Start =========================
    if (termsAll) {
        termsAll.addEventListener('change', function () {
            const isChecked = this.checked;
            termsCheckboxes.forEach(checkbox => {
                checkbox.checked = isChecked;
            });
        });
    }

    // 개별 약관 체크박스 변경 시 전체 동의 체크박스 상태 업데이트
    termsCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            updateTermsAllCheckbox();
        });
    });

    // 전체 동의 체크박스 상태 업데이트 함수
    function updateTermsAllCheckbox() {
        if (!termsAll) return;

        const allChecked = Array.from(termsCheckboxes).every(checkbox => checkbox.checked);
        termsAll.checked = allChecked;
    }

    // 약관 상세보기 버튼 클릭 이벤트
    $('.terms-detail-btn').on('click', function (e) {
        e.preventDefault();

        // 클릭된 버튼에서 데이터 속성 가져오기
        const termsTitle = $(this).attr('data-terms-title');
        const termsId = $(this).attr('data-terms-id');

        // 모달 제목 설정
        $('#termsDetailModalLabel').text(termsTitle + ' 상세보기');

        // 모든 약관 내용 숨기기
        $('.terms-content').addClass('d-none');

        // 선택한 약관 내용 표시 또는 로드
        const selectedTermsContent = $('#termsContent-' + termsId);
        if (selectedTermsContent.length > 0) {
            selectedTermsContent.removeClass('d-none');
        } else {
            // 약관 내용이 로드되지 않은 경우 로드
            loadTermsContent(termsId);
        }

        // 기존 모달 숨기기 전에 포커스 제거
        document.activeElement.blur();

        // Bootstrap 모달 객체 직접 사용하여 모달 전환
        const dataAddModalEl = document.getElementById('dataAddModal');
        const dataAddModal = bootstrap.Modal.getInstance(dataAddModalEl);

        // 기존 모달 숨기기
        if (dataAddModal) {
            dataAddModal.hide();
            $('#dataAddModal').attr('insert', '');
        }

        // 약관 모달 표시 전에 이벤트 리스너 추가
        $('#dataAddModal').one('hidden.bs.modal', function () {
            const termsDetailModalEl = document.getElementById('termsDetailModal');
            const termsDetailModal = new bootstrap.Modal(termsDetailModalEl);
            $('#termsDetailModal').removeAttr('insert');
            termsDetailModal.show();
        });
    });

    // 약관 상세보기 모달 닫기 버튼 이벤트
    $('#termsDetailModal .btn-close, #termsDetailModal .btn-secondary').on('click', function (e) {
        e.preventDefault();

        // 포커스 제거
        document.activeElement.blur();

        // Bootstrap 모달 객체 직접 사용
        const termsDetailModalEl = document.getElementById('termsDetailModal');
        const termsDetailModal = bootstrap.Modal.getInstance(termsDetailModalEl);

        // 약관 모달 숨기기
        if (termsDetailModal) {
            termsDetailModal.hide();
            $('#termsDetailModal').attr('insert', '');
        }

        // 원래 모달 다시 표시
        $('#termsDetailModal').one('hidden.bs.modal', function () {
            const dataAddModalEl = document.getElementById('dataAddModal');
            const dataAddModal = new bootstrap.Modal(dataAddModalEl);
            $('#dataAddModal').removeAttr('insert');
            dataAddModal.show();
        });
    });

    // 모달 포커스 관리 개선
    $('#termsDetailModal').on('shown.bs.modal', function () {
        // 모달 내 첫 번째 포커스 가능한 요소에 포커스
        $(this).find('.modal-title').attr('tabindex', '-1').focus();
    });

    $('#dataAddModal').on('shown.bs.modal', function () {
        // 모달 내 첫 번째 입력 필드에 포커스
        $(this).find('.modal-title').attr('tabindex', '-1').focus();
    });

    // 약관 내용 로드 함수 - 정적 리소스만 사용
    function loadTermsContent(termsId) {
        console.log('약관 내용 로드 시작:', termsId);

        // 약관 내용을 담을 컨테이너 생성
        let termsContainer = $('#termsContent-' + termsId);
        if (termsContainer.length === 0) {
            termsContainer = $('<div id="termsContent-' + termsId + '" class="terms-content"></div>');
            $('#termsDetailModal .modal-body').append(termsContainer);
        }

        // 약관 내용 로드 중 표시
        termsContainer.html('<p class="text-center">약관 내용을 불러오는 중입니다...</p>');
        termsContainer.removeClass('d-none');

        // 약관 ID에 따라 다른 URL 사용
        let url = '/terms/' + termsId + '_content.htm';

        console.log('약관 URL:', url);

        // AJAX로 약관 내용 로드
        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'html',
            success: function (html) {
                console.log('약관 내용 로드 성공');

                // HTML 파싱
                try {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, 'text/html');
                    const wordSection = doc.querySelector('.WordSection1');

                    let title = '';
                    if (termsId === 'etf') {
                        title = 'NICE페이먼츠 전자금융거래 이용약관 동의';
                    } else if (termsId === 'regularBilling') {
                        title = '정기과금(자동승인)이용약관 동의';
                    } else if (termsId === 'privacy') {
                        title = '개인정보 수집 및 이용에 대한 동의';
                    } else if (termsId === 'privacy3rd') {
                        title = '개인정보 제3자 제공에 대한 동의';
                    }

                    if (wordSection) {
                        termsContainer.html('<div class="terms-content-body"><h5>' + title + '</h5>' + wordSection.innerHTML + '</div>');
                    } else {
                        termsContainer.html('<div class="terms-content-body"><h5>' + title + '</h5>' + doc.body.innerHTML + '</div>');
                    }
                } catch (error) {
                    console.error('약관 내용 파싱 오류:', error);
                    termsContainer.html('<p class="text-danger">약관 내용을 파싱하는데 실패했습니다.</p>');
                }

                applyTermsContentStyle();
            },
            error: function (xhr, status, error) {
                console.error('약관 로드 오류:', xhr.status, error);
                termsContainer.html('<p class="text-danger">약관 내용을 불러오는데 실패했습니다: ' + error + '</p>');
            }
        });
    }

    // 약관 내용 스타일 적용 함수
    function applyTermsContentStyle() {
        // 이미 스타일이 적용되어 있는지 확인
        if ($('#terms-content-style').length > 0) {
            return;
        }

        const style = $('<style id="terms-content-style"></style>');
        style.text(`
        .terms-content-body {
            font-size: 12px !important;
            line-height: 1.5 !important;
            max-height: 400px;
            overflow-y: auto;
            padding: 10px;
        }
        .terms-content-body p {
            font-size: 12px !important;
            margin-bottom: 0.5rem !important;
        }
        .terms-content-body span {
            font-size: 12px !important;
        }
        .terms-content-body * {
            font-size: 12px !important;
            max-width: 100%;
        }
        #termsDetailModal .modal-dialog {
            max-width: 800px;
        }
        #termsDetailModal .modal-body {
            max-height: 500px;
            overflow-y: auto;
        }
    `);
        $('head').append(style);
    }

    //=== 약관 End ===========================

    // 모달이 닫히기 시작할 때 포커스 제거
    $('.modal').on('hide.bs.modal', function () {
        // 모달 내부의 모든 포커스 가능한 요소에서 포커스 제거
        $(this).find('button, input, select, textarea, a').blur();

        // 다른 요소에 포커스 이동
        setTimeout(function () {
            document.body.focus();
        }, 10);
    });

    // 취소 버튼 클릭 이벤트 핸들러
    $('.modal .btn-cancel, .modal .btn-close, .modal .btn-secondary').on('click', function (e) {
        // 포커스를 버튼에서 제거
        document.activeElement.blur();

        // 다른 요소에 포커스 이동
        setTimeout(function () {
            document.body.focus();
        }, 10);
    });

    //===============빌키 발급 요청 시작====
    $('#billkeyBtn').on('click', function () {
        console.log('빌키발급 START >> ');
        if (!validateCardInfo()) {
            return;
        }

        //카드 유효기간 포맷(YYMM)
        const expYear = $('#cardExpYear').val();
        const expMonth = $('#cardExpMonth').val();
        const cardExpire = expYear + expMonth;

        //카드번호 조합
        const cardNum = $('#cardNum1').val() + $('#cardNum2').val() + $('#cardNum3').val() + $('#cardNum4').val();

        //빌키 발급 요청 데이터
        const requestData = {
            cardNum: cardNum,
            cardExpire: cardExpire,
            cardPwd: $('#cardPwd').val(),
            bizNo: $('#bizNo').val(),
            bizName: $('#bizName').val()
        };

        //빌키 발급 요청
        $.ajax({
            url: '/corp/billkey',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            success: function (response) {
                console.log('빌키 발급 결과:', response);
                $('#bid').val(response.BID);
                $('#cardCode').val(response.CardCode);
                $('#cardName').val(response.CardName);
                $('#authDate').val(response.AuthDate);
            },
            error: function (error) {
                console.log('빌키 발급 오류:', error);
                alert('빌키 발급 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });


    });

    // BID 유무 체크
    function validateBid() {
        const bid = $('#bid').val();
        let validateResult = true;
        if (!bid) {
            validateResult = false;
        }
        return validateResult;
    }

    // 카드 정보 유효성 검사 함수
    function validateCardInfo() {
        const cardNum1 = $('#cardNum1').val();
        const cardNum2 = $('#cardNum2').val();
        const cardNum3 = $('#cardNum3').val();
        const cardNum4 = $('#cardNum4').val();
        const cardExpMonth = $('#cardExpMonth').val();
        const cardExpYear = $('#cardExpYear').val();
        const cardPwd = $('#cardPwd').val();
        const bizName = $('#bizName').val();
        const bizNo = $('#bizNo').val();        //법인사업자번호 혹은 생년월일

        console.log(cardNum1, cardNum2, cardNum3, cardNum4, cardExpMonth, cardExpYear, cardPwd, bizName, bizNo);

        // 모든 필드가 입력되었는지 확인
        if (!bizName || !bizNo) {
            alert('법인 정보를 모두 입력해 주세요.');
            return false;
        }

        if (!cardNum1 || !cardNum2 || !cardNum3 || !cardNum4 ||
            !cardExpMonth || !cardExpYear || !cardPwd) {
            alert('카드 정보를 모두 입력해 주세요.');
            return false;
        }

        // 카드번호 길이 확인
        if (cardNum1.length !== 4 || cardNum2.length !== 4 ||
            cardNum3.length !== 4 || cardNum4.length !== 4) {
            alert('카드번호가 잘못되었습니다.');
            return false;
        }

        // 유효기간 형식 확인
        if (cardExpMonth.length !== 2 || cardExpYear.length !== 2 ||
            parseInt(cardExpMonth) < 1 || parseInt(cardExpMonth) > 12) {
            alert('유효기간이 잘못되었습니다.');
            return false;
        }

        // 카드 비밀번호 길이 확인
        if (cardPwd.length !== 2) {
            alert('카드 비밀번호가 잘못되었습니다.');
            return false;
        }

        // 생년월일/사업자번호 길이 확인
        if (bizNo.length == 6 || bizNo.length == 10) {

            return true;
        } else {
            alert('생년월일 6자리 혹은 사업자번호 10자리를 입력해주세요.');
            return false;
        }
        // if (birthDay.length !== 6 || birthDay.length !== 10) {
        //     return false;
        // }

        return true;
    }

    // 카드번호 입력 후 자동으로 다음 입력란으로 이동하는 기능
    $('#cardNum1, #cardNum2, #cardNum3').on('input', function () {
        if (this.value.length === 4) {
            $(this).next().next().focus();
        }
    });

    // 유효기간 월 입력 후 자동으로 연도 입력란으로 이동
    $('#cardExpMonth').on('input', function () {
        if (this.value.length === 2) {
            $('#cardExpYear').focus();
        }
    });


    //===============빌키 발급 요청 끝====

    // toggleInputFunction();

    $('#resetBtn').on('click', function () {
        window.location.replace('/corp/list');
    });

    $('#searchBtn').on('click', function () {
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

    $('#size').on('change', function () {
        const urlParams = new URLSearchParams(window.location.search);
        const selectedSize = document.getElementById("size").value;
        const selectedManfCd = urlParams.get('manfCdSearch') || '';
        const selectedChgSpeedCd = urlParams.get('chgSpeedCdSearch') || '';

        window.location.href = "/corp/list?page=0&size=" + selectedSize +
            "&manfCdSearch=" + (selectedManfCd) +
            "&chgSpeedCdSearch=" + (selectedChgSpeedCd);
    });

    $('#pageList').on('click', 'tr', function () {
        selectRow = $(this);
        bizId = selectRow.find('td').eq(0).attr('id');
    });

    $('#addBtn').on('click', function (event) {
        event.preventDefault();
        modalCon = false;
        btnMsg = "등록";
        $('#modalBtn').text(btnMsg);
        $('#bizName').val('');
        $('#bizNo').val('');
        deleteCardInfo();
    });

    function deleteCardInfo() {
        $('#bizName').val('');
        $('#bizNo').val('');
        $('#cardNum1').val('');
        $('#cardNum2').val('');
        $('#cardNum3').val('');
        $('#cardNum4').val('');
        $('#bid').val('');
        $('#cardExpMonth').val('');
        $('#cardExpYear').val('');
        $('#termsEFT').prop('checked', false);
        $('#termsRegularBilling').prop('checked', false);
        $('#termsPrivacy').prop('checked', false);
        $('#termsPrivacy3rd').prop('checked', false);
    }

    $('#editBtn').on('click', function (event) {
        event.preventDefault();
        modalCon = true;
        btnMsg = "수정";
        $('#modalBtn').text(btnMsg);
        $.ajax({
            type: 'GET',
            url: `/corp/get/${bizId}`,
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {
                console.log("data: ", data);
                $('#bizName').val(data.bizName || '');
                $('#bizNo').val(data.bizNo || '');
                $('#cardNum1').val(data.cardNum1 || '');
                $('#cardNum2').val(data.cardNum2 || '');
                $('#cardNum3').val(data.cardNum3 || '');
                $('#cardNum4').val(data.cardNum4 || '');
                $('#bid').val(data.bid || '');
                $('#cardExpMonth').val(data.cardExpireMonth || '');
                $('#cardExpYear').val(data.cardExpireYear || '');
                $('#termsEFT').prop('checked', data.termsEtf || false);
                $('#termsRegularBilling').prop('checked', data.termsRb || false);
                $('#termsPrivacy').prop('checked', data.termsPrivacy || false);
                $('#termsPrivacy3rd').prop('checked', data.termsPrivacy3rd || false);

            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $('#deleteBtn').on('click', function () {
        btnMsg = "삭제";

        if (confirmSubmit(btnMsg)) {

            $.ajax({
                type: 'DELETE',
                url: `/corp/delete/${bizId}`,
                contentType: "application/json",
                success: function (response) {
                    // console.log(response);
                    alert(response);
                    window.location.reload();
                    // window.location.replace('/corp/list');
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
    });

    $('#modalBtn').on('click', function (event) {
        event.preventDefault();

        if (!validateBid()) {
            alert('빌키가 존재하지 않습니다. 빌키발급을 진행해주세요.');
            return;
        }

        // 약관에 동의하지 않은 경우 경고 메시지 표시
        if (!checkTerms()) {
            alert('필수 이용약관에 모두두 동의해주세요.');
            return;
        }

        console.log('카드번호: ' + updateCardNum());
        if (confirmSubmit(btnMsg)) {
            const DATA = {
                bizNo: $('#bizNo').val(),
                bizName: $('#bizName').val(),
                bid: $('#bid').val(),
                cardNum: updateCardNum(),
                cardCode: $('#cardCode').val(),
                cardName: $('#cardName').val(),
                cardExpireMonth: $('#cardExpMonth').val(),
                cardExpireYear: $('#cardExpYear').val(),
                termsEtf: $('#termsEFT').is(':checked'),
                termsRb: $('#termsRegularBilling').is(':checked'),
                termsPrivacy: $('#termsPrivacy').is(':checked'),
                termsPrivacy3rd: $('#termsPrivacy3rd').is(':checked'),
                authDate: $('#authDate').val(),
            };

            const URL = modalCon ? `/corp/update/${bizId}` : `/corp/new`;
            const TYPE = modalCon ? 'PATCH' : 'POST';

            $.ajax({
                url: URL,
                method: TYPE,
                contentType: 'application/json',
                data: JSON.stringify(DATA),
                success: function (response) {
                    window.location.reload();
                },
                error: function (error) {
                    alert(error);
                }
            });
        }
    });

    $('#opSearch').on('change', function () {
        toggleInputFunction();
    });

    function checkTerms() {
        // 약관 동의 체크박스 확인
        // 필수 약관 체크박스 확인
        const requiredTermsCheckboxes = document.querySelectorAll('.terms-item input[type="checkbox"][data-required="true"]');
        let allRequiredTermsChecked = true;

        // 모든 필수 약관 체크박스가 체크되었는지 확인
        requiredTermsCheckboxes.forEach(function (checkbox) {
            if (!checkbox.checked) {
                allRequiredTermsChecked = false;
            }
        });

        return allRequiredTermsChecked;
    }

    function toggleInputFunction() {
        const opSearch = document.getElementById('opSearch').value;
        const contentSearch = document.getElementById('contentSearch');

        if (!opSearch || opSearch === "null") {
            contentSearch.setAttribute('disabled', 'disabled');
            contentSearch.removeAttribute('oninput');
            contentSearch.value = '';
        } else {
            contentSearch.removeAttribute('disabled');

            if (opSearch === "bizNo") {
                contentSearch.setAttribute('oninput', 'addHyphenBizNo(this)');
            } else {
                contentSearch.removeAttribute('oninput');
            }
        }
    }

    function updateCardNum() {
        var cardNum1 = document.getElementById('cardNum1').value;
        var cardNum2 = document.getElementById('cardNum2').value;
        var cardNum3 = document.getElementById('cardNum3').value;
        var cardNum4 = document.getElementById('cardNum4').value;

        return cardNum1 + cardNum2 + cardNum3 + cardNum4;
    }

    //=================== 결제 테스트 Code =====================
    $('#testPaymentBtn').on('click', function () {
        console.log('결제 테스트');

        const testPaymentData = {
            amount: 1004,
            orderId: '1234'
        };

        //로딩표시
        const $btn = $(this);
        const originalText = $btn.text();
        $btn.prop('disabled', true).text('결제 처리중...');

        //내부컨트롤러로 요청
        $.ajax({
            url: '/corp/payment/test',  // 내부 컨트롤러 엔드포인트
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(testPaymentData),
            success: function (response) {
                console.log('결제 테스트 성공:', response);
                $('#otid').val(response.TID);
                alert('결제 테스트가 성공했습니다.\n' + JSON.stringify(response, null, 2));
            },
            error: function (xhr, status, error) {
                console.error('결제 테스트 실패:', xhr.status, error);
                let errorMessage = '결제 테스트가 실패했습니다.';

                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse && errorResponse.message) {
                        errorMessage += '\n오류: ' + errorResponse.message;
                    }
                } catch (e) {
                    errorMessage += '\n상태: ' + xhr.status + '\n오류: ' + error;
                }

                alert(errorMessage);
            },
            complete: function () {
                // 버튼 상태 복원
                $btn.prop('disabled', false).text(originalText);
            }
        });
    });

    $('#testCancelBtn').on('click', function () {
        console.log('결제 취소');

        const testCancelData = {
            amount: 1004,
            orderId: '1234',
            tid: $('#otid').val()
        };

        //로딩표시
        const $btn = $(this);
        const originalText = $btn.text();
        $btn.prop('disabled', true).text('취소 처리중...');

        //내부 컨트롤러 요청
        $.ajax({
            url: '/corp/payment/test/cancel',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(testCancelData),
            success: function (response) {
                console.log('결제 취소 성공:', response);
                alert('결제 취소가 성공했습니다.\n' + JSON.stringify(response, null, 2));
            },
            error: function (xhr, status, error) {
                console.error('결제 취소 실패:', xhr.status, error);
                let errorMessage = '결제 취소가 실패했습니다.';

                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse && errorResponse.message) {
                        errorMessage += '\n오류: ' + errorResponse.message;
                    }
                } catch (e) {
                    errorMessage += '\n상태: ' + xhr.status + '\n오류: ' + error;
                }

                alert(errorMessage);
            },
            complete: function () {
                //버튼 상태 복원
                $btn.prop('disabled', false).text(originalText);
            }
        });

    });

    $('#tradeData').on('click', function () {
        console.log('거래대사조회');

        $.ajax({
            url: '/corp/payment/trade/data',
            method: 'GET',
            success: function (response) {
                console.log('거래대사조회 성공:', response);
            },
            error: function (xhr, status, error) {
                console.error('거래대사조회 실패:', xhr.status, error);
                alert('거래대사 조회에 실패했습니다.\n상태: ' + xhr.status + '\n오류: ' + error);
            }
        });
    });
});