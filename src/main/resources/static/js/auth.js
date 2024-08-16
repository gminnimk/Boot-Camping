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

export const token = getTokenFromLocalStorage();
export let username = null;
export let notificationSocket = null;
export let chatSocket = null;

if (token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    console.log('디코딩된 사용자 정보:', payload);

    username = payload.sub;
    console.log('로그인한 사용자 username:', username);

    notificationSocket = new WebSocket(`wss://bootcamping.org/notifications?username=${username}`);

    notificationSocket.onopen = function() {
        console.log('Notification WebSocket 연결이 설정되었습니다');
    };

    notificationSocket.onmessage = function(event) {
        const message = event.data;
        console.log('서버로부터 알림 메시지가 도착했습니다:', message);

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

    notificationSocket.onclose = function(event) {
        console.log('Notification WebSocket 연결이 닫혔습니다:', event);
    };

    notificationSocket.onerror = function(error) {
        console.error('Notification WebSocket 오류가 발생했습니다:', error);
    };

    chatSocket = new WebSocket(`wss://bootcamping.org/chat?username=${username}`);

    chatSocket.onopen = function() {
        console.log('Chat WebSocket 연결이 설정되었습니다');
    };

    chatSocket.onmessage = function(event) {
        console.log('서버로부터 채팅 메시지가 도착했습니다:', event.data);
    };

    chatSocket.onclose = function(event) {
        console.log('Chat WebSocket 연결이 닫혔습니다:', event);
    };

    chatSocket.onerror = function(error) {
        console.error('Chat WebSocket 오류가 발생했습니다:', error);
    };
}

window.username = username;
window.notificationSocket = notificationSocket;
window.chatSocket = chatSocket;
