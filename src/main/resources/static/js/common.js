// 사이드바 토글
$(function() {
    $(".side-nav-detail ul .nav-list-detail").hide();
    $(".nav-menu > .nav-list").click(function(e){
        e.preventDefault();
        $(this).siblings(".nav-list-detail").slideToggle(300);

        // Toggle the arrow direction
        let arrow = $(this).find(".font-ico-arrow");
        if(arrow.hasClass("fa-chevron-down")) {
            arrow.removeClass("fa-chevron-down").addClass("fa-chevron-up");
        } else {
            arrow.removeClass("fa-chevron-up").addClass("fa-chevron-down");
        }
    });
});


document.addEventListener('DOMContentLoaded', () => {
    // 사이드바 hover 
    const navItems = document.querySelectorAll('.side-nav .list-hover');
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            navItems.forEach(nav => nav.classList.remove('active'));
            item.classList.add('active');
        });
    });


    // 테이블 단일 선택 및 수정/삭제 기능
    const tableBody = document.getElementById('pageList');
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');

    function updateBtn() {
        const selectedCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');
        if (selectedCheckboxes.length === 1) {
            // 체크된 항목이 하나일 때 버튼 활성화
            editBtn.disabled = false;
            deleteBtn.disabled = false;
        } else {
            // 체크된 항목이 없거나 두 개 이상일 때 버튼 비활성화
            editBtn.disabled = true;
            deleteBtn.disabled = true;
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

    document.getElementById('deleteBtn').addEventListener('click', () => {
        const checkedBox = document.querySelector('input[type="checkbox"]:checked'); // 단일 선택을 가정
    
        if (checkedBox) {
            // 사용자에게 삭제 여부 확인
            const isConfirmed = confirm('정말로 이 항목을 삭제하시겠습니까?');
            
            if (isConfirmed) {
                const idx = parseInt(checkedBox.getAttribute('data-index')); // 체크된 항목의 data-index 값 추출
                list.splice(idx, 1); // 데이터 배열에서 해당 인덱스 삭제
                
                showList(1, list.length); // 변경된 리스트 렌더링
                
                // 버튼 비활성화
                document.getElementById('deleteBtn').disabled = true;
                document.getElementById('editBtn').disabled = true;
            }
        } else {
            alert('삭제할 항목을 선택하세요.');
        }
    });
});

// 금액 단위 포맷 함수
$(document).ready(function() {
    $('.price-format').each(function() {
        var price = parseInt($(this).text(), 10);
        $(this).text(price.toLocaleString('ko-KR'));
    });
});

// 주소 검색 함수
function postSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수
            
            if(data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else{
                addr = data.jibunAddress;
            }

            document.getElementById('postCode').value = data.zonecode;
            document.getElementById('address').value = addr;
            // 커서를 상세주소 필드로 이동
            document.getElementById('addressDetail').focus();
        }
    }).open();
}