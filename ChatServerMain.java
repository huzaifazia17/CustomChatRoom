
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ChatServerMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ChatService chatService = new ChatServiceImpl();
            Naming.rebind("ChatService", chatService);
            System.out.println("Chat Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
