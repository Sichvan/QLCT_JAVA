const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

const formatCurrency = (amount) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);

async function fetchBudgets() {
    try {
        const response = await fetch('/api/budgets', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const budgets = await response.json();
        
        const listDiv = document.getElementById('budget-list');
        listDiv.innerHTML = '';

        if (budgets.length === 0) {
            listDiv.innerHTML = `
                <div class="text-center" style="color: grey; margin-top: 40px;">
                    <i class="material-icons" style="font-size: 64px; opacity: 0.5;">account_balance_wallet</i>
                    <p style="margin-top: 10px;">${t('budget.no_budget')}</p>
                </div>
            `;
            return;
        }

        budgets.forEach(budget => {
            // Tính phần trăm chi tiêu
            let percent = (budget.spentAmount / budget.limitAmount) * 100;
            if (percent > 100) percent = 100;
            
            // Đổi màu thanh tiến trình
            let colorClass = '';
            if (percent > 75 && percent <= 100) colorClass = 'warning';
            if (budget.isExceeded) colorClass = 'danger';

            const translatedCatName = tCat(budget.category, budget.categoryName);
            listDiv.innerHTML += `
                <div class="card">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <span class="bold" style="font-size: 16px;">${translatedCatName}</span>
                        <i class="material-icons" style="color: red; cursor: pointer; font-size: 20px;" onclick="deleteBudget('${budget.id}')">delete</i>
                    </div>
                    
                    <div class="progress-bar">
                        <div class="progress-fill ${colorClass}" style="width: ${percent}%;"></div>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; font-size: 13px;">
                        <span style="color: ${budget.isExceeded ? 'red' : 'green'}; font-weight: bold;">${t('budget.spent')}: ${formatCurrency(budget.spentAmount)}</span>
                        <span class="bold">${t('budget.limit')}: ${formatCurrency(budget.limitAmount)}</span>
                    </div>
                    
                    ${budget.isExceeded ? `<p style="color: red; font-size: 12px; margin-top: 8px; font-style: italic;"><i class="material-icons" style="font-size: 14px; vertical-align: middle;">warning</i> ${t('budget.exceeded')}</p>` : ''}
                </div>
            `;
        });
    } catch (err) {
        console.error('Error loading budgets:', err);
    }
}

async function deleteBudget(id) {
    if (confirm(t('budget.confirm_delete'))) {
        await fetch(`/api/budgets/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        fetchBudgets();
    }
}

// Chạy khi load trang
fetchBudgets();