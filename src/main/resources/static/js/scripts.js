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


window.addEventListener('DOMContentLoaded', (event) => {
    toggleSidebar();
    checkLoginStatus();
});

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
