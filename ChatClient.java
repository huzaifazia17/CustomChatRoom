import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            ChatService chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");
            Scanner scanner = new Scanner(System.in);

            String username = null; // Declare username to persist after authentication
            String currentChatRoom = null; // Keeps track of the current chat room

            System.out.println("Welcome to the Chat Application!");
            System.out.println("1. Sign In");
            System.out.println("2. Create Account");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (option == 1) { // Sign In
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (chatService.authenticateUser(username, password)) {
                    System.out.println("Authenticated successfully!");
                } else {
                    System.out.println("Authentication failed! Please check your username and password.");
                    return;
                }

            } else if (option == 2) { // Create Account
                System.out.print("Enter a new username: ");
                username = scanner.nextLine();
                System.out.print("Enter a new password: ");
                String password = scanner.nextLine();

                if (chatService.createUser(username, password)) {
                    System.out.println("Account created successfully! Continuing to the application...");
                } else {
                    System.out.println("Account creation failed! Username might already exist.");
                    return;
                }
            } else {
                System.out.println("Invalid option. Exiting...");
                return;
            }

            // Show the main menu
            System.out.println("\nOptions:");
            System.out.println("1. Create Chat Room");
            System.out.println("2. Join Chat Room");
            System.out.println("3. List Users in Chat Room");
            System.out.println("4. Quit Application");
            System.out.print("Choose an option: ");

            while (true) {
                if (currentChatRoom == null) { // Outside a chat room
                    int mainOption = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (mainOption) {
                        case 1: // Create Chat Room
                            System.out.print("Enter chat room name: ");
                            String roomName = scanner.nextLine();
                            System.out.print("Enter chat room password: ");
                            String roomPassword = scanner.nextLine();
                            if (chatService.createChatRoom(roomName, roomPassword)) {
                                System.out.println("Chat room created successfully!");
                                currentChatRoom = roomName;
                                startChat(chatService, scanner, username, currentChatRoom);
                            } else {
                                System.out.println("Chat room creation failed!");
                            }
                            break;

                        case 2: // Join Chat Room
                            System.out.print("Enter chat room name: ");
                            String joinRoomName = scanner.nextLine();
                            System.out.print("Enter chat room password: ");
                            String joinRoomPassword = scanner.nextLine();
                            if (chatService.joinChatRoom(username, joinRoomName, joinRoomPassword)) {
                                System.out.println("Joined chat room successfully!");
                                currentChatRoom = joinRoomName;
                                startChat(chatService, scanner, username, currentChatRoom);
                            } else {
                                System.out.println("Failed to join chat room!");
                            }
                            break;

                        case 3: // List Users in a Chat Room
                            if (currentChatRoom != null) {
                                System.out
                                        .println("Users in room: " + chatService.listUsersInChatRoom(currentChatRoom));
                            } else {
                                System.out.println("You are not in a chat room. Please join one first.");
                            }
                            break;

                        case 4: // Quit Application
                            System.out.println("Exiting application. Goodbye!");
                            return;

                        default:
                            System.out.println("Invalid option. Please try again.");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startChat(ChatService chatService, Scanner scanner, String username, String chatRoom) {
        System.out.println("You are now in the chat room: " + chatRoom);
        System.out.println("Type '/quit' to leave the chat or '/option <number>' to execute a menu option.");

        // Create a separate thread to listen for incoming messages
        Thread messageListener = new Thread(() -> {
            try {
                while (true) {
                    // Continuously fetch messages from the server
                    String message = chatService.getIncomingMessage(chatRoom, username);
                    if (message != null && !message.isEmpty()) {
                        // Format the message display
                        System.out.print("\r" + message + "\n"); // Print the message and move to a new line
                        System.out.print("[" + username + "] > "); // Reprint the input prompt
                    }
                }
            } catch (Exception e) {
                System.out.println("Error receiving messages: " + e.getMessage());
            }
        });

        messageListener.start(); // Start the listener thread

        while (true) {
            try {
                System.out.print("[" + username + "] > ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("/quit")) {
                    System.out.println("Leaving the chat room...");
                    break;
                } else if (message.startsWith("/option")) {
                    int option = Integer.parseInt(message.split(" ")[1]);
                    System.out.println("Executing option " + option + "...");
                    executeOption(chatService, option, scanner, username, chatRoom);
                } else {
                    // Format the message with the desired format
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String formattedMessage = "[" + username + "]: (" + timestamp + "): " + message;

                    // Send the formatted message to the server
                    chatService.sendMessageToRoom(chatRoom, formattedMessage, username);
                }
            } catch (Exception e) {
                System.out.println("Error in chat: " + e.getMessage());
            }
        }
    }

    private static void executeOption(ChatService chatService, int option, Scanner scanner, String username,
            String chatRoom) throws Exception {
        switch (option) {
            case 3: // List Users in Chat Room
                if (chatRoom != null && !chatRoom.isEmpty()) {
                    System.out.println("Other users in this room: " + chatService.listUsersInChatRoom(chatRoom));
                } else {
                    System.out.println("You are not in a chat room. Please join one first.");
                }
                break;

            case 4: // Quit Application
                System.out.println("Exiting application. Goodbye!");
                System.exit(0);

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

}
