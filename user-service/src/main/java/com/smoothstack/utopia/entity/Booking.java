package com.ss.uthopia.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "booking")
@Table(name = "tbl_booking")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookingId;

	@ManyToOne()
	@JoinColumns({ @JoinColumn(name = "bookerId", referencedColumnName = "userId") })
	private User user;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tbl_bookings_has_travelers", joinColumns = {
			@JoinColumn(name = "bookingId") }, inverseJoinColumns = { @JoinColumn(name = "travelerId") })
	private List<Traveler> travelers;

	private boolean isActive;

	private String stripeId;

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User users) {
		this.user = users;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	public List<Traveler> getTravelers() {
		return travelers;
	}

	public void setTravelers(List<Traveler> travelers) {
		this.travelers = travelers;
	}
}
