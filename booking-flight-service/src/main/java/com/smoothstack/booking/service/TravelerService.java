package com.smoothstack.booking.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.smoothstack.booking.dao.BookingDao;
import com.smoothstack.booking.dao.BookingsHasTravelersDao;
import com.smoothstack.booking.dao.TravelerDao;
import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.BookingsHasTravelers;
import com.smoothstack.booking.entity.Traveler;
import com.smoothstack.booking.exceptions.DeleteLastTravelerFromBookingException;
import com.smoothstack.booking.exceptions.ResourceNotFoundException;
import com.smoothstack.constants.ResourceType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelerService {
  @Autowired
  TravelerDao travelerDao;

  @Autowired
  BookingsHasTravelersDao bookingTravelerDao;

  @Autowired
  BookingDao bookingDao;

  public Integer userOwnsTraveler(Integer userId, Integer travelerId) {
    Optional<BookingsHasTravelers> bookingTraveler = bookingTravelerDao.findByTravelerId(travelerId);
    if (bookingTraveler.isEmpty()) {
      throw new ResourceNotFoundException(travelerId, ResourceType.TRAVELER);
    }

    Optional<Booking> booking = bookingDao.findById(bookingTraveler.get().getBookingId().longValue());
    if (booking.isEmpty()) {
      throw new ResourceNotFoundException(bookingTraveler.get().getBookingId(), ResourceType.BOOKING);
    }

    if (booking.get().getBookerId() == userId) {
      return booking.get().getBookingId().intValue();
    } else {
      return null;
    }
  }

  public Traveler updateTraveler(Traveler traveler) {
    Optional<Traveler> dbTraveler = travelerDao.findById(traveler.getTravelerId());

    if (dbTraveler.isEmpty()) {
      throw new ResourceNotFoundException(traveler.getTravelerId(), ResourceType.TRAVELER);
    }

    return travelerDao.save(traveler);
  }

  @Transactional
	public void deleteTraveler(Integer travelerId, Integer bookingIdOfTraveler) {
		Optional<Traveler> dbTraveler = travelerDao.findById(travelerId);
    if (dbTraveler.isEmpty()) {
      throw new ResourceNotFoundException(travelerId, ResourceType.TRAVELER);
    }
    
    Optional<Booking> booking = bookingDao.findById(bookingIdOfTraveler.longValue());
    if (booking.isEmpty()) {
      throw new ResourceNotFoundException(bookingIdOfTraveler, ResourceType.BOOKING);
    }
    if (booking.get().getTravelers().size() == 1) {
      throw new DeleteLastTravelerFromBookingException();
    }
    booking.get().getTravelers().remove(dbTraveler.get());
    bookingDao.save(booking.get());
		travelerDao.delete(dbTraveler.get());
	}

  @Transactional
  public void addTravelerToBooking(Traveler traveler, Booking booking) {
    travelerDao.save(traveler);
    booking.getTravelers().add(traveler);
    bookingDao.save(booking);
  }
}
