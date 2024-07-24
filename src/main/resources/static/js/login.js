const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');
const normalUserBtn = document.getElementById('normalUserBtn');
const adminUserBtn = document.getElementById('adminUserBtn');
const adminNameContainer = document.getElementById('adminNameContainer');
const nameSearch = document.getElementById('nameSearch');
const nameList = document.getElementById('nameList');
const names = ['Name1', 'Name2', 'Name3', 'Name4', 'Name5']; // Your list of names

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});

normalUserBtn.addEventListener('click', () => {
    normalUserBtn.classList.add('active');
    adminUserBtn.classList.remove('active');
    updateContent('normal');
});

adminUserBtn.addEventListener('click', () => {
    adminUserBtn.classList.add('active');
    normalUserBtn.classList.remove('active');
    updateContent('admin');
});

function updateContent(userType) {
    const signUpTitle = document.getElementById('signUpTitle');
    const signUpSpan = document.getElementById('signUpSpan');
    const signUpButton = document.getElementById('signUpButton');
    const signInTitle = document.getElementById('signInTitle');
    const signInSpan = document.getElementById('signInSpan');
    const signInButton = document.getElementById('signInButton');
    const forgotPassword = document.getElementById('forgotPassword');
    const welcomeBackTitle = document.getElementById('welcomeBackTitle');
    const welcomeBackText = document.getElementById('welcomeBackText');
    const helloTitle = document.getElementById('helloTitle');
    const helloText = document.getElementById('helloText');

    // Apply fade-out effect to the current content
    document.querySelector('.overlay-left').classList.add('fade-out');
    document.querySelector('.overlay-right').classList.add('fade-out');

    setTimeout(() => {
        if (userType === 'admin') {
            signUpTitle.textContent = '부트캠프 계정 생성';
            signUpSpan.textContent = '또는 이메일로 등록';
            signUpButton.textContent = '부트캠프 계정 등록';
            signInTitle.textContent = '부트캠프 로그인';
            signInSpan.textContent = '또는 계정으로 로그인';
            signInButton.textContent = '부트캠프 로그인';
            forgotPassword.textContent = '부트캠프 비밀번호를 잊으셨나요?';
            welcomeBackTitle.textContent = '환영합니다!';
            welcomeBackText.textContent = '부트캠프 정보를 입력해주세요';
            helloTitle.textContent = '어서오세요!';
            helloText.textContent = '여러분의 부트캠프를 소개해주세요';

            // Show the searchable name input
            adminNameContainer.style.display = 'block';
        } else {
            signUpTitle.textContent = '계정 생성';
            signUpSpan.textContent = '또는 이메일로 등록';
            signUpButton.textContent = '가입하기';
            signInTitle.textContent = '로그인';
            signInSpan.textContent = '또는 계정으로 로그인';
            signInButton.textContent = '로그인';
            forgotPassword.textContent = '비밀번호를 잊으셨나요?';
            welcomeBackTitle.textContent = '환영합니다!';
            welcomeBackText.textContent = '개인 정보로 입력해주세요';
            helloTitle.textContent = '안녕하세요!';
            helloText.textContent = '여러분의 의견을 들려주세요';

            // Hide the searchable name input
            adminNameContainer.style.display = 'none';
        }

        // Apply fade-in effect to the updated content
        document.querySelector('.overlay-left').classList.remove('fade-out');
        document.querySelector('.overlay-right').classList.remove('fade-out');
        document.querySelector('.overlay-left').classList.add('fade-in');
        document.querySelector('.overlay-right').classList.add('fade-in');
    }, 500); // Duration should match the CSS animation duration
}

// Initialize with normal user content
updateContent('normal');

// Populate name list
function populateNameList() {
    nameList.innerHTML = ''; // Clear the list
    names.forEach(name => {
        const li = document.createElement('li');
        li.textContent = name;
        li.addEventListener('click', () => {
            nameSearch.value = name;
            nameList.style.display = 'none';
        });
        nameList.appendChild(li);
    });
}

populateNameList();

// Search functionality
nameSearch.addEventListener('input', () => {
    const filter = nameSearch.value.toLowerCase();
    const items = nameList.querySelectorAll('li');
    items.forEach(item => {
        const text = item.textContent.toLowerCase();
        item.style.display = text.includes(filter) ? '' : 'none';
    });
    nameList.style.display = filter ? 'block' : 'none';
});

document.addEventListener('click', (event) => {
    if (!adminNameContainer.contains(event.target)) {
        nameList.style.display = 'none';
    }
});