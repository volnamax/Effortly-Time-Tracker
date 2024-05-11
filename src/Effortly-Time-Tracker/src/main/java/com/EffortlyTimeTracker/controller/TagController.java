package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.tag.TagCreateDTO;
import com.EffortlyTimeTracker.DTO.tag.TagDTO;
import com.EffortlyTimeTracker.DTO.tag.TagResponseDTO;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.mapper.TagMapper;
import com.EffortlyTimeTracker.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//todo add check then no project and task
//todo add get operations
@Slf4j
@Tag(name = "Tag-controller")
@RestController
@RequestMapping("api/tag")
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @Autowired
    public TagController(TagService tagService, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @Operation(summary = "Add tag", description = "need: name, projectID")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TagResponseDTO> addTag(@Valid @RequestBody TagCreateDTO tagDTO) {
        log.info("api/tag/add");
        log.info("Adding tag : {}", tagDTO);
        TagEntity tag = tagMapper.toEntity(tagDTO);
        log.info("Adding tag ENTITY: {}", tag);

        Integer taskId = tagMapper.getTaskIdFromTagDTO(tagDTO);
        log.info("Adding task id: {}", taskId);

        TagEntity tagEntity = tagService.addTag(tag, taskId);
        log.info("Adding tag add: {}", tagEntity);
        TagResponseDTO res = tagMapper.tagToTagResponseDTO(tagEntity);
        log.info("Adding tag DTO: {}", res);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell tag by id",
            description = "need id")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delTag(@RequestParam(required = true) Integer tagId) {
        log.info("api/tag/del");
        log.info("Deling tag : {}", tagId);
        tagService.delTagById(tagId);
    }

    @Operation(summary = "Get tag by id",
            description = "need id")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public TagResponseDTO getTag(@RequestParam(required = true) Integer tagId) {
        log.info("api/tag/get");
        log.info("Getting tag : {}", tagId);
        TagEntity tag = tagService.getTagkById(tagId);
        log.info(" tag Entity: {}", tag);

        TagResponseDTO tagResponseDTO = tagMapper.tagToTagResponseDTO(tag);
        log.info(" tag ResponseDTO: {}", tagResponseDTO);
        return tagResponseDTO;
    }

    @Operation(summary = "Get all tag")
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> getAllTag() {
        List<TagEntity> tagEntities = tagService.getAllTag();
        return tagMapper.FullEntityToDto(tagEntities);
    }
}

