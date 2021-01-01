package com.smoothstack.utopia.service;

import com.smoothstack.utopia.dao.BookingDao;
import com.smoothstack.utopia.entity.Booking;
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
