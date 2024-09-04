package com.example.commentservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="CommentInforDto")
public class CommentInforDto {

    private int commentId;

    private String userName;

    private String comment;

    @ApiModelProperty(value = "Comment's date")
    private Timestamp time;

    @ApiModelProperty(value = "User's image")
    private byte[] userImage;
}
