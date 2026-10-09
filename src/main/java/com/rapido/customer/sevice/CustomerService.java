package com.rapido.customer.sevice;

import java.awt.print.Book;
import java.time.Duration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rapido.customer.dto.Bookingdto;
import com.rapido.customer.dto.Customerdto;
import com.rapido.customer.dto.ResponseStructure;
import com.rapido.customer.dto.ResponseStructure2;
import com.rapido.customer.dto.RideSelectionRedis;
import com.rapido.customer.dto.RiderBookingDto;
import com.rapido.customer.dto.SearchDestlocRespdto;
import com.rapido.customer.dto.Selectridedto;
import com.rapido.customer.entity.Booking;
import com.rapido.customer.entity.Cordinate;
import com.rapido.customer.entity.Customer;
import com.rapido.customer.exception.AddressNotFoundException;
import com.rapido.customer.exception.BookingNotFoundException;
import com.rapido.customer.exception.CustomerAlreadyPresentException;
import com.rapido.customer.exception.CustomerNotFoundException;
import com.rapido.customer.repositary.BookingRepo;
import com.rapido.customer.repositary.CordinateRepo;
import com.rapido.customer.repositary.CustomerRepo;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepo repo;

	public Customer createcustomer(Customerdto dto) {

		Customer c = new Customer();
		c.setEmail(dto.getEmail());
		c.setName(dto.getName());
		c.setMobile(dto.getMobile());
		c.setGender(dto.getGender());
		Customer save = repo.save(c);
		int otp = save.getId();
		int temp = otp;
		int count = 0;
		while (temp != 0) {
			temp = temp / 10;
			count++;
		}

		String otpValue = String.format("%04d", otp % 10000);
		c.setOtp(otpValue);

		repo.save(c);
		return c;

	}

	public ResponseStructure<Customer> delete(int id) {

		Customer customer = repo.findById(id).orElseThrow(() -> new CustomerNotFoundException());

		ResponseStructure<Customer> resp = new ResponseStructure<Customer>();
		resp.setStatuscode(HttpStatus.FOUND.value());
		resp.setMessage("Deleted Successfully");
		resp.setData(customer);
		repo.save(customer);
		repo.delete(customer);
		return resp;

	}

	public ResponseStructure<Customer> findbyid(int id) {
		Customer customer = repo.findById(id).orElseThrow(() -> new CustomerNotFoundException());
		ResponseStructure<Customer> resp = new ResponseStructure<Customer>();
		resp.setStatuscode(HttpStatus.FOUND.value());
		resp.setMessage("Customer Found");
		resp.setData(customer);
		return resp;

	}

	@Autowired
	private RestTemplate rest;

	public ResponseStructure<ArrayList<SearchDestlocRespdto>> findloc(String searchkey) {

		String url = "https://us1.locationiq.com/v1/search?key=pk.b448bbeae3949f8ed1caa2776fa04823&q=" + searchkey// geocoding
																													// giving
																													// name
																													// and
																													// getting
																													// loc
				+ "&format=json&";

		ArrayList<Object> list = rest.getForObject(url, ArrayList.class);

		ArrayList<SearchDestlocRespdto> searchloocdest = new ArrayList<>();

		for (Object obj : list) {

			Map<String, Object> map = (Map<String, Object>) obj;

			SearchDestlocRespdto result = new SearchDestlocRespdto();

			result.setAddress((String) map.get("display_name"));

			result.setLatitude(Double.parseDouble((String) map.get("lat")));

			result.setLongititude(Double.parseDouble((String) map.get("lon")));

			searchloocdest.add(result);
		}

		if (searchloocdest.isEmpty()) {
			throw new AddressNotFoundException();

		}
		ResponseStructure<ArrayList<SearchDestlocRespdto>> resp = new ResponseStructure<>();

		resp.setStatuscode(HttpStatus.OK.value());
		resp.setMessage("Matching addresses found");
		resp.setData(searchloocdest);

		return resp;
	}

	@Autowired
	private CordinateRepo cordrepo;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ObjectMapper mapper;

