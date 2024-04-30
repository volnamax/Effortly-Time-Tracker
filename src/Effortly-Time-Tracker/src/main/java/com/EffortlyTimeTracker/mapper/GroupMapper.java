//package com.EffortlyTimeTracker.mapper;
//
//import com.EffortlyTimeTracker.DTO.GroupDTO;
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


