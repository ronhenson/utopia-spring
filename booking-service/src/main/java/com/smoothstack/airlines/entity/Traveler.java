package com.smoothstack.airlines.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "tbl_traveler")
@NoArgsConstructor
@RequiredArgsConstructor
public class Traveler {
	
	@NonNull @Id private Integer travelerId;
	
	@NonNull private String name;
	
	@NonNull private String address;
	
	@NonNull private String phone;
	
	@NonNull private String email;
	
	@NonNull private Timestamp dob;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "travelers")
	private Set<Booking> bookings = new HashSet<>();
}
