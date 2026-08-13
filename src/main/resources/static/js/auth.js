// ========================================================
// HÀM HIỂN THỊ / ẨN MẬT KHẨU
// ========================================================
function togglePassword(inputId, iconElement) {
    const input = document.getElementById(inputId);
    if (input.type === 'password') {
        input.type = 'text';
        iconElement.textContent = 'visibility';
    } else {
        input.type = 'password';
        iconElement.textContent = 'visibility_off';
    }
}

// ========================================================
// LOGIC XỬ LÝ FORM AUTH
// ========================================================
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
    const confirmPwGroup = document.getElementById('confirm-password-group');
    const toggleMsg = document.getElementById('toggle-msg');
    const errorMessage = document.getElementById('error-message');

    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');

    // 1. TỰ ĐỘNG BƠM THÊM Ô NHẬP OTP & QUÊN MẬT KHẨU VÀO UI
    const otpGroup = document.createElement('div');
    otpGroup.className = 'input-group';
    otpGroup.style.display = 'none';
    otpGroup.innerHTML = `
        <label>Mã OTP (6 số gửi về Email)</label>
        <input type="text" id="otpCode" placeholder="Nhập mã OTP 6 số" maxlength="6" autocomplete="off">
    `;
    form.insertBefore(otpGroup, errorMessage);

    const forgotLinkContainer = document.createElement('div');
    forgotLinkContainer.style.textAlign = 'center';
    forgotLinkContainer.style.marginTop = '16px';
    forgotLinkContainer.innerHTML = `
        <a href="#" id="btn-forgot" style="color: #4361ee; text-decoration: none; font-size: 14px; font-weight: 600; transition: 0.3s;">Quên mật khẩu?</a>
    `;
    form.appendChild(forgotLinkContainer);
    const btnForgot = document.getElementById('btn-forgot');

    // 2. QUẢN LÝ CÁC CHẾ ĐỘ HIỂN THỊ CỦA FORM
    let currentMode = 'LOGIN';
    let pendingEmail = '';

    function showMessage(msg, isSuccess = false) {
        errorMessage.textContent = msg;
        errorMessage.style.display = 'block';
        if (isSuccess) {
            errorMessage.style.color = '#10b981';
            errorMessage.style.background = 'rgba(16, 185, 129, 0.1)';
        } else {
            errorMessage.style.color = '#ef4444';
            errorMessage.style.background = 'rgba(239, 68, 68, 0.1)';
        }
    }

    function updateUI() {
        errorMessage.textContent = ''; 
        errorMessage.style.display = 'none';
        
        registerFields.style.display = 'none';
        confirmPwGroup.style.display = 'none'; 
        otpGroup.style.display = 'none';
        emailInput.parentElement.style.display = 'block';
        passwordInput.parentElement.style.display = 'block';
        
        emailInput.disabled = false;
        passwordInput.required = true;
        confirmPasswordInput.required = false;
        document.getElementById('otpCode').required = false;

        forgotLinkContainer.style.display = 'none';
        toggleMsg.style.display = 'inline';
        toggleLink.style.display = 'inline';

        const pwLabel = passwordInput.previousElementSibling;

        if (currentMode === 'LOGIN') {
            formTitle.textContent = 'Đăng Nhập';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">login</i> Đăng Nhập';
            toggleMsg.textContent = 'Chưa có tài khoản?';
            toggleLink.textContent = 'Đăng ký ngay';
            forgotLinkContainer.style.display = 'block';
            if(pwLabel) pwLabel.textContent = 'Mật khẩu';

        } else if (currentMode === 'REGISTER') {
            formTitle.textContent = 'Đăng Ký Tài Khoản';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">mail</i> Nhận mã OTP';
            toggleMsg.textContent = 'Đã có tài khoản?';
            toggleLink.textContent = 'Đăng nhập';
            registerFields.style.display = 'block';
            confirmPwGroup.style.display = 'block'; 
            confirmPasswordInput.required = true; 
            if(pwLabel) pwLabel.textContent = 'Mật khẩu';

        } else if (currentMode === 'VERIFY_REG') {
            formTitle.textContent = 'Xác Nhận Email';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">verified</i> Hoàn tất Đăng ký';
            emailInput.value = pendingEmail;
            emailInput.disabled = true; 
            passwordInput.parentElement.style.display = 'none'; 
            passwordInput.required = false;
            otpGroup.style.display = 'block'; 
            document.getElementById('otpCode').required = true;
            toggleMsg.textContent = '';
            toggleLink.textContent = 'Quay lại đăng ký';

        } else if (currentMode === 'FORGOT') {
            formTitle.textContent = 'Khôi Phục Mật Khẩu';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">send</i> Nhận mã OTP';
            passwordInput.parentElement.style.display = 'none'; 
            passwordInput.required = false;
            toggleMsg.textContent = 'Nhớ mật khẩu?';
            toggleLink.textContent = 'Đăng nhập';

        } else if (currentMode === 'RESET') {
            formTitle.textContent = 'Tạo Mật Khẩu Mới';
            submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">save</i> Lưu mật khẩu mới';
            emailInput.value = pendingEmail;
            emailInput.disabled = true;
            otpGroup.style.display = 'block'; 
            document.getElementById('otpCode').required = true;
            
            if(pwLabel) pwLabel.textContent = 'Mật khẩu mới (Tối thiểu 6 ký tự)';
            confirmPwGroup.style.display = 'block';
            confirmPasswordInput.required = true;
            toggleMsg.textContent = '';
            toggleLink.textContent = 'Quay lại đăng nhập';
        }
    }

    // --- CHUYỂN TRANG ---
    toggleLink.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentMode === 'LOGIN' || currentMode === 'VERIFY_REG') currentMode = 'REGISTER';
        else currentMode = 'LOGIN';
        updateUI();
    });

    btnForgot.addEventListener('click', (e) => {
        e.preventDefault();
        currentMode = 'FORGOT';
        updateUI();
    });

    updateUI();

    // 3. XỬ LÝ GỌI API
    form.addEventListener('submit', async (e) => {
        e.preventDefault(); 
        errorMessage.style.display = 'none';
        
        const email = emailInput.value.trim();
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        const otpCode = document.getElementById('otpCode').value.trim();
        
        if (!/^[^\s@]+@[^\s@]+\.(com|edu|vn|org|net|edu\.vn|com\.vn)$/i.test(email)) {
            return showMessage('Email không hợp lệ! Hỗ trợ các đuôi: .com, .edu, .vn, .org, .net');
        }

        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/;

        let url = '';
        let payload = {};

        if (currentMode === 'LOGIN') {
            url = '/api/auth/login';
            payload = { username: email, password: password };
        } 
        else if (currentMode === 'REGISTER') {
            const fullName = document.getElementById('fullName').value.trim();
            const phone = document.getElementById('phone').value.trim();

            if (!fullName) return showMessage('Vui lòng nhập họ và tên!');
            if (!/^\d{10}$/.test(phone)) return showMessage('Số điện thoại không hợp lệ! Phải bao gồm 10 chữ số.');
            if (password !== confirmPassword) return showMessage('Mật khẩu xác nhận không khớp!');
            if (!passwordRegex.test(password)) return showMessage('Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số!');

            url = '/api/auth/register';
            payload = { fullName, username: email, phone, password };
        }
        else if (currentMode === 'VERIFY_REG') {
            if (otpCode.length !== 6) return showMessage('Vui lòng nhập đủ 6 số OTP!');
            url = '/api/auth/verify-register';
            payload = { email: pendingEmail, otp: otpCode };
        }
        else if (currentMode === 'FORGOT') {
            url = '/api/auth/forgot-password';
            payload = { email: email };
        }
        else if (currentMode === 'RESET') {
            if (otpCode.length !== 6) return showMessage('Vui lòng nhập đủ 6 số OTP!');
            if (password !== confirmPassword) return showMessage('Mật khẩu xác nhận không khớp!');
            if (!passwordRegex.test(password)) return showMessage('Mật khẩu mới phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường và số!');
            
            url = '/api/auth/reset-password';
            payload = { email: pendingEmail, otp: otpCode, newPassword: password };
        }

        submitBtn.disabled = true;
        const originalBtnText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">hourglass_empty</i> Đang xử lý...';

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const data = await response.json();

            if (response.ok) {
                if (currentMode === 'LOGIN' || currentMode === 'VERIFY_REG') {
                    localStorage.setItem('jwt_token', data.token);
                    localStorage.setItem('user_info', JSON.stringify(data.user));
                    
                    if (data.user && data.user.role === 'admin') {
                        window.location.href = '/screens/admin.html';
                    } else {
                        window.location.href = '/screens/home.html';
                    }
                } 
                else if (currentMode === 'REGISTER') {
                    pendingEmail = email;
                    currentMode = 'VERIFY_REG';
                    updateUI();
                    showMessage('Mã OTP đã được gửi! Vui lòng kiểm tra email.', true);
                }
                else if (currentMode === 'FORGOT') {
                    pendingEmail = email;
                    currentMode = 'RESET';
                    updateUI();
                    showMessage('Mã OTP khôi phục đã được gửi vào email!', true);
                }
                else if (currentMode === 'RESET') {
                    alert("Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
                    currentMode = 'LOGIN';
                    updateUI();
                }

            } else {
                showMessage(data.message || 'Tài khoản hoặc mật khẩu không đúng!');
            }
        } catch (error) {
            showMessage('Không thể kết nối đến máy chủ!');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalBtnText; 
            if (currentMode === 'VERIFY_REG') submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">verified</i> Hoàn tất Đăng ký';
            if (currentMode === 'RESET') submitBtn.innerHTML = '<i class="material-icons" style="font-size: 20px;">save</i> Lưu mật khẩu mới';
        }
    });
});