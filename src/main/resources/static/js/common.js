// 사이드바 토글
$(function () {
    $(".side-nav-detail ul .nav-list-detail").hide();

    var isSubmenuOpen = false; // 서브메뉴가 열렸는지 상태 저장

    $(".nav-menu > .nav-list").click(function (e) {
        e.preventDefault();
        $(this).siblings(".nav-list-detail").slideToggle(300);

        // Toggle the arrow direction
        let arrow = $(this).find(".font-ico-arrow");
        if (arrow.hasClass("fa-chevron-down")) {
            arrow.removeClass("fa-chevron-down").addClass("fa-chevron-up");
            isSubmenuOpen = true;   // 서브메뉴가 열림
        } else {
            arrow.removeClass("fa-chevron-up").addClass("fa-chevron-down");
            isSubmenuOpen = false;  // 서브메뉴 닫힘
        }
    });

    // 사이드바에서 마우스가 벗어나면 서브메뉴 닫힘 처리
    $('.side-nav').mouseleave(function () {
        console.log('mouseleave event');

        // 서브메뉴가 열려있으면, 열림 -> 닫힘
        if (isSubmenuOpen) {
            $('.side-nav-detail ul .nav-list-detail').slideUp(300);
            $('.font-ico-arrow').removeClass('fa-chevron-up').addClass('fa-chevron-down');
            console.log('서브메뉴 닫힘');
        }
    });
});

function confirmSubmit(msg) {
    return confirm("정말로 해당 데이터를 " + msg + "하시겠습니까?");
}

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
$(document).ready(function () {
    $('.price-format').each(function () {
        var price = parseInt($(this).text(), 10);
        $(this).text(price.toLocaleString('ko-KR'));
    });
});

// 주소 검색 함수
function postSearch() {
    new daum.Postcode({
        oncomplete: function (data) {
            var addr = ''; // 주소 변수

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            document.getElementById('zipCode').value = data.zonecode;
            document.getElementById('address').value = addr;
            // 커서를 상세주소 필드로 이동
            document.getElementById('addressDetail').focus();
        }
    }).open();
}

// 날짜 포맷팅 함수(ex. 2024-10-29 12:13:00)
function formatDate(date) {
    // padStart(2, '0'): 2자리 수로 포맷팅
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 + 1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// input[type="number"] maxLength setting function
function maxLengthCheck(object) {
    if(object.value.length > object.maxLength) {
        object.value = object.value.slice(0, object.maxLength);
    }
}

function addHyphen(input) {
    let value = input.value.replace(/\D/g, '');  // 숫자만 추출
    let formattedValue = '';

    if (value.length <= 3) {
        formattedValue = value;
    } else if (value.length <= 6) {
        formattedValue = value.substring(0, 3) + '-' + value.substring(3);
    } else if (value.length <= 10) {
        formattedValue = value.substring(0, 3) + '-' + value.substring(3, 6) + '-' + value.substring(6);
    } else {
        formattedValue = value.substring(0, 3) + '-' + value.substring(3, 7) + '-' + value.substring(7, 11);
    }

    // maxlength을 넘지 않도록 체크
    if (formattedValue.length <= 13) {
        input.value = formattedValue;
    } else {
        input.value = formattedValue.substring(0, 13);
    }
}


// Regular expression
// 1. email
function isEmail(asValue) {
	var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    var result = regExp.test(asValue);

    if (!result) {
        alert('유효하지 않은 이메일 형식입니다.');
    }

	return result;
}

// 2. id(영문으로 시작, 영문+숫자 조합 6~20자)
function isId(asValue) {
	var regExp = /^[a-z][a-z0-9]{5,19}$/
    var result = regExp.test(asValue);

    if (!result) {
        alert("아이디는 영문자로 시작하고 6~20자의 영문자 및 숫자로 구성되어야 합니다.");
    }

    return result;
}

// 3. password(8~16자 영문, 숫자 조합)
function isPassword(asValue) {
	var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
    var result = regExp.test(asValue);

    if(!result) {
        alert("비밀번호는 8~16자의 영문자와 숫자를 최소 하나씩 포함해야 합니다.");
    }   

    return result;
}

// 4. password(8~16자 영문, 숫자, 특수문자 최소 한 가지씩 조합)
function isPasswordSpecial(asValue) {
    // 사용가능 특수문자: $~!@%*^?&()-_=+
	var regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
    // var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[a-zA-Z0-9]{8,16}$/;
    var result = regExp.test(asValue);

    if (!result) {
        alert("비밀번호는 8~16자의 영문자, 숫자, 특수문자를 최소 하나씩 포함해야 합니다.");
    }

	return result;
}