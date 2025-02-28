package com.postiy.postify.services;

import com.postiy.postify.dto.postdto.tagDto.TagRequestDto;
import com.postiy.postify.dto.postdto.tagDto.TagResponseDto;
import com.postiy.postify.enteties.Post;
import com.postiy.postify.enteties.Tag;
import com.postiy.postify.repository.PostRepository;
import com.postiy.postify.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TagService {
      @Autowired
    TagRepository tagRepository;
      @Autowired
    PostRepository postRepository;


    public Tag updateTag(String name, TagRequestDto tagRequestDto) {
        String newName = tagRequestDto.getName();
        Tag tag = tagRepository.findByName(name).orElseThrow(()->

                 new ResponseStatusException(HttpStatus.NOT_FOUND, "tag with name:  " + name + "  not found"));


        tag.setName(newName);
        return tagRepository.save(tag);
    }

    public void deleteTagByName(String name) {
        Tag tag = tagRepository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag with name '" + name + "' not found"));

        Page<Post> posts = postRepository.findByTagsName(name, Pageable.unpaged());
        if (!posts.isEmpty()) {

            posts.forEach(post -> post.getTags().remove(tag));
            postRepository.saveAll(posts); // Save the updated posts if needed
        }
        tagRepository.deleteByName(name);
    }


    public  List<Tag> getAll(){
        return  tagRepository.findAll();
    }
    public  Tag create(Tag tag){
        if(tagRepository.findByName(tag.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tag with name:"+tag.getName()+" alredy exist");
        }
        return  tagRepository.save(tag);
    }
    public Tag convertToTag(TagRequestDto tagRequestDto){
        return  new Tag(0,tagRequestDto.getName());
    }
    public TagResponseDto convertToTagResponseDto(Tag tag) {
        return new TagResponseDto(tag.getId(),tag.getName());
    }
}
