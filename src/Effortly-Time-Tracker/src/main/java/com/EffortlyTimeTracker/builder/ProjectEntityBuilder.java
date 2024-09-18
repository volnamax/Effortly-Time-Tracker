package com.EffortlyTimeTracker.builder;

import com.EffortlyTimeTracker.entity.*;

import java.util.List;
import java.util.Set;

public class ProjectEntityBuilder {
    private Integer projectId;
    private String name;
    private String description;
    private UserEntity userProject;
    private GroupEntity group;
    private List<TableEntity> tables;
    private Set<TagEntity> tags;

    public ProjectEntityBuilder withProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public ProjectEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProjectEntityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProjectEntityBuilder withUserProject(UserEntity userProject) {
        this.userProject = userProject;
        return this;
    }

    public ProjectEntityBuilder withGroup(GroupEntity group) {
        this.group = group;
        return this;
    }

    public ProjectEntityBuilder withTables(List<TableEntity> tables) {
        this.tables = tables;
        return this;
    }

    public ProjectEntityBuilder withTags(Set<TagEntity> tags) {
        this.tags = tags;
        return this;
    }

    public ProjectEntity build() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectId(projectId);
        projectEntity.setName(name);
        projectEntity.setDescription(description);
        projectEntity.setUserProject(userProject);
        projectEntity.setGroup(group);
        projectEntity.setTables(tables);
        projectEntity.setTags(tags);
        return projectEntity;
    }
}
