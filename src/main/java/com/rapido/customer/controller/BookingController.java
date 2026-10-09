package com.rapido.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.rapido.customer.sevice.BookingService;

@RestController
public class BookingController {
	@Autowired
	private BookingService service;
	

}
