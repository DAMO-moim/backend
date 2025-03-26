package com.springboot.category.entity;

import com.springboot.chatroom.entity.ChatRoom;
import com.springboot.member.entity.MemberCategory;
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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<SubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<MemberCategory> memberCategories = new ArrayList<>();

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    public void setSubCategory(SubCategory subCategory) {
        subCategories.add(subCategory);
        if (subCategory.getCategory() != this) {
            subCategory.setCategory(this);
        }
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        if (chatRoom.getCategory() != this) {
            chatRoom.setCategory(this);
        }
    }
}
