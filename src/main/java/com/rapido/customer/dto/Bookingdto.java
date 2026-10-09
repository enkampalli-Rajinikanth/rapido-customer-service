package com.rapido.customer.dto;

import com.rapido.customer.entity.Address;
import com.rapido.customer.entity.Customer;

public class Bookingdto {

	private int bookingid;
	private int riderid;
	private double fare;
	private double distance;
	private String rideStatus;
	

	private double sourceLatitude;
	private double sourceLongitude;
	private String idempotencyid;

	private double destinationLatitude;
	private double destinationLongitude;
	private double ridershare;
	private double platformshare;
private String bookingdate;

	

	
	public Bookingdto(int bookingid, int riderid, double fare, double distance, String rideStatus, double sourceLatitude,
		double sourceLongitude, String idempotencyid, double destinationLatitude, double destinationLongitude,
		double ridershare, double platformshare, String bookingdate) {
	super();
	this.bookingid = bookingid;
	this.riderid = riderid;
	this.fare = fare;
	this.distance = distance;
	this.rideStatus = rideStatus;
	this.sourceLatitude = sourceLatitude;
	this.sourceLongitude = sourceLongitude;
	this.idempotencyid = idempotencyid;
	this.destinationLatitude = destinationLatitude;
	this.destinationLongitude = destinationLongitude;
	this.ridershare = ridershare;
	this.platformshare = platformshare;
	this.bookingdate = bookingdate;
}

	public Bookingdto() {
		super();
	}

	public int getBookingid() {
		return bookingid;
	}

	public void setBookingid(int bookingid) {
		this.bookingid = bookingid;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getSourceLatitude() {
		return sourceLatitude;
	}

	public void setSourceLatitude(double sourceLatitude) {
		this.sourceLatitude = sourceLatitude;
	}

	public double getSourceLongitude() {
		return sourceLongitude;
	}

	public void setSourceLongitude(double sourceLongitude) {
		this.sourceLongitude = sourceLongitude;
	}

	public double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	public int getRiderid() {
		return riderid;
	}

	public void setRiderid(int riderid) {
		this.riderid = riderid;
	}

	public String getRideStatus() {
		return rideStatus;
	}

	public void setRideStatus(String rideStatus) {
		this.rideStatus = rideStatus;
	}

	public String getIdempotencyid() {
		return idempotencyid;
	}

	public void setIdempotencyid(String idempotencyid) {
		this.idempotencyid = idempotencyid;
	}

	public double getRidershare() {
		return ridershare;
	}

	public void setRidershare(double ridershare) {
		this.ridershare = ridershare;
	}

	public double getPlatformshare() {
		return platformshare;
	}

	public void setPlatformshare(double platformshare) {
		this.platformshare = platformshare;
	}

	public String getBookingdate() {
		return bookingdate;
	}

	public void setBookingdate(String bookingdate) {
		this.bookingdate = bookingdate;
	}
	

}