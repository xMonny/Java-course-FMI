package bg.sofia.uni.fmi.mjt.wish.list.string.stuff;

public interface Show {

    String LINE_SEPARATOR = System.lineSeparator();
    String WHITE_SPACES = "\\s+";
    String VALID_USERNAME_REGEX = "[a-zA-Z0-9\\.\\-_]+";

    //NULL message
    String NULL_MESSAGE = "[ Message is null ]";

    //Register messages
    String INVALID_USERNAME_MESSAGE = "[ Username %s is invalid, select a valid one ]";
    String USERNAME_TAKEN_MESSAGE = "[ Username %s is already taken, select another one ]";
    String USERNAME_REGISTERED_MESSAGE = "[ Username %s successfully registered ]";
    String USER_NOT_REGISTERED_MESSAGE = "[ Student with username %s is not registered ]";

    //Login messages
    String USER_LOGIN_MESSAGE = "[ User %s successfully logged in ]";
    String INVALID_COMBINATION_MESSAGE = "[ Invalid username/password combination ]";
    String ALREADY_LOGGED_IN = "[ You are logged in ]";
    String USER_NOT_LOGGED_MESSAGE = "[ You are not logged in ]";

    //Logout messages
    String USER_LOGOUT_MESSAGE = "[ Successfully logged out ]";

    //Disconnect messages
    String USER_DISCONNECTED_MESSAGE = "[ Disconnected from server ]";

    //Post-wish messages
    String GIFT_EXIST_MESSAGE = "[ The same gift for student %s was already submitted ]";
    String GIFT_SUBMITTED_MESSAGE = "[ Gift %s for student %s submitted successfully ]";

    //Get-wish messages
    String NO_STUDENT_PRESENTS = "[ There are no students present in the wish list ]";

    //Unknown command message
    String UNKNOWN_COMMAND = "[ Unknown command ]";

    //Valid commands
    String REGISTER_COMMAND = "register";
    String LOGIN_COMMAND = "login";
    String LOGOUT_COMMAND = "logout";
    String DISCONNECT_COMMAND = "disconnect";
    String POST_WISH_COMMAND = "post-wish";
    String GET_WISH_COMMAND = "get-wish";
}
