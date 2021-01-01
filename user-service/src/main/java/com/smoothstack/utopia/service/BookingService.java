package com.ss.uthopia.service;

import com.ss.uthopia.dao.BookingDao;
import com.ss.uthopia.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    private BookingDao bookingDao;

    public BookingService(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    public Optional<Booking> findById(long id) {
        return this.bookingDao.findById(id);
    }
}
