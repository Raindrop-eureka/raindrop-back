package com.example.demo.scene.repository;

import com.example.demo.scene.domain.Scene;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SceneMapper {

    @Select("SELECT scene_id, social_id, theme, latitude, longitude, is_visible " +
            "FROM scene WHERE scene_id = #{sceneId}")
    Scene findBySceneId(Long sceneId);

    @Insert("INSERT INTO scene (social_id, theme, latitude, longitude, is_visible) " +
            "VALUES (#{user.socialId}, #{theme}, #{latitude}, #{longitude}, #{isVisible})")
    @Options(useGeneratedKeys = true, keyProperty = "sceneId")  // sceneId를 자동으로 설정
    void saveScene(Scene scene);

    @Delete("DELETE FROM scene WHERE scene_id = #{sceneId}")
    void deleteScene(Long sceneId);

    @Update("UPDATE scene SET theme = #{theme}, latitude = #{latitude}, longitude = #{longitude}, " +
            "is_visible = #{isVisible} WHERE scene_id = #{sceneId}")
    void updateScene(Scene scene);
}
