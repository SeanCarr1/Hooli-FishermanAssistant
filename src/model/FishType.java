// Model class representing a fish type for a user. Used for organizing and describing fish species.
package model;

public class FishType {
    // Username of the owner of this fish type
    private final String username;
    // Name of the fish type
    private String name;
    // Notes or description for the fish type
    private String notes;

    /**
     * Constructs a new FishType with the given username, name, and notes.
     */
    public FishType(String username, String name, String notes) {
        this.username = username;
        this.name = name;
        this.notes = notes;
    }

    // Getters and setters for all fields
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getNotes() { return notes; }
    public void setName(String name) { this.name = name; }
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Converts this fish type to a string for saving to file.
     * Format: username|name|notes
     */
    public String toFileString() {
        return username + "|" + name + "|" + (notes == null ? "" : notes);
    }

    /**
     * Parses a FishType from a file string. Returns null if invalid.
     */
    public static FishType fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 2) return null;
        String username = parts[0];
        String name = parts[1];
        String notes = parts.length > 2 ? parts[2] : "";
        return new FishType(username, name, notes);
    }
}


