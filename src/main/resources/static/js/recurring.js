const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

const formatCurrency = (amount) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);

async function fetchRecurring() {
    try {
        const response = await fetch('/api/recurring', { headers: { 'Authorization': `Bearer ${token}` } });
        const recurrings = await response.json();
        
        const listDiv = document.getElementById('recurring-list');
        listDiv.innerHTML = '';

        if (recurrings.length === 0) {
            listDiv.innerHTML = `
                <div class="text-center" style="color: grey; margin-top: 40px;">
                    <i class="material-icons" style="font-size: 64px; opacity: 0.5;">event_repeat</i>
                    <p style="margin-top: 10px;">Chưa có thiết lập định kỳ nào</p>
                </div>
            `;
            return;
        }

        recurrings.forEach(item => {
            const isExpense = item.type === 'expense';
            const color = isExpense ? 'red' : 'green';
            const sign = isExpense ? '-' : '+';

            listDiv.innerHTML += `
                <div class="card" style="display: flex; align-items: center;">
                    <i class="material-icons" style="background: ${color}15; color: ${color}; padding: 12px; border-radius: 50%; margin-right: 12px;">
                        autorenew
                    </i>
                    <div style="flex: 1;">
                        <div class="bold" style="font-size: 16px;">${item.categoryName || item.category}</div>
                        <div style="font-size: 12px; color: grey;">Ngày ${item.dayOfMonth} hàng tháng • ${item.note || ''}</div>
                    </div>
                    <div style="text-align: right;">
                        <div class="bold" style="color: ${color}; font-size: 15px;">${sign}${formatCurrency(item.amount)}</div>
                        <i class="material-icons" style="color: red; cursor: pointer; font-size: 18px; margin-top: 8px;" onclick="deleteRecurring('${item.id}')">delete</i>
                    </div>
                </div>
            `;
        });
    } catch (err) { console.error(err); }
}

async function deleteRecurring(id) {
    if (confirm("Bạn có chắc muốn xóa giao dịch định kỳ này?")) {
        await fetch(`/api/recurring/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        fetchRecurring();
    }
}

// Thay alert trong recurring_page.html bằng hàm addRecurring()
async function addRecurring() {
    const type = prompt("Nhập loại giao dịch (gõ: expense HOẶC income):", "expense");
    const amount = prompt("Số tiền định kỳ hàng tháng:");
    const day = prompt("Ngày thực hiện trong tháng (1-31):", "1");
    if (!amount || !day) return;

    await fetch('/api/recurring', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
            type: type, amount: parseInt(amount), dayOfMonth: parseInt(day), 
            category: 'category', categoryName: 'Định kỳ', note: 'Thêm nhanh'
        })
    });
    fetchRecurring();
}

fetchRecurring();