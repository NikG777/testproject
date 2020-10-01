package com.nikita.testproject.repo;
import com.nikita.testproject.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleEntityRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByName(String name);
}