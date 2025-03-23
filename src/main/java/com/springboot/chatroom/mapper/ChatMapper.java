package com.springboot.chatroom.mapper;


import com.springboot.chatroom.dto.ChatRoomDto;
import com.springboot.chatroom.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryName")
    ChatRoomDto.Response chatRoomToChatRoomResponseDto(ChatRoom chatRoom);
    List<ChatRoomDto.Response> chatRoomToChatRoomResponseDtos(List<ChatRoom> chatRooms);
}
