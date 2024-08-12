import { getTokenFromLocalStorage } from './auth.js';

document.addEventListener("DOMContentLoaded", function() {
    const token = getTokenFromLocalStorage();
    let username = null;

    if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        username = payload.sub;
    }

    if (!username) {
        console.error("username이 정의되지 않았습니다.");
        return;
    }

    const messageArea = document.getElementById('messageArea');
    const messageInput = document.getElementById('messageInput');
    const sendButton = document.getElementById('sendButton');

    socket.onmessage = function(event) {
        const newMessage = document.createElement('div');
        newMessage.textContent = event.data;
        messageArea.appendChild(newMessage);
        messageArea.scrollTop = messageArea.scrollHeight;
    };

    sendButton.addEventListener('click', function() {
        const message = messageInput.value;
        if (message) {
            socket.send(JSON.stringify({ message: message, username: username }));
            messageInput.value = '';
        }
    });
});
