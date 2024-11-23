# **Distributed Chat Application**

## **Overview**
This is a distributed chat application developed using **Java RMI (Remote Method Invocation)**. The application allows users to:
- Sign in or create an account.
- Create or join chat rooms dynamically.
- Send and receive real-time messages in chat rooms with timestamps.
- View the list of users in the current chat room.
- Use commands dynamically while chatting, such as `/quit` or `/option <number>`.

This application demonstrates the use of distributed object-based communication, thread-safe message handling, and a modular client-server architecture.

---

## **Features**
1. **User Authentication**:
   - Users can sign in with a username and password or create a new account.
   - Secure access ensures only authenticated users can participate.

2. **Dynamic Chat Room Creation and Management**:
   - Users can create or join chat rooms dynamically, with optional password protection.
   - Each chat room maintains a list of active users.

3. **Real-Time Messaging with Timestamps**:
   - Users can send and receive messages in real-time.
   - Each message includes a timestamp (HH:mm:ss) and the sender's username.

4. **List Users in a Chat Room**:
   - Users can view the list of active participants in their current chat room.

5. **Interactive Commands**:
   - `/quit`: Leave the chat room.
   - `/option <number>`: Execute menu options dynamically (e.g., view users in the chat room).

---


