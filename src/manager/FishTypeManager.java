// FishTypeManager: handles adding, editing, deleting, loading, and saving fish types for users.
package manager;

import java.io.*;
import java.util.*;
import model.FishType;

public class FishTypeManager {
    // Where fish types are saved
    private final String filePath;
    // List of all fish types in memory
    private final List<FishType> fishTypes = new ArrayList<>();

    // Make a new manager and load all fish types from file
    public FishTypeManager(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }

    // Get all fish types for a user
    public List<FishType> getFishTypesForUser(String username) {
        List<FishType> result = new ArrayList<>();
        for (FishType ft : fishTypes) {
            if (ft.getUsername().equals(username)) {
                result.add(ft);
            }
        }
        return result;
    }

    // Add a new fish type and save to file
    public void addFishType(FishType fishType) {
        fishTypes.add(fishType);
        saveToFile();
    }

    // Change an existing fish type and save to file
    public void updateFishType(FishType oldType, FishType newType) {
        int idx = fishTypes.indexOf(oldType);
        if (idx != -1) {
            fishTypes.set(idx, newType);
            saveToFile();
        }
    }

    // Remove a fish type and save to file
    public void deleteFishType(FishType fishType) {
        fishTypes.remove(fishType);
        saveToFile();
    }

    // Load all fish types from file into memory
    public void loadFromFile() {
        fishTypes.clear();
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                FishType ft = FishType.fromFileString(line);
                if (ft != null) fishTypes.add(ft);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save all fish types in memory to file
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (FishType ft : fishTypes) {
                bw.write(ft.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


