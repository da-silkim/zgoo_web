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

    $('#editPasswordModal').on('show.bs.modal', function () {
        $('#dataAddModal .modal-content').addClass('blur-background');
    });

    $('#editPasswordModal').on('hidden.bs.modal', function () {
        $('#dataAddModal .modal-content').removeClass('blur-background');
    });

    $('#bizSearchModal').on('show.bs.modal', function () {
        $('#dataAddModal .modal-content').addClass('blur-background');
    });

    $('#bizSearchModal').on('hidden.bs.modal', function () {
        $('#dataAddModal .modal-content').removeClass('blur-background');
    });

    $('#conditionModal').on('show.bs.modal', function () {
        $('#dataAddModal .modal-content').addClass('blur-background');
    });

    $('#conditionModal').on('hidden.bs.modal', function () {
        $('#dataAddModal .modal-content').removeClass('blur-background');
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
                var parentMenuHtml = '';
                if (menu.menuUrl === '/map') {
                    parentMenuHtml = `
                        <li class="nav-menu list-hover">
                            <a href="javascript:openMap('${menu.menuUrl}')" target="_self">
                                <span class="nav-list">
                                    <i class="${menu.iconClass}"></i>
                                    <span class="nav-list-txt">${menu.menuName}</span>
                                </span>
                            </a>
                        </li>
                    `;
                } else {
                    parentMenuHtml = `
                        <li class="nav-menu list-hover">
                            <a href="${menu.menuUrl}">
                                <span class="nav-list">
                                    <i class="${menu.iconClass}"></i>
                                    <span class="nav-list-txt">${menu.menuName}</span>
                                </span>
                            </a>
                        </li>
                    `;
                }
                
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


$(document).ready(function () {
    formatNumbers('.price-format', '원');
    formatNumbers('.int-format');
});

// 금액 단위 포맷 함수
function formatNumbers(selector, suffix = '') {
    $(selector).each(function () {
        const number = parseInt($(this).text().replace(/[^0-9]/g, ''), 10);
        if (!isNaN(number)) {
            $(this).text(number.toLocaleString('ko-KR') + suffix);
        }
    });
}

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
            document.getElementById('addressDetail').focus();

            const latitude = document.getElementById('latitude');
            const longitude = document.getElementById('longitude');
            const sido = document.getElementById('sido');

            if (latitude && longitude) {
                const geocoder = new kakao.maps.services.Geocoder();
                geocoder.addressSearch(addr, (result, status) => {
                    if (status === kakao.maps.services.Status.OK) {
                        latitude.value = result[0].y;
                        longitude.value = result[0].x;

                        if (sido) {
                            sido.value = toAddress(longitude.value, latitude.value);
                        }
                    }
                })
            }
        }
    }).open();
}

function toAddress(lon, lat) {
    if (!lat || !lon) {
        console.warn('좌표 값이 없습니다. lat:', lat, ', lng:', lng);
        return;
    }

    $.ajax({
        url: `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${lon}&y=${lat}&input_coord=WGS84`,
        type: 'GET',
        headers: {
            'Authorization' : 'KakaoAK 4fae289535008a0dea1eb592bc662462'
        },
        success: function(result) {
            console.log(result);
            let totalCount = result.meta.total_count;
            if (totalCount > 0) {
                addr = result.documents[0].region_1depth_name;
                document.getElementById('sido').value = addr;
                console.log('addr: ', addr);
            }
        },
        error: function(e) {
            console.error(e);
        }
    });
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

function phoneFormat(input) {
    let value = input.value.replace(/[^0-9]/g, '');

    if (value.startsWith('02')) {
        if (value.length > 10) {
            value = value.slice(0, 10);
        }
    } else if (value.length > 11) {
        value = value.slice(0, 11);
    }

    input.value = value.replace(/(^02|^0[0-9]{2})([0-9]{3,4})([0-9]{4})/, `$1-$2-$3`);
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

function openMap(menuUrl) {
    window.open(menuUrl, 'mapPopup', 'width=1300,height=820,scrollbars=yes,resizable=yes');
}

function replacePage(pageUrl) {
    window.location.replace(pageUrl);
}

function searchOption() {
    const selectedSize = document.getElementById('size').value;
    const form = document.getElementById('searchForm');

    const hiddenSizeInput = document.createElement('input');
    hiddenSizeInput.type = 'hidden';
    hiddenSizeInput.name = 'size';
    hiddenSizeInput.value = selectedSize;
    hiddenSizeInput.id = 'hiddenSizeInput';

    form.appendChild(hiddenSizeInput);
    form.submit();
}

function updatePageSize(selectElement, baseUrl, paramKeys) {
    const urlParams = new URLSearchParams(window.location.search);
    const selectedSize = selectElement.value;

    let newUrl = `${baseUrl}?page=0&size=${selectedSize}`;

    paramKeys.forEach(key => {
        const value = urlParams.get(key) || '';
        newUrl += `&${key}=${encodeURIComponent(value)}`;
    });

    window.location.href = newUrl;
}

function excelDownload(baseUrl, paramKeys) {
    const urlParams = new URLSearchParams(window.location.search);
    let newUrl = `${baseUrl}?`;

    paramKeys.forEach(key => {
        const value = urlParams.get(key) || '';
        newUrl += `&${key}=${encodeURIComponent(value)}`;
    });

    // 먼저 fetch로 권한 확인
    fetch(newUrl, {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    }).then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                console.error(err.error);
            });
        }

        // 권한 있으면 다운로드 링크 생성 및 클릭
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = newUrl;
        document.body.appendChild(a);
        a.click();

        setTimeout(() => {
            document.body.removeChild(a);
        }, 1000);

    }).catch(error => {
        console.error("요청 중 오류 발생:", error);
    });

}

function goToList(baseUrl, paramKeys) {
    const urlParams = new URLSearchParams(window.location.search);
    const selectedPage = urlParams.get('page') || 0;
    const selectedSize = urlParams.get('size') || 10;

    let newUrl = `${baseUrl}?page=${selectedPage}&size=${selectedSize}`;

    paramKeys.forEach(key => {
        const value = urlParams.get(key) || '';
        newUrl += `&${key}=${encodeURIComponent(value)}`;
    });

    window.location.href = newUrl;
}

function buttonControl(row, url) {
    const cbox = row.closest('tr').find('input[type="checkbox"]');
    if (cbox.length > 0 && cbox.is(':checked')) {
        console.log('Checkbox is checked.');
        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                if (response.btnControl) {
                    $('#buttonContainer').html(`
                        <button class="btn btn-data-edit" id="editBtn"
                            data-bs-toggle="modal" data-bs-target="#dataAddModal">
                            <i class="fa-regular fa-pen-to-square"></i>수정
                        </button>
                        <button class="btn btn-data-delete" id="deleteBtn">
                            <i class="bi bi-trash"></i>삭제
                        </button>
                    `);
                } else {
                    $('#buttonContainer').empty();
                }
            },
            error: function(error) {
                console.error(error);
            }
        });
    } else {
        console.log('Checkbox is not checked.');
        btnControl = false;
        $('#buttonContainer').empty();
    }
}