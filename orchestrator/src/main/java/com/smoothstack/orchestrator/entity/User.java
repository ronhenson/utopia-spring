package com.smoothstack.orchestrator.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "tbl_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    @Builder.Default
    private UserRole userRole = UserRole.USER;

    @Builder.Default
    private Boolean locked = false;

    @Builder.Default
    private Boolean enabled = false;
}
