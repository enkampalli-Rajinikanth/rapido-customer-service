package com.rapido.customer.repositary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rapido.customer.entity.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

	// Idempotency check
	Optional<Booking> findByIdempotencyKey(String idempotencyKey);

      public List<Booking> findByCustomerId(int customerid);

	public List<Booking>  findByriderid(int riderid);

}
