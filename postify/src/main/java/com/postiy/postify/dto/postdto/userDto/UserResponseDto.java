package com.postiy.postify.dto.postdto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private  int id;
    private String username;
    private String email;
    private Set<String> roles;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;


}
