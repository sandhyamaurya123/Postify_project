package com.postiy.postify.controller;

import com.postiy.postify.dto.postdto.userDto.UserParsialRequestDto;
import com.postiy.postify.dto.postdto.userDto.UserRequestDto;
import com.postiy.postify.dto.postdto.userDto.UserResponseDto;
import com.postiy.postify.enteties.User;
import com.postiy.postify.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto){
        User user= userService.convertToUser(userRequestDto);
        User createdUser=userService.create(user);
        UserResponseDto userResponse=userService.convertToUserResponseDto(createdUser);
        return ResponseEntity.ok().body(userResponse);
    }


    @GetMapping("")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestParam(defaultValue = "ASC")String sortDirection,
            @RequestParam(defaultValue = "id")String sortBy)

    {
        Page<UserResponseDto> allUser=userService.getAll(page, size, sortDirection, sortBy)
                .map(user -> userService.convertToUserResponseDto(user));
        return ResponseEntity.ok().body(allUser);
    }

@GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable int id){
        User user=userService.getByid(id);
        UserResponseDto userResponse=userService.convertToUserResponseDto(user);
        return  ResponseEntity.ok(userResponse);
}


@PutMapping("/{id}")
public ResponseEntity<UserResponseDto> upadteUserById(@PathVariable int id,@Valid @RequestBody UserRequestDto userRequestDto){
        User user=userService.convertToUser(userRequestDto);
        User updateUser=userService.updateById(id,user);
        UserResponseDto userResponse=userService.convertToUserResponseDto(updateUser);
        return ResponseEntity.ok(userResponse);
}

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> upadteParsialUserById(@PathVariable int id, @Valid @RequestBody UserParsialRequestDto userParsialRequestDto){
        User user=userService.convertToUser(userParsialRequestDto);
        User updateUser=userService.updateById(id,user);
        UserResponseDto userResponse=userService.convertToUserResponseDto(updateUser);
        return ResponseEntity.ok(userResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
