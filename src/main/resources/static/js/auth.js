export function saveTokenToLocalStorage(token) {
    localStorage.setItem('accessToken', token);
}

export function getTokenFromLocalStorage() {
    return localStorage.getItem('accessToken');
}

export function saveTokenToSessionStorage(token) {
    sessionStorage.setItem('accessToken', token);
}

export function getTokenFromSessionStorage() {
    return sessionStorage.getItem('accessToken');
}

export function saveTokenToCookie(token) {
    document.cookie = "accessToken=" + token + "; path=/";
}

export function getTokenFromCookie() {
    return document.cookie
        .split('; ')
        .find(row => row.startsWith('accessToken'))
        ?.split('=')[1];
}

export function setAuthHeader(config) {
    const token = getTokenFromLocalStorage();
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
}

const token = getTokenFromLocalStorage();
let username = null;

if (token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    console.log('디코딩된 사용자 정보:', payload);

    username = payload.sub;
    console.log('로그인한 사용자 username:', username);
}

window.socket = new WebSocket(`ws://localhost:8080/ws/notifications?username=${username}`);

socket.onopen = function() {
    console.log('WebSocket 연결이 설정되었습니다');
};

socket.onmessage = function(event) {
    const message = event.data;
    console.log('서버로부터 메시지가 도착했습니다:', message);

    Swal.fire({
        toast: true,
        position: 'center',
        icon: 'info',
        title: message,
        showConfirmButton: true,
        customClass: {
            title: 'black-text'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            location.reload();
        }
    });
};

socket.onclose = function(event) {
    console.log('WebSocket 연결이 닫혔습니다:', event);
};

socket.onerror = function(error) {
    console.error('WebSocket 오류가 발생했습니다:', error);
};
