// 사이드바 토글
$(function () {
    // $(".side-nav-detail ul .nav-list-detail").hide();
    $(".nav-list-detail").hide();
    $(".nav-list-sub-detail").hide();

    var isSubmenuOpen = false; // 서브메뉴가 열렸는지 상태 저장
    var isSub2menuOpen = false; // 서브메뉴가 열렸는지 상태 저장

    // $(".nav-menu > .nav-list").click(function (e) {
    $(document).on('click', '.nav-menu > .nav-list', function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).siblings(".nav-list-detail").slideToggle(300);

        // Toggle the arrow direction
        let arrow = $(this).find(".font-ico-arrow");
        if (arrow.hasClass("fa-chevron-down")) {
            arrow.removeClass("fa-chevron-down").addClass("fa-chevron-up");
            isSubmenuOpen = true;   // 중위메뉴가 열림
        } else {
            arrow.removeClass("fa-chevron-up").addClass("fa-chevron-down");
            isSubmenuOpen = false;  // 중위메뉴 닫힘
        }

        // nav-menu-sub의 하위 메뉴는 열리지 않도록 강제
        $('.nav-menu-sub .nav-list-sub-detail').slideUp(300); // 모든 하위 메뉴 닫기
        $('.nav-menu-sub .font-ico-arrow').removeClass("fa-chevron-up").addClass("fa-chevron-down"); // 화살표 원래대로 돌리기
    });

    $(document).on('click', '.nav-menu-sub > .nav-list', function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).siblings(".nav-list-sub-detail").slideToggle(300);

        // Toggle the arrow direction
        let arrow = $(this).find(".font-ico-arrow");
        if (arrow.hasClass("fa-chevron-down")) {
            arrow.removeClass("fa-chevron-down").addClass("fa-chevron-up");
            isSub2menuOpen = true;   // 하위메뉴가 열림
        } else {
            arrow.removeClass("fa-chevron-up").addClass("fa-chevron-down");
            isSub2menuOpen = false;  // 하위메뉴 닫힘
        }
    });

    // 사이드바에서 마우스가 벗어나면 서브메뉴 닫힘 처리
    $('.side-nav').mouseleave(function () {
        console.log('mouseleave event');

        // 서브메뉴가 열려있으면, 열림 -> 닫힘
        if (isSubmenuOpen) {
            $('.side-nav-detail ul .nav-list-detail').slideUp(300);
            $('.font-ico-arrow').removeClass('fa-chevron-up').addClass('fa-chevron-down');
            // $('.nav-menu-sub .nav-list-sub-detail').slideUp(300);
            console.log('서브메뉴 닫힘');
        }

        if (isSub2menuOpen) {
            $('.nav-menu-sub .nav-list-sub-detail').slideUp(300);
            $('.font-ico-arrow').removeClass('fa-chevron-up').addClass('fa-chevron-down');
            console.log('서브메뉴 닫힘');
        }
    });
});

function confirmSubmit(msg) {
    return confirm("정말로 해당 데이터를 " + msg + "하시겠습니까?");
}

