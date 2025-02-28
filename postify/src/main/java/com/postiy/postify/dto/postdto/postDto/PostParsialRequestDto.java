package com.postiy.postify.dto.postdto.postDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostParsialRequestDto {

    @Size(min = 3,max = 100,message = "Title must be between 3 or 100 character")
    private   String title;

    @Size(min = 5,max = 200,message = "Description must be between 5 or 200 character")
    private String description;
    private Set<String> tags=new HashSet<>();
}
