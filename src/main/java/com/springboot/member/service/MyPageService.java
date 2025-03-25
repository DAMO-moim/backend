package com.springboot.member.service;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.service.GroupService;
import com.springboot.member.dto.MyPageDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MyPageMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MyPageService {
    private final MemberService memberService;
    private final BoardService boardService;
    private final GroupService groupService;
    private final MyPageMapper myPageMapper;

    public MyPageService(MemberService memberService, BoardService boardService, GroupService groupService, MyPageMapper myPageMapper) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.groupService = groupService;
        this.myPageMapper = myPageMapper;
    }

    public Page<MyPageDto.BoardsResponse> getMyBoards(long memberId, String category, int page, int size){
        Member findMember = memberService.findVerifiedMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Board> boards;

        if(category.equalsIgnoreCase("ALL")){
            boards = boardService.findBoardsByMember(findMember, pageable);
        }else{
            boards = boardService.findBoardByMemberCategory(findMember, category, pageable);
        }

        return boards.map(myPageMapper::boardToBoardsResponse);
    }

    public Page<MyPageDto.GroupsResponse> getMyGroups(long memberId, String category, boolean leaderOnly, int page, int size){
        Member findMember = memberService.findVerifiedMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (category.equalsIgnoreCase("ALL")) {
            if (leaderOnly) {
                // 전체 모임 중에서 내가 리더인 것만
                Page<GroupMember> groupMembers = groupService.findGroupsByRole(
                        findMember, GroupMember.GroupRoles.GROUP_LEADER, pageable);
                return groupMembers.map(gm ->
                        myPageMapper.groupToGroupsResponse(gm.getGroup(), findMember));
            } else {
                // 전체 모임
                Page<Group> groups = groupService.findGroupsByMember(findMember, pageable);
                return groups.map(group ->
                        myPageMapper.groupToGroupsResponse(group, findMember));
            }
        } else {
            if (leaderOnly) {
                // 특정 카테고리 + 내가 리더인 모임
                Page<GroupMember> groupMembers = groupService.findGroupsByCategoryAndRole(
                        findMember, category, GroupMember.GroupRoles.GROUP_LEADER, pageable);
                return groupMembers.map(gm ->
                        myPageMapper.groupToGroupsResponse(gm.getGroup(), findMember));
            } else {
                // 특정 카테고리 + 내가 속한 모든 모임
                Page<GroupMember> groupMembers = groupService.findGroupsByCategory(findMember, category, pageable);
                return groupMembers.map(gm ->
                        myPageMapper.groupToGroupsResponse(gm.getGroup(),
                                findMember));
            }
        }
    }
}
