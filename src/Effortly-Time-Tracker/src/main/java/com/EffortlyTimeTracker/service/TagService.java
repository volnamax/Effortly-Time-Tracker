package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.entity.TaskTagEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.repository.TagRepository;
import com.EffortlyTimeTracker.repository.TaskRepository;
import com.EffortlyTimeTracker.repository.TaskTagRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;

    @Autowired
    public TagService(TagRepository tagRepository, ProjectRepository projectRepository, TaskRepository taskRepository, TaskTagRepository taskTagRepository) {
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskTagRepository = taskTagRepository;
    }

    public TagEntity addTag(@NonNull TagEntity tagEntity, Integer taskId) {
        log.info("add new task: {}", tagEntity.getName());

        // Находим проект по ID
        ProjectEntity project = projectRepository.findById(tagEntity.getProject().getProjectId()).orElseThrow(()
                -> new ProjectNotFoundException(tagEntity.getProject().getProjectId()));

        TagEntity tag = tagRepository.save(tagEntity);

        project.getTags().add(tag);
        projectRepository.save(project); // Обновляем проект с новым тегом

        // Связываем тег с задачами
        if (taskId != null) {
            TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
            TaskTagEntity taskTag = TaskTagEntity.builder().task(task).tag(tag).build();
            taskTagRepository.save(taskTag);
        }


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
        return tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
    }


    public List<TagEntity> getAllTag() {
        return tagRepository.findAll();
    }

}
