package com.rapido.customer.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rapido.customer.repositary.AddressRepo;

@Service
public class AdddressService {
	
	@Autowired
	private AddressRepo repo;

	
}
