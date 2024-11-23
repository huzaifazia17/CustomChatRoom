import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private Map<String, String> users = new HashMap<>();
    // Stores messages for each chat room
    private Map<String, List<String>> chatRoomMessages = new HashMap<>();
    private Map<String, Map<String, Queue<String>>> chatRoomUserQueues = new HashMap<>(); // Separate queues for each

    private Map<String, Set<String>> chatRoomUserMap = new HashMap<>();

    public ChatServiceImpl() throws RemoteException {
        super();
        // Preload some users
        users.put("Alice", "password1");
        users.put("Bob", "password2");
    }

    @Override
    public boolean authenticateUser(String username, String password) throws RemoteException {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    @Override
    public boolean createUser(String username, String password) throws RemoteException {
        if (users.containsKey(username)) {
            return false; // Username already exists
        }
        users.put(username, password);
        return true;
    }

    @Override
    public boolean createChatRoom(String chatRoomName, String password) throws RemoteException {
        // Logic for creating chat rooms
        return true;
    }

    @Override
    public boolean joinChatRoom(String username, String chatRoomName, String password) throws RemoteException {
        // Ensure the chat room exists
        if (!chatRoomUserQueues.containsKey(chatRoomName)) {
            return false; // Chat room doesn't exist
        }

        // Add the user to the chat room's user map
        chatRoomUserQueues.putIfAbsent(chatRoomName, new HashMap<>());
        chatRoomUserQueues.get(chatRoomName).putIfAbsent(username, new LinkedList<>());

        // Track users in the chat room
        chatRoomUserMap.putIfAbsent(chatRoomName, new HashSet<>());
        chatRoomUserMap.get(chatRoomName).add(username);

        return true; // Successfully joined the chat room
    }

    @Override
    public String listUsersInChatRoom(String chatRoomName) throws RemoteException {
        // Fetch the list of users in the chat room
        Set<String> usersInRoom = chatRoomUserMap.getOrDefault(chatRoomName, new HashSet<>());

        if (usersInRoom.isEmpty()) {
            return "No users in the chat room.";
        }

        // Return the list of users as a comma-separated string
        return String.join(", ", usersInRoom);
    }

    @Override
    public boolean sendPrivateMessage(String recipient, String message, String sender) throws RemoteException {
        // Logic for sending private messages
        return true;
    }

    @Override
    public void sendMessageToRoom(String chatRoomName, String message, String sender) throws RemoteException {
        // Ensure chat room exists in user queue map
        chatRoomUserQueues.putIfAbsent(chatRoomName, new HashMap<>());

        // Get all user queues for this chat room
        Map<String, Queue<String>> userQueues = chatRoomUserQueues.get(chatRoomName);

        // Add the message to every user's queue in the chat room
        synchronized (userQueues) {
            for (Map.Entry<String, Queue<String>> entry : userQueues.entrySet()) {
                Queue<String> userQueue = entry.getValue();
                userQueue.add(message);
            }
        }
    }

    @Override
    public String getIncomingMessage(String chatRoomName, String username) throws RemoteException {
        // Ensure chat room exists in user queue map
        chatRoomUserQueues.putIfAbsent(chatRoomName, new HashMap<>());

        // Get the user's personal queue for the chat room
        Map<String, Queue<String>> userQueues = chatRoomUserQueues.get(chatRoomName);
        userQueues.putIfAbsent(username, new LinkedList<>()); // Initialize queue if it doesn't exist

        Queue<String> userQueue = userQueues.get(username);

        // Retrieve the next message from the user's queue, if available
        synchronized (userQueue) {
            return userQueue.poll(); // Poll removes and returns the head of the queue, or null if empty
        }
    }

}
