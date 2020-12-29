package com.example.io.service;

import com.example.io.model.Chat;

import java.util.List;

public interface ChatService {

    boolean addChatMessage(Chat chat);
    List<Chat> getChats();
}
