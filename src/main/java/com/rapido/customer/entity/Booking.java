package com.rapido.customer.entity;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	private Customer customer;
	
	private String pickuploc;
	
	private String destinationloc;
	@OneToOne(cascade = CascadeType.ALL)
	private Cordinate  sourcecord;
	@OneToOne(cascade = CascadeType.ALL)
	private Cordinate destinationcord;
	private String paymenttype;
	
	private String vehicletype;
	private int riderid;
   private String bookingdate;
   private String bookingtime;
   private String pickuptime;
   private  String droptime;
   private double fare;
   private String rideStatus;
   private String paymentStatus;
   private double distance;
   private double riderShare;
   private double platformShare;
   //idempotency

   @Column(unique = true, nullable = false)
   private String idempotencyKey;

  

   public Booking(int id, Customer customer, String pickuploc, String destinationloc, Cordinate sourcecord,
		Cordinate destinationcord, String paymenttype, String vehicletype, int riderid, String bookingdate,
		String bookingtime, String pickuptime, String droptime, double fare, String rideStatus, String paymentStatus,
		double distance, double riderShare, double platformShare, String idempotencyKey) {
	super();
	this.id = id;
	this.customer = customer;
	this.pickuploc = pickuploc;
	this.destinationloc = destinationloc;
	this.sourcecord = sourcecord;
	this.destinationcord = destinationcord;
	this.paymenttype = paymenttype;
	this.vehicletype = vehicletype;
	this.riderid = riderid;
	this.bookingdate = bookingdate;
	this.bookingtime = bookingtime;
	this.pickuptime = pickuptime;
	this.droptime = droptime;
	this.fare = fare;
	this.rideStatus = rideStatus;
	this.paymentStatus = paymentStatus;
	this.distance = distance;
	this.riderShare = riderShare;
	this.platformShare = platformShare;
	this.idempotencyKey = idempotencyKey;
}

   public Booking() {
	super();
   }

   public int getId() {
	return id;
   }

   public void setId(int id) {
	this.id = id;
   }

   public Customer getCustomer() {
	return customer;
   }

   public void setCustomer(Customer customer) {
	this.customer = customer;
   }

   public String getPickuploc() {
	return pickuploc;
   }

   public void setPickuploc(String pickuploc) {
	this.pickuploc = pickuploc;
   }

   public String getDestinationloc() {
	return destinationloc;
   }

   public void setDestinationloc(String destinationloc) {
	this.destinationloc = destinationloc;
   }

   public Cordinate getSourcecord() {
	return sourcecord;
   }

   public void setSourcecord(Cordinate sourcecord) {
	this.sourcecord = sourcecord;
   }

   public Cordinate getDestinationcord() {
	return destinationcord;
   }

   public void setDestinationcord(Cordinate destinationcord) {
	this.destinationcord = destinationcord;
   }

   public String getPaymenttype() {
	return paymenttype;
   }

   public void setPaymenttype(String paymenttype) {
	this.paymenttype = paymenttype;
   }

   public String getVehicletype() {
	return vehicletype;
   }

   public void setVehicletype(String vehicletype) {
	this.vehicletype = vehicletype;
   }

   public int getRiderid() {
	return riderid;
   }

   public void setRiderid(int riderid) {
	this.riderid = riderid;
   }

   public String getBookingdate() {
	return bookingdate;
   }

   public void setBookingdate(String bookingdate) {
	this.bookingdate = bookingdate;
   }

   public String getBookingtime() {
	return bookingtime;
   }

   public void setBookingtime(String bookingtime) {
	this.bookingtime = bookingtime;
   }

   public String getPickuptime() {
	return pickuptime;
   }

   public void setPickuptime(String pickuptime) {
	this.pickuptime = pickuptime;
   }

   public String getDroptime() {
	return droptime;
   }

   public void setDroptime(String droptime) {
	this.droptime = droptime;
   }

   public double getFare() {
	return fare;
   }

   public void setFare(double fare) {
	this.fare = fare;
   }

   public String getRideStatus() {
	return rideStatus;
   }

   public void setRideStatus(String rideStatus) {
	this.rideStatus = rideStatus;
   }

   public String getPaymentStatus() {
	return paymentStatus;
   }

   public void setPaymentStatus(String paymentStatus) {
	this.paymentStatus = paymentStatus;
   }

   public double getDistance() {
	return distance;
   }

   public void setDistance(double distance) {
	this.distance = distance;
   }

   public String getIdempotencyKey() {
	return idempotencyKey;
   }

   public void setIdempotencyKey(String idempotencyKey) {
	this.idempotencyKey = idempotencyKey;
   }

   public double getRiderShare() {
	return riderShare;
   }

   public void setRiderShare(double riderShare) {
	this.riderShare = riderShare;
   }

   public double getPlatformShare() {
	return platformShare;
   }

   public void setPlatformShare(double platformShare) {
	this.platformShare = platformShare;
   }

}