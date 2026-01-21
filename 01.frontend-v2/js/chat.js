// 전역 변수
let conversationId = null;
let projectName = 'default-project'; // Vector Store용 프로젝트 이름
let isTyping = false;
let apiPath = '/api/chat'; // 기본 API 경로

// DOM 요소
const chatForm = document.getElementById('chatForm');
const messageInput = document.getElementById('messageInput');
const messagesContainer = document.getElementById('messages');
const sendBtn = document.getElementById('sendBtn');
const newChatBtn = document.getElementById('newChatBtn');

// 초기화
document.addEventListener('DOMContentLoaded', () => {
    initializeEventListeners();
    adjustTextareaHeight();
});

// 이벤트 리스너 초기화
function initializeEventListeners() {
    chatForm.addEventListener('submit', handleSubmit);
    messageInput.addEventListener('input', adjustTextareaHeight);
    messageInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && !e.shiftKey && !e.isComposing) {
            e.preventDefault();
            handleSubmit(e);
        }
    });
    
    // Path Selector 이벤트
    const pathSelector = document.getElementById('pathSelector');
    if (pathSelector) {
        // URL 파라미터로 초기값 설정
        pathSelector.value = apiPath;
        
        // 선택 변경 시 API Path 업데이트
        pathSelector.addEventListener('change', function() {
            apiPath = this.value;
            console.log('API Path 변경됨:', apiPath);
        });
    }
    
    newChatBtn.addEventListener('click', startNewConversation);
}

// 메시지 전송 처리
async function handleSubmit(e) {
    e.preventDefault();
    
    const message = messageInput.value.trim();
    if (!message || isTyping) return;
    
    const welcomeMessage = document.querySelector('.welcome-message');
    if (welcomeMessage) {
        welcomeMessage.remove();
    }
    
    appendMessage(message, 'user');
    messageInput.value = '';
    adjustTextareaHeight();
    
    showTypingIndicator();
    isTyping = true;
    sendBtn.disabled = true;
    
    try {
        // Form data 생성
        const formData = new URLSearchParams();
        formData.append('question', message);
    
        
        const response = await fetch(apiPath, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        });
        
        if (!response.ok) {
            throw new Error('서버 오류가 발생했습니다.');
        }
        
        const data = await response.text();
        hideTypingIndicator();
        
        // 응답은 plain text 형식
        appendMessage(data, 'assistant');
        
    } catch (error) {
        console.error('Error:', error);
        hideTypingIndicator();
        appendMessage('죄송합니다. 오류가 발생했습니다. 다시 시도해주세요.', 'assistant');
    } finally {
        isTyping = false;
        sendBtn.disabled = false;
        messageInput.focus();
    }
}

// 메시지 추가
function appendMessage(content, role) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${role}`;
    
    const avatar = document.createElement('div');
    avatar.className = 'message-avatar';
    
    // 이미지 아바타 사용
    const avatarImg = document.createElement('img');
    avatarImg.src = role === 'user' ? 'image/user.png' : 'image/assistant.png';
    avatarImg.alt = role === 'user' ? 'User' : 'Assistant';
    avatarImg.className = 'avatar-img';
    avatar.appendChild(avatarImg);
    
    const contentDiv = document.createElement('div');
    contentDiv.className = 'message-content';
    
    // HTML 렌더링 처리
    contentDiv.innerHTML = content;
    
    messageDiv.appendChild(avatar);
    messageDiv.appendChild(contentDiv);
    
    messagesContainer.appendChild(messageDiv);
    scrollToBottom();
}

// 타이핑 인디케이터 표시
function showTypingIndicator() {
    const indicator = document.createElement('div');
    indicator.id = 'typingIndicator';
    indicator.className = 'message assistant';
    indicator.innerHTML = `
        <div class="message-avatar">
            <img src="image/assistant.png" alt="Assistant" class="avatar-img">
        </div>
        <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
        </div>
    `;
    messagesContainer.appendChild(indicator);
    scrollToBottom();
}

// 타이핑 인디케이터 제거
function hideTypingIndicator() {
    const indicator = document.getElementById('typingIndicator');
    if (indicator) {
        indicator.remove();
    }
}

// 스크롤을 맨 아래로
function scrollToBottom() {
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

// textarea 높이 자동 조절
function adjustTextareaHeight() {
    messageInput.style.height = 'auto';
    messageInput.style.height = Math.min(messageInput.scrollHeight, 200) + 'px';
}

// 새 대화 시작
function startNewConversation() {
    if (confirm('새 대화를 시작하시겠습니까? 현재 대화 내용이 초기화됩니다.')) {
        // 로컬에서 메시지 초기화
        conversationId = null;
        messagesContainer.innerHTML = `
            <div class="welcome-message">
                <img src="image/robot.png" alt="SKALA AI" class="welcome-logo">
                <h2>👋 SKALA AI Chat Memory 데모에 오신 것을 환영합니다!</h2>
                <p>위에서 Chat Memory 방식을 선택하고 질문을 입력해보세요.</p>
                <div class="welcome-features">
                    <span class="feature-badge">💾 In-Memory</span>
                    <span class="feature-badge">🗄️ Vector Store</span>
                </div>
            </div>
        `;
    }
}
