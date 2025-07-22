package ui.dialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import model.TripRecord;

/**
 * TripDialog: dialog window for adding a new trip record.
 */
public class TripDialog extends JDialog {
    private final JTextField dateField;
    private final JComboBox<String> boatTypeComboBox;
    private final JTextField fuelUsedField;
    private final JTextField durationField;
    private boolean confirmed = false;
    private final String username;

    // Make a new dialog for adding a trip
    public TripDialog(Frame owner, String username, java.util.List<model.TripRecord> existingTrips) {
        super(owner, true);
        this.username = username;
        setTitle("Add Trip");
        setSize(350, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        form.add(dateField);
        form.add(new JLabel("Boat Type:"));
        boatTypeComboBox = new JComboBox<>(new String[] {"Small Canoe", "Motorized Bangka", "Small Trawler"});
        form.add(boatTypeComboBox);
        form.add(new JLabel("Fuel Used (L):"));
        fuelUsedField = new JTextField();
        form.add(fuelUsedField);
        form.add(new JLabel("Duration (h):"));
        durationField = new JTextField();
        form.add(durationField);
        add(form, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            if (validateFields(existingTrips)) {
                confirmed = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());
    }

    private boolean validateFields(java.util.List<TripRecord> existingTrips) {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            // Prevent duplicate trip dates for the same user
            if (existingTrips.stream().anyMatch(t -> t.getDate().equals(date))) {
                JOptionPane.showMessageDialog(this, "A trip for this date already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            Double.parseDouble(fuelUsedField.getText().trim());
            Double.parseDouble(durationField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter valid values for all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public TripRecord getTripRecord() {
        return new TripRecord(
            username,
            LocalDate.parse(dateField.getText().trim()),
            (String) boatTypeComboBox.getSelectedItem(),
            Double.parseDouble(fuelUsedField.getText().trim()),
            Double.parseDouble(durationField.getText().trim()),
            new java.util.ArrayList<>()
        );
    }
}
