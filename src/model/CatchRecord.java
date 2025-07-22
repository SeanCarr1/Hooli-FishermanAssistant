// Model class representing a daily catch record for a fisherman. Stores catch details for analytics and reporting.
package model;

import java.util.Objects;

/**
 * Model class representing a daily catch record for a fisherman. Stores catch details for analytics and reporting.
 */
public class CatchRecord {
    // Username of the owner of this catch record
    private String username;
    // Date of the catch
    private java.time.LocalDate date;
    // Type of fish caught
    private String fishType;
    // Amount of fish caught (in kg)
    private double amount;
    // Location where the catch was made
    private String location;
    // Date of the trip this catch belongs to (optional)
    private java.time.LocalDate tripDate;

    /**
     * Constructs a new CatchRecord with all fields.
     */
    public CatchRecord(String username, java.time.LocalDate date, String fishType, double amount, String location, java.time.LocalDate tripDate) {
        this.username = username;
        this.date = date;
        this.fishType = fishType;
        this.amount = amount;
        this.location = location;
        this.tripDate = tripDate;
    }

    /**
     * Backward compatibility constructor (no tripDate).
     */
    public CatchRecord(String username, java.time.LocalDate date, String fishType, double amount, String location) {
        this(username, date, fishType, amount, location, null);
    }

    // Getters and setters for all fields
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public java.time.LocalDate getDate() { return date; }
    public void setDate(java.time.LocalDate date) { this.date = date; }

    public String getFishType() { return fishType; }
    public void setFishType(String fishType) { this.fishType = fishType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public java.time.LocalDate getTripDate() { return tripDate; }
    public void setTripDate(java.time.LocalDate tripDate) { this.tripDate = tripDate; }

    /**
     * Checks if two CatchRecords are equal (by all fields).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatchRecord that = (CatchRecord) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(username, that.username) &&
                Objects.equals(date, that.date) &&
                Objects.equals(fishType, that.fishType) &&
                Objects.equals(location, that.location) &&
                Objects.equals(tripDate, that.tripDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, date, fishType, amount, location, tripDate);
    }

    @Override
    public String toString() {
        return "CatchRecord{" +
                "username='" + username + '\'' +
                ", date=" + date +
                ", fishType='" + fishType + '\'' +
                ", amount=" + amount +
                ", location='" + location + '\'' +
                ", tripDate=" + tripDate +
                '}';
    }
}
