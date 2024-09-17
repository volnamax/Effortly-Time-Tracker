package com.EffortlyTimeTracker.builder;

import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;

public class UserEntityBuilder {
    private int userId = 1;  // Значения по умолчанию
    private String userName = "DefaultName";
    private String userSecondName = "DefaultSecondName";
    private String email = "default@example.com";
    private String passwordHash = "defaultPassword";
    private RoleEntity role;

    public UserEntityBuilder() {
        this.role = new RoleEntity();
        this.role.setName(Role.USER);
    }

    public UserEntityBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public UserEntityBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserEntityBuilder withUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
        return this;
    }

    public UserEntityBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserEntityBuilder withPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public UserEntityBuilder withRole(RoleEntity role) {
        this.role = role;
        return this;
    }

    public UserEntity build() {
        UserEntity user = new UserEntity();
        user.setUserId(this.userId);
        user.setUserName(this.userName);
        user.setUserSecondname(this.userSecondName);
        user.setEmail(this.email);
        user.setPasswordHash(this.passwordHash);
        user.setRole(this.role);
        return user;
    }
}
