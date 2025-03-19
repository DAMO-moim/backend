package com.springboot.tag.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.tag.dto.ResponseDto;
import com.springboot.tag.entity.Tag;
import com.springboot.tag.mapper.TagMapper;
import com.springboot.tag.service.TagService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final TagMapper mapper;

    public TagController(TagService tagService, TagMapper mapper) {
        this.tagService = tagService;
        this.mapper = mapper;
    }


    @Operation(summary = "태그 목록 조회", description = "모든 태그를 태그 타입별로 그룹화하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그 목록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<SingleResponseDto<ResponseDto>> getTagsByType() {
        List<Tag> tags = tagService.getAllTags();
        ResponseDto responseDto = mapper.tagtoResponseDto(tags);

        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

//    @Operation(summary = "특정 태그 타입 조회", description = "tag_type에 해당하는 태그 목록만 조회합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "태그 조회 성공",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ResponseDto.class))),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @GetMapping("/filter")
//    public ResponseEntity<SingleResponseDto<ResponseDto>> getTagsByType(@RequestParam(name = "tag_type") String tagType) {
//        List<Tag> tags = tagService.getAllTags();  // 모든 태그 조회
//        Map<String, List<String>> groupedTags = ResponseDto.from(tags); // 태그 타입별 그룹화
//
//        if (tagType == null || tagType.isEmpty()) {
//            return ResponseEntity.ok(new SingleResponseDto<>(new ResponseDto(groupedTags)));
//        }
//
//        Map<String, List<String>> filteredTags = new HashMap<>();
//        if (groupedTags.containsKey(tagType)) { // 특정 태그 타입이 존재하는 경우
//            filteredTags.put(tagType, groupedTags.get(tagType));
//        }
//
//        ResponseDto responseDto = new ResponseDto(filteredTags);
//        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
//    }
}
