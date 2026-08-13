document.addEventListener('DOMContentLoaded', () => {
    const sidebarHtml = `
            <div class="sidebar-logo"><i class="material-icons">account_balance_wallet</i> QLCT_HK</div>
            <nav class="sidebar-menu">
                <a href="/screens/home.html" class="menu-item"><i class="material-icons">dashboard</i> <span data-i18n="nav.overview">Tổng quan</span></a>
                <a href="/screens/wallet.html" class="menu-item"><i class="material-icons">account_balance</i> <span data-i18n="nav.wallet">Ví của tôi</span></a>
                <a href="/screens/stats.html" class="menu-item"><i class="material-icons">pie_chart</i> <span data-i18n="nav.stats">Thống kê</span></a>
                <a href="/screens/chat.html" class="menu-item"><i class="material-icons">smart_toy</i> <span data-i18n="nav.ai">Trợ lý AI</span></a>
            </nav>
            <div class="sidebar-footer"><a href="/screens/settings.html" class="menu-item"><i class="material-icons">settings</i> <span data-i18n="nav.settings">Cài đặt</span></a></div>
    `;

    const sidebarContainer = document.getElementById('sidebar-container');
    if (sidebarContainer) {
        sidebarContainer.innerHTML = sidebarHtml;
        
        // Cập nhật trạng thái active
        const currentPath = window.location.pathname;
        const menuItems = sidebarContainer.querySelectorAll('.menu-item');
        menuItems.forEach(item => {
            if (item.getAttribute('href') === currentPath) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        });

        // Nếu file i18n.js đã chạy trước, cần gọi lại để dịch các phần tử mới tạo
        if (typeof applyLanguage === 'function') {
            applyLanguage();
        }
    }
});
