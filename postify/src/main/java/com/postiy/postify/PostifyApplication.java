package com.postiy.postify;

import com.postiy.postify.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostifyApplication {
@Autowired
	UserService userService;
	public static void main(String[] args) {

		SpringApplication.run(PostifyApplication.class, args);
	}
   @PostConstruct
	public void init(){
		try{
			System.out.println("Creating Super User");
			userService.createSuperUser("akshada","akshada19","thoratakshada96@gmail.com");
			System.out.println("Super user created");

		}catch (Exception e){
			e.printStackTrace();
			System.out.println("Error creating super user:"+e.getMessage());
		}
   }
}
