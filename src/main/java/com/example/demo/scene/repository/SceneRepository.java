package com.example.demo.scene.repository;

import com.example.demo.scene.domain.Scene;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    Optional<Scene> findByUser_SocialId(String socialId);
}
