package com.rapido.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rapido.customer.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalException {
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseStructure<String> handleCustomer() {
		ResponseStructure<String> resp = new ResponseStructure<String>();
		resp.setStatuscode(HttpStatus.NOT_FOUND.value());
		resp.setMessage("Customer Notfound");
		resp.setData("Customer Not found");
		return resp;

	}

	@ExceptionHandler(AddressNotFoundException.class)
	public ResponseStructure<String> handleAddress() {
		ResponseStructure<String> resp = new ResponseStructure<String>();
		resp.setStatuscode(HttpStatus.NOT_FOUND.value());
		resp.setMessage("Addresss Notfound");
		resp.setData("Address Not found");
		return resp;

	}

	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseStructure<String> handleBoooking() {
		ResponseStructure<String> resp = new ResponseStructure<String>();
		resp.setStatuscode(HttpStatus.NOT_FOUND.value());
		resp.setMessage("Booking Not found");
		resp.setData("Bookings Not found");
		return resp;

	}

	@ExceptionHandler(CustomerAlreadyPresentException.class)
	public ResponseStructure<String> handlecustomercreationExcpe() {
		ResponseStructure<String> resp = new ResponseStructure<String>();
		resp.setStatuscode(HttpStatus.NOT_FOUND.value());
		resp.setMessage("Customer with id already present");
		resp.setData("Change the id");
		return resp;

	}

	

}
