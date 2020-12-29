package com.example.io.repository;

import com.example.io.model.Chat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository {

    @Insert("INSERT INTO tb_chat (message, username) VALUES (#{message}, #{username})")
    boolean addChatMessage(Chat chat);

    @Select("SELECT * FROM tb_chat")
    @Results({
            @Result(property = "message", column = "message"),
            @Result(property = "username", column = "username"),
    })
    List<Chat> getChats();
}
