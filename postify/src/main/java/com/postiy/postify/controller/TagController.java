package com.postiy.postify.controller;

import com.postiy.postify.dto.postdto.tagDto.TagRequestDto;
import com.postiy.postify.dto.postdto.tagDto.TagResponseDto;
import com.postiy.postify.enteties.Tag;
import com.postiy.postify.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {
   @Autowired
   TagService tagService;

    @PutMapping("/{name}")  // Adjusted to use {name} as a String
    public ResponseEntity<TagResponseDto> updateTagByName(@PathVariable String name, @RequestBody TagRequestDto tagRequestDto) {
           return new ResponseEntity<TagResponseDto>(tagService.
                convertToTagResponseDto(tagService.updateTag(name,tagRequestDto)),HttpStatus.OK);


    }
    @DeleteMapping("/{name}")
    public  ResponseEntity<Void> deleteTagName(@PathVariable String name){
        tagService.deleteTagByName(name);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTags(){
        return new ResponseEntity<List<TagResponseDto>>(tagService.getAll().stream().map(tag -> tagService
                .convertToTagResponseDto(tag)).collect(Collectors.toList()),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagRequestDto tagRequestDto){
        Tag tag=tagService.convertToTag(tagRequestDto);
        Tag createtag=tagService.create(tag);
       return new ResponseEntity<>(tagService.convertToTagResponseDto(createtag),HttpStatus.OK);
    }
}