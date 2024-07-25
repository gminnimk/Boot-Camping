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

    // 로그아웃 버튼 클릭 시 로그아웃 요청을 보내고 로그인 페이지로 리다이렉트
    const logoutButton = document.querySelector('.nav-item.logout');
    if (logoutButton) {
        logoutButton.addEventListener('click', function() {
            fetch('/api/auth/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                credentials: 'include',
            })
                .then(response => {
                    if (response.ok) {
                        // Clear tokens from localStorage
                        localStorage.removeItem('accessToken');
                        localStorage.removeItem('refreshToken');
                        window.location.href = '/login'; // 로그아웃 성공 시 로그인 페이지로 리다이렉트
                    } else {
                        console.error('Logout failed');
                    }
                })
                .catch(error => console.error('Error:', error));
        });
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
});
