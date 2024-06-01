package com.EffortlyTimeTracker.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.repository.RoleRepository;

//@Mapper(componentModel = "spring")
//public abstract class RoleMapper {
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Named("roleToRoleName")
//    public String roleToRoleName(RoleEntity role) {
//        return role != null ? role.getName().name() : "USER";
//    }
//
//    @Named("roleNameToRole")
//    public RoleEntity roleNameToRole(String roleName) {
//        return roleRepository.findByName(Role.valueOf(roleName))
//                .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + roleName));
//    }
//}
