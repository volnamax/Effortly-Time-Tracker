package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.group.GroupCreateDTO;
import com.EffortlyTimeTracker.DTO.group.GroupDTO;
import com.EffortlyTimeTracker.DTO.group.GroupResponseDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
//import com.EffortlyTimeTracker.mapper.GroupMapper;
import com.EffortlyTimeTracker.mapper.GroupMapper;
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
    private final GroupMapper groupMapper;

    @Autowired
    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @Operation(summary = "Add group", description = "need name, proj id")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupResponseDTO> addTask(@Valid @RequestBody GroupCreateDTO groupDTO) {
        log.info("api/group/add");
        log.info("Add group DTO: {}", groupDTO);
        GroupEntity groupEntity = groupMapper.toEntity(groupDTO);
        log.info("Add group ENTITY: {}", groupEntity);

        log.info("id proj is = {} ", groupEntity.getProject().getProjectId() );
        GroupEntity savedGroupEntity = groupService.addGroup(groupEntity);
        log.info("Saved group entity: {}", savedGroupEntity);

        GroupResponseDTO resultDTO = groupMapper.toDto(savedGroupEntity);
        log.info("Saved group DTO: {}", resultDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
    }

    @Operation(summary = "Dell group by id",
            description = "need id")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delGroup(@RequestParam(required = true) Integer id) {
        log.info("api/group/del");
        log.info("id group to del = {}", id);
        groupService.delGroupById(id);
    }

    @Operation(summary = "Get group by id",
            description = "need id")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public GroupResponseDTO getGroup(@RequestParam(required = true) Integer id) {
        log.info("api/group/del");
        log.info("id group to get = {}", id);
        GroupResponseDTO groupResponseDTO =  groupMapper.toDto(groupService.getGroupById(id));
        log.info("groupResponseDTO = {}", groupResponseDTO);
        return groupResponseDTO;
    }

    @Operation(summary = "Get all group")
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupResponseDTO> getGroupAll() {
        log.info("api/group/get-all");
        List <GroupResponseDTO> groupResponseDTOS = groupMapper.toDto(groupService.getAllGroup());
        log.info("groupResponseDTOS = {}", groupResponseDTOS);
        return groupResponseDTOS;
    }
}

