import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    boolean authenticateUser(String username, String password) throws RemoteException;

    boolean createUser(String username, String password) throws RemoteException;

    boolean createChatRoom(String chatRoomName, String password) throws RemoteException;

    boolean joinChatRoom(String username, String chatRoomName, String password) throws RemoteException;

    String listUsersInChatRoom(String chatRoomName) throws RemoteException;

    void sendMessageToRoom(String chatRoomName, String message, String sender) throws RemoteException;

    boolean sendPrivateMessage(String recipient, String message, String sender) throws RemoteException;

    String getIncomingMessage(String chatRoomName, String username) throws RemoteException;
}
