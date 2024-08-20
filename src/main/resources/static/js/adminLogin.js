// Get elements by their IDs
const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');
const loginForm = document.getElementById('loginForm');
const signupForm = document.getElementById('signupForm');

// Handle sign-up and sign-in button clicks
signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});

document.addEventListener('DOMContentLoaded', function() {
    const logoLink = document.getElementById('logoLink');

    logoLink.addEventListener('click', function(e) {
        e.preventDefault(); // 기본 앵커 동작 방지
        window.location.href = '/home'; // /home으로 페이지 이동
    });
});

// Validate username function
function validateUsername() {
    const username = document.getElementById('signupId');
    const usernameError = document.getElementById('signupIdError');
    const usernamePattern = /^[a-z0-9]+$/;

    if (!usernamePattern.test(username.value) || username.value.length < 6 || username.value.length > 20) {
        username.classList.add('invalid');
        usernameError.style.display = 'block';
        usernameError.textContent = '아이디는 소문자 + 숫자로 구성되어야 하며, 6자 이상 20자 이하이어야 합니다.';
        return false;
    } else {
        username.classList.remove('invalid');
        usernameError.style.display = 'none';
        return true;
    }
}

// Validate password function
function validatePassword() {
    const password = document.getElementById('signupPassword');
    const passwordError = document.getElementById('signupPasswordError');
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])\S{6,}$/;

    if (!passwordPattern.test(password.value) || password.value.length < 6 || password.value.length > 255) {
        password.classList.add('invalid');
        passwordError.style.display = 'block';
        passwordError.textContent = '비밀번호는 알파벳 대소문자 + 숫자 + 특수문자로 구성되어야 하며, 최소 6자 이상이어야 합니다.';
        return false;
    } else {
        password.classList.remove('invalid');
        passwordError.style.display = 'none';
        return true;
    }
}

// Validate name function
function validateName() {
    const name = document.getElementById('signupName');
    const nameError = document.getElementById('signupNameError');
    const namePattern = /^[a-zA-Z가-힣]+$/;

    if (!namePattern.test(name.value) || name.value.length < 2 || name.value.length > 20) {
        name.classList.add('invalid');
        nameError.style.display = 'block';
        nameError.textContent = '이름은 한글 또는 영어로 구성되어야 하며, 2자 이상 20자 이하이어야 합니다.';
        return false;
    } else {
        name.classList.remove('invalid');
        nameError.style.display = 'none';
        return true;
    }
}

// Event listeners for validation on blur
document.getElementById('signupId').addEventListener('blur', validateUsername);
document.getElementById('signupPassword').addEventListener('blur', validatePassword);
document.getElementById('signupName').addEventListener('blur', validateName);

// 회원가입 버튼 이벤트 리스너
document.getElementById('signUpButton').addEventListener('click', async (event) => {
    event.preventDefault();

    const username = document.getElementById('signupId');
    const password = document.getElementById('signupPassword');
    const name = document.getElementById('signupName');

    // 유효성 검사 패턴
    const usernamePattern = /^[a-z0-9]{6,20}$/;
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])\S{6,}$/;
    const namePattern = /^[a-zA-Z가-힣]{2,20}$/;

    const isUsernameValid = validateUsername();
    const isPasswordValid = validatePassword();
    const isNameValid = validateName();

    if (isUsernameValid && isPasswordValid && isNameValid) {
        const data = {
            username: username.value,
            password: password.value,
            name: name.value
        };

        fetch(`/api/admin/auth/signup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
            if (data.statuscode === '201') {
                Swal.fire({
                    title: '회원가입 완료',
                    text: '회원가입이 완료되었습니다.',
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then(() => {
                    window.location.reload();
                });
            } else {
                Swal.fire({
                    title: '회원가입 실패',
                    text: `오류 발생: ${data.msg}`,
                    icon: 'error',
                    confirmButtonText: '확인'
                });
            }
        })
        .catch(error => {
            console.error('서버 오류:', error);
            Swal.fire({
                title: '회원가입 실패',
                text: '서버와의 통신 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        });
    }
});

// 로그인 버튼 이벤트 리스너
document.getElementById('signInButton').addEventListener('click', async (event) => {
    event.preventDefault();

    const username = document.getElementById('loginId').value;
    const password = document.getElementById('loginPassword').value;

    const data = {
        username: username,
        password: password
    };

    try {
        const response = await fetch('/api/admin/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (response.ok) {
            localStorage.setItem('accessToken', result.data.accessToken);
            localStorage.setItem('refreshToken', result.data.refreshToken);
            window.location.href = '/home';
        } else {
            Swal.fire({
                title: '로그인 실패',
                text: result.message || '알 수 없는 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        }
    } catch (error) {
        console.error('서버 오류:', error);
        Swal.fire({
            title: '로그인 실패',
            text: '서버와의 통신 오류가 발생했습니다.',
            icon: 'error',
            confirmButtonText: '확인'
        });
    }
});

// Theme toggle functionality (assuming the function exists)
function toggleTheme() {
    const isDarkMode = document.body.classList.toggle('dark-theme');
    const themeToggleCircle = document.querySelector('.toggle-circle');
    if (isDarkMode) {
        themeToggleCircle.classList.add('dark');
    } else {
        themeToggleCircle.classList.remove('dark');
    }
}

// Add event listener for theme toggle
document.querySelector('.toggle-container').addEventListener('click', toggleTheme);
