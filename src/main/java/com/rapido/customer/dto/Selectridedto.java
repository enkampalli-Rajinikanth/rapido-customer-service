package com.rapido.customer.dto;

import com.rapido.customer.entity.Cordinate;

public class Selectridedto {
	private int customerid;
	private Cordinate sourceloc;
	private Cordinate destloc;

	public Selectridedto(int customerid, Cordinate sourceloc, Cordinate destloc) {
		super();
		this.customerid = customerid;
		this.sourceloc = sourceloc;
		this.destloc = destloc;
	}

	public Selectridedto() {
		super();
	}

	public Cordinate getSourceloc() {
		return sourceloc;
	}

	public void setSourceloc(Cordinate sourceloc) {
		this.sourceloc = sourceloc;
	}

	public Cordinate getDestloc() {
		return destloc;
	}

	public void setDestloc(Cordinate destloc) {
		this.destloc = destloc;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

}
