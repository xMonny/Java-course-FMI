package bg.sofia.uni.fmi.mjt.wish.list.server.scope;

import bg.sofia.uni.fmi.mjt.wish.list.string.stuff.Show;
import org.junit.Test;

import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ServerActionsTest {

    //Valid commands for tests
    private static final String HEY_COMMAND = "hey";
    private static final String REGISTER_SIMONA = "register Simona 123";
    private static final String REGISTER_JOHNNY = "register johnny-._ 123";
    private static final String LOGIN_SIMONA = "login Simona 123";
    private static final String LOGIN_JOHNNY = "login johnny-._ 123";
    private static final String POST_WISH_SIMONA_BIKE = "post-wish Simona bike";
    private static final String POST_WISH_SIMONA_CAKE = "post-wish Simona cake";
    private static final String POST_WISH_SIMONA_MORE_PRESENTS = "post-wish Simona more presents";

    //Invalid commands for tests
    private static final String REGISTER_WRONG_SIMONA = "register Sim@na 123";
    private static final String REGISTER_WRONG_NUMBER_ARGUMENTS = "register 123";
    private static final String LOGIN_SIMONA_WRONG_PASSWORD = "login Simona 1";
    private static final String LOGIN_WRONG_NUMBER_ARGUMENTS = "login Simona 1 1";
    private static final String LOGOUT_WRONG_NUMBER_ARGUMENTS = "logout Simona";
    private static final String DISCONNECT_WRONG_NUMBER_ARGUMENTS = "disconnect Simona";
    private static final String GET_WISH_WRONG_NUMBER_ARGUMENTS = "get-wish Simona";
    private static final String POST_WISH_WRONG_NUMBER_ARGUMENTS = "post-wish Simona";

    //Post-wish messages for tests
    private static final String SIMONA_PRESENT_BIKE = "[ Simona : [bike] ]";
    private static final String SIMONA_PRESENTS_BIKE_CAKE_MORE_PRESENTS = "[ Simona : [bike, cake, more presents] ]";

    private final SocketChannel socketChannelMock = mock(SocketChannel.class);
    private final ServerActions serverActions = new ServerActions();

    @Test
    public void testAnswerMessageHey() {
        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, HEY_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerNullUsernameRegister() {
        String expectedAnswer = Show.NULL_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, null);
        String actualAnswer = serverActions.getLastServerResponse();
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerInvalidUsernameRegister() {
        String expectedAnswer = Show.INVALID_USERNAME_MESSAGE.formatted("Sim@na") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, REGISTER_WRONG_SIMONA);
        String actualAnswer = serverActions.getLastServerResponse();
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerWrongNumberOfDataRegister() {
        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, REGISTER_WRONG_NUMBER_ARGUMENTS);
        String actualAnswer = serverActions.getLastServerResponse();
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerCorrectRegister() {
        String expectedAnswer = Show.USERNAME_REGISTERED_MESSAGE.formatted("Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        String actualAnswer = serverActions.getLastServerResponse();
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerNotAllowRegisterWhenLoggedIn() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.ALREADY_LOGGED_IN + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerLogoutWhenNotLoggedIn() {
        String expectedAnswer = Show.USER_NOT_LOGGED_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerLogoutWrongNumberArguments() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, LOGOUT_WRONG_NUMBER_ARGUMENTS);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerLogout() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.USER_LOGOUT_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerUsernameAlreadyTakenRegister() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);

        String expectedAnswer = Show.USERNAME_TAKEN_MESSAGE.formatted("Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerNotAbleLoginWhenLogin() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.ALREADY_LOGGED_IN + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, LOGIN_SIMONA);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerInvalidUsernameLogin() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);

        String expectedAnswer = Show.INVALID_COMBINATION_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, LOGIN_JOHNNY);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerInvalidPasswordLogin() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);

        String expectedAnswer = Show.INVALID_COMBINATION_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, LOGIN_SIMONA_WRONG_PASSWORD);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerCorrectLoginAfterLogout() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);

        String expectedAnswer = Show.USER_LOGIN_MESSAGE.formatted("Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, LOGIN_SIMONA);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerLoginWrongNumberArguments() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);

        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, LOGIN_WRONG_NUMBER_ARGUMENTS);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerDisconnect() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.USER_DISCONNECTED_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.DISCONNECT_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerDisconnectWrongNumberArguments() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, DISCONNECT_WRONG_NUMBER_ARGUMENTS);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerNotAblePostWishWhenNotLoggedIn() {
        String firstExpectedAnswer = Show.USER_NOT_LOGGED_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        String firstActualAnswer = serverActions.getLastServerResponse();

        assertEquals(firstExpectedAnswer, firstActualAnswer);
    }

    @Test
    public void testAnswerPostWishWhenUserToGiftIsNotRegistered() {
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);

        String expectedAnswer = Show.USER_NOT_REGISTERED_MESSAGE.formatted("Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerPostWishGiftYourself() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.GIFT_SUBMITTED_MESSAGE.formatted("bike", "Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerPostWishGiftForOtherUser() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);

        String expectedAnswer = Show.GIFT_SUBMITTED_MESSAGE.formatted("bike", "Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerPostWishGiftWithMoreWords() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);

        String expectedAnswer = Show.GIFT_SUBMITTED_MESSAGE.formatted("more presents", "Simona")
                + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_MORE_PRESENTS);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerPostWishSameGift() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);

        String expectedAnswer = Show.GIFT_EXIST_MESSAGE.formatted("Simona") + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerPostWishWrongNumberArguments() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);

        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, POST_WISH_WRONG_NUMBER_ARGUMENTS);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerNotAbleGetWishWhenNotLoggedIn() {
        String expectedAnswer = Show.USER_NOT_LOGGED_MESSAGE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.GET_WISH_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerEmptyWishListWhenNoPostWish() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);

        String expectedAnswer = Show.NO_STUDENT_PRESENTS + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.GET_WISH_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerEmptyWishListWhenOnlyLoggedUserHasPresents() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);

        String expectedAnswer = Show.NO_STUDENT_PRESENTS + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.GET_WISH_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerGetWishListForOtherUser() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);

        String expectedAnswer = SIMONA_PRESENT_BIKE + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.GET_WISH_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerGetWishListForOtherUserMorePresents() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, Show.LOGOUT_COMMAND);
        serverActions.answer(socketChannelMock, REGISTER_JOHNNY);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_CAKE);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_MORE_PRESENTS);

        String expectedAnswer = SIMONA_PRESENTS_BIKE_CAKE_MORE_PRESENTS + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, Show.GET_WISH_COMMAND);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testAnswerGetWishListWrongNumberArguments() {
        serverActions.answer(socketChannelMock, REGISTER_SIMONA);
        serverActions.answer(socketChannelMock, POST_WISH_SIMONA_BIKE);

        String expectedAnswer = Show.UNKNOWN_COMMAND + Show.LINE_SEPARATOR;
        serverActions.answer(socketChannelMock, GET_WISH_WRONG_NUMBER_ARGUMENTS);
        String actualAnswer = serverActions.getLastServerResponse();

        assertEquals(expectedAnswer, actualAnswer);
    }
}
