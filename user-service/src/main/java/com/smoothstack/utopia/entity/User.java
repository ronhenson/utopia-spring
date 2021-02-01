package com.smoothstack.utopia.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

	@NonNull
	private UserRole userRole;

	private Boolean locked = false;

	private Boolean enabled = false;
}
