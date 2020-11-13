package bg.sofia.uni.fmi.mjt.netflix.exceptions;
import bg.sofia.uni.fmi.mjt.netflix.account.Account;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Account acc) {
        super("User " + "\"" + acc.username() + "\"" + " is not found");
    }
}
