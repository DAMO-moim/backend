package com.springboot.group.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.board.entity.Board;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class GroupMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private GroupRoles groupRoles = GroupRoles.GROUP_MEMBER;

    // 영속성 전이, 동기화
    public void setMember(Member member) {
        this.member = member;
        if (member.getGroupMembers().contains(this)) {
            member.setGroupMember(this);
        }
    }

    // 영속성 전이, 동기화
    public void setGroup(Group group) {
        this.group = group;
        if (group.getGroupMembers().contains(this)) {
            group.setGroupMember(this);
        }
    }

    public enum GroupRoles {
        GROUP_LEADER("모임장"),
        GROUP_MEMBER("모임원");

        @Getter
        private String status;

        GroupRoles(String status) {
            this.status = status;
        }
    }
}
