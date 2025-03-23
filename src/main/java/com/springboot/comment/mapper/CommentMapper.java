package com.springboot.comment.mapper;

import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment commentPostDtoToComment(CommentDto.Post postDto);
    Comment commentPatchDtoToComment(CommentDto.Patch requestBody);
    List<CommentDto.Response> commentToCommentResponseDtos(List<Comment>comment);

    default CommentDto.Response boardToBoardResponseDto(Comment comment){
        CommentDto.Response responseDto = new CommentDto.Response(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getMember().getMemberId(),
                comment.getMember().getName()
        );
        return responseDto;
    }
}
