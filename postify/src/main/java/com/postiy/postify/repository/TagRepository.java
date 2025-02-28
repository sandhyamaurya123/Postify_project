package com.postiy.postify.repository;

import com.postiy.postify.enteties.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Integer> {
   Optional<Tag> findByName(String name);

   @Transactional
    void  deleteByName(String name);

}
