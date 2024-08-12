import { getTokenFromLocalStorage } from './auth.js';

document.addEventListener('DOMContentLoaded', function() {
    const page = 0;
    const size = 10;
    loadNotifications(page, size);

    if (window.socket) {
        window.socket.onmessage = function(event) {
            let messageData;

            try {
                messageData = JSON.parse(event.data);
            } catch (e) {
                messageData = { message: event.data };
            }

            console.log('서버로부터 메시지가 도착했습니다:', messageData);

            Swal.fire({
                toast: true,
                position: 'center',
                icon: 'info',
                title: messageData.message,
                showConfirmButton: true,
                customClass: {
                    title: 'black-text'
                }
            });

            updateNotificationList(messageData);
        };

        window.socket.onclose = function(event) {
            console.log('WebSocket 연결이 닫혔습니다:', event);
        };

        window.socket.onerror = function(error) {
            console.error('WebSocket 오류가 발생했습니다:', error);
        };
    } else {
        console.error('WebSocket이 설정되지 않았습니다.');
    }
});

window.markAsRead = function(notificationId) {
    if (notificationId !== null) {
        axios.post(`/api/notifications/${notificationId}/read`)
            .then(() => {
                const liElement = document.querySelector(`li[data-id='${notificationId}']`);
                if (liElement) {
                    liElement.classList.remove('unread');
                    liElement.classList.add('read');

                    const button = liElement.querySelector('button');
                    if (button) {
                        button.textContent = '읽음';
                        button.disabled = true;
                    }
                }
            })
            .catch(err => console.error('알림 읽음 처리 중 오류 발생:', err));
    }
};

function loadNotifications(page = 0, size = 10) {
    const token = getTokenFromLocalStorage();
    if (!token) {
        console.error('토큰을 가져올 수 없습니다. 사용자가 로그인되어 있는지 확인하세요.');
        return;
    }

    axios.get('/api/notifications', {
        headers: {
            'Authorization': `Bearer ${token}`
        },
        params: {
            page: page,
            size: size
        }
    })
        .then(response => {
            console.log('받은 알림 데이터:', response.data);
            const notificationList = document.getElementById('notificationList');
            const paginationContainer = document.getElementById('pagination');

            if (notificationList) {
                notificationList.innerHTML = '';
                if (response.data.content.length === 0) {
                    notificationList.innerHTML = '<li>알림이 없습니다.</li>';
                } else {
                    response.data.content.forEach(notification => {
                        updateNotificationList(notification);
                    });
                }

                if (paginationContainer) {
                    setupPagination(paginationContainer, response.data.number, response.data.totalPages);
                } else {
                    console.error('pagination 요소를 찾을 수 없습니다.');
                }
            } else {
                console.error('notificationList 요소를 찾을 수 없습니다.');
            }
        })
        .catch(err => {
            console.error('알림 목록 로딩 중 오류 발생:', err);
            if (err.response && err.response.status === 401) {
                Swal.fire({
                    icon: 'error',
                    title: '인증 오류',
                    text: '로그인이 만료되었거나 유효하지 않은 인증 정보입니다. 다시 로그인해 주세요.'
                });
            }
        });
}

function updateNotificationList(notification) {
    const notificationList = document.getElementById('notificationList');
    if (notificationList) {
        const newNotification = document.createElement('li');
        newNotification.classList.add(notification.read ? 'read' : 'unread');
        newNotification.setAttribute('data-id', notification.id || '');
        newNotification.innerHTML = `
            <span>${notification.message}</span>
            ${notification.id ? `<button onclick="markAsRead(${notification.id})">${notification.read ? '읽음' : '확인'}</button>` : ''}
        `;
        notificationList.prepend(newNotification);
    }
}

function setupPagination(container, currentPage, totalPages) {
    if (!container) {
        console.error('pagination 컨테이너가 존재하지 않습니다.');
        return;
    }

    container.innerHTML = '';

    if (currentPage > 0) {
        const prevButton = document.createElement('button');
        prevButton.textContent = 'Previous';
        prevButton.onclick = () => loadNotifications(currentPage - 1);
        container.appendChild(prevButton);
    }

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i + 1;
        if (i === currentPage) {
            pageButton.classList.add('active');
        }
        pageButton.onclick = () => loadNotifications(i);
        container.appendChild(pageButton);
    }

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('button');
        nextButton.textContent = 'Next';
        nextButton.onclick = () => loadNotifications(currentPage + 1);
        container.appendChild(nextButton);
    }
}
