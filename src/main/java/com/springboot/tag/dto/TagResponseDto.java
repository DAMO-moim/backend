package com.springboot.tag.dto;

import com.springboot.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "ResponseDto", description = "태그 응답 DTO")
public class TagResponseDto {
    @Schema(
            description = "태그 목록을 태그 타입별로 그룹화한 데이터",
            example = "{ \"MBTI\": [\"INFP\", \"ISFP\"], " +
                    "\"age\": [\"20대\", \"30대\"], " +
                    "\"mood\": [\"화목\", \"발랄\"] }"
    )
    private Map<String, List<String>> tags;

    public static Map<String, List<String>> from(List<Tag> tags) {
        return tags.stream()
                .collect(Collectors.groupingBy(
                        Tag::getTagType,  // tagType을 기준으로 그룹화
                        Collectors.mapping(Tag::getTagName, Collectors.toList()) // tagName만 리스트로 저장
                ));
    }
}
