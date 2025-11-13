package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "users")
public class User {

    // GETTER & SETTER
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 25, nullable = false)
    private String username;

    @Setter
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Setter
    @Column(length = 255, nullable = false)
    private String password;

    @Column(name = "created_at", insertable = false, updatable = false)
    private String createdAt;

    public User() {}

    public User(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
