package com.rapido.customer.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rapido.customer.repositary.BookingRepo;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepo repo;

}
