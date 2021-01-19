package com.smoothstack.orchestrator.entity;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "User")
@Table(name = "tbl_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @JsonIgnore
    @Builder.Default
    private UserRole userRole = UserRole.USER;

    @JsonIgnore
    @Builder.Default
    private Boolean locked = false;

    @JsonIgnore
    @Builder.Default
    private Boolean enabled = false;
}
