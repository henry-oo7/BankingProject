public class Account {
    private double balance = 0;
    private Customer customer;

    Account(Customer customer) {
        this.customer = customer;
    }
    Account(Customer customer, double balance) {
        this.customer = customer;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }
    public void withdraw(double amount) {
        balance -= amount;
    }

    public Customer getCustomer() {
        return customer;
    }
}
