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