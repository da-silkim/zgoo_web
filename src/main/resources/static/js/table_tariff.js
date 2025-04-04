document.addEventListener('DOMContentLoaded', () => {
    // 테이블 단일 선택 및 수정/삭제 기능
    const tableBody = document.getElementById('pageList');
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');

    const tableSub = document.getElementById('pageListSub');
    const addBtnSub = document.getElementById('addBtnSub');
    const editBtnSub = document.getElementById('editBtnSub');
    const deleteBtnSub = document.getElementById('deleteBtnSub');

    function updateBtn() {
        const selectedCheckboxes = document.querySelectorAll('#pageList input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            // Find the corresponding row for the selected checkbox
            const selectedRow = selectedCheckboxes[0].closest('tr');
            const applyCode = selectedRow.cells[5]?.innerText.trim(); // Get applyCode from the 6th cell
            console.log("적용코드:", applyCode);

            if (editBtn) {
                let editvisible = applyCode === "적용예정";
                console.log("editvisible:", editvisible);
                editBtn.disabled = !editvisible;
                if (editvisible) {
                    editBtn.removeAttribute("hidden");
                } else {
                    editBtn.setAttribute("hidden", true);
                }
            }
            if (deleteBtn) { deleteBtn.disabled = false; deleteBtn.removeAttribute("hidden"); }
            if (addBtnSub) { addBtnSub.disabled = false; addBtnSub.removeAttribute("hidden"); }
        } else {
            // 체크된 항목이 없거나 두 개 이상일 때 버튼 비활성화
            if (editBtn) { editBtn.disabled = true; editBtn.setAttribute("hidden", true); }
            if (deleteBtn) { deleteBtn.disabled = true; deleteBtn.setAttribute("hidden", true); }
            if (addBtnSub) { addBtnSub.disabled = true; addBtnSub.setAttribute("hidden", true); }
        }
    }

    function updateSubBtn() {
        const selectedCheckboxes = document.querySelectorAll('#pageListSub input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            // 체크된 항목이 하나일 때 버튼 활성화
            editBtnSub.disabled = false;
            deleteBtnSub.disabled = false;
        } else {
            // 체크된 항목이 없거나 두 개 이상일 때 버튼 비활성화
            editBtnSub.disabled = true;
            deleteBtnSub.disabled = true;
        }
    }

    // 행 클릭 또는 체크박스 클릭 시 단일 선택 처리
    tableBody.addEventListener('click', (event) => {
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


    // document.getElementById('deleteBtn').addEventListener('click', () => {
    //     const checkedBox = document.querySelector('input[type="checkbox"]:checked'); // 단일 선택을 가정

    //     if (checkedBox) {
    //         // 사용자에게 삭제 여부 확인
    //         const isConfirmed = confirm('정말로 이 항목을 삭제하시겠습니까?');

    //         if (isConfirmed) {
    //             const idx = parseInt(checkedBox.getAttribute('data-index')); // 체크된 항목의 data-index 값 추출
    //             list.splice(idx, 1); // 데이터 배열에서 해당 인덱스 삭제

    //             showList(1, list.length); // 변경된 리스트 렌더링

    //             // 버튼 비활성화
    //             document.getElementById('deleteBtn').disabled = true;
    //             document.getElementById('editBtn').disabled = true;
    //         }
    //     } else {
    //         alert('삭제할 항목을 선택하세요.');
    //     }
    // });

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