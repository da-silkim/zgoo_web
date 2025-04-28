document.addEventListener("DOMContentLoaded", function () {

    /**
     * 검색버튼 클릭 이벤트 처리
     */
    document.getElementById("searchBtn").addEventListener("click", function () {
        // 폼 제출 (size 값은 이미 hidden input에 있음)
        document.getElementById('searchForm').submit();
    });

    /**
     * 초기화 버튼 클릭 이벤트 처리
     */
    document.getElementById("resetBtn").addEventListener("click", function () {
        // 모든 입력 필드 초기화
        document.getElementById('opSearch').value = '';
        document.getElementById('contentSearch').value = '';
        document.getElementById('recvFromSearch').value = '';
        document.getElementById('recvToSearch').value = '';

        // 폼 제출 (초기화된 상태로)
        document.getElementById('searchForm').submit();
    });

    /**
     * 모달 초기화 및 이벤트 설정
     */
    initializeContentModal();
});

/**
 * 내용 전체 보기 모달 표시 함수
 * @param {HTMLElement} element - 클릭된 테이블 셀 요소
 */
function showFullContent(element) {
    const content = element.getAttribute('data-content');
    document.getElementById('modalContent').textContent = content;
    const modal = new bootstrap.Modal(document.getElementById('contentModal'));
    modal.show();
}

/**
 * 모달 초기화 및 이벤트 설정 함수
 */
function initializeContentModal() {
    // 모달이 이미 존재하는지 확인
    let contentModal = document.getElementById('contentModal');

    // 모달이 없으면 생성
    if (!contentModal) {
        const modalHTML = `
        <div class="modal fade" id="contentModal" tabindex="-1" aria-labelledby="contentModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="contentModalLabel">상세 내용</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <pre id="modalContent" style="white-space: pre-wrap; word-break: break-all;"></pre>
                    </div>
                </div>
            </div>
        </div>`;

        // 모달 HTML을 body에 추가
        document.body.insertAdjacentHTML('beforeend', modalHTML);
    }

    // 내용이 긴 셀에 클릭 이벤트 추가 (이미 HTML에서 onclick으로 처리되어 있으므로 선택적)
    /*
    document.querySelectorAll('.content-truncate').forEach(cell => {
        cell.addEventListener('click', function() {
            showFullContent(this);
        });
    });
    */
}