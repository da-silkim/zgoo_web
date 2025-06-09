document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.getElementById('pageList');
    const addBtn = document.getElementById('addBtn');
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');

    const tableSub = document.getElementById('pageListSub');
    const addBtnSub = document.getElementById('addBtnSub');
    const editBtnSub = document.getElementById('editBtnSub');
    const deleteBtnSub = document.getElementById('deleteBtnSub');

    const tableBody2 = document.getElementById('pageList2');
    const editBtnSec = document.getElementById('editBtnSec');
    const deleteBtnSec = document.getElementById('deleteBtnSec');

    const saveBtn = document.getElementById('saveBtn');
    const excelBtn = document.getElementById('excelBtn');

    // if (menuAuthority.modYn === 'Y') {
    //     if (saveBtn) saveBtn.style.display = 'inline-block';
    //     if (addBtn) addBtn.style.display = 'inline-block';
    //     if (addBtnSub) addBtnSub.style.display = 'inline-block';
    //     if (editBtn) editBtn.style.display = 'inline-block';
    //     if (editBtnSub) editBtnSub.style.display = 'inline-block';
    //     if (deleteBtn) deleteBtn.style.display = 'inline-block';
    //     if (deleteBtnSub) deleteBtnSub.style.display = 'inline-block';
    // } else {
    //     if (saveBtn) saveBtn.style.display = 'none';
    //     if (addBtn) addBtn.style.display = 'none';
    //     if (addBtnSub) addBtnSub.style.display = 'none';
    //     if (editBtn) editBtn.style.display = 'none';
    //     if (editBtnSub) editBtnSub.style.display = 'none';
    //     if (deleteBtn) deleteBtn.style.display = 'none';
    //     if (deleteBtnSub) deleteBtnSub.style.display = 'none';
    // }

    // if (menuAuthority.readYn === 'Y') {
    //     // 조회 기능 처리
    // } else {
    //     // 조회 기능 처리
    // }

    // if (menuAuthority.excelYn === 'Y') {
    //     if (excelBtn) excelBtn.style.display = 'inline-block';
    // } else {
    //     if (excelBtn) excelBtn.style.display = 'none';
    // }

    // 펌웨어 업데이트 테이블인 경우 이벤트 처리 제외
    if (tableBody && tableBody.closest('.fw-update-table')) {
        return;
    }

    function updateBtn() {
        const selectedCheckboxes = document.querySelectorAll('#pageList input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            // 체크된 항목이 하나일 때 버튼 활성화
            if (editBtn) { editBtn.disabled = false; editBtn.removeAttribute("hidden"); }
            if (deleteBtn) { deleteBtn.disabled = false; deleteBtn.removeAttribute("hidden"); }
            if (addBtnSub) { addBtnSub.disabled = false; addBtnSub.removeAttribute("hidden"); }
        } else {
            // 체크된 항목이 없거나 두 개 이상일 때 버튼 비활성화
            if (editBtn) { editBtn.disabled = true; editBtn.setAttribute("hidden", true); }
            if (deleteBtn) { deleteBtn.disabled = true; deleteBtn.setAttribute("hidden", true); }
            if (addBtnSub) { addBtnSub.disabled = true; addBtnSub.setAttribute("hidden", true); }
        }
    }

    function updateBtn2() {
        const selectedCheckboxes = document.querySelectorAll('#pageList2 input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            if (editBtnSec) { editBtnSec.disabled = false; editBtnSec.removeAttribute("hidden"); }
            if (deleteBtnSec) { deleteBtnSec.disabled = false; deleteBtnSec.removeAttribute("hidden"); }
        } else {
            if (editBtnSec) { editBtnSec.disabled = true; editBtnSec.setAttribute("hidden", true); }
            if (deleteBtnSec) { deleteBtnSec.disabled = true; deleteBtnSec.setAttribute("hidden", true); }
        }
    }


    function updateSubBtn() {
        const selectedCheckboxes = document.querySelectorAll('#pageListSub input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            // 체크된 항목이 하나일 때 버튼 활성화
            if (editBtnSub) { editBtnSub.disabled = false; editBtnSub.removeAttribute("hidden"); }
            if (deleteBtnSub) { deleteBtnSub.disabled = false; deleteBtnSub.removeAttribute("hidden"); }
        } else {
            // 체크된 항목이 없거나 두 개 이상일 때 버튼 비활성화
            if (editBtnSub) { editBtnSub.disabled = true; editBtnSub.setAttribute("hidden", true); }
            if (deleteBtnSub) { deleteBtnSub.disabled = true; deleteBtnSub.setAttribute("hidden", true); }
        }
    }

    if (tableBody) {
        const links = tableBody.querySelectorAll('a');
        links.forEach(link => {
            link.addEventListener('click', (event) => {
                event.stopPropagation(); // <a> 태그 이벤트 버블링 중지
            });
        });

        // 행 클릭 또는 체크박스 클릭 시 단일 선택 처리
        tableBody.addEventListener('click', (event) => {
            let checkbox, row;

            // 클릭한 요소가 링크(<a>)인 경우 체크박스 활성화를 무시
            if (event.target.tagName === 'A') {
                return;
            }

            // 클릭한 요소가 체크박스인 경우
            if (event.target.type === 'checkbox') {
                checkbox = event.target;
                row = checkbox.closest('tr');
            }

            // 클릭한 요소가 체크박스가 아닌 경우 (행 클릭 시)
            else {
                row = event.target.closest('tr');
                if (!row) return; // tr이 없을 경우 무시
                checkbox = row.querySelector('input[type="checkbox"]');
                if (!checkbox) return; // 체크박스가 없을 경우 무시
                checkbox.checked = !checkbox.checked; // 체크박스 상태 반전
            }

            // 단일 선택을 위해 모든 체크박스를 해제
            const checkboxes = tableBody.querySelectorAll('input[type="checkbox"]');
            checkboxes.forEach(cb => {
                if (cb !== checkbox) {
                    cb.checked = false;
                    cb.closest('tr').classList.remove('table-selected-active');
                }
            });

            // 클릭된 체크박스의 상태에 따라 배경색 설정
            if (checkbox.checked) {
                row.classList.add('table-selected-active');
            } else {
                row.classList.remove('table-selected-active');
            }

            updateBtn();
        });
    }

    // 페이지 당 테이블이 두 개 존재할 때
    if (tableBody2) {
        tableBody2.addEventListener('click', (event) => {
            let checkbox, row;

            if (event.target.tagName === 'A') {
                return;
            }

            if (event.target.type === 'checkbox') {
                checkbox = event.target;
                row = checkbox.closest('tr');
            } else {
                row = event.target.closest('tr');
                if (!row) return; // tr이 없을 경우 무시
                checkbox = row.querySelector('input[type="checkbox"]');
                if (!checkbox) return; // 체크박스가 없을 경우 무시
                checkbox.checked = !checkbox.checked; // 체크박스 상태 반전
            }

            const checkboxes = tableBody2.querySelectorAll('input[type="checkbox"]');
            checkboxes.forEach(cb => {
                if (cb !== checkbox) {
                    cb.checked = false;
                    cb.closest('tr').classList.remove('table-selected-active');
                }
            });

            if (checkbox.checked) {
                row.classList.add('table-selected-active');
            } else {
                row.classList.remove('table-selected-active');
            }

            updateBtn2();
        });
    }

    // 공통코드 테이블 처리 코드
    if (tableSub) {
        tableSub.addEventListener('click', (event) => {
            let checkbox, row;

            // 클릭한 요소가 체크박스인 경우
            if (event.target.type === 'checkbox') {
                checkbox = event.target;
                row = checkbox.closest('tr');
            }

            // 클릭한 요소가 체크박스가 아닌 경우 (행 클릭 시)
            else {
                row = event.target.closest('tr');
                if (!row) return; // tr이 없을 경우 무시
                checkbox = row.querySelector('input[type="checkbox"]');
                if (!checkbox) return; // 체크박스가 없을 경우 무시
                checkbox.checked = !checkbox.checked; // 체크박스 상태 반전
            }

            // 단일 선택을 위해 모든 체크박스를 해제
            const checkboxes = tableSub.querySelectorAll('input[type="checkbox"]');
            checkboxes.forEach(cb => {
                if (cb !== checkbox) {
                    cb.checked = false;
                    cb.closest('tr').classList.remove('table-selected-active');
                }
            });

            // 클릭된 체크박스의 상태에 따라 배경색 설정
            if (checkbox.checked) {
                row.classList.add('table-selected-active');
            } else {
                row.classList.remove('table-selected-active');
            }

            updateSubBtn();
        });
    }
});