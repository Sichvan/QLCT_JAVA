const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

async function loadAdminData() {
    try {
        // Lấy thống kê
        const statRes = await fetch('/api/admin/stats', { headers: { 'Authorization': `Bearer ${token}` }});
        if (!statRes.ok) {
            alert("Bạn không có quyền Admin! Đang chuyển hướng về trang chủ...");
            window.location.href = '/screens/home.html';
            return;
        }
        const stats = await statRes.json();
        document.getElementById('admin-total-users').textContent = stats.totalUsers;
        document.getElementById('admin-new-users').textContent = stats.newUsers;

        // Lấy danh sách user
        const userRes = await fetch('/api/admin/users', { headers: { 'Authorization': `Bearer ${token}` }});
        const users = await userRes.json();
        
        const listDiv = document.getElementById('admin-user-list');
        listDiv.innerHTML = '';
        
        users.forEach(u => {
            listDiv.innerHTML += `
                <div class="card" style="display: flex; justify-content: space-between; align-items: center; padding: 16px;">
                    <div style="flex: 1;">
                        <div style="font-weight: bold; font-size: 16px;">${u.fullName || 'Chưa có tên'}</div>
                        <div style="font-size: 13px; color: grey; margin-top: 4px;">
                            ${u.username}<br>SĐT: ${u.phone || 'Trống'}
                        </div>
                    </div>
                    <i class="material-icons" style="color: red; cursor: pointer; padding: 8px; background: #ffebee; border-radius: 50%;" 
                       onclick="deleteUser('${u.id}', '${u.username}')">delete</i>
                </div>
            `;
        });
    } catch (err) { 
        console.error(err); 
    }
}

async function deleteUser(id, email) {
    if(confirm(`Cảnh báo: Bạn có chắc chắn muốn xóa tài khoản "${email}" vĩnh viễn không?`)) {
        await fetch(`/api/admin/users/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` }});
        loadAdminData(); // Tải lại danh sách
    }
}

function logoutAdmin() {
    localStorage.clear();
    window.location.href = '/index.html';
}

// Chạy khi load trang
loadAdminData();