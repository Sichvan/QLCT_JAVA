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
                    <p style="margin-top: 10px;">${t('goal.no_goal')}</p>
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
                <div class="card" style="cursor: pointer;" onclick="depositGoal('${goal.id}', '${goal.name.replace(/'/g, "\\'")}')">
                    <div style="display: flex; align-items: center;">
                        <div style="background: ${color}20; padding: 10px; border-radius: 50%; margin-right: 12px; display: flex; justify-content: center; align-items: center;">
                            <i class="material-icons" style="color: ${color};">${icon}</i>
                        </div>
                        <div style="flex: 1;">
                            <div class="bold" style="font-size: 16px;">${goal.name}</div>
                            <div style="font-size: 12px; color: ${isCompleted ? 'green' : 'grey'};">
                                ${isCompleted ? t('goal.completed') : t('goal.click_to_deposit')}
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
    if (confirm(t('goal.confirm_delete'))) {
        await fetch(`/api/goals/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
        fetchGoals();
    }
}

async function depositGoal(id, name) {
    const promptText = t('goal.deposit_prompt').replace('{name}', name);
    const amountStr = prompt(promptText);
    if (!amountStr) return;
    
    const amount = parseInt(amountStr);
    if (isNaN(amount) || amount <= 0) {
        alert(t('goal.invalid_amount')); return;
    }

    try {
        const res = await fetch(`/api/goals/${id}/deposit`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount: amount })
        });
        if (res.ok) fetchGoals();
        else alert(t('goal.deposit_error'));
    } catch (err) { console.error(err); }
}

fetchGoals();