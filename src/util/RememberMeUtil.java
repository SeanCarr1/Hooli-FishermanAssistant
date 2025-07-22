// RememberMeUtil: helps save, load, and clear the remembered username for auto-login.
package util;

import java.io.*;

public class RememberMeUtil {
    // File where the username is saved
    private static final String FILE = "remember_me.txt";

    // Save the username to file
    public static void saveUsername(String username) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE))) {
            out.println(username);
        } catch (IOException e) {
            // ignore
        }
    }

    // Load the username from file
    public static String loadUsername() {
        try (BufferedReader in = new BufferedReader(new FileReader(FILE))) {
            String line = in.readLine();
            return (line != null) ? line.trim() : null;
        } catch (IOException e) {
            return null;
        }
    }

    // Delete the remember me file
    public static void clear() {
        new File(FILE).delete();
    }
}
