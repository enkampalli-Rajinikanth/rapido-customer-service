package com.rapido.customer.dto;

import java.util.HashMap;

public class ResponseStructure2 {

    private String source;
    private String destination;
    private double distance;
    private HashMap<String, Double> fare;

    public ResponseStructure2() {
        super();
    }

    public ResponseStructure2(String source, String destination,
                              double distance, HashMap<String, Double> fare) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.fare = fare;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public HashMap<String, Double> getFare() {
        return fare;
    }

    public void setFare(HashMap<String, Double> fare) {
        this.fare = fare;
    }
}