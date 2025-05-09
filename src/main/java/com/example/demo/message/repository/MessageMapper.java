package com.example.demo.message.repository;

import com.example.demo.message.domain.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    // sceneId에 해당하는 메시지들 조회
    @Select("""
        SELECT message_id, scene_id, nickname, content, model_id, created_at 
        FROM message 
        WHERE scene_id = #{sceneId}
    """)
    List<Message> findMessagesBySceneId(Long sceneId);

    // 메시지 생성
    @Insert("INSERT INTO message (scene_id, nickname, content, model_id, created_at) " +
            "VALUES (#{scene.sceneId}, #{nickname}, #{content}, #{modelId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void saveMessage(Message message);

    // 메시지 삭제
    @Delete("DELETE FROM message WHERE message_id = #{messageId}")
    void deleteMessage(Long messageId);
}