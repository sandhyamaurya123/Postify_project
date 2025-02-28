package com.postiy.postify.services;

import com.postiy.postify.dto.postdto.userDto.UserParsialRequestDto;
import com.postiy.postify.dto.postdto.userDto.UserRequestDto;
import com.postiy.postify.dto.postdto.userDto.UserResponseDto;
import com.postiy.postify.enteties.User;
import com.postiy.postify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.postiy.postify.enteties.Role;
//import javax.management.relation.Role;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private final  RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,RoleService roleService,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleService=roleService;
        this.passwordEncoder=passwordEncoder;
    }
    public  User createSuperUser(String username, String password,String email){
        roleService.createAdminRole();
        Optional<User> existingUser=userRepository.findByUsername(username);
        if (existingUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User allredy exist,");

        }
        User user=new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        Role adminRole=roleService.findByName("ROLE_ADMIN");
        user.setRoles(Set.of(adminRole));
        return userRepository.save(user);
    }

    @PreAuthorize("permitAll()")
    public User create(User user){
        Optional<User> existingUser=userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user with userName:" + user.getUsername() + "is alredy exist");
        }
        Role role=roleService.findByName("ROLE_USER");
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getAll(int page, int size, String sortDirection, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        return userRepository.findAll(pageable);

    }
    @PreAuthorize("hasRole('ROLE_USER')")
    public  User getByid(int id){
        return  userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id:"+id+"is not found"));

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  void deleteById(int id){
        getByid(id);
        userRepository.deleteById(id);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    public User updateById(int id, User user){
        User existinguser=getByid(id);
        if (user.getUsername()!=null){
            existinguser.setUsername(user.getUsername());
        }
        if (user.getPassword()!=null){
            existinguser.setPassword(user.getPassword());
        }
        if (user.getEmail()!=null){
            existinguser.setEmail(user.getEmail());
        }
        return  userRepository.save(existinguser);
    }




    public User convertToUser(UserRequestDto userRequestDto){
        User user=new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setEmail(userRequestDto.getEmail());
        return user;
    }
    public User convertToUser(UserParsialRequestDto userParsialRequestDto){
        User user=new User();
        user.setUsername(userParsialRequestDto.getUsername());
        user.setPassword(userParsialRequestDto.getPassword());
        user.setEmail(userParsialRequestDto.getEmail());
        return user;
    }

    public UserResponseDto convertToUserResponseDto(User user){
        UserResponseDto userResponseDto=new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCreateDate(user.getCreateDate());
        userResponseDto.setLastModifiedDate(user.getLastModifiedDate());
        userResponseDto.setRoles(user.getRoles().stream().
                map(Role::getName).collect(Collectors.toSet()));
        return  userResponseDto;
    }
}
