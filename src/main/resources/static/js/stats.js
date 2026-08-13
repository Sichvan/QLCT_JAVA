const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

// TỪ ĐIỂN DỊCH TÊN DANH MỤC SANG TIẾNG VIỆT
const categoryMap = {
    // Chi tiêu
    'eating': 'Ăn uống', 
    'shopping': 'Mua sắm', 
    'transport': 'Di chuyển',
    'education': 'Học tập', 
    'health': 'Sức khỏe', 
    'entertainment': 'Giải trí',
    'utilities': 'Tiện ích', 
    'gift': 'Quà tặng', 
    'other_expense': 'Chi tiêu khác',
    
    // Thu nhập
    'salary': 'Tiền lương',
    'investment': 'Đầu tư', 
    'gift_income': 'Được tặng',
    'other_income': 'Thu nhập khác',

    // Nhóm Vay & Nợ
    'borrowing': 'Đi vay',
    'lending': 'Cho vay',
    'repaying': 'Trả nợ',
    'collecting': 'Thu nợ'
};

let currentStatsType = 'expense';
let currentStartDate = null;
let currentEndDate = null;

// ======= BỘ LỌC THỜI GIAN =======
function onStatsFilterChange() {
    const filterType = document.getElementById('stats-filter-type').value;
    const dateInput = document.getElementById('stats-filter-date');
    
    if (filterType === '30days') {
        dateInput.style.display = 'none';
    } else {
        dateInput.style.display = 'inline-block';
        if (filterType === 'day') {
            dateInput.type = 'date';
            dateInput.value = new Date().toISOString().split('T')[0];
        } else if (filterType === 'month') {
            dateInput.type = 'month';
            const now = new Date();
            dateInput.value = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
        } else if (filterType === 'year') {
            dateInput.type = 'number';
            dateInput.value = new Date().getFullYear();
            dateInput.min = 2020;
            dateInput.max = 2030;
        }
    }
}

function applyStatsFilter() {
    const filterType = document.getElementById('stats-filter-type').value;
    const dateValue = document.getElementById('stats-filter-date').value;
    
    if (filterType === '30days') {
        currentStartDate = null;
        currentEndDate = null;
    } else if (filterType === 'day' && dateValue) {
        currentStartDate = dateValue;
        currentEndDate = dateValue;
    } else if (filterType === 'month' && dateValue) {
        const [year, month] = dateValue.split('-');
        const lastDay = new Date(year, month, 0).getDate();
        currentStartDate = `${year}-${month}-01`;
        currentEndDate = `${year}-${month}-${lastDay}`;
    } else if (filterType === 'year' && dateValue) {
        currentStartDate = `${dateValue}-01-01`;
        currentEndDate = `${dateValue}-12-31`;
    }
    
    loadStats(currentStatsType);
}

async function loadStats(type = 'expense') {
    currentStatsType = type;
    
    // Cập nhật màu nút Tab
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.style.background = 'none';
        btn.style.color = 'var(--text-muted)';
    });
    
    const activeTab = document.getElementById(`tab-${type}`);
    if (activeTab) {
        activeTab.style.background = 'var(--primary-color)';
        activeTab.style.color = 'white';
    }

    try {
        let url = `/api/stats/pie-chart?type=${type}`;
        if (currentStartDate && currentEndDate) {
            url += `&startDate=${currentStartDate}&endDate=${currentEndDate}`;
        }
        
        const res = await fetch(url, { 
            headers: { 'Authorization': `Bearer ${token}` } 
        });
        const data = await res.json();
        render(data);
    } catch (e) { 
        console.error("Lỗi khi tải dữ liệu thống kê:", e); 
    }
}

function render(data) {
    const ctx = document.getElementById('myChart');
    if (!ctx) return;
    
    const list = document.getElementById('stat-list');
    const colors = ['#4361ee', '#4cc9f0', '#f72585', '#7209b7', '#10b981', '#f59e0b', '#3a0ca3', '#ef4444'];
    
    // Hủy biểu đồ cũ nếu có trước khi vẽ cái mới
    if (window.chartObj) window.chartObj.destroy();
    
    window.chartObj = new Chart(ctx.getContext('2d'), {
        type: 'doughnut',
        data: {
            labels: data.map(i => { const tName = t('cat.'+i.id); return tName !== 'cat.'+i.id ? tName : (categoryMap[i.id] || i.id); }),
            datasets: [{ 
                data: data.map(i => i.total), 
                backgroundColor: colors,
                borderWidth: 2,
                borderColor: localStorage.getItem('theme') === 'dark' ? '#1c1c1e' : '#ffffff'
            }]
        },
        options: {
            plugins: {
                legend: { display: false }
            },
            maintainAspectRatio: false,
            cutout: '65%'
        }
    });

    // Render danh sách chi tiết bên dưới biểu đồ
    if (list) {
        list.innerHTML = data.length ? data.map((item, i) => {
            const tName = t('cat.'+item.id);
            const dispName = tName !== 'cat.'+item.id ? tName : (categoryMap[item.id] || item.id);
            return `
            <div style="display: flex; justify-content: space-between; padding: 16px 0; border-bottom: 1px solid var(--border-color);">
                <div style="display: flex; align-items: center; gap: 12px;">
                    <div style="width: 12px; height: 12px; border-radius: 50%; background: ${colors[i % colors.length]};"></div>
                    <span class="bold" style="color: var(--text-main);">${dispName}</span>
                </div>
                <span class="bold" style="color: var(--text-main);">${new Intl.NumberFormat('vi-VN', {style:'currency', currency:'VND'}).format(item.total)}</span>
            </div>
        `}).join('') : `<p style="text-align:center; padding:20px; color:var(--text-muted);">${t('stats.no_data') || 'Không có dữ liệu'}</p>`;
    }
}

loadStats('expense');