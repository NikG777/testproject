package com.nikita.testproject.repo;

import com.nikita.testproject.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Entity;
import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByLogin(String login);

    UserEntity findByActivationCode(String code);
    UserEntity findByEmail(String email);
    UserEntity findUserEntityByPasswordEntity_Token(String token);

}
