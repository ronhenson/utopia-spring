package com.smoothstack.booking.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smoothstack.booking.dao.BookingDao;
import com.smoothstack.booking.dao.BookingsHasTravelersDao;
import com.smoothstack.booking.dao.FlightDao;
import com.smoothstack.booking.dao.TravelerDao;
import com.smoothstack.booking.entity.Booking;
import com.smoothstack.booking.entity.Flight;
import com.smoothstack.booking.entity.Traveler;
import com.smoothstack.booking.exceptions.ResourceExistsException;
import com.smoothstack.booking.exceptions.ResourceNotFoundException;
import com.smoothstack.constants.ResourceType;

@Service
public class BookingService {

	@Autowired
	BookingDao bookingDao;

	@Autowired
	FlightDao flightDao;

	@Autowired
	TravelerDao travelerDao;

	@Autowired
	BookingsHasTravelersDao bookingsHasTravelersDao;

	public Booking getBookingById(Long bookingId) throws ResourceNotFoundException {
		Optional<Booking> booking = bookingDao.findById(bookingId);
		if (booking.isEmpty()) {
			throw new ResourceNotFoundException(bookingId.intValue(), ResourceType.BOOKING);
		}
		return booking.get();
	}

	@Transactional
	public Booking createBooking(Booking booking)
			throws Exception {
		if(booking.getBookerId() == null) throw new Exception("Booker Id must not be empty");
		if(booking.getFlights().isEmpty()) throw new Exception("Flights can't be empty");
		if(booking.getStripeId() == null) throw new Exception("Stripe Id can't be empty");
		if(booking.getTravelers().isEmpty()) throw new Exception("Must have travelers to create booking");

		return bookingDao.save(booking);
	}

	public Booking updateBooking(Booking booking) throws ResourceNotFoundException {
		Optional<Booking> dbBooking = bookingDao.findById(booking.getBookingId().longValue());
		if (dbBooking.isEmpty()) {
			throw new ResourceNotFoundException(booking.getBookingId().intValue(), ResourceType.BOOKING);
		}
		Booking bookingToUpdate = dbBooking.get();
		bookingToUpdate.setIsActive(booking.getIsActive());
		bookingToUpdate.setStripeId(booking.getStripeId());
		return bookingDao.save(bookingToUpdate);
	}

	@Transactional
	public void deleteBooking(Integer bookingId) throws ResourceNotFoundException {
		Optional<Booking> dbBooking = bookingDao.findById(bookingId.longValue());
		if (dbBooking.isEmpty()) {
			throw new ResourceNotFoundException(bookingId, ResourceType.BOOKING);
		}
		bookingDao.delete(dbBooking.get());
	}

	public List<Booking> getAllBookings() {
		return bookingDao.findAll();
	}

	public List<Booking> getByUserId(Integer userId) {
		return bookingDao.findByBookerId(userId);
	}

	public Booking userOwnsBooking(Integer userId, Integer bookingId) {
		Optional<Booking> booking = bookingDao.findById(bookingId.longValue());
		if (booking.isEmpty()) {
			return null;
		}
		return booking.get().getBookerId() == userId ? booking.get() : null;
	}
}
