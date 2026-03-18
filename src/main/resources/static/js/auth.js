document.addEventListener('DOMContentLoaded', () => {
    // Dọn dẹp token lỗi (nếu có)
    const token = localStorage.getItem('jwt_token');
    if (token && token.length < 20) {
        localStorage.removeItem('jwt_token');
    }

    const form = document.getElementById('auth-form');
    const toggleLink = document.getElementById('toggle-link');
    const formTitle = document.getElementById('form-title');
    const submitBtn = document.getElementById('submit-btn');
    const registerFields = document.getElementById('register-fields');
    const confirmPwGroup = document.getElementById('confirm-password-group'); // Thêm biến này
    const toggleMsg = document.getElementById('toggle-msg');
    const errorMessage = document.getElementById('error-message');

    let isLoginMode = true;

    // Chuyển đổi qua lại giữa Đăng nhập và Đăng ký
    toggleLink.addEventListener('click', (e) => {
        e.preventDefault();
        isLoginMode = !isLoginMode;
        errorMessage.textContent = ''; 
        form.reset(); 

        if (isLoginMode) {
            formTitle.textContent = 'Đăng Nhập';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">login</i> Đăng Nhập';
            toggleMsg.textContent = 'Chưa có tài khoản?';
            toggleLink.textContent = 'Đăng ký ngay';
            registerFields.style.display = 'none';
            confirmPwGroup.style.display = 'none'; // Ẩn ô xác nhận MK
            document.getElementById('confirmPassword').required = false; 
        } else {
            formTitle.textContent = 'Đăng Ký Tài Khoản';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">person_add</i> Đăng Ký';
            toggleMsg.textContent = 'Đã có tài khoản?';
            toggleLink.textContent = 'Đăng nhập';
            registerFields.style.display = 'block';
            confirmPwGroup.style.display = 'block'; // Hiện ô xác nhận MK
            document.getElementById('confirmPassword').required = true; 
        }
    });

    // Xử lý khi bấm nút Submit
    form.addEventListener('submit', async (e) => {
        e.preventDefault(); 
        errorMessage.textContent = ''; 
        
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        
        // 1. Kiểm tra Email
        if (!email.endsWith('@gmail.com')) {
            errorMessage.textContent = 'Email phải có định dạng đuôi là @gmail.com';
            return;
        }

        // 2. Kiểm tra Mật khẩu chung
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;
        if (!passwordRegex.test(password)) {
            errorMessage.textContent = 'Mật khẩu phải có ít nhất 6 ký tự, bao gồm cả chữ cái và chữ số!';
            return;
        }

        // 3. Kiểm tra riêng cho Đăng Ký
        let fullName = '';
        let phone = '';
        if (!isLoginMode) {
            fullName = document.getElementById('fullName').value.trim();
            phone = document.getElementById('phone').value.trim();
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (!fullName) {
                errorMessage.textContent = 'Vui lòng nhập họ và tên!';
                return;
            }

            const phoneRegex = /^\d{10}$/;
            if (!phoneRegex.test(phone)) {
                errorMessage.textContent = 'Số điện thoại không hợp lệ! Phải bao gồm chính xác 10 chữ số.';
                return;
            }

            // KIỂM TRA MẬT KHẨU XÁC NHẬN CÓ KHỚP KHÔNG
            if (password !== confirmPassword) {
                errorMessage.textContent = 'Mật khẩu xác nhận không khớp!';
                return;
            }
        }
        
        // Bắt đầu gọi API
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">hourglass_empty</i> Đang xử lý...';

        const url = isLoginMode ? '/api/auth/login' : '/api/auth/register';
        const payload = { username: email, password: password };

        if (!isLoginMode) {
            payload.fullName = fullName;
            payload.phone = phone;
        }

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const data = await response.json();

            if (response.ok) {
                localStorage.setItem('jwt_token', data.token || data.accessToken);
                localStorage.setItem('user_info', JSON.stringify(data.user || payload));
                window.location.href = '/screens/home.html';
            } else {
                errorMessage.textContent = data.message || 'Tài khoản hoặc mật khẩu không đúng!';
            }
        } catch (error) {
            errorMessage.textContent = 'Không thể kết nối đến máy chủ!';
            console.error(error);
        } finally {
            submitBtn.disabled = false;
            if (isLoginMode) {
                submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">login</i> Đăng Nhập';
            } else {
                submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">person_add</i> Đăng Ký';
            }
        }
    });
});