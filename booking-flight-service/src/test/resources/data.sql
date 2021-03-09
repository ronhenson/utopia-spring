insert into tbl_users (
  userId,
  password,
  email,
  firstName,
  lastName,
  enabled,
  locked,
  userRole
)
values
(1, 'password', 'user@1.com', 'user', 'one', 1, 0, 0),
(2, 'password', 'user@2.com', 'user', 'two', 1, 0, 0),
(3, 'password', 'user@3.com', 'user', 'three', 1, 0, 0);

insert into tbl_booking (bookingId, isActive, stripeId, bookerId)
values

(1, 1, 'payment', 1),
(2, 1, 'payment', 2);


insert into tbl_flight_details (flightNumber, departCityId, arriveCityId)
values
('flt1', 'LAX', 'JFK');

insert into tbl_flight (flightId, departTime, seatsAvailable, price, arrivalTime, flightNumber)
values
(1, '1990-01-01', 300, 200, '1990-01-01', 'flt1');

insert into tbl_traveler (address, dob, email, name, phone, travelerId)
values
('123 Example Rd', '1990-01-01', 'test@gmail.com', 'traveler', '123456789', 1);
