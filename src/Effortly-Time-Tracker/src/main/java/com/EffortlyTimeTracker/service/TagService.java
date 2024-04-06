package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.entity.TagProject;
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

    public TagProject addTag(@NonNull TagDTO tagDTO) {
        log.info("add new task: {}", tagDTO.getName());
        TagProject tag = tagRepository.save(TagProject.builder()
                .name(tagDTO.getName())
                .project(tagDTO.getProject())
                .color(tagDTO.getColor())
                .tasks(tagDTO.getTasks())
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

    public TagProject getTagkById(Integer tagId) {
        TagProject tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        log.info("Get = " + tag);
        return tag;
    }


    public List<TagProject> getAllTag() {
        List<TagProject> tag = tagRepository.findAll();
        log.info("GetALL = " + tag);
        return tag;
    }
}
