package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;

public class Revolut implements RevolutAPI{
    private final double VALUTE = 1.95583;
    private final Account[] revolutAccounts;
    private final Card[] revolutCards;
    private double foundMinimalAmount = Double.MAX_VALUE;
    private int rememberIndex = 0;
    private boolean isFoundAccount = false;

    public Revolut(Account[] accounts, Card[] cards) {
        revolutAccounts = accounts;
        revolutCards = cards;
    }

    private boolean isCardInRevolut(Card card) {
        for (Card c: revolutCards) {
            if (card.equals(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAccountInRevolut(double amount, String currency) {
        for (int i = 0; i < revolutAccounts.length; i++) {
            if (currency.equals(revolutAccounts[i].getCurrency()) && amount <= revolutAccounts[i].getAmount()) {
                if (revolutAccounts[i].getAmount() <= foundMinimalAmount) {
                    foundMinimalAmount = revolutAccounts[i].getAmount();
                    rememberIndex = i;
                    isFoundAccount = true;
                }
            }
        }
        if (isFoundAccount) {
            double foundAccountAmount = revolutAccounts[rememberIndex].getAmount();
            if (foundAccountAmount - amount <= 0) {
                revolutAccounts[rememberIndex].decreaseAmount(foundAccountAmount);
            }
            else {
                revolutAccounts[rememberIndex].decreaseAmount(amount);
            }
            foundMinimalAmount = Double.MAX_VALUE;
            rememberIndex = 0;
            isFoundAccount = false;
            return true;
        }
        return false;
    }

    private boolean isAccountInRevolut(Account account) {
        for (Account acc: revolutAccounts) {
            if (acc.equals(account)) {
                return true;
            }
        }
        return false;
    }

    private boolean areCardAndAccountAccepted(Card card, int pin, double amount, String currency) {
        if (!isCardInRevolut(card)) {
            return false;
        }
        if (!card.isValid()) {
            return false;
        }
        if (card.isBlocked()) {
            return false;
        }
        if (!card.checkPin(pin)) {
            return false;
        }
        if (!isAccountInRevolut(amount, currency)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean pay(Card card, int pin, double amount, String currency) {
        if (!card.getType().equals("PHYSICAL")) {
            return false;
        }
        if (!areCardAndAccountAccepted(card, pin, amount, currency)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean payOnline(Card card, int pin, double amount, String currency, String shopURL) {
        if (shopURL.endsWith(".biz")) {
            return false;
        }
        if (!areCardAndAccountAccepted(card, pin, amount, currency)) {
            return false;
        }
        if (card.getType().equals("VIRTUALONETIME")) {
            card.block();
        }
        return true;
    }

    @Override
    public boolean addMoney(Account account, double amount) {
        if (!isAccountInRevolut(account)) {
            return false;
        }
        account.increaseAmount(amount);
        return true;
    }

    @Override
    public boolean transferMoney(Account from, Account to, double amount) {
        if (!isAccountInRevolut(from)) {
            return false;
        }
        if (!isAccountInRevolut(to)) {
            return false;
        }
        if (from.getIBAN().equals(to.getIBAN())) {
            return false;
        }
        if (from.getCurrency().equals("EUR") && to.getCurrency().equals("BGN")) {
            if (from.getAmount() - amount <= 0) {
                return false;
            }
            else {
                from.decreaseAmount(amount);
                to.increaseAmount(amount*VALUTE);
            }
        }
        else if (from.getCurrency().equals("BGN") && to.getCurrency().equals("EUR")) {
            if (from.getAmount() - amount <= 0) {
                return false;
            }
            else {
                from.decreaseAmount(amount);
                to.increaseAmount(amount/VALUTE);
            }
        }
        else {
            if (from.getAmount() - amount <= 0) {
                return false;
            }
            else {
                from.decreaseAmount(amount);
                to.increaseAmount(amount);
            }
        }
        return true;
    }

    @Override
    public double getTotalAmount() {
        double totalAmount = 0;
        for (Account acc: revolutAccounts) {
            if (acc.getCurrency().equals("BGN")) {
                totalAmount += acc.getAmount();
            }
            else {
                totalAmount += acc.getAmount()*VALUTE;
            }
        }
        return totalAmount;
    }
}
