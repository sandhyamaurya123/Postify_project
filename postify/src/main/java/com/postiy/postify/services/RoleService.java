package com.postiy.postify.services;

import com.postiy.postify.dto.postdto.roleDto.RoleRequestDto;
import com.postiy.postify.enteties.User;
import com.postiy.postify.enteties.Role;  // Ensure you import your Role entity class
import com.postiy.postify.repository.RoleRepository;
import com.postiy.postify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public void createAdminRole() {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + name));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role: " + role.getName() + " already exists");
        }
        return roleRepository.save(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Set<Role> setRoles(RoleRequestDto roleRequestDto) {
        User user = userRepository.findById(roleRequestDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Role role =roleRepository.findById(roleRequestDto.getRoleId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"role not found"));
         if (user.getRoles().contains(role))    {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user already have same role");
         }
        user.getRoles().add(role);
        userRepository.save(user);
        return user.getRoles();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Set<Role> removeRolesFromUser(RoleRequestDto roleRequestDto){
        User user=userRepository.findById(roleRequestDto.getUserId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
        Role rolesToRemove=roleRepository.findById(roleRequestDto.getRoleId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"role is not found"));
        boolean hasRolesToRemove=user.getRoles().remove(rolesToRemove);
        if (!hasRolesToRemove){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"user does not have specified roles");
        }
        userRepository.save(user);
        return user.getRoles();
    }
   public List<Role> getAllRoles(RoleRequestDto rolerequestDto){
        return  roleRepository.findAll();
    }
}
