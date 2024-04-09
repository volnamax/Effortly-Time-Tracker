package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.GroupDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group-controller")
@RestController
@RequestMapping("api/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Operation(summary = "Add group")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupEntity addTask(@Valid @RequestBody GroupDTO groupDTO) {
        return groupService.addGroup(groupDTO);
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

