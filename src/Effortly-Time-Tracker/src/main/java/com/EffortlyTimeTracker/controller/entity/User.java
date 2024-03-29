package com.EffortlyTimeTracker.controller.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
