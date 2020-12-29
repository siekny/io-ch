package com.example.io.restcontroller.socket.io;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.io.model.Chat;
import com.example.io.restcontroller.ChatRestController;
import com.example.io.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatIOController {
    private SocketIONamespace namespaceChat;
    private Map<SocketIOClient, String> chatUsers = new HashMap<>();

    private ChatRestController chatRestController;


    @Autowired
    public void setChatRestController(ChatRestController chatRestController) {
        this.chatRestController = chatRestController;
    }

    @Autowired
    public ChatIOController(SocketIOServer socketIOServer) {
        namespaceChat = socketIOServer.addNamespace("/chat");

        namespaceChat.addConnectListener(onConnectEvent);
        namespaceChat.addDisconnectListener(onDisconnectEvent);
        namespaceChat.addEventListener("message", Chat.class, onChatEvent);
        namespaceChat.addEventListener("join", String.class, onUserJoinEvent);
        namespaceChat.addEventListener("typing", String.class, onUserTypingEvent);
        namespaceChat.addEventListener("stop typing", String.class, onUserStopTypingEvent);
    }

    private ConnectListener onConnectEvent = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient socketIOClient) {
            System.out.println("connected to /chat namespace : " + socketIOClient.getSessionId());
            chatRestController.getChats();
        }
    };

    private DisconnectListener onDisconnectEvent = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient socketIOClient) {
            namespaceChat.getBroadcastOperations().sendEvent("leave", chatUsers.get(socketIOClient));
            chatUsers.remove(socketIOClient);
            System.out.println("disconnected from /chat namespace : " + socketIOClient.getSessionId());
        }
    };

    private DataListener<Chat> onChatEvent = new DataListener<>() {
        @Override
        public void onData(SocketIOClient client, Chat chat, AckRequest ackSender) throws Exception {

            namespaceChat.getBroadcastOperations().sendEvent("message", client, chat);
            System.out.println("Chat /chat namespace: " + chat);
            ackSender.sendAckData("Message sent!");
            chatRestController.addChat(chat);
        }
    };

    private DataListener<String> onUserJoinEvent = new DataListener<String>() {
        @Override
        public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
            chatUsers.put(client, username);
            namespaceChat.getBroadcastOperations().sendEvent("join", username);
        }
    };

    private DataListener<String> onUserTypingEvent = new DataListener<String>() {
        @Override
        public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
            namespaceChat.getBroadcastOperations().sendEvent("typing", client, username);
        }
    };

    private DataListener<String> onUserStopTypingEvent = new DataListener<String>() {
        @Override
        public void onData(SocketIOClient client, String username, AckRequest ackSender) throws Exception {
            namespaceChat.getBroadcastOperations().sendEvent("stop typing", client, username);
        }
    };

}
