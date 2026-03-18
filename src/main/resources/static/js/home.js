const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

const formatCurrencyText = (amount) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
const formatDate = (dateStr) => new Date(dateStr).toLocaleDateString('vi-VN');

let userCategories = []; 

// Chống lỗi gõ tiếng Việt / Unikey cho Form Sửa
function unformatCurrency(input) { if (input.value) input.value = input.value.replace(/\./g, ''); }
function formatCurrency(input) {
    let val = input.value.replace(/\D/g, '');
    if (val) input.value = parseInt(val, 10).toLocaleString('vi-VN');
    else input.value = '';
}
function getRawNumber(formattedStr) {
    if (!formattedStr) return 0;
    return parseInt(formattedStr.toString().replace(/\D/g, ''), 10) || 0;
}

// 1. TẢI DỮ LIỆU TRANG CHỦ
async function fetchHomeData() {
    try {
        const response = await fetch('/api/transactions', { headers: { 'Authorization': `Bearer ${token}` } });
        const data = await response.json();

        document.getElementById('total-balance').textContent = formatCurrencyText(data.balance);
        document.getElementById('month-income').textContent = formatCurrencyText(data.monthIncome);
        document.getElementById('month-expense').textContent = formatCurrencyText(data.monthExpense);

        const listDiv = document.getElementById('tx-list');
        listDiv.innerHTML = '';
        
        if(data.transactions.length === 0) {
            listDiv.innerHTML = '<p class="text-center" style="color: grey; margin-top: 20px;">Chưa có giao dịch nào</p>';
            return;
        }

        data.transactions.forEach(tx => {
            let colorClass = tx.type === 'income' ? 'text-success' : (tx.type === 'expense' ? 'text-danger' : '');
            let sign = tx.type === 'expense' ? '-' : (tx.type === 'income' ? '+' : '');
            const safeNote = tx.note ? tx.note.replace(/'/g, "\\'") : '';
            const safeCatName = tx.categoryName ? tx.categoryName.replace(/'/g, "\\'") : tx.category;

            listDiv.innerHTML += `
                <div class="tx-item" style="position: relative; display: flex; align-items: center;">
                    <div class="tx-icon-wrapper">
                        <img src="/assets/icons/${tx.category}.png" width="28" height="28" onerror="this.outerHTML='<i class=\\'material-icons\\' style=\\'color: #64748b;\\'>category</i>'">
                    </div>
                    <div class="tx-info" style="flex: 1;">
                        <div class="tx-title">${safeCatName}</div>
                        <div class="tx-date">${formatDate(tx.date)} ${tx.note ? '• '+tx.note : ''}</div>
                    </div>
                    <div class="tx-amount ${colorClass}" style="margin-right: 90px;">${sign}${formatCurrencyText(tx.amount)}</div>
                    
                    <div style="position: absolute; right: 16px; display: flex; gap: 8px;">
                        <div onclick="openEditModal('${tx.id}', '${tx.type}', ${tx.amount}, '${tx.category}', '${safeNote}', '${tx.date}')" style="background: rgba(67, 97, 238, 0.1); color: var(--primary-color); border-radius: 8px; padding: 6px; display: flex; cursor: pointer;" title="Sửa">
                            <i class="material-icons" style="font-size: 18px;">edit</i>
                        </div>
                        <div onclick="deleteTx('${tx.id}')" style="background: rgba(239, 68, 68, 0.1); color: var(--danger-color); border-radius: 8px; padding: 6px; display: flex; cursor: pointer;" title="Xóa">
                            <i class="material-icons" style="font-size: 18px;">delete</i>
                        </div>
                    </div>
                </div>
            `;
        });
    } catch (err) {
        console.error("Lỗi tải giao dịch:", err);
    }
}

// 2. KIỂM TRA VƯỢT NGÂN SÁCH ĐỂ HIỂN THỊ CẢNH BÁO
async function checkBudgetExceeded() {
    try {
        const response = await fetch('/api/budgets', { headers: { 'Authorization': `Bearer ${token}` } });
        if (!response.ok) return;
        const budgets = await response.json();
        
        let hasExceeded = false;
        for (let budget of budgets) {
            // Kiểm tra số chi > Hạn mức
            if (budget.spentAmount > budget.limitAmount) {
                hasExceeded = true;
                break;
            }
        }
        
        if (hasExceeded) {
            const toast = document.getElementById('toast-notification');
            if(toast) {
                // Đợi 0.5s sau khi load xong trang để popup hiện lên cho mượt
                setTimeout(() => {
                    toast.classList.add('show');
                    // Tự động lặn xuống sau 4 giây
                    setTimeout(() => {
                        toast.classList.remove('show');
                    }, 4000);
                }, 500);
            }
        }
    } catch (err) {
        console.error('Lỗi kiểm tra ngân sách:', err);
    }
}

async function deleteTx(id) {
    if (confirm("Bạn có chắc chắn muốn xóa giao dịch này không? Số dư ví sẽ được cập nhật lại.")) {
        try {
            await fetch(`/api/transactions/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
            fetchHomeData(); 
            checkBudgetExceeded(); // Cập nhật lại xem còn bị vượt không sau khi xóa
        } catch(e) { alert("Lỗi khi xóa giao dịch!"); }
    }
}

async function openEditModal(id, type, amount, category, note, dateStr) {
    document.getElementById('edit-tx-id').value = id;
    document.getElementById('edit-tx-type').value = type;
    
    // Format tiền để đưa vào Modal
    const amountInput = document.getElementById('edit-tx-amount');
    amountInput.value = parseInt(amount, 10).toLocaleString('vi-VN');
    
    document.getElementById('edit-tx-note').value = note;
    
    if (dateStr) {
        const d = new Date(dateStr);
        const yyyy = d.getFullYear();
        const mm = String(d.getMonth() + 1).padStart(2, '0');
        const dd = String(d.getDate()).padStart(2, '0');
        document.getElementById('edit-tx-date').value = `${yyyy}-${mm}-${dd}`;
    }

    if (userCategories.length === 0) {
        const res = await fetch('/api/categories', { headers: { 'Authorization': `Bearer ${token}` }});
        userCategories = await res.json();
    }

    const selectBox = document.getElementById('edit-tx-category');
    selectBox.innerHTML = '';
    
    userCategories.forEach(cat => {
        if (type === 'loan' || cat.type === type) {
            const isSelected = (cat.icon === category) ? 'selected' : '';
            selectBox.innerHTML += `<option value="${cat.icon}" ${isSelected}>${cat.name}</option>`;
        }
    });

    if (selectBox.innerHTML === '') {
        selectBox.innerHTML = `<option value="${category}">${category}</option>`;
    }

    document.getElementById('editTxModal').classList.add('active');
}

function closeEditModal() { document.getElementById('editTxModal').classList.remove('active'); }

async function submitEditTx() {
    const id = document.getElementById('edit-tx-id').value;
    const type = document.getElementById('edit-tx-type').value;
    // Dùng getRawNumber để bỏ dấu chấm, lấy lại số thực
    const amount = getRawNumber(document.getElementById('edit-tx-amount').value);
    const note = document.getElementById('edit-tx-note').value;
    const date = document.getElementById('edit-tx-date').value;
    
    const catSelect = document.getElementById('edit-tx-category');
    const category = catSelect.value;
    const categoryName = catSelect.options[catSelect.selectedIndex].text;

    if (!amount || amount <= 0) return alert("Số tiền không hợp lệ!");

    const payload = { type: type, amount: amount, category: category, categoryName: categoryName, date: new Date(date).toISOString(), note: note };

    try {
        const res = await fetch(`/api/transactions/${id}`, {
            method: 'PUT',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            closeEditModal();
            fetchHomeData(); 
            checkBudgetExceeded(); // Cập nhật lại cảnh báo
        } else { alert("Lỗi cập nhật giao dịch!"); }
    } catch (e) { alert("Không thể kết nối máy chủ"); }
}

// Chạy khi khởi động trang chủ
fetchHomeData();
checkBudgetExceeded();