package com.EffortlyTimeTracker.mapper;


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
