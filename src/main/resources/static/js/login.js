document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    const loginBtn = document.getElementById("login-form-btn");
    const rememberMeCheckbox = loginForm.querySelector('input[type="checkbox"]');

    // 페이지 로드 시 쿠키에서 ID를 불러오기
    const savedId = getCookie("savedId");
    if (savedId) {
        loginForm.id.value = savedId;
        rememberMeCheckbox.checked = true;
    }

    loginBtn.addEventListener("click", (event) => {
        event.preventDefault();
        event.stopPropagation();

        const id = loginForm.id.value.trim();
        const password = loginForm.password.value.trim();

        // 유효성 검사 통과 실패
        if (!validateForm(loginForm)) {
            loginForm.classList.add("was-validated");
            return;
        }

        if (rememberMeCheckbox.checked) {
            setCookie("savedId", id, 7); // 7일 동안 ID 저장
        } else {
            eraseCookie("savedId");
        }

        // location.replace('../dashboard.html');
        location.replace('/dashboard');
        // location.replace('/commoncode');
        
        // if (id === "dongah" && password === "dongah1!") {
        //     alert("Login successful");
        //     location.replace('../dashboard.html');
        // } else {
        //     alert("Login failed");
        // }
    });

    // 유효성 검사 처리
    function validateForm(form) {
        return form.checkValidity();
    }
});

// 쿠키 설정
function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString;
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

// 쿠키 읽기
function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while(c.charAt(0) === ' ') c = c.substring(1, c.length);
        if(c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

// 쿠키 삭제
function eraseCookie(name) {
    document.cookie = name + '=; Max-Age=-99999999;';
}