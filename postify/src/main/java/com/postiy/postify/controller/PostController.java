package com.postiy.postify.controller;

import com.postiy.postify.dto.postdto.postDto.PostParsialRequestDto;
import com.postiy.postify.dto.postdto.postDto.PostRequestDto;
import com.postiy.postify.dto.postdto.postDto.PostResponseDto;
import com.postiy.postify.enteties.Post;
import com.postiy.postify.enteties.Tag;
import com.postiy.postify.services.PostServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@RestController
@RequestMapping("/posts")
public class PostController {
   @Autowired
 PostServices postServices;
    @GetMapping("")
    public  ResponseEntity<Page<PostResponseDto>> getPost(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestParam(defaultValue = "ASC")String sortDirection,
            @RequestParam(defaultValue = "id")String sortBy){
        Page<PostResponseDto> posts=postServices.getAll(page,size,sortDirection,sortBy).map(postServices::convertTagResponseDto);
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/search")
    public  ResponseEntity<Page<PostResponseDto>> searchPosts(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestParam(defaultValue = "ASC")String sortDirection,
            @RequestParam(defaultValue = "id")String sortBy,
            @RequestParam(name = "value" ,defaultValue = "") String search
    ){
        Page<PostResponseDto> posts=postServices.searchPosts(page,size,sortDirection,sortBy,search).map(postServices::convertTagResponseDto);
        return ResponseEntity.ok(posts);
    }



    @PostMapping("user/{userId}")
    public  ResponseEntity<PostResponseDto> postCreate(@PathVariable int userId,@Valid @RequestBody PostRequestDto postRequestDto){
      Post post=postServices.convertToPost(postRequestDto);
      Post createdPost=postServices.create(post,userId);
      return ResponseEntity.status(201).body(postServices.convertTagResponseDto(createdPost));
   }
@GetMapping("/{id}")
    public ResponseEntity<PostResponseDto>getPostById(@PathVariable int id){
       return new ResponseEntity<PostResponseDto>(postServices.convertTagResponseDto( postServices.getPost(id)), HttpStatus.OK);
}

    @GetMapping("/tag")
    public  ResponseEntity<Page<PostResponseDto>> getPostByTag(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestParam(defaultValue = "ASC")String sortDirection,
            @RequestParam(defaultValue = "id")String sortBy,
            @RequestParam(name = "value" ,defaultValue = "") String tag
    ){
        Page<Post> posts = postServices.getByTag(page, size, sortDirection, sortBy, tag);

        // Convert posts to PostResponseDto
        Page<PostResponseDto> response = posts.map(postServices::convertTagResponseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }





    @GetMapping("/title")
    public  ResponseEntity<Page<PostResponseDto>> getPostByTitle(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestParam(defaultValue = "ASC")String sortDirection,
            @RequestParam(defaultValue = "id")String sortBy,
            @RequestParam(name = "value" ,defaultValue = "") String title
    ){

        Page<Post> posts = postServices.getByTitle(page, size, sortDirection, sortBy, title);

        // Convert posts to PostResponseDto
        Page<PostResponseDto> reasponse = posts.map(postServices::convertTagResponseDto);

        return new ResponseEntity<>(reasponse,HttpStatus.OK);

    }


@PutMapping("/{id}/users/{userId}")
public ResponseEntity<PostResponseDto> updatePostById(@PathVariable int id,@Valid @RequestBody PostRequestDto postRequestDto,@PathVariable int userId) {
    Post updatePost= postServices.update(id, postServices.convertToPost(postRequestDto),userId);

    return ResponseEntity.ok(postServices.convertTagResponseDto(updatePost));
    }
    @PatchMapping("/{id}/users/{userId}")
    public ResponseEntity<PostResponseDto> updatePostPartialById(
            @PathVariable int id,
            @Valid @RequestBody PostParsialRequestDto postPartialRequestDto,
            @PathVariable int userId) {  // annotate userId here
        Post post = postServices.convertToPost(postPartialRequestDto);
        Post updatedPost = postServices.update(id, post, userId);
        return ResponseEntity.ok(postServices.convertTagResponseDto(updatedPost));
    }
@DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<?>deletePostById(@PathVariable int id,@PathVariable int userId){
       postServices.delete(id,userId);
       return ResponseEntity.noContent().build();
}

@GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getUserPostById(@PathVariable int userId){
   List<Post> userPosts=postServices.getUserPosts(userId);
   List<PostResponseDto> posts=userPosts.stream().map(post -> postServices.convertTagResponseDto(post)).toList();
return ResponseEntity.ok(posts);
    }
}
