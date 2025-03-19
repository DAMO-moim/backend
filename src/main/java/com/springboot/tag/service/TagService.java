package com.springboot.tag.service;

import com.springboot.tag.entity.Tag;
import com.springboot.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll(); // DB에서 전체 태그 조회
    }
}
