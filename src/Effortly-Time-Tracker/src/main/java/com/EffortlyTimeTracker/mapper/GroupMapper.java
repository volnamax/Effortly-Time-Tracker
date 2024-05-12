package com.EffortlyTimeTracker.mapper;


import com.EffortlyTimeTracker.DTO.group.GroupCreateDTO;
import com.EffortlyTimeTracker.DTO.group.GroupResponseDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(source = "projectId", target = "project", qualifiedByName = "projectIdToProject")
    GroupEntity toEntity(GroupCreateDTO dto);


    // Helper methods to handle the conversion of projectId to ProjectEntity and members to user IDs
    @Named("projectIdToProject")
    default ProjectEntity projectIdToProject(Integer projectId) {
        if (projectId == null) {
            return null;
        }
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(projectId);
        return project;
    }

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "members", target = "users", qualifiedByName = "membersToUserIds")
    GroupResponseDTO toDto(GroupEntity entity);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "members", target = "users", qualifiedByName = "membersToUserIds")
    List<GroupResponseDTO> toDto(List<GroupEntity> entity);

    // Helper method to convert from Set<GroupMemberEntity> to Set<Integer> for user IDs
    @Named("membersToUserIds")
    default Set<Integer> membersToUserIds(Set<GroupMermberEntity> members) {
        if (members == null) {
            return null;
        }
        return members.stream()
                .map(member -> member.getUser().getUserId()) // Assuming there's a getId() method in UserEntity
                .collect(Collectors.toSet());
    }

}

//import com.EffortlyTimeTracker.DTO.group.GroupDTO;
//import com.EffortlyTimeTracker.entity.GroupEntity;
//import com.EffortlyTimeTracker.entity.ProjectEntity;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class GroupMapper {
//    public GroupEntity dtoToEntityCreate(GroupDTO groupDTO) {
//        GroupEntity groupEntity = new GroupEntity();
//        groupEntity.setName(groupDTO.getName());
//        groupEntity.setDescription(groupDTO.getDescription());
//
//        ProjectEntity projectEntity = new ProjectEntity();
//        projectEntity.setProjectId(groupDTO.getProjectId());
//        groupEntity.setProject(projectEntity);
//
////        Set<GroupMermberEntity> members = groupDTO.getUsersGroup().stream()
////                .map(user -> {
////                    GroupMermberEntity member = new GroupMermberEntity();
////                    member.setUser(user);
////                    member.setGroup(groupEntity);
////                    return member;
////                }).collect(Collectors.toSet());
////
////        groupEntity.setMembers(members);
//        return groupEntity;
//    }
//
//    public GroupDTO entityToDto(GroupEntity groupEntity) {
////        Set<UserEntity> users = groupEntity.getMembers().stream()
////                .map(GroupMermberEntity::getUser)
////                .collect(Collectors.toSet());
//
//        GroupDTO groupDTO = new GroupDTO();
//        groupDTO.setName(groupEntity.getName());
//        groupDTO.setDescription(groupEntity.getDescription());
//        groupDTO.setProjectId(groupEntity.getProject().getProjectId());
//
//        return groupDTO;
//    }
//}
//
//
//


