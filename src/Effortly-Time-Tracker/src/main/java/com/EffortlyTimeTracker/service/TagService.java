package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.entity.TagProject;
import com.EffortlyTimeTracker.entity.TaskTable;
import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.TagRepositoty;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagService {

    private final TagRepositoty tagRepositoty;

    @Autowired
    public TagService(TagRepositoty tagRepositoty) {
        this.tagRepositoty = tagRepositoty;
    }

    public TagProject addTag(@NonNull TagDTO tagDTO) {
        log.info("add new task: {}", tagDTO.getName());
        TagProject tag = tagRepositoty.save(TagProject.builder()
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
        if (!tagRepositoty.existsById(tagId)) {
            throw new TagNotFoundException(tagId);
        }
        tagRepositoty.deleteById(tagId);
        log.info("Task with id {} deleted", tagId);
    }

    public TagProject getTagkById(Integer tagId) {
        TagProject tag = tagRepositoty.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        log.info("Get = " + tag);
        return tag;
    }


    public List<TagProject> getAllTag() {
        List<TagProject> tag = tagRepositoty.findAll();
        log.info("GetALL = " + tag);
        return tag;
    }
}
