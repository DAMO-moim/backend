package com.springboot.group.controller;

import com.springboot.group.dto.GroupDto;
import com.springboot.group.mapper.GroupMapper;
import com.springboot.group.service.GroupService;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@ApiOperation(value = "모임 정보 API", tags = {"Group Controller"})
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper mapper;

    public GroupController(GroupService groupService, GroupMapper mapper) {
        this.groupService = groupService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "모임 등록", notes = "모임을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모임 등록 완료"),
            @ApiResponse(code = 400, message = "Group Validation failed")
    })
    @PostMapping
    public ResponseEntity groupPost(@Valid @RequestBody GroupDto.Post groupPostDto) {
        return null;
    }

    @ApiOperation(value = "모임 수정", notes = "모임 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모임 수정 완료"),
            @ApiResponse(code = 404, message = "Group Not Found")
    })
    @PatchMapping("/{group-id}")
    public ResponseEntity groupPatch(@PathVariable("group-id") @Positive long groupId,
                                     @Valid @RequestBody GroupDto.Patch groupPatchDto,
                                     @AuthenticationPrincipal Member authenticatedmember){
        return null;
    }

    @ApiOperation(value = "모임 조회", notes = "모임 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모임 조회 완료"),
            @ApiResponse(code = 404, message = "Group Not Found")
    })
    @GetMapping("/{group-id}")
    public ResponseEntity groupPatch(@PathVariable("group-id") @Positive long groupId,
                                     @AuthenticationPrincipal Member authenticatedmember){
        return null;
    }

    @ApiOperation(value = "모임 전체 조회", notes = "카테고리별 전체 모임을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모임 조회 완료"),
            @ApiResponse(code = 400, message = "Group Bad Request")
    })
    @GetMapping
    public ResponseEntity getBoards(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size,
                                    @RequestParam String sort,
                                    @AuthenticationPrincipal Member member){
        return null;
    }

    @ApiOperation(value = "모임 삭제", notes = "하나의 모임을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모임 삭제 완료"),
            @ApiResponse(code = 404, message = "Group Not Found")
    })
    @DeleteMapping("/{group-id}")
    public ResponseEntity deleteBoard(@PathVariable("group-id") @Positive long groupId,
                                      @AuthenticationPrincipal Member member){

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
