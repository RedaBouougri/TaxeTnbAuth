package com.example.taxetnbauth.security.springjwt.repository;


import com.example.taxetnbauth.security.springjwt.models.ERole;
import com.example.taxetnbauth.security.springjwt.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
