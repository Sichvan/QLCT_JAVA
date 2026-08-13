// ======= HỆ THỐNG ĐA NGÔN NGỮ (i18n) =======
const translations = {
    vi: {
        // Sidebar
        'nav.overview': 'Tổng quan',
        'nav.wallet': 'Ví của tôi',
        'nav.stats': 'Thống kê',
        'nav.ai': 'Trợ lý AI',
        'nav.settings': 'Cài đặt',
        'nav.home': 'Trang chủ',
        'nav.personal': 'Cá nhân',

        // Home
        'home.greeting': 'Chào buổi sáng,',
        'home.loading': 'Đang tải...',
        'home.total_assets': 'Tổng Tài Sản Hiện Có',
        'home.updated': 'Cập nhật:',
        'home.month_income': 'Thu nhập tháng',
        'home.month_expense': 'Chi tiêu tháng',
        'home.recent': 'Giao dịch gần đây',
        'home.add': 'Ghi chép',
        'home.no_tx': 'Chưa có giao dịch nào',
        'home.loading_tx': 'Đang tải dữ liệu...',
        'home.budget_warning': 'Cảnh báo: Chi tiêu tháng này đã vượt ngân sách!',
        'home.edit_tx': 'Sửa giao dịch',
        'home.amount': 'Số tiền (VNĐ)',
        'home.category': 'Danh mục',
        'home.date': 'Ngày giao dịch',
        'home.note': 'Ghi chú',
        'home.cancel': 'HỦY',
        'home.update': 'CẬP NHẬT',
        'home.confirm_delete': 'Bạn có chắc chắn muốn xóa giao dịch này không? Số dư ví sẽ được cập nhật lại.',
        'home.err_delete': 'Lỗi khi xóa giao dịch!',
        'home.err_update': 'Lỗi cập nhật giao dịch!',
        'home.err_server': 'Không thể kết nối máy chủ!',
        'home.err_amount': 'Số tiền không hợp lệ!',

        // Wallet
        'wallet.title': 'Ví của tôi',
        'wallet.total_assets': 'TỔNG TÀI SẢN',
        'wallet.lending': 'Cho vay',
        'wallet.borrowing': 'Đang nợ',
        'wallet.utilities': 'Tiện ích',
        'wallet.budget': 'Ngân sách',
        'wallet.goal': 'Mục tiêu',
        'wallet.export': 'Xuất dữ liệu',
        'wallet.recurring': 'Định kỳ',
        'wallet.export_title': 'Xuất dữ liệu CSV',
        'wallet.export_type': 'Kiểu xuất',
        'wallet.export_all': 'Tất cả giao dịch',
        'wallet.export_day': 'Theo ngày',
        'wallet.export_month': 'Theo tháng',
        'wallet.export_year': 'Theo năm',
        'wallet.export_time': 'Chọn thời gian',
        'wallet.export_cancel': 'HỦY',
        'wallet.export_download': 'TẢI CSV',

        // Transaction Add
        'tx.title': 'Thêm Giao Dịch',
        'tx.expense': 'Chi Tiêu',
        'tx.income': 'Thu Nhập',
        'tx.loan': 'Vay / Nợ',
        'tx.amount': 'Số tiền (VNĐ)',
        'tx.select_cat': 'Chọn Danh mục',
        'tx.loading_cat': 'Đang tải danh mục...',
        'tx.date': 'Ngày giao dịch',
        'tx.note': 'Ghi chú',
        'tx.note_placeholder': 'Chi tiết giao dịch (không bắt buộc)...',
        'tx.person': 'Vay/Nợ ai?',
        'tx.person_placeholder': 'VD: Nguyễn Văn A',
        'tx.save': 'LƯU GIAO DỊCH',
        'tx.no_cat': 'Chưa có danh mục nào.',
        'tx.amount_placeholder': 'VD: 500.000',
        'tx.error_amount': 'Vui lòng nhập số tiền hợp lệ!',
        'tx.error_cat': 'Vui lòng chọn 1 danh mục!',
        'tx.error_server': 'Không thể kết nối máy chủ!',
        'tx.error_general': 'Có lỗi xảy ra!',

        // Stats
        'stats.title': 'Thống kê',
        'stats.expense': 'Chi Tiêu',
        'stats.income': 'Thu Nhập',
        'stats.detail': 'Chi tiết danh mục',
        'stats.loading': 'Đang tải dữ liệu...',
        'stats.no_data': 'Không có dữ liệu',
        'stats.30days': '30 ngày qua',
        'stats.by_day': 'Theo ngày',
        'stats.by_month': 'Theo tháng',
        'stats.by_year': 'Theo năm',
        'stats.filter': 'Lọc',

        // Settings
        'settings.title': 'Cài đặt cá nhân',
        'settings.system': 'Tùy chọn hệ thống',
        'settings.dark_mode': 'Giao diện Tối (Dark Mode)',
        'settings.language': 'Ngôn ngữ (Language)',
        'settings.categories': 'Quản lý Danh mục',
        'settings.edit_profile': 'Sửa mật khẩu & thông tin',
        'settings.logout': 'Đăng xuất',
        'settings.logout_confirm': 'Bạn có chắc chắn muốn đăng xuất?',
        'settings.edit_photo': 'SỬA ẢNH',

        // Categories
        'cat.eating': 'Ăn uống',
        'cat.shopping': 'Mua sắm',
        'cat.transport': 'Di chuyển',
        'cat.education': 'Học tập',
        'cat.health': 'Sức khỏe',
        'cat.other_expense': 'Chi tiêu khác',
        'cat.salary': 'Tiền lương',
        'cat.investment': 'Đầu tư',
        'cat.other_income': 'Thu nhập khác',
        'cat.lending': 'Cho vay',
        'cat.borrowing': 'Đi vay',
        'cat.repaying': 'Trả nợ',
        'cat.collecting': 'Thu nợ',

        // Budget
        'budget.title': 'Ngân sách',
        'budget.month_budget': 'Ngân sách tháng này',
        'budget.btn_add': 'Thêm mới',
        'budget.loading': 'Đang tải dữ liệu...',
        'budget.no_budget': 'Chưa có ngân sách nào được thiết lập',
        'budget.modal_title': 'Thiết lập ngân sách',
        'budget.cat': 'Danh mục',
        'budget.amount': 'Số tiền hạn mức (VNĐ)',
        'budget.amount_placeholder': 'VD: 5.000.000',
        'budget.btn_cancel': 'HỦY',
        'budget.btn_save': 'LƯU LẠI',
        'budget.spent': 'Đã chi',
        'budget.limit': 'Hạn mức',
        'budget.exceeded': 'Đã vượt ngân sách!',
        'budget.warning_toast': 'Cảnh báo: Bạn đã chi tiêu vượt quá một số ngân sách!',
        'budget.confirm_delete': 'Bạn có chắc muốn xóa ngân sách này?',

        // Goal
        'goal.title': 'Mục tiêu tiết kiệm',
        'goal.my_goals': 'Danh sách mục tiêu',
        'goal.btn_add': 'Thêm mới',
        'goal.loading': 'Đang tải dữ liệu...',
        'goal.no_goal': 'Chưa có mục tiêu nào',
        'goal.completed': 'Đã hoàn thành!',
        'goal.click_to_deposit': 'Nhấn vào để nạp tiền',
        'goal.modal_title': 'Tạo mục tiêu mới',
        'goal.name': 'Tên mục tiêu',
        'goal.name_placeholder': 'VD: Mua xe máy...',
        'goal.amount': 'Số tiền cần đạt (VNĐ)',
        'goal.amount_placeholder': 'VD: 50.000.000',
        'goal.btn_cancel': 'HỦY',
        'goal.btn_create': 'TẠO MỤC TIÊU',
        'goal.btn_save': 'LƯU LẠI',
        'goal.add_edit_title': 'Thêm/Sửa mục tiêu',
        'goal.deposit_title': 'Nạp tiền tiết kiệm',
        'goal.deposit_amount_label': 'Số tiền nạp (VNĐ)',
        'goal.btn_deposit': 'NẠP VÀO',
        'goal.confirm_delete': 'Bạn có chắc muốn xóa mục tiêu này?',
        'goal.deposit_prompt': 'Nhập số tiền muốn nạp vào mục tiêu "{name}":\n(VD: 50000)',
        'goal.invalid_amount': 'Số tiền không hợp lệ!',
        'goal.deposit_error': 'Lỗi nạp tiền!',
        'goal.target_prefix': 'Mục tiêu: ',
        'goal.remaining_prefix': 'Số tiền còn thiếu: ',
        'goal.err_name': 'Vui lòng nhập tên mục tiêu!',
        'goal.err_amount': 'Số tiền không hợp lệ!',
        'goal.err_conn': 'Lỗi kết nối',
        'goal.deposit_excess': 'Bạn nhập dư tiền!\nHệ thống sẽ chỉ trích đúng {amount} để hoàn thành mục tiêu này.',
        'goal.deposit_success': 'Nạp tiền thành công!',
        'goal.deposit_fail': 'Lỗi nạp tiền! (Ví của bạn có đủ tiền không?)',
        'goal.deposit_complete': '🎉 CHÚC MỪNG BẠN! 🎉\nBạn đã tiết kiệm đủ tiền. Mục tiêu này đã hoàn thành và sẽ được gỡ khỏi danh sách.',

        // Recurring
        'recurring.title': 'Giao dịch định kỳ',
        'recurring.list_title': 'Giao dịch lặp lại',
        'recurring.btn_add': 'Thêm mới',
        'recurring.loading': 'Đang tải dữ liệu...',
        'recurring.no_recurring': 'Chưa có thiết lập định kỳ nào',
        'recurring.monthly_day': 'Ngày {day} hàng tháng',
        'recurring.modal_title': 'Tạo giao dịch định kỳ',
        'recurring.name': 'Tên giao dịch',
        'recurring.name_placeholder': 'VD: Tiền nhà, Internet...',
        'recurring.amount': 'Số tiền (VNĐ)',
        'recurring.amount_placeholder': 'VD: 5.000.000',
        'recurring.type': 'Loại',
        'recurring.expense': 'Chi tiêu',
        'recurring.income': 'Thu nhập',
        'recurring.day_label': 'Ngày (1-31)',
        'recurring.note_label': 'Ghi chú (Tên giao dịch)',
        'recurring.note_placeholder': 'VD: Đóng tiền trọ',
        'recurring.btn_save': 'LƯU LỊCH',
        'recurring.auto_run': 'Tự động kích hoạt vào ngày {day} hàng tháng',
        'recurring.type_expense': 'Khoản chi',
        'recurring.type_income': 'Khoản thu',
        'recurring.cycle': 'Chu kỳ',
        'recurring.cycle_daily': 'Hàng ngày',
        'recurring.cycle_weekly': 'Hàng tuần',
        'recurring.cycle_monthly': 'Hàng tháng',
        'recurring.cycle_yearly': 'Hàng năm',
        'recurring.start_date': 'Ngày bắt đầu',
        'recurring.btn_cancel': 'HỦY',
        'recurring.btn_create': 'TẠO MỚI',
        'recurring.add_edit_title': 'Tạo/Sửa lịch tự động',
        'recurring.confirm_delete': 'Bạn có chắc muốn xóa giao dịch định kỳ này?',
        'recurring.err_amount': 'Số tiền không hợp lệ!',
        'recurring.err_day': 'Ngày trong tháng phải từ 1 đến 31!',
        'recurring.err_conn': 'Lỗi kết nối!',

        // Account Settings
        'account.title': 'Sửa thông tin cá nhân',
        'account.fullname': 'Họ và tên',
        'account.fullname_placeholder': 'Nhập họ tên của bạn',
        'account.phone': 'Số điện thoại',
        'account.phone_placeholder': 'Nhập số điện thoại',
        'account.change_pwd': 'Đổi mật khẩu (Bỏ trống nếu không đổi)',
        'account.current_pwd': 'Mật khẩu hiện tại',
        'account.new_pwd': 'Mật khẩu mới',
        'account.btn_save': 'LƯU THAY ĐỔI',
        'account.err_req_old_pw': 'Vui lòng nhập mật khẩu hiện tại để đổi mật khẩu mới!',
        'account.update_success': 'Cập nhật thông tin thành công!',
        'account.update_fail': 'Cập nhật thất bại!',
        'account.err_server': 'Lỗi kết nối!',
        'account.err_wrong_pw': 'Mật khẩu hiện tại không chính xác',

        // Category Management
        'cat_manage.title': 'Quản lý Danh mục',
        'cat_manage.add_title': 'Thêm Danh Mục Mới',
        'cat_manage.edit_title': 'Sửa Danh Mục',
        'cat_manage.cancel_edit': 'HỦY SỬA',
        'cat_manage.name_label': 'TÊN DANH MỤC',
        'cat_manage.name_placeholder': 'VD: Mua sắm, Lương...',
        'cat_manage.type_label': 'Loại giao dịch',
        'cat_manage.type_expense': 'Khoản Chi',
        'cat_manage.type_income': 'Khoản Thu',
        'cat_manage.type_note': '(Không thể đổi loại khi sửa)',
        'cat_manage.icon_label': 'Chọn biểu tượng',
        'cat_manage.btn_add': 'THÊM DANH MỤC MỚI',
        'cat_manage.btn_update': 'CẬP NHẬT LẠI',
        'cat_manage.list_title': 'Danh sách của bạn',
        'cat_manage.loading': 'Đang tải dữ liệu...',
        'cat_manage.no_cat': 'Chưa có danh mục nào',
        'cat_manage.confirm_delete': 'Bạn có chắc chắn muốn xóa danh mục này?',
        'cat_manage.name_required': 'Vui lòng nhập tên danh mục!',
        'cat_manage.server_error': 'Lỗi kết nối đến máy chủ!',

        // Account Edit
        'account.title': 'Sửa thông tin cá nhân',
        'account.general_info': 'Thông tin chung',
        'account.fullname': 'Họ và Tên',
        'account.fullname_placeholder': 'Nhập họ tên của bạn',
        'account.phone': 'Số điện thoại',
        'account.phone_placeholder': 'Nhập số điện thoại',
        'account.change_pwd': 'Đổi mật khẩu (Bỏ trống nếu không đổi)',
        'account.current_pwd': 'Mật khẩu hiện tại',
        'account.new_pwd': 'Mật khẩu mới',
        'account.err_req_old_pw': 'Vui lòng nhập mật khẩu cũ để đổi mật khẩu!',
        'account.update_success': 'Cập nhật thông tin thành công!',
        'account.update_fail': 'Cập nhật thất bại!',
        'account.err_server': 'Lỗi kết nối máy chủ!',
        'account.btn_save': 'LƯU THAY ĐỔI',
        'account.err_req_old_pw': 'Vui lòng nhập mật khẩu hiện tại để xác nhận đổi mật khẩu!',
        'account.update_success': 'Cập nhật thông tin thành công!',
        'account.update_fail': 'Cập nhật thất bại!',
        'account.err_server': 'Lỗi kết nối máy chủ',

        'tx_add.tab_expense': 'Chi Tiêu',
        'tx_add.tab_income': 'Thu Nhập',
        'tx_add.tab_loan': 'Vay / Nợ',
        'tx_add.title': 'Thêm Giao Dịch',

        // Chat AI
        'chat.title': 'Trợ lý AI',
        'chat.status': 'Đang hoạt động - Sẵn sàng tư vấn',
        'chat.placeholder': 'Hỏi AI bất cứ điều gì về tài chính...',
        'chat.clear_confirm': 'Bạn có chắc muốn xóa toàn bộ đoạn hội thoại này?',
        'chat.welcome': 'Xin chào! Mình là Sichvan - Trợ lý tài chính thông minh của bạn. Mình đã nắm được số dư hiện tại của bạn. Bạn muốn mình tư vấn cách tiết kiệm, hay lập kế hoạch chi tiêu tháng này?',
        'chat.error': '⚠️ Xin lỗi, hiện tại máy chủ AI đang bận. Vui lòng thử lại sau!',

        // Auth
        'auth.login': 'Đăng nhập',
        'auth.register': 'Đăng Ký Tài Khoản',
        'auth.email': 'Email',
        'auth.password': 'Mật khẩu',
        'auth.confirm_pw': 'Xác nhận mật khẩu',
        'auth.fullname': 'Họ và tên',
        'auth.phone': 'Số điện thoại',
        'auth.no_account': 'Chưa có tài khoản?',
        'auth.register_now': 'Đăng ký ngay',
        'auth.has_account': 'Đã có tài khoản?',
        'auth.login_now': 'Đăng nhập',
        'auth.forgot': 'Quên mật khẩu?',
    },
    en: {
        // Sidebar
        'nav.overview': 'Dashboard',
        'nav.wallet': 'My Wallet',
        'nav.stats': 'Statistics',
        'nav.ai': 'AI Assistant',
        'nav.settings': 'Settings',
        'nav.home': 'Home',
        'nav.personal': 'Profile',

        // Home
        'home.greeting': 'Good morning,',
        'home.loading': 'Loading...',
        'home.total_assets': 'Total Assets',
        'home.updated': 'Updated:',
        'home.month_income': 'Monthly Income',
        'home.month_expense': 'Monthly Expense',
        'home.recent': 'Recent Transactions',
        'home.add': 'Add New',
        'home.no_tx': 'No transactions yet',
        'home.loading_tx': 'Loading data...',
        'home.budget_warning': 'Warning: This month\'s spending exceeded budget!',
        'home.edit_tx': 'Edit Transaction',
        'home.amount': 'Amount (VNĐ)',
        'home.category': 'Category',
        'home.date': 'Date',
        'home.note': 'Note',
        'home.cancel': 'CANCEL',
        'home.update': 'UPDATE',
        'home.confirm_delete': 'Are you sure you want to delete this transaction? Your wallet balance will be updated.',
        'home.err_delete': 'Error deleting transaction!',
        'home.err_update': 'Error updating transaction!',
        'home.err_server': 'Cannot connect to server!',
        'home.err_amount': 'Invalid amount!',

        // Wallet
        'wallet.title': 'My Wallet',
        'wallet.total_assets': 'TOTAL ASSETS',
        'wallet.lending': 'Lending',
        'wallet.borrowing': 'Borrowing',
        'wallet.utilities': 'Utilities',
        'wallet.budget': 'Budget',
        'wallet.goal': 'Goals',
        'wallet.export': 'Export Data',
        'wallet.recurring': 'Recurring',
        'wallet.export_title': 'Export CSV Data',
        'wallet.export_type': 'Export type',
        'wallet.export_all': 'All transactions',
        'wallet.export_day': 'By day',
        'wallet.export_month': 'By month',
        'wallet.export_year': 'By year',
        'wallet.export_time': 'Select time',
        'wallet.export_cancel': 'CANCEL',
        'wallet.export_download': 'DOWNLOAD CSV',

        // Transaction Add
        'tx.title': 'Add Transaction',
        'tx.expense': 'Expense',
        'tx.income': 'Income',
        'tx.loan': 'Loan / Debt',
        'tx.amount': 'Amount (VNĐ)',
        'tx.select_cat': 'Select Category',
        'tx.loading_cat': 'Loading categories...',
        'tx.date': 'Transaction Date',
        'tx.note': 'Note',
        'tx.note_placeholder': 'Transaction details (optional)...',
        'tx.person': 'Who?',
        'tx.person_placeholder': 'E.g: John Doe',
        'tx.save': 'SAVE TRANSACTION',
        'tx.no_cat': 'No categories found.',
        'tx.amount_placeholder': 'Ex: 500,000',
        'tx.error_amount': 'Please enter a valid amount!',
        'tx.error_cat': 'Please select a category!',
        'tx.error_server': 'Cannot connect to server!',
        'tx.error_general': 'An error occurred!',

        // Stats
        'stats.title': 'Statistics',
        'stats.expense': 'Expense',
        'stats.income': 'Income',
        'stats.detail': 'Category Details',
        'stats.loading': 'Loading data...',
        'stats.no_data': 'No data available',
        'stats.30days': 'Last 30 days',
        'stats.by_day': 'By day',
        'stats.by_month': 'By month',
        'stats.by_year': 'By year',
        'stats.filter': 'Filter',

        // Settings
        'settings.title': 'Settings',
        'settings.system': 'System Options',
        'settings.dark_mode': 'Dark Mode',
        'settings.language': 'Language',
        'settings.categories': 'Manage Categories',
        'settings.edit_profile': 'Edit Password & Info',
        'settings.logout': 'Logout',
        'settings.logout_confirm': 'Are you sure you want to logout?',
        'settings.edit_photo': 'EDIT',

        // Categories
        'cat.eating': 'Eating',
        'cat.shopping': 'Shopping',
        'cat.transport': 'Transport',
        'cat.education': 'Education',
        'cat.health': 'Health',
        'cat.other_expense': 'Other Expense',
        'cat.salary': 'Salary',
        'cat.investment': 'Investment',
        'cat.other_income': 'Other Income',
        'cat.lending': 'Lending',
        'cat.borrowing': 'Borrowing',
        'cat.repaying': 'Repaying',
        'cat.collecting': 'Collecting',

        // Budget
        'budget.title': 'Budget',
        'budget.month_budget': 'This Month Budget',
        'budget.btn_add': 'Add New',
        'budget.loading': 'Loading...',
        'budget.no_budget': 'No budget set yet',
        'budget.modal_title': 'Set Budget',
        'budget.cat': 'Category',
        'budget.amount': 'Limit Amount',
        'budget.amount_placeholder': 'Ex: 5,000,000',
        'budget.btn_cancel': 'CANCEL',
        'budget.btn_save': 'SAVE',
        'budget.spent': 'Spent',
        'budget.limit': 'Limit',
        'budget.exceeded': 'Budget exceeded!',
        'budget.warning_toast': 'Warning: You have exceeded some budgets!',
        'budget.confirm_delete': 'Are you sure you want to delete this budget?',

        // Goal
        'goal.title': 'Savings Goals',
        'goal.my_goals': 'Goals List',
        'goal.btn_add': 'Add New',
        'goal.loading': 'Loading...',
        'goal.no_goal': 'No goals set yet',
        'goal.completed': 'Completed!',
        'goal.click_to_deposit': 'Click to deposit',
        'goal.modal_title': 'Create New Goal',
        'goal.name': 'Goal Name',
        'goal.name_placeholder': 'Ex: Buy a motorbike...',
        'goal.amount': 'Target Amount',
        'goal.amount_placeholder': 'Ex: 50,000,000',
        'goal.btn_cancel': 'CANCEL',
        'goal.btn_create': 'CREATE GOAL',
        'goal.btn_save': 'SAVE',
        'goal.add_edit_title': 'Add/Edit Goal',
        'goal.deposit_title': 'Deposit Savings',
        'goal.deposit_amount_label': 'Amount (VNĐ)',
        'goal.btn_deposit': 'DEPOSIT',
        'goal.confirm_delete': 'Are you sure you want to delete this goal?',
        'goal.deposit_prompt': 'Enter amount to deposit into goal "{name}":\n(Ex: 50000)',
        'goal.invalid_amount': 'Invalid amount!',
        'goal.deposit_error': 'Error depositing!',
        'goal.target_prefix': 'Target: ',
        'goal.remaining_prefix': 'Remaining amount: ',
        'goal.err_name': 'Please enter a goal name!',
        'goal.err_amount': 'Invalid amount!',
        'goal.err_conn': 'Connection error',
        'goal.deposit_excess': 'You entered excess amount!\nThe system will only deduct {amount} to complete this goal.',
        'goal.deposit_success': 'Deposit successful!',
        'goal.deposit_fail': 'Deposit error! (Do you have enough balance?)',
        'goal.deposit_complete': '🎉 CONGRATULATIONS! 🎉\nYou have saved enough money. This goal is completed and will be removed from the list.',

        // Recurring
        'recurring.title': 'Recurring Transactions',
        'recurring.list_title': 'Recurring List',
        'recurring.btn_add': 'Add New',
        'recurring.loading': 'Loading...',
        'recurring.no_recurring': 'No recurring transactions set',
        'recurring.monthly_day': 'Day {day} monthly',
        'recurring.modal_title': 'Create Recurring Transaction',
        'recurring.name': 'Transaction Name',
        'recurring.name_placeholder': 'Ex: Rent, Internet...',
        'recurring.amount': 'Amount',
        'recurring.amount_placeholder': 'Ex: 5,000,000',
        'recurring.type': 'Type',
        'recurring.expense': 'Expense',
        'recurring.income': 'Income',
        'recurring.day_label': 'Day (1-31)',
        'recurring.note_label': 'Note (Transaction Name)',
        'recurring.note_placeholder': 'Ex: Rent payment',
        'recurring.btn_save': 'SAVE',
        'recurring.auto_run': 'Auto activates on day {day} of month',
        'recurring.type_expense': 'Expense',
        'recurring.type_income': 'Income',
        'recurring.cycle': 'Cycle',
        'recurring.cycle_daily': 'Daily',
        'recurring.cycle_weekly': 'Weekly',
        'recurring.cycle_monthly': 'Monthly',
        'recurring.cycle_yearly': 'Yearly',
        'recurring.start_date': 'Start Date',
        'recurring.btn_cancel': 'CANCEL',
        'recurring.btn_create': 'CREATE',
        'recurring.add_edit_title': 'Create/Edit Recurring',
        'recurring.confirm_delete': 'Are you sure you want to delete this recurring transaction?',
        'recurring.err_amount': 'Invalid amount!',
        'recurring.err_day': 'Day of month must be between 1 and 31!',
        'recurring.err_conn': 'Connection error!',

        // Account Settings
        'account.title': 'Edit Profile',
        'account.fullname': 'Full Name',
        'account.fullname_placeholder': 'Enter your full name',
        'account.phone': 'Phone Number',
        'account.phone_placeholder': 'Enter your phone number',
        'account.change_pwd': 'Change Password (Leave blank to keep)',
        'account.current_pwd': 'Current Password',
        'account.new_pwd': 'New Password',
        'account.btn_save': 'SAVE CHANGES',
        'account.err_req_old_pw': 'Please enter current password to set a new one!',
        'account.update_success': 'Profile updated successfully!',
        'account.update_fail': 'Profile update failed!',
        'account.err_server': 'Connection error!',
        'account.err_wrong_pw': 'Incorrect current password',

        // Category Management
        'cat_manage.title': 'Manage Categories',
        'cat_manage.add_title': 'Add New Category',
        'cat_manage.edit_title': 'Edit Category',
        'cat_manage.cancel_edit': 'CANCEL EDIT',
        'cat_manage.name_label': 'CATEGORY NAME',
        'cat_manage.name_placeholder': 'Ex: Shopping, Salary...',
        'cat_manage.type_label': 'Transaction Type',
        'cat_manage.type_expense': 'Expense',
        'cat_manage.type_income': 'Income',
        'cat_manage.type_note': '(Cannot change type when editing)',
        'cat_manage.icon_label': 'Select Icon',
        'cat_manage.btn_add': 'ADD CATEGORY',
        'cat_manage.btn_update': 'UPDATE',
        'cat_manage.list_title': 'Your Categories',
        'cat_manage.loading': 'Loading data...',
        'cat_manage.no_cat': 'No categories yet',
        'cat_manage.confirm_delete': 'Are you sure you want to delete this category?',
        'cat_manage.name_required': 'Please enter a category name!',
        'cat_manage.server_error': 'Cannot connect to server!',

        // Account Edit
        'account.title': 'Edit Profile',
        'account.general_info': 'General Info',
        'account.fullname': 'Full Name',
        'account.fullname_placeholder': 'Enter your full name',
        'account.phone': 'Phone Number',
        'account.phone_placeholder': 'Enter phone number',
        'account.change_pwd': 'Change Password (Leave blank to keep)',
        'account.current_pwd': 'Current Password',
        'account.new_pwd': 'New Password',
        'account.err_req_old_pw': 'Please enter current password to change it!',
        'account.update_success': 'Profile updated successfully!',
        'account.update_fail': 'Update failed!',
        'account.err_server': 'Server connection error!',
        'account.btn_save': 'SAVE CHANGES',
        'account.err_req_old_pw': 'Please enter current password to change password!',
        'account.update_success': 'Profile updated successfully!',
        'account.update_fail': 'Update failed!',
        'account.err_server': 'Server connection error',

        'tx_add.tab_expense': 'Expense',
        'tx_add.tab_income': 'Income',
        'tx_add.tab_loan': 'Loan / Debt',
        'tx_add.title': 'Add Transaction',

        // Chat AI
        'chat.title': 'AI Assistant',
        'chat.status': 'Online - Ready to assist',
        'chat.placeholder': 'Ask AI anything about finance...',
        'chat.clear_confirm': 'Are you sure you want to clear the chat?',
        'chat.welcome': 'Hello! I am Sichvan - Your smart financial assistant. I have your current balance. Would you like me to advise on saving, or plan a budget for this month?',
        'chat.error': '⚠️ Sorry, the AI server is busy. Please try again later!',

        // Auth
        'auth.login': 'Login',
        'auth.register': 'Create Account',
        'auth.email': 'Email',
        'auth.password': 'Password',
        'auth.confirm_pw': 'Confirm Password',
        'auth.fullname': 'Full Name',
        'auth.phone': 'Phone Number',
        'auth.no_account': 'Don\'t have an account?',
        'auth.register_now': 'Register now',
        'auth.has_account': 'Already have an account?',
        'auth.login_now': 'Login',
        'auth.forgot': 'Forgot password?',
    }
};

