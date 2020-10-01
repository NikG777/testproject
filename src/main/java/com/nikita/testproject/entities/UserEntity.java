package com.nikita.testproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_table")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String login;

    @Column
    private String password;
    @Column
    private String url_picture;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleEntity roleEntity;
    @Column
    private String email;
    @Column
    private String activationCode;
   // @Column
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_reset_id",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pass_id"))
    private PasswordEntity passwordEntity;

}
