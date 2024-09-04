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
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/get-comment/{postId}")
    // @PreAuthorize("hasRole('USER') or hasRole('BUSINESS')")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Get all post by it's ID", nickname = "getAllPostByIdOperation")
    @ApiResponses({
            @ApiResponse(code = SC_BAD_REQUEST, message = "Bad request", response = HttpExceptionResponse.class),
            @ApiResponse(code = SC_UNAUTHORIZED, message = "Unauthorized", response = HttpExceptionResponse.class),
            @ApiResponse(code = SC_NOT_FOUND, message = "Unauthorized", response = HttpExceptionResponse.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error", response = HttpExceptionResponse.class)})
    public ResponseEntity<List<CommentInforDto>> getAllCommentByPostId(
            @ApiParam(value = "Get comment by post ID.", required = true) @PathVariable("postId") int postId) {
        return commentService.getCommentByPostId(postId);
    }

    @PostMapping(value = "/sent-comment")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Send comment by it's user ID", nickname = "sendCommentOperation")
    @ApiResponses({
            @ApiResponse(code = SC_BAD_REQUEST, message = "Bad request", response = HttpExceptionResponse.class),
            @ApiResponse(code = SC_UNAUTHORIZED, message = "Unauthorized", response = HttpExceptionResponse.class),
            @ApiResponse(code = SC_NOT_FOUND, message = "Unauthorized", response = HttpExceptionResponse.class),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal server error", response = HttpExceptionResponse.class)})
    public ResponseEntity<Comment> sendCommentByUserId(@ApiParam(value = "User credentials") @RequestBody SendCommentDto sendCommentDto,
                                                       HttpServletRequest request) {
        return commentService.sendComment(sendCommentDto, request);
    }
}
