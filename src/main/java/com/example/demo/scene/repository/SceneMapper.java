package com.example.demo.scene.repository;

import com.example.demo.scene.domain.Scene;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SceneMapper {
    // Scene 조회 - Location 테이블 조인 추가
    @Select("""
        SELECT s.scene_id, s.social_id, s.theme, s.is_message_visible,
               l.location_id, l.latitude, l.longitude
        FROM scene s
        JOIN location l ON s.location_id = l.location_id
        WHERE s.scene_id = #{sceneId}
    """)
    @Results({
            @Result(property = "sceneId", column = "scene_id"),
            @Result(property = "user.socialId", column = "social_id"),
            @Result(property = "theme", column = "theme"),
            @Result(property = "isMessageVisible", column = "is_message_visible"),
            // Location 매핑 추가
            @Result(property = "location.locationId", column = "location_id"),
            @Result(property = "location.latitude", column = "latitude"),
            @Result(property = "location.longitude", column = "longitude")
    })
    Scene findBySceneId(Long sceneId);

    // Scene 생성 - Location 참조 추가
    @Insert("INSERT INTO scene (social_id, location_id, theme, is_message_visible) " +
            "VALUES (#{user.socialId}, #{location.locationId}, #{theme}, #{isMessageVisible})")
    @Options(useGeneratedKeys = true, keyProperty = "sceneId")
    void saveScene(Scene scene);


    // Scene 삭제
    @Delete("DELETE FROM scene WHERE scene_id = #{sceneId}")
    void deleteScene(Long sceneId);

    // Scene 수정
    @Update("""
        UPDATE scene
        SET theme = #{theme},
            is_message_visible = #{isMessageVisible}
        WHERE scene_id = #{sceneId}
    """)
    void updateScene(Scene scene);
}
