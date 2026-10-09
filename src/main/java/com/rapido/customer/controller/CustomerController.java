package com.rapido.customer.controller;

import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import javax.print.attribute.standard.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rapido.customer.dto.Bookingdto;
import com.rapido.customer.dto.Customerdto;
import com.rapido.customer.dto.ResponseStructure;
import com.rapido.customer.dto.ResponseStructure2;
import com.rapido.customer.dto.RideFareDto;
import com.rapido.customer.dto.SearchDestlocRespdto;
import com.rapido.customer.dto.Selectridedto;
import com.rapido.customer.entity.Booking;
import com.rapido.customer.entity.Customer;
import com.rapido.customer.repositary.BookingRepo;
import com.rapido.customer.repositary.CustomerRepo;
import com.rapido.customer.sevice.CustomerService;

import tools.jackson.databind.JsonNode;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService service;

	@PostMapping("/customer/createacc")
	public Customer createcustomer(@RequestBody Customerdto dto) {
		return service.createcustomer(dto);
	}

	@DeleteMapping("/customer/deleteAcc")
	public ResponseStructure<Customer> deleteAcc(@RequestParam int id) {
		return service.delete(id);
	}

	@GetMapping("/customer/find")
	public ResponseStructure<Customer> findcust(@RequestParam int id) {
		return service.findbyid(id);
	}

	@GetMapping("/customer/search/droploc")
	public ResponseStructure<ArrayList<SearchDestlocRespdto>> searchdroploc(@RequestParam String searchkey) {
		return service.findloc(searchkey);
	}

	@PostMapping("/customer/selectride")
	public ResponseStructure2 selectRide(@RequestBody Selectridedto dto) {
		return service.selectride(dto);
	}

	@Autowired
	private StringRedisTemplate redisTemplate;

	@GetMapping("/redis-test")
	public String redisTest() {

		redisTemplate.opsForValue().set("message", "Hello Redis");

		return redisTemplate.opsForValue().get("message");
	}
	// implementing the idempotency

	@PostMapping("/customer/confirmride")
	public ResponseStructure<Booking> confirmRide(@RequestParam int customerid, @RequestParam String vehicletype,
			@RequestParam String idempotencyid) {
		return service.confirmRide(customerid, vehicletype, idempotencyid);
	}

//implementing Idempotency
	@PutMapping("/customer/booking/accept/{bookingid}/{riderid}")
	public String acceptBooking(@PathVariable int bookingid, @PathVariable int riderid) {

		return service.acceptBooking(bookingid, riderid);
	}

	@GetMapping("/customer/booking/getAcceptDet/{bookingid}")
	public Bookingdto getdetforAcceptingRide(@PathVariable int bookingid, @PathVariable String idempotencyid) {

		return service.getDetforAcceptBooking(bookingid, idempotencyid);
	}

	@GetMapping("/customer/booking/{bookingid}")
	public Bookingdto moveTowardPickup(@PathVariable int bookingid) {
		return service.moveTowardsPickup(bookingid);
	}

	@PutMapping("/customer/updateBooking/arrival/{bookingid}")
	public void updateArrival(@PathVariable int bookingid) {
		service.updateArrival(bookingid);

	}

	@GetMapping("/customer/validateotp/{bookingid}")
	public String validateOtp(@PathVariable int bookingid) {
		return service.validateOtp(bookingid);
	}

	@PutMapping("/customer/bookingRidestarted/{bookingid}")
	public Booking updateBookingStarted(@PathVariable int bookingid) {
		return service.updateBookingStarted(bookingid);
	}

	@GetMapping("/customer/getdestination/{bookingid}")
	public Bookingdto MoveTowardsDestination(@PathVariable int bookingid) {
		return service.MoveTowardsDestination(bookingid);
	}

	@GetMapping("/customer/completeride/{bookingid}")
	public Bookingdto CompleteRide(@PathVariable int bookingid) {
		return service.completeRide(bookingid);
	}

	@PostMapping("/customer/cancelRide")
	public ResponseStructure<String> cancelRide(@RequestParam int bookingid) {
		return service.cancelRide(bookingid);
	}

	@GetMapping("/customer/getCancelrideInfo/{bookingid}")
	public ResponseStructure<String> getCancleRideByrider(@PathVariable int bookingid) {
		return service.getcancelRideByrider(bookingid);
	}
	@GetMapping("/customer/ridehistory")
	public List<Booking> rideHistory(@RequestParam int customerid,@RequestParam String rideStatus) { //completed/cancelled/All
		 return service.rideHistory(customerid,rideStatus);
		
	}
	@GetMapping("/customer/getRidestatus/{riderid}")
	public ArrayList<Bookingdto> rideHistoryofRider(@PathVariable int riderid) {
	return service.rideHistoryOfRider(riderid);
	}
	@GetMapping("/customer/getriderSummary/{riderid}")
	public ArrayList<Bookingdto> getriderSummary(@PathVariable int riderid) {
		 return service.getriderSummary(riderid);
	}

}
