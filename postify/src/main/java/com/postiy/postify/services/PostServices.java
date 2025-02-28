package com.postiy.postify.services;

import com.postiy.postify.dto.postdto.postDto.PostParsialRequestDto;
import com.postiy.postify.dto.postdto.postDto.PostRequestDto;
import com.postiy.postify.dto.postdto.postDto.PostResponseDto;
import com.postiy.postify.enteties.Post;
import com.postiy.postify.enteties.Tag;
import com.postiy.postify.enteties.User;
import com.postiy.postify.repository.PostRepository;
import com.postiy.postify.repository.TagRepository;
import com.postiy.postify.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;



@Service
public class PostServices {
    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    UserService userService;
@Autowired
    UserRepository userRepository;
@Autowired
EmailService emailService;

    public Post create(Post post,int userId) {
        User user=userService.getByid(userId);
        post.setUser(user);

        post.setTags(getPersistedTags(post.getTags()));

       Post savePost= postRepository.save(post);
       emailService.sendEmail(user.getEmail(),"Post Creation","Your Post with Title:"+post.getTitle()+"has been created on:"+post.getCreateDate());
       return savePost;
    }

    public Post getPost(int id) {
        return postRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "post with id " + id + " not found"));

    }


    @Transactional
    public Post update(int id, Post updatePost,int userId) {
        User user=userService.getByid(userId);
        Post userPost=user.getPosts().stream().filter(post -> post.getId()==id).
                findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post with id:"+id+"not found"));
        if (updatePost.getTitle()!=null){
            userPost.setTitle(updatePost.getTitle());
        }
        if (updatePost.getDescription()!=null){
            userPost.setDescription(updatePost.getDescription());
        }
        if (!updatePost.getTags().isEmpty())
            userPost.setTags(updatePost.getTags());
return  postRepository.save(userPost);
    }
       @Transactional
    public void delete(int id,int userId) {
        User user=userService.getByid(userId);
        Post userPost=user.getPosts().stream().filter(post -> post.getId()==id).
                findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post with id:"+id+"not found"));
        userPost.setTags(new HashSet<>());
        user.getPosts().removeIf(post -> post.getId()==id);
       userRepository.save(user);
        postRepository.deleteById(id);
    }

    public Page<Post> getAll(int page,int size,String sortDirection,String sortBy){
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return postRepository.findAll(pageable);
    }


    public Page<Post> searchPosts(int page, int size, String sortDirection, String sortBy, String search){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        return postRepository.searchPosts(search, pageable);
    }


    public Page<Post> getByTitle(int page, int size, String sortDirection, String sortBy,String title){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        return  postRepository.findByTitleContaining(title,Pageable.unpaged());
    }


    public Page<Post> getByTag(int page, int size, String sortDirection, String sortBy,String tag){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
        return postRepository.findByTagsName(tag,Pageable.unpaged());
    }

    public PostResponseDto convertTagResponseDto(Post post) {
        PostResponseDto postResponseDto=new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setDescription(post.getDescription());
        postResponseDto.setTags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        postResponseDto.setCreateDate(post.getCreateDate());
        postResponseDto.setLastModifiedDate(post.getLastModifiedDate());
        postResponseDto.setAuthor(post.getUser().getUsername());
        return postResponseDto;

    }

    public Post convertToPost(PostRequestDto postRequestDto) {
      Post post= new Post();
      post.setTitle(postRequestDto.getTitle());
      post.setDescription(postRequestDto.getDescription());
      post.setTags(postRequestDto.getTags().stream().map(name->new Tag(name)).collect(Collectors.toSet()));
return post;

        }
    public Post convertToPost(@Valid PostParsialRequestDto postPartialRequestDto) {
        Post post= new Post();
        post.setTitle(postPartialRequestDto.getTitle());
        post.setDescription(postPartialRequestDto.getDescription());
        post.setTags(postPartialRequestDto.getTags().stream().map(name->new Tag(name)).collect(Collectors.toSet()));
        return post;
    }

public List<Post> getUserPosts(int userId){
    User user=userService.getByid(userId);
    return user.getPosts();
}
private Set<Tag> getPersistedTags(Set<Tag> tags){
        if (tags==null|| tags.isEmpty()){
            return new HashSet<>();
        }
        return  tags.stream().map(tag -> {
                    return tagRepository.findByName(tag.getName()).orElseGet(()->tagRepository.save(tag));
                }
        ).collect(Collectors.toSet());}


}

