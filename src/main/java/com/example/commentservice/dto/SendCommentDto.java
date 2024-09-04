package com.example.commentservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="SendCommentDto")
public class SendCommentDto {

    private int userId;

    private int postId;

    @ApiModelProperty(value = "New comment", dataType = "String")
    private String comment;
}
