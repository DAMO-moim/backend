package com.springboot.message.mapper;

import com.springboot.message.dto.MessageDto;
import com.springboot.message.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {
    @Mapping(target = "member.memberId", source = "memberId")
    @Mapping(target = "chatRoom.chatRoomId", source = "chatRoomId")
    Message messagePostToMessage(MessageDto.Post requestBody);

    @Mapping(source = "member.image", target = "writerProfileImage")
    @Mapping(source = "member.name", target = "writer")
    @Mapping(source = "createdAt", target = "createdAt")
    MessageDto.Response messageToMessageResponse(Message message);

    List<MessageDto.Response> messagesToMessageResponses(List<Message> messages);
}
