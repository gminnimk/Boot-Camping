document.addEventListener('DOMContentLoaded', function () {
    const socket = new WebSocket("ws://localhost:8080/chat");

    const messageArea = document.getElementById('messageArea');
    const messageInput = document.getElementById('messageInput');

    socket.onmessage = function(event) {
        const newMessage = document.createElement('div');
        newMessage.textContent = event.data;
        messageArea.appendChild(newMessage);
        messageArea.scrollTop = messageArea.scrollHeight; // 자동 스크롤
    };

    document.querySelector('button').addEventListener('click', function () {
        const message = messageInput.value;
        if (message) {
            socket.send(message);
            messageInput.value = '';
        }
    });

    // SweetAlert 예시
    document.querySelector('button').addEventListener('click', function () {
        Swal.fire({
            title: '메시지 전송',
            text: '메시지가 성공적으로 전송되었습니다.',
            icon: 'success',
            confirmButtonText: '확인'
        });
    });

    // 사이드바 토글 기능
    window.toggleSidebar = function() {
        const sidebar = document.querySelector('.sidebar');
        sidebar.classList.toggle('closed');

        const openButton = document.querySelector('.open-sidebar-button');
        openButton.classList.toggle('hidden');
    };
});