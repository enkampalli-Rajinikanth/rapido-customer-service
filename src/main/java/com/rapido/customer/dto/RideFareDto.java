package com.rapido.customer.dto;

public class RideFareDto {

    private String vehicleType;
    private double distance;
    private double fare;

    public RideFareDto() {
    }

    public RideFareDto(String vehicleType, double distance, double fare) {
        this.vehicleType = vehicleType;
        this.distance = distance;
        this.fare = fare;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }
}