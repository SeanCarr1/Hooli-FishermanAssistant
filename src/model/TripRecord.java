// Model class representing a fishing trip record. Stores trip details for analytics and reporting.
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripRecord {
    // User name of the owner of this trip
    private String username;
    // Date of the trip
    private LocalDate date;
    // Type of boat used for the trip
    private String boatType; // "Small Canoe", "Motorized Bangka", "Small Trawler"
    // Amount of fuel used (in liters)
    private double fuelUsed;
    // Duration of the trip (in hours)
    private double tripDuration;
    // List of catch IDs associated with this trip
    private List<String> catchIds; // or List<CatchRecord> if needed

    public TripRecord(String username, LocalDate date, String boatType, double fuelUsed, double tripDuration, List<String> catchIds) {
        this.username = username;
        this.date = date;
        this.boatType = boatType;
        this.fuelUsed = fuelUsed;
        this.tripDuration = tripDuration;
        this.catchIds = catchIds;
    }

	// No-argument constructor for TripRecord
    public TripRecord() {
        this.username = "";
        this.date = LocalDate.now();
        this.boatType = "";
        this.fuelUsed = 0.0;
        this.tripDuration = 0.0;
        this.catchIds = new ArrayList<>();
    }

	// Getters and setters for all fields
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getBoatType() { return boatType; }
    public void setBoatType(String boatType) { this.boatType = boatType; }
    public double getFuelUsed() { return fuelUsed; }
    public void setFuelUsed(double fuelUsed) { this.fuelUsed = fuelUsed; }
    public double getTripDuration() { return tripDuration; }
    public void setTripDuration(double tripDuration) { this.tripDuration = tripDuration; }
    public List<String> getCatchIds() { return catchIds; }
    public void setCatchIds(List<String> catchIds) { this.catchIds = catchIds; }
}
