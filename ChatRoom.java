
import java.util.HashSet;
import java.util.Set;

public class ChatRoom {
    private String name;
    private String password;
    private Set<String> users;

    public ChatRoom(String name, String password) {
        this.name = name;
        this.password = password;
        this.users = new HashSet<>();
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void broadcastMessage(String message, String sender) {
        System.out.println("[" + name + "] " + sender + ": " + message);
    }
}
