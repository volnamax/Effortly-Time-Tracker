package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
import com.EffortlyTimeTracker.repository.TagRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagEntity addTag(@NonNull TagDTO tagDTO) {
        log.info("add new task: {}", tagDTO.getName());
        TagEntity tag = tagRepository.save(TagEntity.builder()
                .name(tagDTO.getName())
                .project(tagDTO.getProject())
                .color(tagDTO.getColor())
//                .tasks(tagDTO.getTasks())
                .build()
        );
        log.info("task added : {}", tag.getTagId());
        return tag;
    }

    public void delTagById(Integer tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new TagNotFoundException(tagId);
        }
        tagRepository.deleteById(tagId);
        log.info("Task with id {} deleted", tagId);
    }

    public TagEntity getTagkById(Integer tagId) {
        TagEntity tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        log.info("Get = " + tag);
        return tag;
    }


    public List<TagEntity> getAllTag() {
        List<TagEntity> tag = tagRepository.findAll();
        log.info("GetALL = " + tag);
        return tag;
    }
}
