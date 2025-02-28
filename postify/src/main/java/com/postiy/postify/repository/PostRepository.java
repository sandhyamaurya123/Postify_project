package com.postiy.postify.repository;

import com.postiy.postify.enteties.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @EntityGraph(value = "Post.tags_user")

    List<Post> findByTitle(String title);
    @EntityGraph(value = "Post.tags_user")

    Page<Post> findByTagsName(String tagName,Pageable pageable);


    @Override
    @EntityGraph(value = "Post.tags_user")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(value = "Post.tags_user")
    @Query("SELECT DISTINCT p FROM posts p LEFT JOIN p.tags t " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Post> searchPosts(@Param("search") String search, Pageable pageable);

    @EntityGraph(value = "Post.tags_user")
    @Query("SELECT p FROM posts p WHERE p.title LIKE %:title%")
    Page<Post> findByTitleContaining(@Param("title") String title,Pageable pageable);

}
