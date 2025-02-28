package com.postiy.postify.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "Post.tags_user",attributeNodes = {@NamedAttributeNode("tags"),@NamedAttributeNode("user")})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

   private   int id;

    private String title;
   private    String description;


@ManyToMany(cascade  ={CascadeType.ALL})
    @JoinTable(
            name = "posts_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags =new HashSet<>();

@CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

@LastModifiedDate
    private LocalDateTime lastModifiedDate;

@ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


}
