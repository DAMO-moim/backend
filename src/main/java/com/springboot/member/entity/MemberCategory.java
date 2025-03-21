package com.springboot.member.entity;

import com.springboot.category.entity.Category;
import com.springboot.group.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    @Min(value = 1)
    @Max(value = 3)
    private int priority;

    public void setMember(Member member) {
        this.member = member;
        if (!member.getMemberCategories().contains(this)) {
            member.setMemberCategory(this);
        }
    }
}
