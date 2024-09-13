package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.tag.TagCreateDTO;
import com.EffortlyTimeTracker.DTO.tag.TagResponseDTO;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.mapper.TagMapper;
import com.EffortlyTimeTracker.service.TagService;
import com.EffortlyTimeTracker.service.middlewareOwn.tag.CheckTagOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.tag.CheckUserIdMatchesCurrentUserTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')or hasRole('ROLE_GUEST')")
    @CheckUserIdMatchesCurrentUserTag
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

    @Operation(summary = "Dell tag by id", description = "need id")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delTag(@RequestParam(required = true) Integer tagId) {
        log.info("api/tag/del");
        log.info("Deling tag : {}", tagId);
        tagService.delTagById(tagId);
    }

    @Operation(summary = "Get tag by id", description = "need id")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @CheckTagOwner
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TagResponseDTO> getAllTag() {
        log.info("api/tag/get-all");
        List<TagEntity> tagEntities = tagService.getAllTag();
        log.info(" tag Entity: {}", tagEntities);
        List<TagResponseDTO> tagResponseDTOS = tagMapper.tagToTagResponseDTO(tagEntities);
        return tagResponseDTOS;
    }
}

