package com.example.demo.scene.repository;

import com.example.demo.scene.domain.Scene;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SceneMapper {
    // Scene 조회 - scene_id 기준
    @Select("""
        SELECT s.scene_id, s.social_id, s.theme, s.is_message_visible,
               u.name AS user_name, u.profile_image_url
        FROM scene s
        LEFT JOIN user u ON s.social_id = u.social_id
        WHERE s.scene_id = #{sceneId}
""")
    @Results({
            @Result(property = "sceneId", column = "scene_id"),
            @Result(property = "theme", column = "theme"),
            @Result(property = "isMessageVisible", column = "is_message_visible"),
            @Result(property = "user.socialId", column = "social_id"),
            @Result(property = "user.name", column = "user_name"),
            @Result(property = "user.profileImageUrl", column = "profile_image_url"),
    })
    Scene findBySceneId(Long sceneId);

    // Scene 생성 - JDBC 타입 명시하여 NULL 문제 방지
    @Insert("INSERT INTO scene (social_id, theme, is_message_visible) " +
            "VALUES (#{user.socialId, jdbcType=VARCHAR}, #{theme, jdbcType=VARCHAR}, #{isMessageVisible, jdbcType=BOOLEAN})")
    @Options(useGeneratedKeys = true, keyProperty = "sceneId")
    void saveScene(Scene scene);

    // Scene 삭제
    @Delete("DELETE FROM scene WHERE scene_id = #{sceneId}")
    void deleteScene(Long sceneId);

    // Scene 수정
    @Update("""
        UPDATE scene
        SET theme = #{theme, jdbcType=VARCHAR},
            is_message_visible = #{isMessageVisible, jdbcType=BOOLEAN}
        WHERE scene_id = #{sceneId}
    """)
    void updateScene(Scene scene);

    // Scene 조회 - socialId 기반으로 Scene 찾기
    @Select("""
        SELECT s.scene_id, s.social_id, s.theme, s.is_message_visible,
               u.name AS user_name, u.profile_image_url
        FROM scene s
        LEFT JOIN user u ON s.social_id = u.social_id
        WHERE s.social_id = #{socialId}
    """)
    @Results({
            @Result(property = "sceneId", column = "scene_id"),
            @Result(property = "theme", column = "theme"),
            @Result(property = "isMessageVisible", column = "is_message_visible"),
            @Result(property = "user.socialId", column = "social_id"),
            @Result(property = "user.name", column = "user_name"),
            @Result(property = "user.profileImageUrl", column = "profile_image_url"),
    })
    Scene findBySocialId(String socialId);

}