package com.example.io.restcontroller;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.io.model.Chat;
import com.example.io.service.ChatService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chats")
@Api(value="chats", description="Socket IO real time chatting")

public class ChatRestController {

    private ChatService chatService;
    private String message;

    @Autowired
    public void setChatService (ChatService chatService) {
        this.chatService = chatService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> addChat (@RequestBody Chat chat) {
        if (chatService.addChatMessage(chat)) {
            message = "Chat has been added successfully";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List> getChats () {
        List<Chat> chats = chatService.getChats();
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

}
