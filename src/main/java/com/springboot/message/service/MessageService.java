package com.springboot.message.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.message.entity.Message;
import com.springboot.message.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    //메세지를 받아 db에 바로 저장
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Message findMessage(long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MESSAGE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Message> findRecentMessages(Long chatRoomId) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
//        return messageRepository.findRecentMessages(chatRoomId, oneDayAgo);
        return messageRepository.findByChatRoom_ChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }
}
