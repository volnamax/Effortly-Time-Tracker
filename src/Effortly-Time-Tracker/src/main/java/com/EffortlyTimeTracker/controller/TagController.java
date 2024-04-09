package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TagDTO;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag-controller")
@RestController
@RequestMapping("api/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Add tag")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TagEntity addTag(@Valid @RequestBody TagDTO tagDTO) {
        return tagService.addTag(tagDTO);
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
    public TagEntity getTag(@RequestParam(required = true) Integer tagId) {
        return tagService.getTagkById(tagId);
    }

    @Operation(summary = "Get all tag")
    @GetMapping("/get-all")
    public List<TagEntity> getTag() {
        return tagService.getAllTag();
    }
}

