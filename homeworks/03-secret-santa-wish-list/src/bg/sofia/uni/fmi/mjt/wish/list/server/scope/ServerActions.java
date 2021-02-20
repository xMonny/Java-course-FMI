package bg.sofia.uni.fmi.mjt.wish.list.server.scope;

import bg.sofia.uni.fmi.mjt.wish.list.communication.Conversation;
import bg.sofia.uni.fmi.mjt.wish.list.string.stuff.Show;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerActions {

    private static final int BUFFER_SIZE = 4096;

    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private String lastServerResponse;
    private final ServerStorage serverStorage = new ServerStorage();

    private void sendMessage(SocketChannel socketChannel, String message) {

        lastServerResponse = Conversation.sendMessage(socketChannel, message, buffer);
    }

    private boolean isUsernameValid(String username) {
        return username.matches(Show.VALID_USERNAME_REGEX);
    }

    private void register(String username, String password, SocketChannel socketChannel) {

        try {
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (serverStorage.isLoggedIn(socketAddress)) {
                sendMessage(socketChannel, Show.ALREADY_LOGGED_IN);
            } else if (!isUsernameValid(username)) {
                sendMessage(socketChannel, Show.INVALID_USERNAME_MESSAGE.formatted(username));
            } else if (serverStorage.isRegistered(username)) {
                sendMessage(socketChannel, Show.USERNAME_TAKEN_MESSAGE.formatted(username));
            } else {
                serverStorage.register(username, password);
                serverStorage.login(socketAddress, username);
                sendMessage(socketChannel, Show.USERNAME_REGISTERED_MESSAGE.formatted(username));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(String username, String password, SocketChannel socketChannel) {

        try {
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (!serverStorage.validCombination(username, password)) {
                sendMessage(socketChannel, Show.INVALID_COMBINATION_MESSAGE);
            } else if (serverStorage.isLoggedIn(socketAddress)) {
                sendMessage(socketChannel, Show.ALREADY_LOGGED_IN);
            } else {
                serverStorage.login(socketAddress, username);
                sendMessage(socketChannel, Show.USER_LOGIN_MESSAGE.formatted(username));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout(SocketChannel socketChannel) {

        try {
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (!serverStorage.isLoggedIn(socketAddress)) {
                sendMessage(socketChannel, Show.USER_NOT_LOGGED_MESSAGE);
            } else {
                serverStorage.logout(socketAddress);
                sendMessage(socketChannel, Show.USER_LOGOUT_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect(SocketChannel socketChannel) {

        try {
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (serverStorage.isLoggedIn(socketAddress)) {
                serverStorage.logout(socketAddress);
            }
            sendMessage(socketChannel, Show.USER_DISCONNECTED_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postWish(String username, String gift, SocketChannel socketChannel) {

        try {
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (!serverStorage.isLoggedIn(socketAddress)) {
                sendMessage(socketChannel, Show.USER_NOT_LOGGED_MESSAGE);
            } else if (!serverStorage.isRegistered(username)) {
                sendMessage(socketChannel, Show.USER_NOT_REGISTERED_MESSAGE.formatted(username));
            } else if (serverStorage.hasPresent(username, gift)) {
                sendMessage(socketChannel, Show.GIFT_EXIST_MESSAGE.formatted(username));
            } else {
                serverStorage.postWish(username, gift);
                sendMessage(socketChannel, Show.GIFT_SUBMITTED_MESSAGE.formatted(gift, username));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getWish(SocketChannel socketChannel) {

        try {
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (!serverStorage.isLoggedIn(socketAddress)) {
                sendMessage(socketChannel, Show.USER_NOT_LOGGED_MESSAGE);
            } else {
                String loggedUsername = serverStorage.getLoginUsername(socketAddress);
                Set<String> studentsWithPresents = excludeUsername(loggedUsername);
                if (studentsWithPresents.isEmpty()) {
                    sendMessage(socketChannel, Show.NO_STUDENT_PRESENTS);
                } else {
                    List<String> studentsUsername = new ArrayList<>(studentsWithPresents);
                    String chosenUsername = getRandomUsername(studentsUsername);
                    Set<String> presentsChosenUsername = serverStorage.getPresent(chosenUsername);
                    printPresents(chosenUsername, presentsChosenUsername, socketChannel);
                    serverStorage.removePresents(chosenUsername);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRandomUsername(List<String> studentsUsername) {

        Random rand = new Random();
        int randomIndex = rand.nextInt(studentsUsername.size());
        return studentsUsername.get(randomIndex);
    }

    private void printPresents(String username, Set<String> presents, SocketChannel socketChannel) {

        sendMessage(socketChannel, "[ " + username + " : " + presents.toString() + " ]");
    }

    private Set<String> excludeUsername(String username) {

        Set<String> studentsWithPresents = serverStorage.getStudentsWithPresents();
        return studentsWithPresents.stream()
                .filter(studentName -> !studentName.equals(username))
                .collect(Collectors.toSet());
    }

    String getClientMessage(SocketChannel socketChannel) {

        String clientMessage = Conversation.getMessage(socketChannel, buffer);
        if (clientMessage == null) {
            return null;
        }
        return clientMessage.trim();
    }

    private boolean validCommand(String[] splitMessage) {

        String command = splitMessage[0];
        switch (command) {
            case (Show.REGISTER_COMMAND), (Show.LOGIN_COMMAND) -> {
                return splitMessage.length == 3;
            }
            case (Show.LOGOUT_COMMAND), (Show.DISCONNECT_COMMAND), (Show.GET_WISH_COMMAND) -> {
                return splitMessage.length == 1;
            }
            case (Show.POST_WISH_COMMAND) -> {
                return splitMessage.length >= 3;
            }
            default -> {
                return false;
            }
        }
    }

    public void answer(SocketChannel socketChannel, String clientMessage) {

        if (clientMessage == null) {
            sendMessage(socketChannel, Show.NULL_MESSAGE);
            return;
        }

        String[] splitMessage = clientMessage.split(Show.WHITE_SPACES);
        if (!validCommand(splitMessage)) {
            sendMessage(socketChannel, Show.UNKNOWN_COMMAND);
        } else {
            String command = splitMessage[0];
            switch (command) {
                case(Show.REGISTER_COMMAND) -> {
                    String username = splitMessage[1];
                    String password = splitMessage[2];
                    register(username, password, socketChannel);
                }
                case(Show.LOGIN_COMMAND) -> {
                    String username = splitMessage[1];
                    String password = splitMessage[2];
                    login(username, password, socketChannel);
                }
                case(Show.LOGOUT_COMMAND) -> logout(socketChannel);
                case(Show.DISCONNECT_COMMAND) -> disconnect(socketChannel);
                case(Show.GET_WISH_COMMAND) -> getWish(socketChannel);
                case(Show.POST_WISH_COMMAND) -> {
                    String username = splitMessage[1];
                    String present = clientMessage.substring(command.length() + username.length() + 2);
                    postWish(username, present, socketChannel);
                }
                default -> sendMessage(socketChannel, Show.UNKNOWN_COMMAND);
            }
        }
    }

    public String getLastServerResponse() {
        return lastServerResponse;
    }
}
