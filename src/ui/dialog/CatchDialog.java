package ui.dialog;

// CatchDialog: dialog window for adding or editing a catch record.
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import manager.FishTypeManager;
import manager.TripManager;
import model.CatchRecord;
import model.TripRecord;
import ui.frame.MainFrame;

/**
 * Dialog for adding or editing a catch record.
 */
public class CatchDialog extends JDialog {

    private final JTextField dateField;
    private final JComboBox<String> fishTypeComboBox;
    private final JTextField amountField;
    private final JTextField locationField;
    private final JComboBox<String> tripComboBox;
    private final JButton addTripButton;
    private final String username;
    private boolean confirmed = false;
    private final TripManager tripManager;
    private java.util.List<String> tripDates;

    // Make a new dialog for adding or editing a catch
    public CatchDialog(Frame owner, String username, CatchRecord record, java.util.List<String> fishTypes, java.util.List<String> tripDates, manager.TripManager tripManager) {
        super(owner, true);
        this.username = username;
        this.tripManager = tripManager;
        this.tripDates = tripDates;
        setTitle(record == null ? "Add Catch Record" : "Edit Catch Record");
        setSize(500, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(record != null ? record.getDate().toString() : "");
        form.add(dateField);
        form.add(new JLabel("Fish Type:"));
        JPanel fishTypeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fishTypeComboBox = new JComboBox<>(fishTypes.toArray(new String[0]));
        if (record != null) {
            fishTypeComboBox.setSelectedItem(record.getFishType());
        }
        fishTypeRow.add(fishTypeComboBox);
        JButton addFishTypeButton = new JButton("Add Fish Type");
        fishTypeRow.add(Box.createHorizontalStrut(8));
        fishTypeRow.add(addFishTypeButton);
        form.add(fishTypeRow);
        form.add(new JLabel("Amount (kg):"));
        amountField = new JTextField(record != null ? String.valueOf(record.getAmount()) : "");
        form.add(amountField);
        form.add(new JLabel("Location:"));
        locationField = new JTextField(record != null ? record.getLocation() : "");
        form.add(locationField);
        form.add(new JLabel("Trip:"));
        JPanel tripRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tripComboBox = new JComboBox<>(tripDates.toArray(new String[0]));
        if (record != null && record.getTripDate() != null) {
            tripComboBox.setSelectedItem(record.getTripDate().toString());
        }
        tripRow.add(tripComboBox);
        addTripButton = new JButton("Add Trip");
        tripRow.add(Box.createHorizontalStrut(8));
        tripRow.add(addTripButton);
        form.add(tripRow);
        // Show selected trip details below
        JLabel tripDetailsLabel = new JLabel();
        form.add(new JLabel("Trip Details:"));
        form.add(tripDetailsLabel);
        tripComboBox.addActionListener(e -> {
            String selected = (String) tripComboBox.getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                TripRecord trip = tripManager.getTripsForUser(username).stream()
                        .filter(t -> t.getDate().toString().equals(selected))
                        .findFirst().orElse(null);
                if (trip != null) {
                    tripDetailsLabel.setText(trip.getBoatType() + ", Fuel: " + trip.getFuelUsed() + "L, Duration: " + trip.getTripDuration() + "h");
                } else {
                    tripDetailsLabel.setText("Trip not found");
                }
            } else {
                tripDetailsLabel.setText("");
            }
        });
        // Trigger details for initial selection
        if (tripComboBox.getItemCount() > 0) {
            tripComboBox.setSelectedIndex(0);
        }

        add(form, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(e -> {
            if (validateFields()) {
                confirmed = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());
        addFishTypeButton.addActionListener(e -> {
            FishTypeDialog dialog = new FishTypeDialog((Frame) SwingUtilities.getWindowAncestor(this), username, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
                FishTypeManager fishTypeManager = null;
                if (parent instanceof MainFrame) {
                    fishTypeManager = ((MainFrame) parent).getFishTypeManager();
                }
                if (fishTypeManager != null) {
                    fishTypeComboBox.removeAllItems();
                    for (String ft : fishTypeManager.getFishTypesForUser(username).stream().map(f -> f.getName()).toArray(String[]::new)) {
                        fishTypeComboBox.addItem(ft);
                    }
                }
            }
        });
        addTripButton.addActionListener(e -> {
            java.util.List<TripRecord> existingTrips = tripManager.getTripsForUser(username);
            TripDialog tripDialog = new TripDialog((Frame) SwingUtilities.getWindowAncestor(this), username, existingTrips);
            tripDialog.setVisible(true);
            if (tripDialog.isConfirmed()) {
                TripRecord newTrip = tripDialog.getTripRecord();
                tripManager.addTrip(newTrip);
                tripComboBox.addItem(newTrip.getDate().toString());
                tripComboBox.setSelectedItem(newTrip.getDate().toString());
            }
        });
    }

    private boolean validateFields() {
        String dateText = dateField.getText().trim();
        String amountText = amountField.getText().trim();
        String fishTypeText = (String) fishTypeComboBox.getSelectedItem();
        String locationText = locationField.getText().trim();

        // Date validation
        LocalDate date;
        try {
            if (dateText.isEmpty()) {
                throw new IllegalArgumentException("Date cannot be empty.");
            }
            date = LocalDate.parse(dateText);
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date cannot be in the future.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date (YYYY-MM-DD) that is not in the future.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Amount validation
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Fish type validation
        if (fishTypeText == null || fishTypeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fish type cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Location validation
        if (locationText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Trip validation
        String tripDateText = (String) tripComboBox.getSelectedItem();
        if (tripDateText == null || tripDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trip must be selected or created.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public CatchRecord getCatchRecord() {
        return new CatchRecord(
                username,
                LocalDate.parse(dateField.getText().trim()),
                (String) fishTypeComboBox.getSelectedItem(),
                Double.parseDouble(amountField.getText().trim()),
                locationField.getText().trim(),
                LocalDate.parse((String) tripComboBox.getSelectedItem())
        );
    }
}
