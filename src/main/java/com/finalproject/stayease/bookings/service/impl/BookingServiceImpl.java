package com.finalproject.stayease.bookings.service.impl;

import com.finalproject.stayease.bookings.dto.BookingItemReqDto;
import com.finalproject.stayease.bookings.dto.BookingReqDto;
import com.finalproject.stayease.bookings.dto.BookingRequestReqDto;
import com.finalproject.stayease.bookings.entity.Booking;
import com.finalproject.stayease.bookings.entity.BookingItem;
import com.finalproject.stayease.bookings.entity.BookingRequest;
import com.finalproject.stayease.bookings.repository.BookingItemRepository;
import com.finalproject.stayease.bookings.repository.BookingRepository;
import com.finalproject.stayease.bookings.repository.BookingRequestRepository;
import com.finalproject.stayease.bookings.service.BookingService;
import com.finalproject.stayease.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingItemRepository bookingItemRepository;
    private final BookingRequestRepository bookingRequestRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingItemRepository bookingItemRepository, BookingRequestRepository bookingRequestRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingItemRepository = bookingItemRepository;
        this.bookingRequestRepository = bookingRequestRepository;
    }

    @Override
    public Booking createBooking(BookingReqDto reqDto, Long userId) {
        Booking newBooking = new Booking();
        // TO DO: Search and validate user

//        newBooking.setUserId(reqDto.getUserId());
        newBooking.setTotalPrice(reqDto.getPrice());
        newBooking.setStatus("In progress");

        createBookingItem(reqDto.getBookingItem(), newBooking);

        if (reqDto.getBookingRequest() != null) {
            createBookingRequest(reqDto.getBookingRequest(), newBooking);
        }

        return bookingRepository.save(newBooking);
    }

    @Override
    public void createBookingItem(BookingItemReqDto bookingItemDto, Booking newBooking) {
        BookingItem bookingItem = new BookingItem();
        bookingItem.setBookingId(newBooking.getId());
        bookingItem.setRoomId(bookingItemDto.getRoomId());
        bookingItem.setCheckInDate(bookingItemDto.getCheckInDate());
        bookingItem.setCheckOutDate(bookingItemDto.getCheckOutDate());
        bookingItem.setPrice(bookingItemDto.getPrice());
        bookingItem.setTotalAdults(bookingItemDto.getTotalAdults());
        bookingItem.setTotalChildren(bookingItemDto.getTotalChildren());
        bookingItem.setTotalInfants(bookingItemDto.getTotalInfants());

        bookingItemRepository.save(bookingItem);
    }

    @Override
    public void createBookingRequest(BookingRequestReqDto reqDto, Booking newBooking) {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setBooking(newBooking);
        if (reqDto.getCheckInTime() != null) {
            bookingRequest.setCheckInTime(reqDto.getCheckInTime());
        }
        if (reqDto.getCheckOutTime() != null) {
            bookingRequest.setCheckOutTime(reqDto.getCheckOutTime());
        }
        bookingRequest.setNonSmoking(reqDto.isNonSmoking());
        if (reqDto.getOther() != null) {
            bookingRequest.setOther(reqDto.getOther());
        }
        bookingRequestRepository.save(bookingRequest);
    }

    @Override
    public Booking getBookingDetail(Long bookingId) {
        return bookingRepository.findById(bookingId).
                orElseThrow(() -> new DataNotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        // TO DO: find and validate user
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking updateBooking(Long bookingId) {
        return null;
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Booking booking = getBookingDetail(bookingId);
        booking.preRemove();
        bookingRepository.save(booking);
    }
}
