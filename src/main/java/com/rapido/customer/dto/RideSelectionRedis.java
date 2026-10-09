package com.rapido.customer.dto;


import java.util.Map;

import com.rapido.customer.entity.Cordinate;

public class RideSelectionRedis {

	private int customerId;

	private Cordinate source;
	private Cordinate destination;
	private String sourceName;
	private String destinationname;

	private double distance;

	private Map<String, Double> fare;

	public RideSelectionRedis(int customerId, Cordinate source, Cordinate destination, String sourceName,
			String destinationname, double distance, Map<String, Double> fare) {
		super();
		this.customerId = customerId;
		this.source = source;
		this.destination = destination;
		this.sourceName = sourceName;
		this.destinationname = destinationname;
		this.distance = distance;
		this.fare = fare;
	}

	public RideSelectionRedis() {
		super();
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Cordinate getSource() {
		return source;
	}

	public void setSource(Cordinate source) {
		this.source = source;
	}

	public Cordinate getDestination() {
		return destination;
	}

	public void setDestination(Cordinate destination) {
		this.destination = destination;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDestinationname() {
		return destinationname;
	}

	public void setDestinationname(String destinationname) {
		this.destinationname = destinationname;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Map<String, Double> getFare() {
		return fare;
	}

	public void setFare(Map<String, Double> fare) {
		this.fare = fare;
	}

	
}