document.addEventListener('DOMContentLoaded', () => {
    $.ajax({
        url: '/api/nav/menu',
        method: 'GET',
        success: function(menuList) {
            console.log("Fetched menu list: ", menuList);
            renderMenu(menuList);
            $(".nav-list-detail").hide();
        },
        error: function(error) {
            console.log("error fetching menu: ", error);
        }
    });

    // 상위 메뉴 렌더링 함수
    function renderMenu(menuList) {
        menuList.forEach(function(menu) {
            if (menu.menuLv === '0' && menu.childCnt === 0 && menu.useYn === 'Y' && menu.menuUrl.startsWith("/")) {
                const parentMenuHtml = `
                    <li class="nav-menu list-hover">
                        <a href="${menu.menuUrl}">
                            <span class="nav-list">
                                <i class="${menu.iconClass}"></i>
                                <span class="nav-list-txt">${menu.menuName}</span>
                            </span>
                        </a>
                    </li>
                `;
                $('#menuContainer').append(parentMenuHtml);
            } else if (menu.menuLv === '0' && menu.childCnt != 0 && menu.useYn === 'Y' && !menu.menuUrl.startsWith("/")) {
                const parentMenuHtml = `
                    <li class="nav-menu">
                        <span class="nav-list">
                                <i class="${menu.iconClass}"></i>
                                <span class="nav-list-txt">${menu.menuName}</span>
                                <i class="fa-solid fa-chevron-down font-ico-arrow"></i>
                            </span>
                        ${menu.childCnt > 0 ? renderMenuLv1(menu, menuList) : ''}
                    </li>
                `;
                $('#menuContainer').append(parentMenuHtml);
            }
        });
    }

    // 중위 메뉴 렌더링 함수
    function renderSubMenu(parentMenu, menuList) {
        const subMenuHtml = `
            <ul class="nav-list-detail">
                ${menuList.filter(function(menu) {
                    return menu.parentCode === parentMenu.menuCode && menu.menuLv === '1' && menu.useYn === 'Y';
                }).map(function(menu) {
                    return `
                        <li class="nav-list list-hover">
                            <a href="${menu.menuUrl}">${menu.menuName}</a>
                        </li>
                    `;
                }).join('')}
            </ul>
        `;
        return subMenuHtml;
    }

    // 중위 메뉴 렌더링 함수
    function renderMenuLv1(parentMenu, menuList) {
        const subMenuHtml = `
            <ul class="nav-list-detail">
                ${menuList.filter(function(menu) {
                    return menu.parentCode === parentMenu.menuCode && menu.menuLv === '1' && menu.useYn === 'Y';
                }).map(function(menu) {
                    if (menu.childCnt === 0) {
                        return `
                            <li class="nav-list list-hover">
                                <a href="${menu.menuUrl}">${menu.menuName}</a>
                            </li>
                        `;
                    } else {
                        return `
                            <span class="nav-menu-sub">
                                <span class="nav-list">
                                    <span class="nav-list-txt">${menu.menuName}</span>
                                    <i class="fa-solid fa-chevron-down font-ico-arrow"></i>
                                </span>
                                ${renderMenuLv2(menu, menuList)}
                            </span>
                        `;
                    }

                }).join('')}
            </ul>
        `;
        return subMenuHtml;
    }

    // 하위 메뉴 렌더링 함수
    function renderMenuLv2(parentMenu, menuList) {
        const menuLvHtml2 =  `
            <ul class="nav-list-sub-detail">
                ${menuList.filter(function(menu) {
                    return menu.parentCode === parentMenu.menuCode && menu.menuLv === '2' && menu.useYn === 'Y';
                }).map(function(menu) {
                    return `
                        <li class="nav-list list-hover">
                            <a href="${menu.menuUrl}">${menu.menuName}</a>
                        </li>
                    `;
                }).join('')}
            </ul>
        `;
        return menuLvHtml2;
    }

    // 상위 메뉴 렌더링 함수
    function renderMenu2(menuList) {
        menuList.forEach(function(menu) {
            if (menu.menuLv === '0' && menu.useYn === 'Y') {
                // 다음 메뉴가 menuLv 1인 경우
                if (menuList[index + 1] && menuList[index + 1].menuLv === '1') {
                    const parentMenuHtml = `
                        <li class="nav-menu">
                            <span class="nav-list">
                                <i class="${menu.iconClass}"></i>
                                <span class="nav-list-txt">${menu.menuName}</span>
                                <i class="fa-solid fa-chevron-down font-ico-arrow"></i>
                            </span>
                            ${menu.childCnt > 0 ? renderSubMenu2(menu, menuList) : ''}
                        </li>
                    `;
                    $('#menuContainer').append(parentMenuHtml);
                } else {
                    const parentMenuHtml = `
                        <li class="nav-menu list-hover">
                            <a href="${menu.menuUrl}">
                                <span class="nav-list">
                                    <i class="${menu.iconClass}"></i>
                                    <span class="nav-list-txt">${menu.menuName}</span>
                                </span>
                            </a>
                        </li>
                    `;
                    $('#menuContainer').append(parentMenuHtml);
                }
            }
        });
    }

    // 중위 메뉴 렌더링 함수
    function renderSubMenu2(parentMenu, menuList) {
        const subMenuHtml = `
            <ul class="nav-list-detail">
                ${menuList.filter(function(menu) {
                    return menu.parentCode === parentMenu.menuCode && menu.menuLv === '1' && menu.useYn === 'Y';
                }).map(function(menu) {
                    return `
                        <li class="nav-list list-hover">
                            <a href="${menu.menuUrl}">${menu.menuName}</a>
                        </li>
                    `;
                }).join('')}
            </ul>
        `;
        return subMenuHtml;
    }

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

            const latitude = document.getElementById('latitude');
            const longtude = document.getElementById('longtude');

            if (latitude && longtude) {
                const geocoder = new kakao.maps.services.Geocoder();
                geocoder.addressSearch(addr, (result, status) => {
                    if (status === kakao.maps.services.Status.OK) {
                        latitude.value = result[0].y;
                        longtude.value = result[0].x;
                    }
                })
            }
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

function addHyphenBizNo(input) {
    let value = input.value.replace(/\D/g, '');
    let formattedValue = '';

    if (value.length <= 3) {
        formattedValue = value;
    } else if (value.length <= 5) {
        formattedValue = value.substring(0, 3) + '-' + value.substring(3);
    } else {
        formattedValue = value.substring(0, 3) + '-' + value.substring(3, 5) + '-' + value.substring(5);
    }

    input.value = formattedValue.length <= 12 ? formattedValue : formattedValue.substring(0, 12);
}

function maskCardNum(cardNumber) {
    return cardNumber.replace(/^(\d{4})-(\d{2})\d{2}-\d{4}-(\d{4})$/, "$1-$2**-****-$3");
}

function maxLengthNum(input, len) {
    input.value = input.value.replace(/\D/g, '');

    if (input.value.length > len) {
        input.value = input.value.substring(0, len);
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