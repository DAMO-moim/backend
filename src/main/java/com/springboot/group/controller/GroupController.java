package com.springboot.group.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.group.dto.GroupDto;
import com.springboot.group.entity.Group;
import com.springboot.group.mapper.GroupMapper;
import com.springboot.group.service.GroupService;
import com.springboot.member.entity.Member;
import com.springboot.utils.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
@Validated
public class GroupController {
    private final static String GROUP_DEFAULT_URL = "/groups";
    private final GroupMapper groupMapper;
    private final GroupService groupService;

    public GroupController(GroupMapper groupMapper, GroupService groupService) {
        this.groupMapper = groupMapper;
        this.groupService = groupService;
    }


    @Operation(summary = "모임 생성", description = "모임을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "모임 생성 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "400", description = "요청이 잘못되었음")
    })
    @PostMapping
    public ResponseEntity postGroup(@RequestBody @Valid GroupDto.Post groupPostDto,
                                    @AuthenticationPrincipal Member authenticatedmember) {
        Group group = groupMapper.groupPostToGroup(groupPostDto);

        Group createGroup = groupService.createGroup(group, authenticatedmember.getMemberId());

        URI location = UriCreator.createUri(GROUP_DEFAULT_URL, createGroup.getGroupId());

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "모임 정보 수정", description = "모임 소개 or 모임 최대 인원 수를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 정보 수정 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "400", description = "요청이 잘못되었음")
    })
    @PatchMapping("/{group-id}")
    public ResponseEntity patchGroup(@PathVariable("group-id") @Positive long groupId,
                                     @RequestBody @Valid GroupDto.Patch groupPatchDto,
                                     @AuthenticationPrincipal Member authenticatedmember) {
        groupPatchDto.setGroupId(groupId);

        Group group = groupMapper.groupPatchToGroup(groupPatchDto);

        Group updateGroup = groupService.updateGroup(group, authenticatedmember.getMemberId());

        GroupDto.Response groupResponse = groupMapper.groupToGroupResponse(updateGroup);

        return new ResponseEntity<>(groupResponse, HttpStatus.OK);
    }

    @Operation(summary = "모임 정보 단일 조회", description = "하나의 모임 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "하나의 모임 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없습니다.")
    })
    @GetMapping("/{group-id}")
    public ResponseEntity getGroup(@PathVariable("group-id") @Positive long groupId,
                                   @AuthenticationPrincipal Member authenticatedmember) {

        Group group = groupService.findGroup(groupId, authenticatedmember.getMemberId());

        GroupDto.Response groupResponse = groupMapper.groupToGroupResponse(group);

        return new ResponseEntity<>(new SingleResponseDto<>(groupResponse), HttpStatus.OK);
    }

    @Operation(summary = "모임 정보 전체 조회", description = "전체 모임 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 모임 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음")
    })
    @GetMapping
    public ResponseEntity getGroups(@RequestParam @Positive int page,
                                    @RequestParam @Positive int size,
                                    @AuthenticationPrincipal Member authenticatedmember) {
        Page<Group> groupPage = groupService.findGroups(page - 1, size, authenticatedmember.getMemberId());
        List<Group> groups = groupPage.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(groupMapper.groupsToGroupResponses(groups), groupPage),
                HttpStatus.OK);
    }

    @Operation(summary = "모임 삭제", description = "모임을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "모임 삭제 완료"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임이 존재하지 않음")
    })
    @DeleteMapping("/{group-id}")
    public ResponseEntity deleteGroup(@PathVariable("group-id") long groupId,
                                      @AuthenticationPrincipal Member authenticatedmember) {
        groupService.deleteGroup(groupId, authenticatedmember.getMemberId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 모임 가입 요청
    @Operation(summary = "모임 가입", description = "하나의 모임에 가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 회원입니다.")
    })
    @PostMapping("/{group-id}/join")
    public ResponseEntity joinGroup(@PathVariable("group-id") long groupId,
                                    @AuthenticationPrincipal Member member) {
        groupService.joinGroup(groupId, member.getMemberId());
        return ResponseEntity.ok().build();
    }
}
