package com.example.commentservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class CommentServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(CommentServiceApplication.class);

	public static void main(String[] args) {
		logger.info("CommentServiceApplication --------- v3 -------- ");
		SpringApplication.run(CommentServiceApplication.class, args);
		logger.info("CommentServiceApplication --------- v3 -------- ");
	}

}
