package com.automatoDev.gcdelivery.provider;

import com.google.firebase.Timestamp;

public class ItensProvider {
    private Timestamp timestamp;
    private double totalPayment;
    private String status;


    public ItensProvider(Timestamp timestamp, double totalPayment, String status) {
        this.timestamp = timestamp;
        this.totalPayment = totalPayment;
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public ItensProvider() {
    }

    public String getStatus() {
        return status;
    }
}
