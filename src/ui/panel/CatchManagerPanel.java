// CatchManagerPanel: panel for managing catches and fish types, with tables and dialogs.
package ui.panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import manager.CatchManager;
import manager.FishTypeManager;
import manager.TripManager;
import model.CatchRecord;
import model.FishType;
import ui.dialog.CatchDialog;
import ui.dialog.FishTypeDialog;

public class CatchManagerPanel extends JPanel {
    private JTable catchTable;
    private DefaultTableModel tableModel;
    private CatchManager catchManager;
    private FishTypeManager fishTypeManager;
    private TripManager tripManager;
    private DefaultTableModel fishTypeTableModel;
    private JTable fishTypeTable;
    private final String username;
    private TripPlannerPanel tripPlannerPanel;

    // Make the catch manager panel
    public CatchManagerPanel(String username, manager.CatchManager catchManager, manager.FishTypeManager fishTypeManager, manager.TripManager tripManager, TripPlannerPanel tripPlannerPanel) {
        this.username = username;
        this.catchManager = catchManager;
        this.fishTypeManager = fishTypeManager;
        this.tripManager = tripManager;
        this.tripPlannerPanel = tripPlannerPanel;
        setLayout(new BorderLayout());

        // Tabbed pane for Catches and Fish Types
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Catches", createCatchesPanel());
        tabbedPane.addTab("Fish Types", createFishTypesPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCatchesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Date", "Fish Type", "Amount", "Location", "Trip Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        catchTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(catchTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        loadCatchRecords();
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e -> {
            java.util.List<String> fishTypes = new java.util.ArrayList<>();
            for (FishType ft : fishTypeManager.getFishTypesForUser(username)) {
                fishTypes.add(ft.getName());
            }
            java.util.List<model.TripRecord> trips = tripManager.getTripsForUser(username);
            java.util.List<String> tripDates = new java.util.ArrayList<>();
            for (model.TripRecord tr : trips) {
                tripDates.add(tr.getDate().toString());
            }
            // Show dialog for catch with trip selection
            CatchDialog dialog = new CatchDialog((JFrame) SwingUtilities.getWindowAncestor(this), username, null, fishTypes, tripDates, tripManager);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                catchManager.addCatch(dialog.getCatchRecord());
                loadCatchRecords();
                if (tripPlannerPanel != null) tripPlannerPanel.refreshData();
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = catchTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a record to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            CatchRecord oldRecord = getRecordFromRow(selectedRow);
            java.util.List<String> fishTypes = new java.util.ArrayList<>();
            for (FishType ft : fishTypeManager.getFishTypesForUser(username)) {
                fishTypes.add(ft.getName());
            }
            java.util.List<model.TripRecord> trips = tripManager.getTripsForUser(username);
            java.util.List<String> tripDates = new java.util.ArrayList<>();
            for (model.TripRecord tr : trips) {
                tripDates.add(tr.getDate().toString());
            }
            CatchDialog dialog = new CatchDialog((JFrame) SwingUtilities.getWindowAncestor(this), username, oldRecord, fishTypes, tripDates, tripManager);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                catchManager.updateRecord(oldRecord, dialog.getCatchRecord());
                loadCatchRecords();
                if (tripPlannerPanel != null) tripPlannerPanel.refreshData();
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = catchTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            CatchRecord record = getRecordFromRow(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                catchManager.deleteRecord(record);
                loadCatchRecords();
                if (tripPlannerPanel != null) tripPlannerPanel.refreshData();
            }
        });
        return panel;
    }

    private JPanel createFishTypesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Fish Type Name", "Notes"};
        fishTypeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fishTypeTable = new JTable(fishTypeTableModel);
        JScrollPane scrollPane = new JScrollPane(fishTypeTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        loadFishTypes();
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e -> {
            FishTypeDialog dialog = new FishTypeDialog((JFrame) SwingUtilities.getWindowAncestor(this), username, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                fishTypeManager.addFishType(dialog.getFishType());
                loadFishTypes();
            }
        });
        editButton.addActionListener(e -> {
            int selectedRow = fishTypeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a fish type to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            FishType oldType = getFishTypeFromRow(selectedRow);
            FishTypeDialog dialog = new FishTypeDialog((JFrame) SwingUtilities.getWindowAncestor(this), username, oldType);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                fishTypeManager.updateFishType(oldType, dialog.getFishType());
                loadFishTypes();
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = fishTypeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a fish type to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            FishType type = getFishTypeFromRow(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this fish type?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fishTypeManager.deleteFishType(type);
                loadFishTypes();
            }
        });
        return panel;
    }

    private void loadCatchRecords() {
        tableModel.setRowCount(0);
        List<CatchRecord> records = catchManager.getCatchesForUser(username);
        for (CatchRecord rec : records) {
            tableModel.addRow(new Object[] {
                rec.getDate(),
                rec.getFishType(),
                rec.getAmount(),
                rec.getLocation(),
                rec.getTripDate() != null ? rec.getTripDate() : "(none)" // Show (none) for legacy catches
            });
        }
    }

    private void loadFishTypes() {
        fishTypeTableModel.setRowCount(0);
        List<FishType> types = fishTypeManager.getFishTypesForUser(username);
        for (FishType ft : types) {
            fishTypeTableModel.addRow(new Object[] { ft.getName(), ft.getNotes() });
        }
    }

    private CatchRecord getRecordFromRow(int row) {
        try {
            Object dateObj = tableModel.getValueAt(row, 0);
            java.time.LocalDate date;
            if (dateObj instanceof java.time.LocalDate) {
                date = (java.time.LocalDate) dateObj;
            } else {
                date = java.time.LocalDate.parse(dateObj.toString());
            }
            String fishType = (String) tableModel.getValueAt(row, 1);
            double amount = Double.parseDouble(tableModel.getValueAt(row, 2).toString());
            String location = (String) tableModel.getValueAt(row, 3);
            Object tripDateObj = tableModel.getValueAt(row, 4);
            java.time.LocalDate tripDate = null;
            if (tripDateObj != null && !"(none)".equals(tripDateObj.toString()) && !tripDateObj.toString().isEmpty()) {
                tripDate = java.time.LocalDate.parse(tripDateObj.toString());
            }
            return new CatchRecord(
                username,
                date,
                fishType,
                amount,
                location,
                tripDate
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading selected record. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private FishType getFishTypeFromRow(int row) {
        String name = (String) fishTypeTableModel.getValueAt(row, 0);
        String notes = (String) fishTypeTableModel.getValueAt(row, 1);
        return new FishType(username, name, notes);
    }

    // Add a public refreshData method to reload from file and update both tables
    public void refreshData() {
        catchManager.loadFromFile();
        fishTypeManager.loadFromFile();
        loadCatchRecords();
        loadFishTypes();
        revalidate();
        repaint();
    }
}
