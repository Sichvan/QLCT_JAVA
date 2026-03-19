package web.expense_management.dtos;

import java.util.List;

public class WalletResponse {
    private double balance;
    private double totalLending;
    private double totalBorrowing;
    private double monthIncome;
    private double monthExpense;
    private List<TransactionDTO> transactions;
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public double getTotalLending() {
        return totalLending;
    }
    public void setTotalLending(double totalLending) {
        this.totalLending = totalLending;
    }
    public double getTotalBorrowing() {
        return totalBorrowing;
    }
    public void setTotalBorrowing(double totalBorrowing) {
        this.totalBorrowing = totalBorrowing;
    }
    public double getMonthIncome() {
        return monthIncome;
    }
    public void setMonthIncome(double monthIncome) {
        this.monthIncome = monthIncome;
    }
    public double getMonthExpense() {
        return monthExpense;
    }
    public void setMonthExpense(double monthExpense) {
        this.monthExpense = monthExpense;
    }
    public List<TransactionDTO> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}