// Hàm lấy ngôn ngữ hiện tại
function getCurrentLang() {
    return localStorage.getItem('app_lang') || 'vi';
}

// Hàm dịch 1 key
function t(key) {
    const lang = getCurrentLang();
    return translations[lang][key] || translations['vi'][key] || key;
}

// Hàm dịch tên danh mục
function tCat(catId, fallbackName) {
    const key = 'cat.' + catId;
    const translated = t(key);
    return translated !== key ? translated : (fallbackName || catId);
}

// Hàm áp dụng ngôn ngữ lên toàn bộ trang
function applyLanguage() {
    const elements = document.querySelectorAll('[data-i18n]');
    elements.forEach(el => {
        const key = el.getAttribute('data-i18n');
        const translated = t(key);
        if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
            el.placeholder = translated;
        } else if (el.tagName === 'OPTION') {
            el.textContent = translated;
        } else {
            el.textContent = translated;
        }
    });
    
    // Cập nhật title trang
    const titleKey = document.querySelector('title[data-i18n]');
    if (titleKey) {
        document.title = t(titleKey.getAttribute('data-i18n'));
    }
}

// Hàm chuyển đổi ngôn ngữ
function toggleLanguage() {
    const current = getCurrentLang();
    const newLang = current === 'vi' ? 'en' : 'vi';
    localStorage.setItem('app_lang', newLang);
    
    // Cập nhật icon
    const langIcon = document.getElementById('lang-icon');
    if (langIcon) {
        langIcon.textContent = newLang === 'vi' ? '🇻🇳 VI' : '🇬🇧 EN';
    }
    
    // Reload trang để áp dụng mọi thứ (kể cả nội dung động)
    window.location.reload();
}

// Tự động áp dụng khi trang load
document.addEventListener('DOMContentLoaded', () => {
    applyLanguage();
    
    const langIcon = document.getElementById('lang-icon');
    if (langIcon) {
        langIcon.textContent = getCurrentLang() === 'vi' ? '🇻🇳 VI' : '🇬🇧 EN';
    }
});
