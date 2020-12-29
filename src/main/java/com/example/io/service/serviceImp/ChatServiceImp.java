package com.example.io.service.serviceImp;

import com.example.io.model.Chat;
import com.example.io.repository.ChatRepository;
import com.example.io.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImp implements ChatService {

    private ChatRepository chatRepository;

    @Autowired
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }


    @Override
    public boolean addChatMessage(Chat chat) {
        return chatRepository.addChatMessage(chat);
    }

    @Override
    public List<Chat> getChats() {
        return chatRepository.getChats();
    }
}
