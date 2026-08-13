const token = localStorage.getItem('jwt_token');
if (!token) window.location.href = '/index.html';

// Hỗ trợ Dark Mode khi chuyển trang
if (localStorage.getItem('theme') === 'dark') document.body.classList.add('dark-mode');

const formatCurrency = (amount) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);

async function fetchWalletData() {
    try {
        const response = await fetch('/api/transactions', { headers: { 'Authorization': `Bearer ${token}` } });
        if (!response.ok) return;
        const data = await response.json();

        document.getElementById('wallet-balance').textContent = formatCurrency(data.balance);
        document.getElementById('wallet-lending').textContent = formatCurrency(data.totalLending || 0);
        document.getElementById('wallet-borrowing').textContent = formatCurrency(data.totalBorrowing || 0);
    } catch (err) { console.error(err); }
}

// ======= XUẤT DỮ LIỆU CSV QUA API BACKEND =======
function openExportModal() {
    document.getElementById('exportModal').classList.add('active');
    document.getElementById('export-filter').value = 'all';
    document.getElementById('export-date-group').style.display = 'none';
}

function closeExportModal() {
    document.getElementById('exportModal').classList.remove('active');
}

function onFilterChange() {
    const filter = document.getElementById('export-filter').value;
    const dateGroup = document.getElementById('export-date-group');
    const dateInput = document.getElementById('export-date');

    if (filter === 'all') {
        dateGroup.style.display = 'none';
    } else {
        dateGroup.style.display = 'block';
        if (filter === 'day') {
            dateInput.type = 'date';
            dateInput.value = new Date().toISOString().split('T')[0];
        } else if (filter === 'month') {
            dateInput.type = 'month';
            const now = new Date();
            dateInput.value = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
        } else if (filter === 'year') {
            dateInput.type = 'number';
            dateInput.value = new Date().getFullYear();
            dateInput.min = 2020;
            dateInput.max = 2030;
        }
    }
}

async function exportToExcel() {
    const filter = document.getElementById('export-filter').value;
    const dateInput = document.getElementById('export-date').value;

    let url = `/api/export/csv?filter=${filter}`;
    if (filter !== 'all' && dateInput) {
        url += `&date=${dateInput}`;
    }

    try {
        const res = await fetch(url, { headers: { 'Authorization': `Bearer ${token}` } });
        if (!res.ok) {
            alert("Lỗi khi xuất dữ liệu!"); return;
        }
        const blob = await res.blob();
        if (blob.size <= 100) {
            alert("Không có giao dịch nào trong khoảng thời gian đã chọn!"); return;
        }
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = `GiaoDich_${new Date().getTime()}.csv`;
        link.click();
        closeExportModal();
    } catch(err) { alert("Lỗi khi xuất dữ liệu!"); }
}

fetchWalletData();