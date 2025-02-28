package com.postiy.postify.repository;

import com.postiy.postify.enteties.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
    Set<Role> findByNameIn(Set<String> name);
}
