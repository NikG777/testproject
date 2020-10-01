package com.nikita.testproject.repo;

import com.nikita.testproject.entities.PasswordEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PasswordEntityRepository extends CrudRepository<PasswordEntity,Integer> {
  PasswordEntity findByToken(String token);
  PasswordEntity deletePasswordEntityByToken(String token);

}
