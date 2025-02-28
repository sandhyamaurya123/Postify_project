package com.postiy.postify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostifyApplicationTests {

	@Test
	void contextLoads() {
	}
@Test
	void testNumberIsPositive(){
		int number=10;
		assertTrue(number>0 ,"Number should Be Positive");
}

}
