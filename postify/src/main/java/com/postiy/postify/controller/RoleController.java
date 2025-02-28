package com.postiy.postify.controller;

import com.postiy.postify.dto.postdto.roleDto.RoleRequestDto;
import com.postiy.postify.enteties.Role;
import com.postiy.postify.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;


    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }


    @PostMapping("/set")
    public ResponseEntity<Set<Role>> setRole(@RequestBody RoleRequestDto roleRequestDto){
        return ResponseEntity.ok(roleService.setRoles(roleRequestDto));
    }
   @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUserRole(@RequestBody RoleRequestDto roleRequestDto){
        roleService.removeRolesFromUser(roleRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles(new RoleRequestDto());
    }
    }
