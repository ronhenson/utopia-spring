package com.smoothstack.utopia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "User")
@Table(name = "tbl_users")
public class User {
	@Id
	@Column(name="userId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String firstName;

	private String lastName;

	private String password;

	private String email;

	private int userRole;
}
