// todo del fatch = lazy

// todo respond to dto format

// todo check to all entity for controller and service


package com.EffortlyTimeTracker.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "group_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "groupId")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer goupId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = true)
    String description;
//
//    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true  )
//    @JsonIgnoreProperties("group")
//    private Set<GroupUser> userGroups = new HashSet<>();
//
//
//
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
//    @JsonIgnore
//    private Project project;

}