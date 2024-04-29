package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.mapper.TagMapper;
import com.EffortlyTimeTracker.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
//todo add check then no project and task
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

    @Operation(summary = "Add tag")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TagDTO> addTag(@Valid @RequestBody TagDTO tagDTO) {
        TagEntity tag = tagMapper.dtoToEntity(tagDTO);
        TagEntity tagEntity = tagService.addTag(tag);
        TagDTO res = tagMapper.entityToDto(tagEntity);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell tag by id",
            description = "need id")
    @DeleteMapping("/del")
    public void delTag(@RequestParam(required = true) Integer tagId) {
        tagService.delTagById(tagId);
    }

    @Operation(summary = "Get tag by id",
            description = "need id")
    @GetMapping("/get")
    public TagDTO getTag(@RequestParam(required = true) Integer tagId) {
        return tagMapper.entityToDto(tagService.getTagkById(tagId));
    }

    @Operation(summary = "Get all tag")
    @GetMapping("/get-all")
    public List<TagDTO> getTag() {
        List<TagEntity> tagEntities = tagService.getAllTag();
        return tagMapper.entitySetToDtoList(tagEntities);
    }
}

