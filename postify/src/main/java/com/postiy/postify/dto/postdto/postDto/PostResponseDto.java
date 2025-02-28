package com.postiy.postify.dto.postdto.postDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
      int id;
     String title;
     String description;
     Set<String> tags=new HashSet<>();
     LocalDateTime createDate;
     LocalDateTime lastModifiedDate;
     String author;



}
