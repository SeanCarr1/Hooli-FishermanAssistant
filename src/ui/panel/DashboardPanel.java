// DashboardPanel: shows the main dashboard with stats, analytics, and summary cards.
package ui.panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import manager.CatchManager;
import manager.FishTypeManager;
import manager.TripManager;
import model.CatchRecord;
import model.TripRecord;
import util.TripAnalytics;

public class DashboardPanel extends JPanel {
    private String username;
    private CatchManager catchManager;
    private FishTypeManager fishTypeManager;
    private TripManager tripManager;
    // Summary cards for analytics
    private SummaryCardPanel totalFuelCard;
    private SummaryCardPanel avgEfficiencyCard;
    private SummaryCardPanel mostUsedBoatCard;
    private SummaryCardPanel co2Card;
    private SummaryCardPanel fuelCostCard;
    private double fuelPricePerLiter = 65.0; // Default, editable by user

    /**
     * Constructs the dashboard panel with analytics and summary cards.
     * @param username The current user
     * @param catchManager Manager for catch records
     * @param fishTypeManager Manager for fish types
     * @param tripManager Manager for trip records
     */
    public DashboardPanel(String username, CatchManager catchManager, FishTypeManager fishTypeManager, TripManager tripManager) {
        this.username = username;
        this.catchManager = catchManager;
        this.fishTypeManager = fishTypeManager;
        this.tripManager = tripManager;
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 30, 30));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel userLabel = new JLabel("Welcome, " + username + "!");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(userLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 40, 40));

        // --- Analytics Summary Section ---
        JPanel analyticsPanel = new JPanel(new GridLayout(2, 2, 30, 10));
        analyticsPanel.setBackground(new Color(40, 40, 40));
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        totalFuelCard = new SummaryCardPanel("Total Fuel Used", "-");
        avgEfficiencyCard = new SummaryCardPanel("Avg. Efficiency", "-");
        mostUsedBoatCard = new SummaryCardPanel("Most Used Boat", "-");
        co2Card = new SummaryCardPanel("Est. CO2 Emissions", "-");
        fuelCostCard = new SummaryCardPanel("Fuel Cost", "-");
        analyticsPanel.add(totalFuelCard);
        analyticsPanel.add(avgEfficiencyCard);
        analyticsPanel.add(mostUsedBoatCard);
        analyticsPanel.add(co2Card);
        JPanel fuelPanel = new JPanel(new BorderLayout());
        fuelPanel.setBackground(new Color(40, 40, 40));
        fuelPanel.add(fuelCostCard, BorderLayout.CENTER);
        JButton editFuelPriceBtn = new JButton("Edit Fuel Price");
        editFuelPriceBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        editFuelPriceBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter fuel price per liter (PHP):", fuelPricePerLiter);
            if (input != null) {
                try {
                    double val = Double.parseDouble(input);
                    if (val < 0) throw new NumberFormatException();
                    fuelPricePerLiter = val;
                    refreshAnalytics();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price. Please enter a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fuelPanel.add(editFuelPriceBtn, BorderLayout.EAST);
        analyticsPanel.add(fuelPanel); // Add after co2Card
        mainPanel.add(analyticsPanel);
        refreshAnalytics();

        // Summary panels (row)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 5, 30, 10));
        summaryPanel.setBackground(new Color(40, 40, 40));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        summaryPanel.add(new SummaryCardPanel("Total Catches", String.valueOf(getTotalCatches())));
        summaryPanel.add(new SummaryCardPanel("Total Weight", getTotalWeight() + " kg"));
        summaryPanel.add(new SummaryCardPanel("Avg Catch Size", getAverageCatchSize() + " kg"));
        summaryPanel.add(new SummaryCardPanel("Fish Types", getFishTypeCount() + " types"));
        summaryPanel.add(new BestCatchCardPanel(username, catchManager));
        mainPanel.add(summaryPanel);

        // Revenue Estimator panel
        mainPanel.add(new RevenueEstimatorPanel(username, catchManager));

        // Recent activity table
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(new RecentActivityPanel(username, catchManager));

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(25, 25, 25));
        JLabel credits = new JLabel("Developed by Sean Carr Geoffrey M. Tenedero");
        credits.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(credits);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(footerPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Returns total number of catches for the user
    private int getTotalCatches() {
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        return records.size();
    }
    // Returns total weight of all catches
    private double getTotalWeight() {
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        double total = 0.0;
        for (CatchRecord rec : records) {
            total += rec.getAmount();
        }
        return Math.round(total * 100.0) / 100.0;
    }
    // Returns number of unique fish types caught
    private int getFishTypeCount() {
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        java.util.Set<String> types = new java.util.HashSet<>();
        for (CatchRecord rec : records) {
            types.add(rec.getFishType());
        }
        return types.size();
    }
    // Returns number of recent catches (up to 3)
    private int getRecentCatchCount() {
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        return Math.min(records.size(), 3);
    }
    // Returns average catch size
    private double getAverageCatchSize() {
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        if (records.isEmpty()) return 0.0;
        double total = 0.0;
        for (CatchRecord rec : records) {
            total += rec.getAmount();
        }
        return Math.round((total / records.size()) * 100.0) / 100.0;
    }

    /**
     * Refreshes all analytics and summary cards with latest data.
     */
    public void refreshAnalytics() {
        // --- Trip Analytics ---
        java.util.List<TripRecord> userTrips = tripManager.getTripsForUser(username);
        java.util.List<model.CatchRecord> userCatches = catchManager.getRecordsForUser(username);
        double totalFuel = TripAnalytics.getTotalFuel(userTrips);
        double avgEff = TripAnalytics.getAverageEfficiency(userTrips, userCatches);
        String mostBoat = TripAnalytics.getMostUsedBoatType(userTrips);
        double co2 = TripAnalytics.getEstimatedCO2(userTrips);
        totalFuelCard.setValue(String.format("%.2f L", totalFuel));
        avgEfficiencyCard.setValue(String.format("%.2f kg/L", avgEff));
        mostUsedBoatCard.setValue(mostBoat);
        co2Card.setValue(String.format("%.0f kg", co2));
        double fuelCost = util.FuelCostEstimator.estimateFuelCost(totalFuel, fuelPricePerLiter);
        fuelCostCard.setValue(String.format("PHP %.2f (%.2f/L)", fuelCost, fuelPricePerLiter));
    }

    /**
     * Reloads data from file and updates the UI.
     */
    public void refreshData() {
        catchManager.loadFromFile();
        fishTypeManager.loadFromFile();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40));
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 30, 30));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel userLabel = new JLabel("Welcome, " + username + "!");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(userLabel);
        headerPanel.add(Box.createVerticalStrut(20));
        add(headerPanel, BorderLayout.NORTH);
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 40, 40));
        // --- Analytics Summary Section ---
        JPanel analyticsPanel = new JPanel(new GridLayout(2, 2, 30, 10));
        analyticsPanel.setBackground(new Color(40, 40, 40));
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        totalFuelCard = new SummaryCardPanel("Total Fuel Used", "-");
        avgEfficiencyCard = new SummaryCardPanel("Avg. Efficiency", "-");
        mostUsedBoatCard = new SummaryCardPanel("Most Used Boat", "-");
        co2Card = new SummaryCardPanel("Est. CO2 Emissions", "-");
        fuelCostCard = new SummaryCardPanel("Fuel Cost", "-");
        analyticsPanel.add(totalFuelCard);
        analyticsPanel.add(avgEfficiencyCard);
        analyticsPanel.add(mostUsedBoatCard);
        analyticsPanel.add(co2Card);
        JPanel fuelPanel = new JPanel(new BorderLayout());
        fuelPanel.setBackground(new Color(40, 40, 40));
        fuelPanel.add(fuelCostCard, BorderLayout.CENTER);
        JButton editFuelPriceBtn = new JButton("Edit Fuel Price");
        editFuelPriceBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        editFuelPriceBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter fuel price per liter (PHP):", fuelPricePerLiter);
            if (input != null) {
                try {
                    double val = Double.parseDouble(input);
                    if (val < 0) throw new NumberFormatException();
                    fuelPricePerLiter = val;
                    refreshAnalytics();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price. Please enter a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fuelPanel.add(editFuelPriceBtn, BorderLayout.EAST);
        analyticsPanel.add(fuelPanel); // Add after co2Card
        mainPanel.add(analyticsPanel);
        refreshAnalytics();
        // Summary panels (row)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 5, 30, 10));
        summaryPanel.setBackground(new Color(40, 40, 40));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        summaryPanel.add(new SummaryCardPanel("Total Catches", String.valueOf(getTotalCatches())));
        summaryPanel.add(new SummaryCardPanel("Total Weight", getTotalWeight() + " kg"));
        summaryPanel.add(new SummaryCardPanel("Avg Catch Size", getAverageCatchSize() + " kg"));
        summaryPanel.add(new SummaryCardPanel("Fish Types", getFishTypeCount() + " types"));
        summaryPanel.add(new BestCatchCardPanel(username, catchManager));
        mainPanel.add(summaryPanel);
        // Revenue Estimator panel
        mainPanel.add(new RevenueEstimatorPanel(username, catchManager));
        // Recent activity table
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(new RecentActivityPanel(username, catchManager));
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(25, 25, 25));
        JLabel credits = new JLabel("Developed by Sean Carr Geoffrey M. Tenedero");
        credits.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(credits);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(footerPanel);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
