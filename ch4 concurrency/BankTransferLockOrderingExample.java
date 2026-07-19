/**
 * BankTransferLockOrderingExample.java
 * 
 * This example demonstrates a bank transfer scenario where 
 * two threads attempt to transfer money between two bank accounts.
 * It uses lock ordering to prevent deadlocks by always acquiring 
 * locks in a consistent order based on the account IDs.
 */
public class BankTransferLockOrderingExample {
    static class BankAccount {
        private final int accountId;
        private int balance;

        public BankAccount(int accountId, int balance) {
            this.accountId = accountId;
            this.balance = balance;
        }

        public int getAccountId() {
            return accountId;
        }

        public int getBalance() {
            return balance;
        }

        private void withdraw(int amount) {
            balance -= amount;
        }

        private void deposit(int amount) {
            balance += amount;
        }
    }

    public static void transfer(BankAccount from, BankAccount to, int amount) {
        BankAccount firstLock;
        BankAccount secondLock;
        if (from.getAccountId() < to.getAccountId()) {
            firstLock = from;
            secondLock = to;
        } else {
            firstLock = to;
            secondLock = from;
        }
        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.getBalance() < amount) {
                    System.out.println("Insufficient balance in account " + from.getAccountId());
                    return;
                }
                from.withdraw(amount);
                to.deposit(amount);
                System.out.println("Transferred " + amount + " from account " + from.getAccountId() + " to account "
                        + to.getAccountId());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount accountA = new BankAccount(1, 1000);
        BankAccount accountB = new BankAccount(2, 1000);

        System.out.println("Beginning:");
        System.out.println("Account A balance = " + accountA.getBalance());
        System.out.println("Account B balance = " + accountB.getBalance());

        Thread threadOne = new Thread(() -> {
            transfer(accountA, accountB, 100);
        });
        Thread threadTwo = new Thread(() -> {
            transfer(accountB, accountA, 200);
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();

        System.out.println("Ending:");
        System.out.println("Account A balance = " + accountA.getBalance());
        System.out.println("Account B balance = " + accountB.getBalance());
    }
}