package com.springboot.group.mapper;

import com.springboot.group.dto.GroupDto;
import com.springboot.group.entity.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    Group groupPostToGroup(GroupDto.Post groupPost);
    Group groupPatchToGroup(GroupDto.Patch groupPatch);
    GroupDto.Response groupToGroupResponse(Group group);
    List<GroupDto.Response> groupsToGroupResponses(List<Group> groups);
}
