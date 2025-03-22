package com.springboot.chatroom.entity;

import com.springboot.category.entity.Category;
import com.springboot.member.entity.Member;
import com.springboot.message.entity.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void setMessage(Message message) {
        messages.add(message);
        if (message.getChatRoom() != this) {
            message.setChatRoom(this);
        }
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category.getChatRoom() != this) {
            category.setChatRoom(this);
        }
    }
}
