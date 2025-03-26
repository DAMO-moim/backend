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
import com.springboot.message.entity.Message;
import com.springboot.message.service.MessageService;
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
    private final MessageService messageService;
    private final MemberService memberService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, CategoryService categoryService, MessageService messageService, MemberService memberService) {
        this.chatRoomRepository = chatRoomRepository;
        this.categoryService = categoryService;
        this.messageService = messageService;
        this.memberService = memberService;
    }

    //특정 채팅방 조회
    @Transactional(readOnly = true)
    public ChatRoom findChatRoom(long chatRoomId, long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        ChatRoom findChat = findVerifiedChatRoom(chatRoomId);
        //회원이 가진 카테고리가 맞는지 검증
        validateMemberInChatRoom(findChat.getCategory(), findMember);

        return findChat;
    }

    //멤버가 카테고리에 포함되어있는지 검증
    public void validateMemberInChatRoom(Category category, Member member){
        List<MemberCategory> memberCategories = memberService.findMemberCategroies(member.getMemberId());
        boolean hasAccess = memberCategories.stream()
                .anyMatch(memberCategory -> memberCategory.getCategory().equals(category));

        if (!hasAccess) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
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
    public ChatRoom findVerifiedChatRoom(long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHAT_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public List<Message> findRecentMessagesForRoom(Long chatRoomId) {
        return messageService.findRecentMessages(chatRoomId);
    }
}
