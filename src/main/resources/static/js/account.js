const token = localStorage.getItem('jwt_token');
const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}');
if (!token) window.location.href = '/index.html';

// Đổ dữ liệu cũ lên form
document.getElementById('prof-name').value = userInfo.fullName || '';
document.getElementById('prof-phone').value = userInfo.phone || '';
document.getElementById('prof-email').value = userInfo.username || '';

async function updateProfile() {
    const name = document.getElementById('prof-name').value;
    const phone = document.getElementById('prof-phone').value;
    const oldPw = document.getElementById('prof-old-pw').value;
    const newPw = document.getElementById('prof-new-pw').value;

    const payload = { fullName: name, phone: phone };

    // Nếu người dùng nhập mật khẩu mới, bắt buộc phải nhập mật khẩu cũ
    if (newPw) {
        if (!oldPw) {
            alert(t('account.err_req_old_pw') || 'Vui lòng nhập mật khẩu hiện tại để đổi mật khẩu mới!');
            return;
        }
        payload.currentPassword = oldPw;
        payload.newPassword = newPw;
    } else if (oldPw) {
        // Nếu chỉ nhập mật khẩu cũ (có thể để đổi thông tin cá nhân cho an toàn)
        payload.currentPassword = oldPw;
    }

    try {
        const res = await fetch('/api/users/profile', {
            method: 'PUT',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        
        const data = await res.json();
        if (res.ok) {
            // Cập nhật lại thông tin dưới LocalStorage
            localStorage.setItem('user_info', JSON.stringify(data.user));
            alert(t('account.update_success') || 'Cập nhật thành công!');
            window.location.href = '/screens/settings.html';
        } else {
            let errorMsg = data.message;
            if (errorMsg === 'Mật khẩu hiện tại không chính xác') {
                errorMsg = t('account.err_wrong_pw') || errorMsg;
            }
            alert(errorMsg || t('account.update_fail') || 'Cập nhật thất bại');
        }
    } catch(e) {
        console.error(e);
        alert(t('account.err_server') || 'Lỗi kết nối!');
    }
}