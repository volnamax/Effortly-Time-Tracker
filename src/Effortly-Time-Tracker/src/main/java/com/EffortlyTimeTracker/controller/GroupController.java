package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.group.AddUserToGroupDTO;
import com.EffortlyTimeTracker.DTO.group.GroupCreateDTO;
import com.EffortlyTimeTracker.DTO.group.GroupResponseDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.mapper.GroupMapper;
import com.EffortlyTimeTracker.unit.GroupService;
import com.EffortlyTimeTracker.unit.middlewareOwn.group.CheckGroupOwner;
import com.EffortlyTimeTracker.unit.middlewareOwn.group.CheckProjectOwner;
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

@Tag(name = "Group-controller")
@RestController
@RequestMapping("api/groups")
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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @CheckProjectOwner
    public ResponseEntity<GroupResponseDTO> addGroup(@Valid @RequestBody GroupCreateDTO groupDTO) {
        log.info("api/group/add");
        log.info("Add group DTO: {}", groupDTO);
        GroupEntity groupEntity = groupMapper.toEntity(groupDTO);
        log.info("Add group ENTITY: {}", groupEntity);

        log.info("id proj is = {} ", groupEntity.getProject().getProjectId());
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @CheckGroupOwner
    public void delGroup(@RequestParam(required = true) Integer id) {
        log.info("api/group/del");
        log.info("id group to del = {}", id);
        groupService.delGroupById(id);
    }

    @Operation(summary = "Get group by id",
            description = "need id")
    @GetMapping("/get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @CheckGroupOwner
    public GroupResponseDTO getGroup(@RequestParam(required = true) Integer id) {
        log.info("api/group/del");
        log.info("id group to get = {}", id);
        GroupResponseDTO groupResponseDTO = groupMapper.toDto(groupService.getGroupById(id));
        log.info("groupResponseDTO = {}", groupResponseDTO);
        return groupResponseDTO;
    }

    //  todo user can hand this
    @Operation(summary = "Get all group")
    @GetMapping("/get-all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GroupResponseDTO> getGroupAll() {
        log.info("api/group/get-all");
        List<GroupResponseDTO> groupResponseDTOS = groupMapper.toDto(groupService.getAllGroup());
        log.info("groupResponseDTOS = {}", groupResponseDTOS);
        return groupResponseDTOS;
    }

    @Operation(summary = "Add user to group", description = "Need groupId and userId")
    @PostMapping("/add-user-to-group")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully added to group", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group or User not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @CheckGroupOwner
    public ResponseEntity<Void> addUserToGroup(@Valid @RequestBody AddUserToGroupDTO addUserToGroupDTO) {
        log.info("api/group/add-user");
        log.info("Adding user to group: {}", addUserToGroupDTO);

        groupService.addUserToGroup(addUserToGroupDTO.getGroupId(), addUserToGroupDTO.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove user from group", description = "Need groupId and userId")
    @DeleteMapping("/remove-user-from-group")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully removed from group", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Group or User not found", content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @CheckGroupOwner
    public ResponseEntity<Void> removeUserFromGroup(@RequestBody AddUserToGroupDTO addUserToGroupDTO) {
        log.info("api/group/remove-user");
        log.info("Removing user from group: {}", addUserToGroupDTO);

        groupService.removeUserFromGroup(addUserToGroupDTO.getGroupId(), addUserToGroupDTO.getUserId());
        return ResponseEntity.ok().build();
    }
}

