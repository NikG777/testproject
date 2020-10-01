package com.nikita.testproject.entities;

import lombok.Data;
import lombok.Value;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user_password_reset_table")
@Data
public class PasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pass_id;
    @Column
    private String token;
    @Column(name = "expiration_date")
    private Date date;
   // @Column
    @OneToOne(mappedBy = "passwordEntity")
    private UserEntity user_table;
}

