package com.springboot.group.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Group {
    private Long groupId;

    private String groupName;

    private String introduction;

    private int maxMemberCount;

    private Member.Gender gender;

    private String startBirth;

    private String endBirth;
}
