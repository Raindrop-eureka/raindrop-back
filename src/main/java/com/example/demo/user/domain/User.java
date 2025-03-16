package com.example.demo.user.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String socialId;

    @Column(nullable = false)
    private String name;
}
