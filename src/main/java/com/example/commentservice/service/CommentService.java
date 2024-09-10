package com.example.commentservice.service;

import com.example.commentservice.dto.SendCommentDto;
import com.example.commentservice.entity.Comment;
import com.example.commentservice.entity.Post;
import com.example.commentservice.entity.User;
import com.example.commentservice.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.example.commentservice.dto.CommentInforDto;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${external.post-service.base-url}")
    private String postServiceBaseUrl;

    @Value("${external.user-service.base-url}")
    private String userServiceBaseUrl;


    public ResponseEntity<List<CommentInforDto>> getCommentByPostId(int postId) {
        try {
            List<Comment> comment = commentRepository.findByPostId(postId);

            if(comment.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<CommentInforDto> commentInforDtoList = comment.stream().map(element -> {
                CommentInforDto commentInforDto = CommentInforDto.builder()
                        .commentId(element.getCommentId())
                        .userName(element.getUserId().getName())
                        .comment(element.getComment())
                        .time(element.getTime())
                        .userImage(element.getUserId().getImage())
                        .build();
                return commentInforDto;
            }).collect(Collectors.toList());
            return new ResponseEntity<>(commentInforDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Comment> sendComment(SendCommentDto sendCommentDto, HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(CommentService.class);
        String headerAuth = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", headerAuth);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String userUrl = UriComponentsBuilder.fromHttpUrl(userServiceBaseUrl)
                .pathSegment("user")
                .queryParam("postId", String.valueOf(sendCommentDto.getUserId()))
                .toUriString();
        User user;
        try {
            ResponseEntity<User> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, entity, User.class);
            logger.info("User Response received: {}", userResponse);
            user = userResponse.getBody();

        } catch (HttpClientErrorException e) {
            logger.error("User ID not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Exception User Response- {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String postUrl = UriComponentsBuilder.fromHttpUrl(postServiceBaseUrl)
                .pathSegment("post")
                .queryParam("postId", String.valueOf(sendCommentDto.getPostId()))
                .toUriString();
        Post post;
        try {
            ResponseEntity<Post> postResponse = restTemplate.exchange(postUrl, HttpMethod.GET, entity, Post.class);
            logger.info("Post Response received: {}", postResponse);
            post = postResponse.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Post ID not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Exception Post Response- {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Comment comment = Comment.builder()
                    .comment(sendCommentDto.getComment())
                    .postId(post)
                    .userId(user)
                    .build();
            commentRepository.save(comment);
            logger.info("Comment saved successfully");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error while saving Comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
