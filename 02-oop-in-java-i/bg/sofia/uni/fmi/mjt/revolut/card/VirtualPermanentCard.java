package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class VirtualPermanentCard extends BasicCard{
    public VirtualPermanentCard(String number, int pin, LocalDate expirationDate) {
        super(number, pin, expirationDate);
    }
    @Override
    public String getType() {
        return "VIRTUALPERMANENT";
    }
}
