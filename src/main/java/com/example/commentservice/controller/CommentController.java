package com.example.commentservice.controller;

import com.example.commentservice.dto.CommentInforDto;
import com.example.commentservice.dto.SendCommentDto;
import com.example.commentservice.entity.Comment;
import com.example.commentservice.error.HttpExceptionResponse;
import com.example.commentservice.service.CommentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CommentInforDto>> getAllCommentByPostId(@RequestParam(value = "postId", required = true) int postId) {
        return commentService.getCommentByPostId(postId);
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> sendCommentByUserId(@RequestBody SendCommentDto sendCommentDto,
                                                       HttpServletRequest request) {
        return commentService.sendComment(sendCommentDto, request);
    }
}
