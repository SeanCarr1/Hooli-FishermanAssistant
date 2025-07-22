// TripManager: handles adding, editing, deleting, loading, and saving trip records for users.
package manager;

import java.io.*;

public class TripManager {
    // List of all trip records in memory
    private java.util.List<model.TripRecord> trips = new java.util.ArrayList<>();
    // Where trip records are saved
    private String filePath;

    // Make a new manager and load all trips from file
    public TripManager(String filePath) {
        this.filePath = filePath;
        loadFromFile(); // Loads all trips into memory at startup
    }

    // Add a new trip and save to file
    public void addTrip(model.TripRecord trip) {
        trips.add(trip);
        saveToFile();
        loadFromFile();
    }

    // Get all trips for a user
    public java.util.List<model.TripRecord> getTripsForUser(String username) {
        if (trips == null) return new java.util.ArrayList<>();
        java.util.List<model.TripRecord> userTrips = new java.util.ArrayList<>();
        for (model.TripRecord tr : trips) {
            if (tr.getUsername().equals(username)) userTrips.add(tr);
        }
        return userTrips;
    }

    // Change an existing trip and save to file
    public void updateTrip(model.TripRecord trip) {
        for (int i = 0; i < trips.size(); i++) {
            model.TripRecord tr = trips.get(i);
            if (tr.getUsername().equals(trip.getUsername()) && tr.getDate().equals(trip.getDate())) {
                trips.set(i, trip); // Replace with updated trip
                break;
            }
        }
        saveToFile();
        loadFromFile();
    }

    // Remove a trip and save to file
    public void deleteTrip(model.TripRecord trip) {
        trips.removeIf(tr -> tr.getUsername().equals(trip.getUsername()) && tr.getDate().equals(trip.getDate()));
        saveToFile();
        loadFromFile();
    }

    // Load all trips from file into memory
    public void loadFromFile() {
        trips.clear();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//") || line.trim().isEmpty()) continue; // Skip comments/empty
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 6) {
                    model.TripRecord tr = new model.TripRecord();
                    tr.setUsername(parts[0]);
                    tr.setDate(java.time.LocalDate.parse(parts[1]));
                    tr.setBoatType(parts[2]);
                    tr.setFuelUsed(Double.parseDouble(parts[3]));
                    tr.setTripDuration(Double.parseDouble(parts[4]));
                    tr.setCatchIds(parts[5].isEmpty() ? new java.util.ArrayList<>() : java.util.Arrays.asList(parts[5].split(",")));
                    trips.add(tr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // If file can't be read, print error
        }
    }

    // Save all trips in memory to file
    private void saveToFile() {
        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("// username|date|boatType|fuelUsed|tripDuration|catchIds");
            for (model.TripRecord tr : trips) {
                String catchIds = tr.getCatchIds() == null ? "" : String.join(",", tr.getCatchIds());
                writer.println(tr.getUsername() + "|" + tr.getDate() + "|" + tr.getBoatType() + "|" + tr.getFuelUsed() + "|" + tr.getTripDuration() + "|" + catchIds);
            }
        } catch (IOException e) {
            e.printStackTrace(); // If file can't be written, print error
        }
    }
}
