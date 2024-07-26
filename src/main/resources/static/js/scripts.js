// 테마 토글 함수
function toggleTheme() {
    const body = document.body;
    const toggleCircle = document.querySelector('.toggle-circle');
    body.classList.toggle('dark');
    toggleCircle.classList.toggle('dark');

    // 현재 테마를 localStorage에 저장
    if (body.classList.contains('dark')) {
        localStorage.setItem('theme', 'dark');
    } else {
        localStorage.setItem('theme', 'light');
    }
}

// 페이지 로드 시 저장된 테마를 적용
window.addEventListener('DOMContentLoaded', (event) => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        const body = document.body;
        const toggleCircle = document.querySelector('.toggle-circle');
        if (savedTheme === 'dark') {
            body.classList.add('dark');
            toggleCircle.classList.add('dark');
        } else {
            body.classList.remove('dark');
            toggleCircle.classList.remove('dark');
        }
    }
});

// 사이드바 토글 함수
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');
    const openButton = document.querySelector('.open-sidebar-button');

    sidebar.classList.toggle('closed');
    openButton.classList.toggle('hidden');

    if (sidebar.classList.contains('closed')) {
        content.style.marginLeft = '30px';
    } else {
        content.style.marginLeft = '300px';
    }
}

// 페이지 로드 시 로그인 상태를 체크하여 버튼을 업데이트하는 함수
function checkLoginStatus() {
    const loginButton = document.querySelector('.add-task-button');
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
        loginButton.textContent = 'Logout';
        loginButton.onclick = onLogout;
    } else {
        loginButton.textContent = 'Login';
        loginButton.onclick = () => location.href = '/api/auth';
    }
}

// 로그아웃 성공 시 호출되는 함수
function onLogoutSuccess() {
    const loginButton = document.querySelector('.add-task-button');
    loginButton.textContent = 'Login';
    loginButton.onclick = () => location.href = '/api/auth';
    alert('로그아웃 성공');
}

// 로그아웃 요청을 보내는 함수
function onLogout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    onLogoutSuccess();
}

// DOMContentLoaded 이벤트 리스너에 로그인 상태 체크 함수 추가
document.addEventListener('DOMContentLoaded', (event) => {
    toggleSidebar();
    checkLoginStatus(); // 로그인 상태를 체크하는 함수 호출
});
