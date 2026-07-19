/**
 * BankTransferLockOrderingExample.java
 * 
 * This example demonstrates a bank transfer scenario where 
 * two threads attempt to transfer money between two bank accounts.
 * It uses lock ordering to prevent deadlocks by always acquiring 
 * locks in a consistent order based on the account IDs.
 */
import java.util.concurrent.ThreadLocalRandom;

public class TransfersLockOrderingExample {
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
        // Starting balance for both accounts
        BankAccount accountA = new BankAccount(1, 10000);
        BankAccount accountB = new BankAccount(2, 10000);

        System.out.println("Beginning:");
        System.out.println("Account A balance = " + accountA.getBalance());
        System.out.println("Account B balance = " + accountB.getBalance());

        final int TOTAL_TRANSFERS = 100;
        final int TRANSFERS_PER_THREAD = TOTAL_TRANSFERS / 2; // 50 each, 100 total

        // Thread 1: repeatedly transfers A -> B
        Thread threadOne = new Thread(() -> {
            for (int i = 0; i < TRANSFERS_PER_THREAD; i++) {
                sleepRandom();
                transfer(accountA, accountB, 100);
            }
        });

        // Thread 2: repeatedly transfers B -> A
        Thread threadTwo = new Thread(() -> {
            for (int i = 0; i < TRANSFERS_PER_THREAD; i++) {
                sleepRandom();
                transfer(accountB, accountA, 200);
            }
        });

        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();

        System.out.println("Ending:");
        System.out.println("Account A balance = " + accountA.getBalance());
        System.out.println("Account B balance = " + accountB.getBalance());
    }

    /**
     * Sleeps for a small random interval (1-10 ms) to interleave
     * thread execution and make lock-ordering behavior more visible.
     */
    private static void sleepRandom() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 11));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}