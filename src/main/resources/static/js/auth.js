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
export let username = sessionStorage.getItem('username') || null;
export let eventSource = null;

function initializeEventSource() {
    if (eventSource) {
        console.log('기존 eventSource를 닫습니다.');
        eventSource.close();
        eventSource = null;
    }

    const encodedToken = encodeURIComponent(token);
    const sseUrl = `/api/notifications/stream?username=${encodeURIComponent(username)}&token=${encodedToken}`;
    console.log('SSE 연결 URL:', sseUrl);

    eventSource = new EventSource(sseUrl);
    window.eventSource = eventSource;

    eventSource.onopen = function(event) {
        console.log('SSE 연결이 성공적으로 열렸습니다.');
    };

    eventSource.onmessage = function(event) {
        console.log('서버로부터 메시지가 도착했습니다:', event.data);

        let messageData;
        try {
            messageData = JSON.parse(event.data);
            console.log('파싱된 알림 데이터:', messageData);
        } catch (e) {
            messageData = { message: event.data };
        }

        Swal.fire({
            toast: true,
            position: 'center',
            icon: 'info',
            title: messageData.message || event.data,
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

    eventSource.onerror = function(error) {
        console.error('SSE 연결 오류:', error);
        if (eventSource.readyState === EventSource.CLOSED) {
            console.log('SSE 연결이 닫혔습니다. 5초 후에 재연결을 시도합니다.');
            setTimeout(initializeEventSource, 5000);
        }
    };
}

if (token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        console.log('디코딩된 사용자 정보:', payload);

        username = payload.sub;
        sessionStorage.setItem('username', username);
        console.log('로그인한 사용자 username:', username);

        initializeEventSource();
    } catch (error) {
        console.error('토큰 디코딩 중 오류 발생:', error);
    }
} else {
    console.error('사용자 인증 토큰을 찾을 수 없습니다.');
}

console.log('최종 eventSource:', eventSource);

window.username = username;
window.eventSource = eventSource;
