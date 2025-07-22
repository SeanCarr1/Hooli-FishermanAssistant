// CatchManager: handles adding, editing, deleting, loading, and saving catch records for users.
package manager;

import java.io.*;
import java.time.LocalDate;
import model.CatchRecord;

/**
 * Manager class for CRUD operations and file I/O for CatchRecord.
 */
public class CatchManager {
    // Where catch records are saved
    private final String filePath;
    // List of all catch records in memory
    private final java.util.List<CatchRecord> records = new java.util.ArrayList<>();

    // Make a new manager and load all catches from file
    public CatchManager(String filePath) {
        this.filePath = filePath;
        loadFromFile();
    }

    // Get all catches for a user
    public java.util.List<CatchRecord> getRecordsForUser(String username) {
        return records.stream()
                .filter(r -> r.getUsername().equals(username))
                .collect(java.util.stream.Collectors.toList());
    }

    // Same as getRecordsForUser (for old code)
    public java.util.List<CatchRecord> getCatchesForUser(String username) {
        return getRecordsForUser(username);
    }

    // Add a new catch and save to file
    public void addRecord(CatchRecord record) {
        records.add(record);
        saveToFile();
    }

    // Same as addRecord (for old code)
    public void addCatch(CatchRecord record) {
        addRecord(record);
    }

    // Change an existing catch and save to file
    public void updateRecord(CatchRecord oldRecord, CatchRecord newRecord) {
        int idx = records.indexOf(oldRecord);
        if (idx != -1) {
            records.set(idx, newRecord);
            saveToFile();
        }
    }

    // Remove a catch and save to file
    public void deleteRecord(CatchRecord record) {
        records.remove(record);
        saveToFile();
    }

    // Load all catches from file into memory
    public void loadFromFile() {
        records.clear();
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                CatchRecord rec = fromFileString(line);
                if (rec != null) records.add(rec);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save all catches in memory to file
    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (CatchRecord rec : records) {
                bw.write(toFileString(rec));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Turn a catch into a line for saving
    private String toFileString(CatchRecord rec) {
        return String.join("|",
                rec.getUsername(),
                rec.getDate().toString(),
                rec.getFishType(),
                Double.toString(rec.getAmount()),
                rec.getLocation(),
                rec.getTripDate() != null ? rec.getTripDate().toString() : ""
        );
    }

    // Make a catch from a line in the file
    private CatchRecord fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length >= 5) {
            try {
                LocalDate tripDate = null;
                if (parts.length >= 6 && !parts[5].isEmpty()) {
                    tripDate = LocalDate.parse(parts[5]);
                }
                return new CatchRecord(
                    parts[0],
                    LocalDate.parse(parts[1]),
                    parts[2],
                    Double.parseDouble(parts[3]),
                    parts[4],
                    tripDate
                );
            } catch (Exception e) {
                // Ignore bad lines
            }
        }
        return null;
    }
}
