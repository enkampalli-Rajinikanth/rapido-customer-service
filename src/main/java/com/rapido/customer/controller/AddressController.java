package com.rapido.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.rapido.customer.sevice.AdddressService;

@RestController
public class AddressController {
	
	@Autowired
	private AdddressService service;
	

}
