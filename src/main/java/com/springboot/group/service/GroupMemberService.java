package com.springboot.group.service;

import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.repository.GroupMemberRepository;
import com.springboot.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    //회원이 모임장 권한이 있는지 검증하는 메서드
    public boolean findLeader(Member member){
        List<GroupMember> findLeaders = groupMemberRepository.findByMemberAndGroupRoles(
                member, GroupMember.GroupRoles.GROUP_LEADER);

        return !findLeaders.isEmpty();
    }

    //회원탈퇴시 가입된 모임과 일정에서 삭제되어야 한다.
    public void deleteAllGroups(Member member){
        List<GroupMember> joinedGroups = groupMemberRepository.findAllByMember(member);

        joinedGroups.stream().forEach(groupMember -> {
            Group group = groupMember.getGroup();
            group.getGroupMembers().remove(groupMember);
            groupMemberRepository.delete(groupMember);
        });
    }

}
