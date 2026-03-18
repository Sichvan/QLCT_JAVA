const token = localStorage.getItem('jwt_token');

const categoryMap = {
    'eating': 'Ăn uống', 'shopping': 'Mua sắm', 'transport': 'Di chuyển',
    'education': 'Học tập', 'health': 'Sức khỏe', 'entertainment': 'Giải trí',
    'other_expense': 'Chi tiêu khác', 'other_income': 'Tiền thu nhập', 'salary': 'Tiền lương',
    'utilities': 'Tiện ích', 'gift': 'Quà tặng', 'investment': 'Đầu tư'
};

async function loadStats(type = 'expense') {
    // Cập nhật màu nút Tab
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.style.background = 'none';
        btn.style.color = 'var(--text-muted)';
    });
    const activeTab = document.getElementById(`tab-${type}`);
    activeTab.style.background = 'var(--primary-color)';
    activeTab.style.color = 'white';

    try {
        const res = await fetch(`/api/stats/pie-chart?type=${type}`, { 
            headers: { 'Authorization': `Bearer ${token}` } 
        });
        const data = await res.json();
        render(data);
    } catch (e) { console.error(e); }
}

function render(data) {
    const ctx = document.getElementById('myChart').getContext('2d');
    const list = document.getElementById('stat-list');
    const colors = ['#4361ee', '#4cc9f0', '#f72585', '#7209b7', '#10b981', '#f59e0b', '#3a0ca3', '#ef4444'];
    
    if (window.chartObj) window.chartObj.destroy();
    
    window.chartObj = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: data.map(i => categoryMap[i.id] || i.id),
            datasets: [{ 
                data: data.map(i => i.total), 
                backgroundColor: colors,
                borderWidth: 2,
                borderColor: localStorage.getItem('theme') === 'dark' ? '#1c1c1e' : '#ffffff'
            }]
        },
        options: {
            plugins: {
                legend: { display: false } // Luôn ẩn chú thích phía trên
            },
            maintainAspectRatio: false,
            cutout: '65%'
        }
    });

    list.innerHTML = data.length ? data.map((item, i) => `
        <div style="display: flex; justify-content: space-between; padding: 16px 0; border-bottom: 1px solid var(--border-color);">
            <div style="display: flex; align-items: center; gap: 12px;">
                <div style="width: 12px; height: 12px; border-radius: 50%; background: ${colors[i % colors.length]};"></div>
                <span class="bold">${categoryMap[item.id] || item.id}</span>
            </div>
            <span class="bold">${new Intl.NumberFormat('vi-VN', {style:'currency', currency:'VND'}).format(item.total)}</span>
        </div>
    `).join('') : '<p style="text-align:center; padding:20px; color:var(--text-muted);">Không có dữ liệu</p>';
}

loadStats('expense');