//selecting the ride and calculating the fare by latittude and longitude
	public ResponseStructure2 selectride(Selectridedto dto) {
		Cordinate source = new Cordinate();
		double sourcelatitude = dto.getSourceloc().getLatitude();
		double sourcelongitude = dto.getSourceloc().getLongitude();
		source.setLatitude(sourcelatitude);
		source.setLongitude(sourcelongitude);

		cordrepo.save(source);
		Cordinate dest = new Cordinate();
		double destlatitude = dto.getDestloc().getLatitude();
		double destlongitude = dto.getDestloc().getLongitude();
		dest.setLatitude(destlatitude);
		dest.setLongitude(destlongitude);
		cordrepo.save(dest);

		// Follow the order Source->longitude,latitude;destination->longitude,latitude

		String url = "https://us1.locationiq.com/v1/directions/driving/" + sourcelongitude + "," + sourcelatitude + ";"
				+ destlongitude + "," + destlatitude + "?key=pk.b448bbeae3949f8ed1caa2776fa04823&overview=false";

		double dist = findDistance(url);

		RestTemplate restTemplate = new RestTemplate();
		// ObjectMapper converts JSON into something Java can navigate.

//By giving these two urls we can SourceNAme and destname
		String sourceUrl = "https://us1.locationiq.com/v1/reverse?lat=" + sourcelatitude + "&lon=" + sourcelongitude
				+ "&format=json&key=pk.b448bbeae3949f8ed1caa2776fa04823";
		String destinationUrl = "https://us1.locationiq.com/v1/reverse?lat=" + destlatitude + "&lon=" + destlongitude
				+ "&format=json&key=pk.b448bbeae3949f8ed1caa2776fa04823";

		String sourceResponse = restTemplate.getForObject(sourceUrl, String.class);

		String destinationResponse = restTemplate.getForObject(destinationUrl, String.class);

		JsonNode sourceJson = mapper.readTree(sourceResponse);
		JsonNode destinationJson = mapper.readTree(destinationResponse);

		String sourceName = sourceJson.get("display_name").asText();// Find the JSON field called display_name.
																	// Convert that JSON value into a normal Java
																	// String.
		String destinationName = destinationJson.get("display_name").asText();

		ResponseStructure2 resp = new ResponseStructure2();

		resp.setSource(sourceName);
		resp.setDestination(destinationName);
		resp.setDistance(dist);

		HashMap<String, Double> fare = new HashMap<>();

//		Bike base fare = ₹20
//				Bike per KM    = ₹8
		double bikeFare = 20 + (dist * 8);
		double autoFare = 30 + (dist * 12);
		double cabFare = 50 + (dist * 18);

		fare.put("BIKE", bikeFare);
		fare.put("AUTO", autoFare);
		fare.put("CAB", cabFare);

		resp.setFare(fare);
		// storing into the redis
		// Store into Redis

		RideSelectionRedis selection = new RideSelectionRedis();

		selection.setCustomerId(dto.getCustomerid());
		selection.setDestination(dest);
		selection.setSource(source);
		selection.setSourceName(sourceName);
		selection.setDestinationname(destinationName);
		selection.setDistance(dist);
		selection.setFare(fare);

		String key = "ride:" + dto.getCustomerid();

		String json = mapper.writeValueAsString(selection);

		System.out.println("========== REDIS SAVE ==========");
		System.out.println("Redis Key   : " + key);
		System.out.println("Redis JSON  : " + json);

		redisTemplate.opsForValue().set(key, json);

		// Immediately read it back
		String storedValue = redisTemplate.opsForValue().get(key);

		System.out.println("Redis Stored Value : " + storedValue);
		System.out.println("================================");

		return resp;

	}

	/*
	 * 1. RestTemplate → GO to LocationIQ and GET the response
	 * 
	 * 2. ObjectMapper → Understand/parse the JSON response
	 * 
	 * 3. JsonNode → Navigate inside that JSON and get the value you want JsonNode
	 * lets you move through that structure to extract values such as distance and
	 * display_name. "The customer sends source and destination latitude and
	 * longitude. I extract those coordinates from the request DTO. I use the
	 * coordinates to call the LocationIQ Directions API and obtain the route
	 * distance in meters. I convert the distance into kilometers. I then use
	 * different base fares and per-kilometer rates for Bike, Auto and Cab to
	 * calculate the respective fares. I also use reverse geocoding to convert the
	 * source and destination coordinates into readable location names. Finally, I
	 * store the distance and the three calculated fares in a response DTO and
	 * return it to the client as JSON."
	 * 
	 * Coordinates → Distance is handled by the Directions API.
	 * 
	 * Coordinates → Human-readable address is handled by the Reverse Geocoding API.
	 * 
	 * Distance → Fare is handled by your own Java business logic.
	 *
	 * 
	 */

	public static double findDistance(String url) {
		ObjectMapper mapper = new ObjectMapper();

		RestTemplate restTemplate = new RestTemplate(); // root
		String response = restTemplate.getForObject(url, String.class); // ↓
		JsonNode root = mapper.readTree(response); // routes
													// ↓
													// first route
													// ↓
													// distance

		double distanceMeters = root.get("routes").get(0).get("distance").asDouble();// routes is an array get(0) means
																						// Give me the first
																						// route.

		double distanceKm = distanceMeters / 1000.0; // it return in meters to convert to Km

		System.out.println("Distance = " + distanceKm + " KM");
		return distanceKm;

	}

	@Autowired
	BookingRepo bookingrepo;

	@Autowired
	ObjectMapper objectMapper = new ObjectMapper();
	// implemnting the idempotency

	public ResponseStructure<Booking> confirmRide(int customerid, String vehicletype, String idempotencyid) {

		// 1. Find customer
		Customer cust = repo.findById(customerid).orElseThrow(() -> new CustomerNotFoundException());

		Optional<Booking> existingBooking = bookingrepo.findByIdempotencyKey(idempotencyid);

		if (existingBooking.isPresent()) {

			ResponseStructure<Booking> resp = new ResponseStructure<Booking>();
			resp.setStatuscode(HttpStatus.ALREADY_REPORTED.value());
			resp.setMessage("alreadyBooking is present");
			resp.setData(existingBooking.get());
			return resp;
		}

		// Always use uppercase because Redis keys are:
		// riders:BIKE
		// riders:AUTO
		String selectedVehicle = vehicletype.toUpperCase();

		// 2. Get customer's ride details from Redis
		String key = "ride:" + cust.getId();

		String redisJson = redisTemplate.opsForValue().get(key);

		if (redisJson == null) {
			throw new RuntimeException("Ride details not found in Redis for key: " + key);
		}

		System.out.println("Ride JSON: " + redisJson);

		// 3. Convert Redis JSON to JsonNode
		JsonNode root = objectMapper.readTree(redisJson);

		// 4. Get source coordinates
		double sourceLat = root.get("source").get("latitude").asDouble();

		double sourceLng = root.get("source").get("longitude").asDouble();

		// 5. Get destination coordinates
		double destinationLat = root.get("destination").get("latitude").asDouble();

		double destinationLng = root.get("destination").get("longitude").asDouble();

		// =====================================================
		// 6. FIND NEARBY RIDERS
		// =====================================================

		String geoKey = "riders:" + selectedVehicle;

		System.out.println("Searching Redis GEO key: " + geoKey);

		GeoRadiusCommandArgs args = GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance();

		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(geoKey,
				new Circle(new Point(sourceLng, sourceLat), new Distance(5, Metrics.KILOMETERS)// within 5k radiud
				), args);
		// =====================================================
		// 7. GET NEARBY RIDER IDs
		// =====================================================

		List<String> nearbyRiderIds = new ArrayList<>();

		for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {

			String riderKey = result.getContent().getName();

			nearbyRiderIds.add(riderKey);

			System.out.println("Nearby Rider: " + riderKey + " Distance: " + result.getDistance());
		}

		System.out.println("Nearby riders within 5 KM: " + nearbyRiderIds);

		// =====================================================
		// 8. CREATE BOOKING
		// =====================================================

		Booking booking = new Booking();
		booking.setRideStatus("Pending");

		booking.setCustomer(cust);

		// =====================================================
		// 9. GET SOURCE / DESTINATION NAME
		// =====================================================

		RestTemplate restTemplate = new RestTemplate();

		String sourceUrl = "https://us1.locationiq.com/v1/reverse?lat=" + sourceLat + "&lon=" + sourceLng
				+ "&format=json" + "&key=pk.b448bbeae3949f8ed1caa2776fa04823";

		String destinationUrl = "https://us1.locationiq.com/v1/reverse?lat=" + destinationLat + "&lon=" + destinationLng
				+ "&format=json" + "&key=pk.b448bbeae3949f8ed1caa2776fa04823";

		String sourceResponse = restTemplate.getForObject(sourceUrl, String.class);

		String destinationResponse = restTemplate.getForObject(destinationUrl, String.class);

		JsonNode sourceJson = objectMapper.readTree(sourceResponse);

		JsonNode destinationJson = objectMapper.readTree(destinationResponse);

		String sourceName = sourceJson.get("display_name").asText();

		String destinationName = destinationJson.get("display_name").asText();

		// =====================================================
		// 10. SET BOOKING DETAILS
		// =====================================================
		// idempoetncy
		booking.setIdempotencyKey(idempotencyid);

		booking.setPickuploc(sourceName);

		booking.setDestinationloc(destinationName);

		booking.setPaymenttype("pending");

		booking.setVehicletype(selectedVehicle);

		booking.setBookingdate(LocalDate.now().toString());

		booking.setBookingtime(LocalDateTime.now().toString());
		// finding dist
		String url = "https://us1.locationiq.com/v1/directions/driving/" + sourceLng + "," + sourceLat + ";"
				+ destinationLng + "," + destinationLat + "?key=pk.b448bbeae3949f8ed1caa2776fa04823&overview=false";

		double dist = findDistance(url);
		booking.setDistance(dist);

		// =====================================================
		// 11. GET FARE
		// =====================================================

		JsonNode fareNode = root.get("fare");

		double fare = fareNode.get(selectedVehicle).asDouble();

		booking.setFare(fare);

		// =====================================================
		// 12. SAVE SOURCE COORDINATE
		// =====================================================

		Cordinate sourcecordinate = new Cordinate();

		sourcecordinate.setLatitude(sourceLat);

		sourcecordinate.setLongitude(sourceLng);

		cordrepo.save(sourcecordinate);

		booking.setSourcecord(sourcecordinate);

		// =====================================================
		// 13. SAVE DESTINATION COORDINATE
		// =====================================================

		Cordinate destcordinate = new Cordinate();

		destcordinate.setLatitude(destinationLat);

		destcordinate.setLongitude(destinationLng);

		cordrepo.save(destcordinate);

		booking.setDestinationcord(destcordinate);
		bookingRepo.save(booking);

		// =====================================================
		// 14. SAVE BOOKING FIRST
		// =====================================================

		System.out.println("Booking created with ID: " + booking.getId());

		// =====================================================
		// 15. SEND BOOKING REQUEST TO EVERY NEARBY RIDER
		// =====================================================

		for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {

			String riderKey = result.getContent().getName();

			int riderId = Integer.parseInt(riderKey.replace("rider:", ""));

			double distance = result.getDistance().getValue();

//			bookingrepo.save(booking);

			System.out.println("Rider: " + riderId);
			System.out.println("Distance: " + distance);
			System.out.println("Metric: " + result.getDistance().getMetric());

			RiderBookingDto dto = new RiderBookingDto();

			dto.setBookingId(booking.getId());
			dto.setFare(fare);
			dto.setDistance(distance);

			String json = objectMapper.writeValueAsString(dto);

			String riderBookingKey = "booking:rider:" + riderId;

			redisTemplate.opsForValue().set(riderBookingKey, json);

			System.out.println("FINAL JSON = " + json);
		}
		// =====================================================
		// 16. DELETE CUSTOMER RIDE DATA
		// =====================================================

		Boolean deleted = redisTemplate.delete(key);

		System.out.println("Redis ride key deleted: " + deleted);

		// =====================================================
		// 17. RESPONSE
		// =====================================================

		ResponseStructure<Booking> resp = new ResponseStructure<>();

		resp.setStatuscode(HttpStatus.ACCEPTED.value());

		resp.setMessage("Ride booked successfully. " + "Nearby riders: " + nearbyRiderIds);

		resp.setData(booking);

		return resp;
	}

	@Autowired
	private BookingRepo bookingRepo;

	public Bookingdto getDetforAcceptBooking(int bookingid, String idempotencyid) {
		Booking b = bookingrepo.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		Bookingdto dto = new Bookingdto();
		dto.setIdempotencyid(idempotencyid);
		dto.setRideStatus(b.getRideStatus());

		return dto;
	}

	public String acceptBooking(int bookingid, int riderid) {

		Booking booking = bookingRepo.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());

		// Check whether another rider already accepted
		if (!"Pending".equalsIgnoreCase(booking.getRideStatus())) {
			return "Ride already accepted by another rider";
		}

		booking.setRiderid(riderid);
		booking.setRideStatus("RiderAssigned");

		bookingRepo.save(booking);

		return "RiderAssigned";
	}

	public Bookingdto moveTowardsPickup(@PathVariable int bookingid) {

		Booking booking = bookingrepo.findById(bookingid)
				.orElseThrow(() -> new com.rapido.customer.exception.BookingNotFoundException());

		Bookingdto dto = new Bookingdto();

		dto.setBookingid(booking.getId());

		dto.setFare(booking.getFare());
		dto.setDistance(booking.getDistance());

		dto.setSourceLatitude(booking.getSourcecord().getLatitude());

		dto.setSourceLongitude(booking.getSourcecord().getLongitude());

		dto.setDestinationLatitude(booking.getDestinationcord().getLatitude());

		dto.setDestinationLongitude(booking.getDestinationcord().getLongitude());

		return dto;
	}

	public void updateArrival(int bookingid) {
		Booking b = bookingrepo.findById(bookingid)
				.orElseThrow(() -> new com.rapido.customer.exception.BookingNotFoundException());
		b.setRideStatus("RiderArrived");
		bookingRepo.save(b);

	}

	public String validateOtp(int bookingid) {
		Booking booking = bookingrepo.findById(bookingid)
				.orElseThrow(() -> new com.rapido.customer.exception.BookingNotFoundException());
		Customer cust = booking.getCustomer();
		return cust.getOtp();

	}

	public Booking updateBookingStarted(int bookingid) {

		Booking booking = bookingrepo.findById(bookingid)
				.orElseThrow(() -> new com.rapido.customer.exception.BookingNotFoundException());
		booking.setRideStatus("RideStarted");
		booking.setPickuptime(LocalDateTime.now().toString());
		bookingRepo.save(booking);
		return booking;
	}

	public Bookingdto MoveTowardsDestination(int bookingid) {
		Booking booking = bookingrepo.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		Bookingdto dto = new Bookingdto();

		dto.setBookingid(booking.getId());

		dto.setFare(booking.getFare());
		dto.setDistance(booking.getDistance());

		dto.setSourceLatitude(booking.getSourcecord().getLatitude());

		dto.setSourceLongitude(booking.getSourcecord().getLongitude());

		dto.setDestinationLatitude(booking.getDestinationcord().getLatitude());

		dto.setDestinationLongitude(booking.getDestinationcord().getLongitude());

		return dto;
	}

	public Bookingdto completeRide(int bookingid) {
		Booking booking = bookingrepo.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		booking.setRideStatus("completed");
		booking.setPaymenttype("Online");
		booking.setPaymentStatus("Paid");
		booking.setDroptime("06-10-2026 06:48 PM");
		double fare = booking.getFare();
		booking.setPlatformShare(fare * 0.10);
		booking.setRiderShare(fare * 0.90);
		bookingrepo.save(booking);
		Bookingdto dto = new Bookingdto();

		dto.setBookingid(booking.getId());
		dto.setRiderid(booking.getRiderid());

		dto.setFare(booking.getFare());
		dto.setDistance(booking.getDistance());

		dto.setSourceLatitude(booking.getSourcecord().getLatitude());

		dto.setSourceLongitude(booking.getSourcecord().getLongitude());

		dto.setDestinationLatitude(booking.getDestinationcord().getLatitude());

		dto.setDestinationLongitude(booking.getDestinationcord().getLongitude());
		return dto;

	}

	public ResponseStructure<String> cancelRide(int bookingid) {
		Booking booking = bookingrepo.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		ResponseStructure<String> resp = new ResponseStructure<String>();
		if ((booking.getRideStatus().equals("RiderArrived")) || (booking.getRideStatus().equals("RiderAssigned"))) {
			booking.setRideStatus("CancelledByCustomer");
			bookingrepo.save(booking);

			resp.setStatuscode(HttpStatus.LOCKED.value());
			resp.setMessage("Ride Cancelled Successfully");
			resp.setData(bookingid + " cancelledBycustomer");
			return resp;
		} else {
			resp.setStatuscode(HttpStatus.LOCKED.value());
			resp.setMessage("Ride Cannot be Cancelled");
			resp.setData(bookingid + " NotCancelled");
			return resp;
		}

	}

	public ResponseStructure<String> getcancelRideByrider(int bookingid) {
		Booking booking = bookingrepo.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
		ResponseStructure<String> resp = new ResponseStructure<String>();
		if ((booking.getRideStatus().equalsIgnoreCase("completed"))
				|| (booking.getRideStatus().equalsIgnoreCase("Cancelled"))
				|| (booking.getRideStatus().equalsIgnoreCase("CancelledByRider"))) {
			resp.setStatuscode(HttpStatus.ACCEPTED.value());
			resp.setMessage("CanNot Cancel The Ride");
			resp.setData(bookingid + " Not cancelled");
			return resp;
		}

		booking.setRideStatus("CancelledByRider");

		bookingrepo.save(booking);

		resp.setStatuscode(HttpStatus.ACCEPTED.value());
		resp.setMessage("Ride Cancelled By Rider");
		resp.setData(bookingid + " cancelledByRider");
		return resp;
	}

	public List<Booking> rideHistory(int customerid, String rideStatus) {

		Customer cust = repo.findById(customerid).orElseThrow(() -> new CustomerNotFoundException());

		List<Booking> bookings = bookingrepo.findByCustomerId(customerid);

		ArrayList<Booking> customerBookings = new ArrayList<>();

		for (Booking booking : bookings) {

			if (rideStatus.equalsIgnoreCase("all")) {
				customerBookings.add(booking);

			} else if (booking.getRideStatus().equalsIgnoreCase(rideStatus)) {
				customerBookings.add(booking);
			}
		}

		return customerBookings;

	}

	public ArrayList<Bookingdto> rideHistoryOfRider(int riderid) {
		List<Booking> book = bookingrepo.findByriderid(riderid);
		ArrayList<Bookingdto> dto = new ArrayList<Bookingdto>();

		for (Booking ele : book) {
			Bookingdto booking = new Bookingdto();
			booking.setBookingid(ele.getId());
			booking.setRiderid(ele.getRiderid());
			booking.setRideStatus(ele.getRideStatus());
			dto.add(booking);

		}
		return dto;

	}

	public ArrayList<Bookingdto> getriderSummary(int riderid) {
		List<Booking> book = bookingrepo.findByriderid(riderid);
		ArrayList<Bookingdto> dto = new ArrayList<Bookingdto>();

		for (Booking ele : book) {
			Bookingdto booking = new Bookingdto();
			booking.setBookingid(ele.getId());
			booking.setRiderid(ele.getRiderid());
			booking.setRideStatus(ele.getRideStatus());
			booking.setFare(ele.getFare());
			booking.setDistance(ele.getDistance());
			booking.setRidershare(ele.getRiderShare());
			booking.setBookingdate(ele.getBookingdate());
			dto.add(booking);

		}
		return dto;

	}

}