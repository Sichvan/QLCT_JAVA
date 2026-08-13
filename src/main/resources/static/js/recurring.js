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
                    <p style="margin-top: 10px;">${t('recurring.no_recurring')}</p>
                </div>
            `;
            return;
        }

        recurrings.forEach(item => {
            const isExpense = item.type === 'expense';
            const color = isExpense ? 'red' : 'green';
            const sign = isExpense ? '-' : '+';
            const translatedCatName = tCat(item.category, item.categoryName);

            listDiv.innerHTML += `
                <div class="card" style="display: flex; align-items: center;">
                    <i class="material-icons" style="background: ${color}15; color: ${color}; padding: 12px; border-radius: 50%; margin-right: 12px;">
                        autorenew
                    </i>
                    <div style="flex: 1;">
                        <div class="bold" style="font-size: 16px;">${translatedCatName}</div>
                        <div style="font-size: 12px; color: grey;">${t('recurring.monthly_day').replace('{day}', item.dayOfMonth)} ${item.note ? '• ' + item.note : ''}</div>
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
    if (confirm(t('recurring.confirm_delete'))) {
        await fetch(`/api/recurring/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        fetchRecurring();
    }
}

fetchRecurring();