package com.nikita.testproject.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role_table")
@Data
public class RoleEntity {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;
@Column
private String name;
}
