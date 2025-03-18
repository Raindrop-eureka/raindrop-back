package com.example.demo.message.repository;

import com.example.demo.message.domain.Message;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MessageMapper {

    @Select("SELECT message_id, scene_id, nickname, content, created_at " +
            "FROM message WHERE message_id = #{messageId}")
    Message findByMessageId(Long messageId);

    @Insert("INSERT INTO message (scene_id, nickname, content, created_at) " +
            "VALUES (#{scene.sceneId}, #{nickname}, #{content}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")  // 자동 생성된 messageId 설정
    void saveMessage(Message message);

    @Update("UPDATE message SET content = #{content} WHERE message_id = #{messageId}")
    void updateMessage(Message message);

    @Delete("DELETE FROM message WHERE message_id = #{messageId}")
    void deleteMessage(Long messageId);
}
