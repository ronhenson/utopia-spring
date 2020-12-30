package com.ss.uthopia.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity(name = "Traveler")
@Table(name = "traveler")
public class Traveler {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "traveler_id")
    private int travelerId;

    @ManyToMany()
    @JoinTable(name = "booking_travelers",
            joinColumns = {@JoinColumn(name = "traveler_id", referencedColumnName = "traveler_id")},
            inverseJoinColumns = {@JoinColumn(name = "booking_id", referencedColumnName = "booking_id")})
    private List<Booking> booking;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
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
