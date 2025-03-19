package com.springboot.group.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class GroupService {
//    private final GroupRepository groupRepository;
//
//    public GroupService(GroupRepository groupRepository) {
//        this.groupRepository = groupRepository;
//    }

    public Group createGroup(Group group, long memberId){
        return null;
    }

    public Group updateGroup(Group group, long memberId){
        return null;
    }

    public Group findGroup(long groupId, long memberId){
        return null;
    }

    public Page<Group> findGroups(int page, int size, String sortType, long memberId){
        Sort sort;

        switch (sortType) {
            case "oldest": // 오래된 글 순(오름차순)
                sort = Sort.by("groupId").ascending();
                break;
            case "likes": //좋아요 순
                sort = Sort.by("likeCount").descending();
                break;
            default:
                //기본값은 최신글 순
                sort = Sort.by("boardId").descending();
                break;
        }

        return null;
    }

    public void deleteGroup(long groupId, long memberId){

    }

    //그룹이 존재하는지 확인하는 메서드
    public Group findVerifiedGroup(long groupId){
      //  Optional<Group> optionalGroup = groupRepository.findById(groupId);
      //  Group group = optionalGroup.orElseThrow(() ->
        //        new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));

      //  return group;
        return null;
    }
}
