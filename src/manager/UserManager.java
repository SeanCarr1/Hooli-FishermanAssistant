// UserManager handles user registration, login, and file I/O for user data.
package manager;

import java.io.*;
import java.util.*;
import model.User;

public class UserManager {
    // Path to the file where user data is stored
    private String usersFile;
    // Map of username to User object, loaded from file
    private Map<String, User> users = new HashMap<>();

    /**
     * Default constructor. Loads users from the default file (users.txt).
     */
    public UserManager() {
        this("users.txt");
    }

    /**
     * Loads users from the specified file.
     * @param usersFile the file to load/save user data
     */
    public UserManager(String usersFile) {
        this.usersFile = usersFile;
        loadUsers(); // Loads all users into memory at startup
    }

    /**
     * Loads all users from the file into the users map.
     * Each line is parsed into a User. Skips invalid lines.
     * If the file is missing or corrupt, the map will be empty.
     */
    private void loadUsers() {
        users.clear();
        File file = new File(usersFile);
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                User user = User.fromFileString(scanner.nextLine());
                if (user != null) {
                    users.put(user.getUsername(), user); // Add user to map
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // If file can't be read, print error
        }
    }

    /**
     * Saves all users from the map to the file.
     * Each user is written as a line. Overwrites the file each time.
     */
    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(usersFile))) {
            for (User user : users.values()) {
                writer.println(user.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace(); // If file can't be written, print error
        }
    }

    /**
     * Registers a new user if the username is not already taken.
     * Adds the user to the map and saves to file.
     * @return true if registration succeeded, false if username exists
     */
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) return false; // Username already exists
        User user = new User(username, password);
        users.put(username, user);
        saveUsers();
        return true;
    }

    /**
     * Attempts to log in a user by checking username and password.
     * Returns the User object if successful, null otherwise.
     */
    public User loginUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null; // Invalid input
        }
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login successful
        }
        return null; // Login failed
    }
}
