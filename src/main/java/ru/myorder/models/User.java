package ru.myorder.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="username", nullable = false, unique = true)
    private String username;
    @Column(name="password_hash", nullable = false)
    private String passwordHash;
    @Column(name="is_admin", nullable=false)
    private Boolean isAdmin;


    public User(String username, String password,String gender, Boolean isAdmin){
        this.username = username;
        this.passwordHash = password;
        this.isAdmin =isAdmin;
    }

    public User(String username, String password, Boolean isAdmin){
        this.username = username;
        this.isAdmin = isAdmin;
        this.passwordHash = password;
    }

    public User(String username, String password){
        this.username = username;
        this.passwordHash = password;
    }

}