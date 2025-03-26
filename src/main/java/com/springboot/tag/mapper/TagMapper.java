package com.springboot.tag.mapper;

import com.springboot.tag.dto.TagResponseDto;
import com.springboot.tag.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {
    //List<Tag>를 받아서 ResponseDto로 변환하는 메서드
    default TagResponseDto tagtoResponseDto(List<Tag> tags) {
        Map<String, List<String>> groupedTags = tags.stream()
                .collect(Collectors.groupingBy(
                        Tag::getTagType,
                        Collectors.mapping(Tag::getTagName, Collectors.toList())
                ));
        return new TagResponseDto(groupedTags);
    }
}