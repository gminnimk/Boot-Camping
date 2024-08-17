document.addEventListener("DOMContentLoaded", async function() {
    const username = window.username;
    const socket = window.chatSocket;

    if (!username) {
        console.error("username이 정의되지 않았습니다.");
        return;
    }

    if (!socket) {
        console.error("WebSocket 연결이 설정되지 않았습니다.");
        return;
    }

    const messageArea = document.getElementById('messageArea');
    const messageInput = document.getElementById('messageInput');
    const sendButton = document.getElementById('sendButton');

    try {
        const response = await fetch('/api/chat');
        const result = await response.json();

        if (response.ok) {
            const messages = result.data;
            messages.forEach((message) => {
                const newMessage = document.createElement('div');
                newMessage.textContent = `${message.name}: ${message.message}`;
                newMessage.classList.add('message');
                if (message.username === username) {
                    newMessage.classList.add('sent');
                } else {
                    newMessage.classList.add('received');
                }
                messageArea.appendChild(newMessage);
            });
            messageArea.scrollTop = messageArea.scrollHeight;
        } else {
            console.error("메시지를 불러오는 중 오류가 발생했습니다:", result);
        }
    } catch (error) {
        console.error("초기 메시지를 불러오는 중 오류가 발생했습니다:", error);
    }

    socket.onmessage = function(event) {
        console.log('서버로부터 실시간 메시지가 도착했습니다:', event.data);
        const data = JSON.parse(event.data);
        const newMessage = document.createElement('div');
        newMessage.textContent = `${data.name}: ${data.message}`;
        newMessage.classList.add('message');
        if (data.username === username) {
            newMessage.classList.add('sent');
        } else {
            newMessage.classList.add('received');
        }
        messageArea.appendChild(newMessage);
        messageArea.scrollTop = messageArea.scrollHeight;
    };

    function sendMessage() {
        const message = messageInput.value;
        if (message) {
            socket.send(JSON.stringify({ message: message, username: username }));
            messageInput.value = '';
        }
    }

    sendButton.addEventListener('click', function() {
        sendMessage();
    });

    messageInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
            event.preventDefault();
        }
    });

    socket.onerror = function(error) {
        console.error('WebSocket 오류가 발생했습니다:', error);
    };

    socket.onclose = function(event) {
        console.log('WebSocket 연결이 닫혔습니다:', event);
    };
});