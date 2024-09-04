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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.example.commentservice.dto.CommentInforDto;
import org.springframework.util.StringUtils;
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
        List<Comment> comment = commentRepository.findByPostId(postId);

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
    }

    public ResponseEntity<Comment> sendComment(SendCommentDto sendCommentDto, HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(CommentService.class);
        String headerAuth = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", headerAuth);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String userUrl = UriComponentsBuilder.fromHttpUrl(userServiceBaseUrl)
                .pathSegment("get-userById", String.valueOf(sendCommentDto.getUserId()))
                .toUriString();

        ResponseEntity<User> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, entity, User.class);
        User user = userResponse.getBody();
        logger.info("User Response received: {}", user);


        String postUrl = UriComponentsBuilder.fromHttpUrl(postServiceBaseUrl)
                .pathSegment("get-postById", String.valueOf(sendCommentDto.getPostId()))
                .toUriString();

        ResponseEntity<Post> postResponse = restTemplate.exchange(postUrl, HttpMethod.GET, entity, Post.class);
        Post post = postResponse.getBody();
        logger.info("Post Response received: {}", post);

        Comment comment = Comment.builder()
                .comment(sendCommentDto.getComment())
                .postId(post)
                .userId(user)
                .build();
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
