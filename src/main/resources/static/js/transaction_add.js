const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

let currentType = 'expense'; // Mặc định là chi tiêu

// Đặt ngày mặc định là hôm nay
document.getElementById('tx-date').valueAsDate = new Date();

// Danh sách Danh mục cứng (giống hệ thống Flutter của bạn)
const categories = {
    expense: [
        { id: 'eating', name: 'Ăn uống' },
        { id: 'shopping', name: 'Mua sắm' },
        { id: 'transport', name: 'Di chuyển' },
        { id: 'education', name: 'Học tập' },
        { id: 'health', name: 'Sức khỏe' },
        { id: 'other_expense', name: 'Chi tiêu khác' }
    ],
    income: [
        { id: 'salary', name: 'Tiền lương' },
        { id: 'investment', name: 'Đầu tư' },
        { id: 'other_income', name: 'Thu nhập khác' }
    ],
    loan: [
        { id: 'lending', name: 'Cho vay' },
        { id: 'borrowing', name: 'Đi vay' },
        { id: 'repaying', name: 'Trả nợ' },
        { id: 'collecting', name: 'Thu nợ' }
    ]
};

// Hàm chuyển đổi tab
function switchType(type) {
    currentType = type;
    
    // Đổi màu tab
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.getElementById(`tab-${type}`).classList.add('active');

    // Cập nhật danh sách Option
    const selectBox = document.getElementById('tx-category');
    selectBox.innerHTML = ''; // Xóa cũ
    
    categories[type].forEach(cat => {
        selectBox.innerHTML += `<option value="${cat.id}">${cat.name}</option>`;
    });
}

// Khởi tạo danh mục lúc đầu
switchType('expense');

// Hàm gửi API
async function submitTransaction() {
    const amount = document.getElementById('tx-amount').value;
    const category = document.getElementById('tx-category').value;
    const categoryName = document.getElementById('tx-category').options[document.getElementById('tx-category').selectedIndex].text;
    const date = document.getElementById('tx-date').value;
    const note = document.getElementById('tx-note').value;
    const msgBox = document.getElementById('tx-msg');

    if (!amount || amount <= 0) {
        msgBox.textContent = "Vui lòng nhập số tiền hợp lệ!";
        return;
    }

    const payload = {
        type: currentType,
        amount: parseFloat(amount),
        category: category,
        categoryName: categoryName,
        date: new Date(date).toISOString(),
        note: note
    };

    try {
        const response = await fetch('/api/transactions', {
            method: 'POST',
            headers: { 
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json' 
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Đã thêm giao dịch thành công!");
            window.location.href = '/screens/home.html'; // Quay về trang chủ
        } else {
            const data = await response.json();
            msgBox.textContent = data.message || "Có lỗi xảy ra!";
        }
    } catch (error) {
        msgBox.textContent = "Không thể kết nối máy chủ!";
    }
}