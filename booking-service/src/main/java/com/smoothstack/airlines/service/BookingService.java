package com.smoothstack.airlines.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smoothstack.airlines.dao.BookingDao;
import com.smoothstack.airlines.dao.BookingsHasTravelersDao;
import com.smoothstack.airlines.dao.FlightDao;
import com.smoothstack.airlines.dao.TravelerDao;
import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.entity.Flight;
import com.smoothstack.airlines.entity.Traveler;
import com.smoothstack.airlines.exceptions.ResourceExistsException;
import com.smoothstack.airlines.exceptions.ResourceNotFoundException;
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
	public Booking createBooking(Booking booking, Long flightId, List<Integer> travelerIds)
			throws ResourceExistsException, ResourceNotFoundException {
		

		Optional<Flight> flight = flightDao.findById(flightId);
		if (flight.isEmpty()) {
			throw new ResourceNotFoundException(flightId.intValue(), ResourceType.FLIGHT);
		}

		if (travelerIds != null) {
			Set<Traveler> travelers = travelerIds.stream().map(id -> {
				Optional<Traveler> traveler = travelerDao.findById(id);
				if (traveler.isEmpty()) {
					throw new ResourceNotFoundException(id, ResourceType.TRAVELER);
				}
	
				return traveler.get();
			}).collect(Collectors.toSet());
			booking.setTravelers(travelers);
		}
		booking.setFlight(flight.get());
		return bookingDao.save(booking);
	}

	public void updateBooking(Booking booking) throws ResourceNotFoundException {
		Optional<Booking> dbBooking = bookingDao.findById(booking.getBookingId().longValue());
		if (dbBooking.isEmpty()) {
			throw new ResourceNotFoundException(booking.getBookingId().intValue(), ResourceType.BOOKING);
		}
		Booking bookingToUpdate = dbBooking.get();
		bookingToUpdate.setIsActive(booking.getIsActive());
		bookingToUpdate.setStripeId(booking.getStripeId());
		bookingToUpdate.setBookerId(booking.getBookerId());
		bookingDao.save(bookingToUpdate);
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
}
