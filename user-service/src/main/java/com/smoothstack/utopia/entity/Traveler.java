package com.ss.uthopia.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity(name = "Traveler")
@Table(name = "tbl_traveler")
public class Traveler {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "traveler_id")
	private int travelerId;

	@ManyToMany()
	@JoinTable(name = "tbl_bookings_has_travelers", joinColumns = { @JoinColumn(name = "travelerId") }, inverseJoinColumns = {
			@JoinColumn(name = "bookingId") })
	private List<Booking> booking;

	private String name;

	private String address;

	private String phone;

	private String email;

	private ZonedDateTime dob;

	public int getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(int travelerId) {
		this.travelerId = travelerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
		return dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}

	public void setDob(ZonedDateTime dob) {
		this.dob = dob;
	}
}
