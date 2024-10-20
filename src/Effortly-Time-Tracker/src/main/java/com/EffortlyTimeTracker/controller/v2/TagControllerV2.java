package com.EffortlyTimeTracker.controller.v2;

import com.EffortlyTimeTracker.DTO.tag.TagCreateDTO;
import com.EffortlyTimeTracker.DTO.tag.TagResponseDTO;
import com.EffortlyTimeTracker.entity.TagEntity;
import com.EffortlyTimeTracker.mapper.TagMapper;
import com.EffortlyTimeTracker.service.TagService;
import com.EffortlyTimeTracker.service.middlewareOwn.tag.CheckTagOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.tag.CheckUserIdMatchesCurrentUserTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Tag-V2-controller")
@RestController
@RequestMapping("api/v2/tags")
public class TagControllerV2 {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @Autowired
    public TagControllerV2(TagService tagService, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @Operation(summary = "Add tag", description = "need: name, projectID")
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project or Task not found", content = @Content)
    })

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

    @Operation(summary = "Delete tag by id", description = "Delete tag by id")
    @DeleteMapping("/{tagId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delTag(@PathVariable("tagId") Integer tagId) {
        log.info("api/tags/{tagId}");
        log.info("Deleting tag: {}", tagId);
        tagService.delTagById(tagId);
    }


    @Operation(summary = "Get tag by id", description = "Get tag by id from the URL")
    @GetMapping("/{tagId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @CheckTagOwner
    public TagResponseDTO getTag(@PathVariable("tagId") Integer tagId) {
        log.info("api/tags/{}", tagId);
        log.info("Getting tag with id: {}", tagId);

        TagEntity tag = tagService.getTagkById(tagId);
        if (tag == null) {
            log.warn("Tag with id {} not found", tagId);
            throw new RuntimeException("Tag not found");
        }

        return tagMapper.tagToTagResponseDTO(tag);
    }



    @Operation(summary = "Get all tag")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TagResponseDTO> getAllTag() {
        log.info("api/tag/get-all");
        List<TagEntity> tagEntities = tagService.getAllTag();
        log.info(" tag Entity: {}", tagEntities);
        List<TagResponseDTO> tagResponseDTOS = tagMapper.tagToTagResponseDTO(tagEntities);
        return tagResponseDTOS;
    }
}

