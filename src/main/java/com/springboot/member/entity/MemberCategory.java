package com.springboot.member.entity;

import com.springboot.category.entity.Category;
import com.springboot.group.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MemberCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MemberCategoryId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void setMember(Member member) {
        this.member = member;
        if (!member.getMemberCategories().contains(this)) {
            member.setMemberCategory(this);
        }
    }
}
