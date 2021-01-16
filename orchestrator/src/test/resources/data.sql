insert into tbl_airport (
  iataIdent, city, name
)
values
  ('JFK', 'Queens', 'JFK Airport'),
  ('LAX', 'Los Angeles', 'Los Angeles International');



insert into tbl_users (
  userId, email, password, firstName, lastName, enabled, locked, userRole
)
-- password is 'password' in hashed form
values
  (1, 'owns@no.bookings', '$2a$10$znSiaMyRsXdscYcVqR6djOzVJGCMgLVMS80/gUqIDt0e2zTkV/O9m', 'Derek', 'Lance', 1, 0, 1),
  (2, 'owns@booking.com', '$2a$10$znSiaMyRsXdscYcVqR6djOzVJGCMgLVMS80/gUqIDt0e2zTkV/O9m', 'Has', 'Bookings', 1, 0, 1),
  (3, 'employee@no.bookings', '$2a$10$znSiaMyRsXdscYcVqR6djOzVJGCMgLVMS80/gUqIDt0e2zTkV/O9m', 'Derek', 'Lance', 1, 0, 2);



insert into tbl_flight_details (
  flightNumber, departCityId, arriveCityId
)
values
  ('1234', 'JFK', 'LAX');



insert into tbl_flight (
  flightId, departTime, seatsAvailable, price, arrivalTime, flightNumber
)
values
  (2, '2020-12-25', 2, 100, '2020-12-26', '1234');



insert into tbl_traveler (
  travelerId, name, address, phone, email, dob
)
values
  (3, 'Derek L', '123 First St', '805-999-9999', 'test@example.com', '1996-01-12');



insert into tbl_booking (
  bookingId, isActive, stripeId, bookerId
)
values
  (4, 1, 'confirmation of payment', 2);