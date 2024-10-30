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