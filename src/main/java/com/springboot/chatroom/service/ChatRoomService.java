package com.springboot.chatroom.service;

import com.springboot.category.entity.Category;
import com.springboot.category.service.CategoryService;
import com.springboot.chatroom.entity.ChatRoom;
import com.springboot.chatroom.repository.ChatRoomRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberCategory;
import com.springboot.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final CategoryService categoryService;
    private final MemberService memberService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, CategoryService categoryService, MemberService memberService) {
        this.chatRoomRepository = chatRoomRepository;
        this.categoryService = categoryService;
        this.memberService = memberService;
    }

    //특정 채팅방 조회
    @Transactional(readOnly = true)
    public ChatRoom findChatRoom(long chatRoomId, long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        ChatRoom findChat = findVerifiedChatRoom(chatRoomId);

        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_NOT_FOUND));
    }

    //전체 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoom> findChatRooms(long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);

        List<ChatRoom> chatRooms = findMember.getMemberCategories().stream()
                .map(memberCategory -> {
                    Category category = memberCategory.getCategory();
                    ChatRoom chatRoom = category.getChatRoom();
                    return chatRoom;
                })
                .collect(Collectors.toList());
        return chatRooms;
    }


    @Transactional(readOnly = true)
    private ChatRoom findVerifiedChatRoom(long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_NOT_FOUND));
    }
}
