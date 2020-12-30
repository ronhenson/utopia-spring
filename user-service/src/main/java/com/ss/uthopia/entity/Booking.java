package com.ss.uthopia.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "booking")
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "booking_id")
    private long bookingId;

    @ManyToOne()
    @JoinColumns({
            @JoinColumn(name="booker_id", referencedColumnName="user_id")
    })
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "booking_travelers",
            joinColumns = {@JoinColumn(name = "booking_id", referencedColumnName = "booking_id")},
    inverseJoinColumns = {@JoinColumn(name = "traveler_id")})
    private List<Traveler> travelers;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "stripe_id")
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
