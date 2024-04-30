package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.GroupDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.mapper.EntityMapper;
//import com.EffortlyTimeTracker.mapper.GroupMapper;
import com.EffortlyTimeTracker.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group-controller")
@RestController
@RequestMapping("api/group")
@Slf4j
public class GroupController {
    private final GroupService groupService;
//    private final GroupMapper groupMapper;

    @Autowired
    private EntityMapper mapper;

    @Autowired
    public GroupController(GroupService groupService, EntityMapper mapper) {
        this.groupService = groupService;
        this.mapper = mapper;
    }

    @Operation(summary = "Add group")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupDTO> addTask(@Valid @RequestBody GroupDTO groupDTO) {

        // Использование маппера с кастомным маппингом projectId -> ProjectEntity
        GroupEntity groupEntity = mapper.groupDTOToGroupWithProject(groupDTO);
        log.info("id proj is = {} ", groupEntity.getProject().getProjectId() );
        GroupEntity savedGroupEntity = groupService.addGroup(groupEntity);

        GroupDTO resultDTO = mapper.groupEntityToGroupDTO(savedGroupEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
//
//        log.info("id proj is = {} group {}", groupDTO.getProjectId(), groupDTO);
//
//        GroupEntity groupEntity = mapper.groupDTOToGroup(groupDTO);
//        log.info("id proj is = {} ", groupEntity.getProject().getProjectId() );
//
//        GroupEntity groupEntityNew =  groupService.addGroup(groupEntity);
//        GroupDTO res = mapper.groupEntityToGroupDTO(groupEntityNew);
//        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    @Operation(summary = "Dell group by id",
            description = "need id")
    @DeleteMapping("/del")
    public void delGroup(@RequestParam(required = true) Integer id) {
        groupService.delGroupById(id);
    }

    @Operation(summary = "Get group by id",
            description = "need id")
    @GetMapping("/get")
    public GroupEntity getGroup(@RequestParam(required = true) Integer id) {
        return groupService.getGroupById(id);
    }

    @Operation(summary = "Get all group")
    @GetMapping("/get-all")
    public List<GroupEntity> getGroupAll() {
        return groupService.getAllGroup();
    }
}

