package com.lss.teachflow.repository;

import com.lss.teachflow.entity.ERole;
import com.lss.teachflow.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> getRoleByName(ERole name);
}
