package ui.panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import manager.CatchManager;
import manager.TripManager;
import model.CatchRecord;
import model.TripRecord;

public class TripPlannerPanel extends JPanel {
    private final String username;
    private final TripManager tripManager;
    private final CatchManager catchManager;
    private final JTable tripTable;
    private final DefaultTableModel tableModel;

    // Update constructor to require CatchManager
    public TripPlannerPanel(String username, TripManager tripManager, CatchManager catchManager) {
        this.username = username;
        this.tripManager = tripManager;
        this.catchManager = catchManager;
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Trip Planner", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Date", "Boat Type", "Fuel Used (L)", "Duration (h)", "Catch Details"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tripTable = new JTable(tableModel);
        tripTable.setRowHeight(28);
        // Set preferred width for the catch details column
        tripTable.getColumnModel().getColumn(4).setPreferredWidth(350);
        // Set a custom cell renderer for HTML/multiline support
        tripTable.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setText(value == null ? "" : value.toString());
            }
        });
        JScrollPane scrollPane = new JScrollPane(tripTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add trip button
        JButton addTripBtn = new JButton("Add Trip");
        addTripBtn.addActionListener(e -> showAddTripDialog());
        JPanel btnPanel = new JPanel();
        btnPanel.add(addTripBtn);
        add(btnPanel, BorderLayout.SOUTH);

        loadTrips();
        refreshTable();
    }

    private void loadTrips() {
        // No-op: tripManager always loads from file
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<TripRecord> trips = tripManager.getTripsForUser(username);
        List<CatchRecord> catches = catchManager.getCatchesForUser(username); // Get all catches for the user
        for (TripRecord tr : trips) {
            // Find all catches for this trip by trip date
            List<CatchRecord> catchesForTrip = catches.stream()
                .filter(c -> c.getTripDate() != null && c.getTripDate().equals(tr.getDate()))
                .collect(java.util.stream.Collectors.toList());

            String catchSummary;
            if (catchesForTrip.isEmpty()) {
                catchSummary = "(none)";
            } else {
                StringBuilder sb = new StringBuilder();
                for (CatchRecord cr : catchesForTrip) {
                    sb.append(String.format("- %s: %.2f kg @ %s<br><br>", cr.getFishType(), cr.getAmount(), cr.getLocation()));
                }
                catchSummary = "<html>" + sb.toString() + "</html>";
            }

            tableModel.addRow(new Object[] {
                tr.getDate(),
                tr.getBoatType(),
                tr.getFuelUsed(),
                tr.getTripDuration(),
                catchSummary
            });
        }
    }

    private void showAddTripDialog() {
        JTextField dateField = new JTextField(10);
        dateField.setText(java.time.LocalDate.now().toString());
        String[] boatTypes = {"Small Canoe", "Motorized Bangka", "Small Trawler"};
        JComboBox<String> boatTypeBox = new JComboBox<>(boatTypes);
        JPanel boatImagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        for (String type : boatTypes) {
            String imgFile = null;
            if (type.equals("Small Canoe")) imgFile = "/images/fishing_canoe.jpg";
            if (type.equals("Motorized Bangka")) imgFile = "/images/motorized_bangka.jpg";
            if (type.equals("Small Trawler")) imgFile = "/images/fishing_skiff.jpg";
            if (imgFile != null) {
                try {
                    java.net.URL imgUrl = getClass().getResource(imgFile);
                    if (imgUrl != null) {
                        ImageIcon icon = new ImageIcon(imgUrl);
                        Image img = icon.getImage().getScaledInstance(120, 72, Image.SCALE_SMOOTH); // Increased by 50%
                        JLabel imgLabel = new JLabel(new ImageIcon(img));
                        imgLabel.setToolTipText(type);
                        boatImagesPanel.add(imgLabel);
                    } else {
                        boatImagesPanel.add(new JLabel("?"));
                    }
                } catch (Exception ex) {
                    boatImagesPanel.add(new JLabel("?"));
                }
            }
        }
        JTextField fuelField = new JTextField(5);
        JTextField durationField = new JTextField(5);
        // Use GridBagLayout for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(dateField, gbc);
        // Boat type row
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Boat Type:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        JPanel boatTypeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        boatTypeRow.add(boatTypeBox);
        boatTypeRow.add(Box.createHorizontalStrut(10));
        boatTypeRow.add(boatImagesPanel);
        formPanel.add(boatTypeRow, gbc);
        // Fuel used
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Fuel Used (L):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(fuelField, gbc);
        // Duration
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Duration (h):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(durationField, gbc);
        int res = JOptionPane.showConfirmDialog(this, formPanel, "Add Trip", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                TripRecord tr = new TripRecord();
                tr.setUsername(username);
                tr.setDate(java.time.LocalDate.parse(dateField.getText())); // Convert String to LocalDate
                tr.setBoatType((String) boatTypeBox.getSelectedItem());
                tr.setFuelUsed(Double.parseDouble(fuelField.getText()));
                tr.setTripDuration(Double.parseDouble(durationField.getText()));
                tripManager.addTrip(tr);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Public method to refresh trip data from file and update the table.
     */
    public void refreshData() {
        tripManager.loadFromFile();
        refreshTable();
        revalidate();
        repaint();
    }
}
