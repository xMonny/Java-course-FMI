package bg.sofia.uni.fmi.mjt.wish.list.server.scope;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ServerStorage {

    //stores username and password for each registered student
    private final Map<String, String> registeredStudents = new HashMap<>();

    //stores remote client socket address and username for each logged in student
    private final Map<SocketAddress, String> loginStudentAddress = new HashMap<>();

    //stores username and set of presents for each student who has post wishes
    private final Map<String, Set<String>> studentPresents = new HashMap<>();

    String getLoginUsername(SocketAddress socketAddress) {

        if (!isLoggedIn(socketAddress)) {
            return null;
        }
        return loginStudentAddress.get(socketAddress);
    }

    Set<String> getStudentsWithPresents() {
        return studentPresents.keySet();
    }

    Set<String> getPresent(String username) {

        if (!studentPresents.containsKey(username)) {
            return null;
        }
        return studentPresents.get(username);
    }

    boolean isRegistered(String username) {
        return registeredStudents.containsKey(username);
    }

    boolean isPasswordCorrect(String username, String password) {

        if (!isRegistered(username)) {
            return false;
        }
        return registeredStudents.get(username).equals(password);
    }

    boolean validCombination(String username, String password) {

        return isRegistered(username) && isPasswordCorrect(username, password);
    }

    void register(String username, String password) {

        registeredStudents.put(username, password);
    }

    void login(SocketAddress socketAddress, String username) {

        loginStudentAddress.put(socketAddress, username);
    }

    void logout(SocketAddress socketAddress) {
        loginStudentAddress.remove(socketAddress);
    }

    boolean isLoggedIn(SocketAddress socketAddress) {
        return loginStudentAddress.containsKey(socketAddress);
    }

    boolean hasPresent(String username, String gift) {

        if (!studentPresents.containsKey(username)) {
            return false;
        }
        return studentPresents.get(username).contains(gift);
    }

    void postWish(String username, String gift) {

        Set<String> gifts = (studentPresents.containsKey(username)
                ? studentPresents.get(username) : new LinkedHashSet<>());
        gifts.add(gift);
        studentPresents.put(username, gifts);
    }

    void removePresents(String username) {
        studentPresents.remove(username);
    }
}
