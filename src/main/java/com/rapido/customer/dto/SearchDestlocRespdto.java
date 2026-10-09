package com.rapido.customer.dto;

import java.util.List;

public class SearchDestlocRespdto {
	private String address;
	private double latitude;
	private double longititude;
	public SearchDestlocRespdto(String address, double latitude, double longititude) {
		super();
		this.address = address;
		this.latitude = latitude;
		this.longititude = longititude;
	}
	public SearchDestlocRespdto() {
		super();
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongititude() {
		return longititude;
	}
	public void setLongititude(double longititude) {
		this.longititude = longititude;
	}
}