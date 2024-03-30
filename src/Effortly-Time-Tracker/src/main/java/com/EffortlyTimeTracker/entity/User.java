package com.EffortlyTimeTracker.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) 

public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id_user;

  //@Column (unique = true)
  private String user_name;

  //  @Column(name = "usnameS", nullable = false)
  private String user_secondname;
//  private String email;
//  private String description;
//  private Date data_sign_in ;
//  private Date data_last_log_in ;

  @Override
  public String toString() {
    return "User{" +
            "id_user=" + id_user +
            ", user_name='" + user_name + '\'' +
            ", user_secondname='" + user_secondname + '\'' +
            '}';
  }
}
