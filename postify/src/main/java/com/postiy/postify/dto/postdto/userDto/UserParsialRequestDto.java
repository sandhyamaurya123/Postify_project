package com.postiy.postify.dto.postdto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserParsialRequestDto {
        @Size(min = 3,max = 20,message = "Username must be between 3 or 20 character")
    private String username;
    @Size(min = 6,max = 15,message = "Password must be between 6 or 15 character")
    private  String password;
    @Email(message = "Email should be valid")
       private String email;


}
