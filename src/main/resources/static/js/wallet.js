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

async function exportToExcel() {
    try {
        const res = await fetch('/api/transactions', { headers: { 'Authorization': `Bearer ${token}` } });
        const data = await res.json();
        
        if (!data.transactions || data.transactions.length === 0) {
            alert("Không có giao dịch nào để xuất!"); return;
        }

        let csvContent = '\uFEFFNgày,Danh mục,Loại,Số tiền,Ghi chú\n';
        data.transactions.forEach(tx => {
            const date = new Date(tx.date).toLocaleDateString('vi-VN');
            const type = tx.type === 'income' ? 'Thu nhập' : (tx.type === 'expense' ? 'Chi tiêu' : 'Vay/Nợ');
            csvContent += `"${date}","${tx.categoryName || tx.category}","${type}","${tx.amount}","${tx.note || ''}"\n`;
        });

        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = `LichSuGiaoDich_${new Date().getTime()}.csv`;
        link.click();
    } catch(err) { alert("Lỗi khi xuất dữ liệu!"); }
}

fetchWalletData();