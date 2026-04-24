const API_BASE = '/api';
let allUsers = [];

document.addEventListener('DOMContentLoaded', init);

async function init() {
    const grid = document.getElementById('userGrid');
    const searchInput = document.getElementById('searchInput');
    const clearBtn = document.getElementById('clearSearch');

    // 검색 입력 이벤트
    searchInput.addEventListener('input', (e) => {
        const query = e.target.value;
        
        // 지우기 버튼 노출 제어
        clearBtn.style.display = query.length > 0 ? 'flex' : 'none';
        
        filterAndRenderUsers(query);
    });

    // 검색어 지우기 버튼
    clearBtn.addEventListener('click', () => {
        searchInput.value = '';
        clearBtn.style.display = 'none';
        searchInput.focus();
        renderUserList(allUsers);
    });

    try {
        const response = await fetch(`${API_BASE}/user/findAll`);
        if (!response.ok) throw new Error('API 로드 실패');
        
        allUsers = await response.json();
        renderUserList(allUsers);
        
    } catch (error) {
        console.error(error);
        grid.innerHTML = `
            <div class="status-message">
                <i class="fa-solid fa-triangle-exclamation" style="font-size: 32px; color: #f87171; margin-bottom: 16px;"></i>
                <p>사용자 정보를 불러올 수 없습니다. 서버 상태를 확인해주세요.</p>
            </div>
        `;
    }
}

function filterAndRenderUsers(query) {
    const term = query.toLowerCase().trim();
    if (!term) {
        renderUserList(allUsers);
        return;
    }

    const filtered = allUsers.filter(user => {
        const name = (user.username || '').toLowerCase();
        const email = (user.email || '').toLowerCase();
        return name.includes(term) || email.includes(term);
    });

    renderUserList(filtered);
}

function renderUserList(users) {
    const grid = document.getElementById('userGrid');
    const userCount = document.getElementById('userCount');
    
    grid.innerHTML = '';
    userCount.textContent = `현재 ${users.length}명의 멤버가 있습니다`;

    if (users.length === 0) {
        grid.innerHTML = `
            <div class="status-message">
                <i class="fa-solid fa-magnifying-glass" style="font-size: 32px; margin-bottom: 16px; opacity: 0.3;"></i>
                <p>검색 결과와 일치하는 사용자가 없습니다.</p>
            </div>
        `;
        return;
    }

    users.forEach(user => {
        grid.appendChild(createUserCard(user));
        if (user.profileId) loadProfileImage(user.id, user.profileId);
    });
}

function createUserCard(user) {
    const card = document.createElement('div');
    card.className = 'user-card';

    // 이니셜 아바타 색상 로직
    const colors = ['#6366f1', '#ec4899', '#8b5cf6', '#10b981', '#f59e0b', '#3b82f6'];
    const charCodeSum = (user.username || '').split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    const bgColor = colors[charCodeSum % colors.length];
    const initial = (user.username || '?').charAt(0).toUpperCase();

    card.innerHTML = `
        <div class="avatar-container">
            <div id="avatar-box-${user.id}" class="avatar-circle" style="background-color: ${bgColor}">
                ${initial}
            </div>
            <div class="status-dot ${user.online ? 'online' : 'offline'}" title="${user.online ? '온라인' : '오프라인'}"></div>
        </div>
        <div class="user-name">${user.username || '이름 없음'}</div>
        <div class="user-email">${user.email || '이메일 정보 없음'}</div>
    `;

    return card;
}

async function loadProfileImage(userId, profileId) {
    const box = document.getElementById(`avatar-box-${userId}`);
    if (!box) return;

    try {
        const response = await fetch(`${API_BASE}/binaryContent/find?binaryContentId=${profileId}`);
        if (!response.ok) return;

        const data = await response.json();
        if (data.bytes) {
            const img = new Image();
            img.src = `data:${data.contentType};base64,${data.bytes}`;
            img.className = 'avatar-img';
            img.onload = () => {
                box.innerHTML = '';
                box.appendChild(img);
                box.style.backgroundColor = 'transparent';
            };
        }
    } catch (e) {
        // 이미지 로딩 실패 시 이니셜 유지
    }
}
