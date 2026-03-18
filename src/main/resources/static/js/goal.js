const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

const formatCurrency = (amount) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);

async function fetchGoals() {
    try {
        const response = await fetch('/api/goals', { headers: { 'Authorization': `Bearer ${token}` } });
        const goals = await response.json();
        
        const listDiv = document.getElementById('goal-list');
        listDiv.innerHTML = '';

        if (goals.length === 0) {
            listDiv.innerHTML = `
                <div class="text-center" style="color: grey; margin-top: 40px;">
                    <i class="material-icons" style="font-size: 64px; opacity: 0.5;">flag</i>
                    <p style="margin-top: 10px;">Chưa có mục tiêu nào</p>
                </div>
            `;
            return;
        }

        goals.forEach(goal => {
            let percent = (goal.currentAmount / goal.targetAmount) * 100;
            if (percent > 100) percent = 100;
            const isCompleted = percent >= 100 || goal.status === 'completed';

            const color = isCompleted ? '#4CAF50' : '#3A86FF';
            const icon = isCompleted ? 'emoji_events' : 'savings';

            listDiv.innerHTML += `
                <div class="card" style="cursor: pointer;" onclick="depositGoal('${goal.id}', '${goal.name}')">
                    <div style="display: flex; align-items: center;">
                        <div style="background: ${color}20; padding: 10px; border-radius: 50%; margin-right: 12px; display: flex; justify-content: center; align-items: center;">
                            <i class="material-icons" style="color: ${color};">${icon}</i>
                        </div>
                        <div style="flex: 1;">
                            <div class="bold" style="font-size: 16px;">${goal.name}</div>
                            <div style="font-size: 12px; color: ${isCompleted ? 'green' : 'grey'};">
                                ${isCompleted ? 'Đã hoàn thành!' : 'Nhấn vào để nạp tiền'}
                            </div>
                        </div>
                        <i class="material-icons" style="color: red; cursor: pointer;" onclick="event.stopPropagation(); deleteGoal('${goal.id}')">delete</i>
                    </div>
                    
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: ${percent}%; background: ${color};"></div>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; font-size: 13px;">
                        <span class="bold" style="color: ${color};">${formatCurrency(goal.currentAmount)}</span>
                        <span style="color: grey;">${formatCurrency(goal.targetAmount)}</span>
                    </div>
                </div>
            `;
        });
    } catch (err) { console.error(err); }
}

async function deleteGoal(id) {
    if (confirm("Bạn có chắc muốn xóa mục tiêu này?")) {
        await fetch(`/api/goals/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        fetchGoals();
    }
}

async function depositGoal(id, name) {
    const amountStr = prompt(`Nhập số tiền muốn nạp vào mục tiêu "${name}":\n(VD: 50000)`);
    if (!amountStr) return;
    
    const amount = parseInt(amountStr);
    if (isNaN(amount) || amount <= 0) {
        alert("Số tiền không hợp lệ!"); return;
    }

    try {
        const res = await fetch(`/api/goals/${id}/deposit`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount: amount })
        });
        if (res.ok) fetchGoals();
        else alert("Lỗi nạp tiền!");
    } catch (err) { console.error(err); }
}

// Thay alert trong goal_page.html bằng hàm addGoal()
async function addGoal() {
    const name = prompt("Nhập tên mục tiêu (VD: Mua xe máy):");
    if (!name) return;
    const target = prompt("Nhập số tiền mục tiêu (VD: 50000000):");
    if (!target || isNaN(target)) return alert("Số tiền không hợp lệ!");

    await fetch('/api/goals', {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name, targetAmount: parseInt(target) })
    });
    fetchGoals();
}

fetchGoals();