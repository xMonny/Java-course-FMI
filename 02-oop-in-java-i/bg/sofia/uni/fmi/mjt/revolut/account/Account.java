package bg.sofia.uni.fmi.mjt.revolut.account;

import java.util.Objects;

public abstract class Account {
    private double amount;
    private String IBAN;

    public Account(String IBAN) {
        this(IBAN, 0);
    }

    public Account(String IBAN, double amount) {
        this.IBAN = IBAN;
        this.amount = amount;
    }

    public abstract String getCurrency();

    public void increaseAmount(double amountToAdd) {
        this.amount += amountToAdd;
    }
    public void decreaseAmount(double amountToDecrease) {
        this.amount -= amountToDecrease;
    }
    public double getAmount() {
        return this.amount;
    }
    public String getIBAN() {
        return this.IBAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getIBAN().equals(account.getIBAN());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIBAN());
    }
}
