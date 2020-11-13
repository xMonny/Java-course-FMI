package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;
import java.util.Objects;

public abstract class BasicCard implements Card{
    private String cardNumber;
    private int codePIN;
    private LocalDate expirationDate;
    private int counterToBlock;
    private boolean isCardBlocked;

    public BasicCard(String number, int pin, LocalDate expirationDate) {
        setCardNumber(number);
        setCodePIN(pin);
        setExpirationDate(expirationDate);
        this.counterToBlock = 0;
    }

    private void setCardNumber(String newCardNumber) {
        this.cardNumber = newCardNumber;
    }
    private void setCodePIN(int newCodePIN) {
        this.codePIN = newCodePIN;
    }
    private void setExpirationDate(LocalDate newExpirationDate) {
        this.expirationDate = newExpirationDate;
    }

    @Override
    public abstract String getType();

    @Override
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public boolean checkPin(int pin) {
        if (isBlocked()) {
            return false;
        }
        if (this.codePIN != pin) {
            this.counterToBlock++;
            if (this.counterToBlock == 3) {
                block();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isBlocked() {
        return this.isCardBlocked;
    }

    @Override
    public void block() {
        this.isCardBlocked = true;
    }

    @Override
    public boolean isValid() {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(this.expirationDate)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicCard)) return false;
        BasicCard basicCard = (BasicCard) o;
        return cardNumber.equals(basicCard.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber);
    }
}
