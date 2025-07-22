// Model class representing a user in the system. Stores username and password.
package model;

// model class for User
public class User {
    // Username for login and identification
    private String username;
    // Password for authentication (stored as plain text, not secure for production)
    private String password;

    /**
     * Constructs a new User with the given username and password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username for this user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password for this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for this user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Converts this user to a string for saving to file.
     * Format: username,password
     */
    public String toFileString() {
        return username + "," + password;
    }

    /**
     * Parses a user from a file string. Returns null if invalid.
     */
    public static User fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
            return new User(parts[0], parts[1]);
        }
        return null;
    }
}
