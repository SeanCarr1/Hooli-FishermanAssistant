// Main application window for Hooli. Handles navigation and panel switching.
package ui.frame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import manager.CatchManager;
import manager.FishTypeManager;
import manager.TripManager;
import ui.component.NavBarFactory;
import ui.dialog.InstructionsFrame;
import ui.panel.*;

// JFrame for main logbook interface
public class MainFrame extends JFrame {
    private String username;
    private boolean isLoggedIn;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Map<String, JPanel> cardMap = new HashMap<>();
    private CatchManager catchManager;
    private FishTypeManager fishTypeManager;
    private TripManager tripManager;

    /**
     * Constructs the main frame, sets up navigation, and initializes all panels.
     * @param username The username of the logged-in user (empty if not logged in)
     */
    public MainFrame(String username) {
        super("Hooli - Main");
        this.username = username;
        this.isLoggedIn = (username != null && !username.isEmpty());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Managers (shared for all panels)
        catchManager = new CatchManager("catch_records.txt");
        fishTypeManager = new FishTypeManager("fish_types.txt");
        tripManager = new TripManager("trip_records.txt");

        // CardLayout for main content
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Dashboard panel (now passes all managers)
        DashboardPanel dashboardPanel = new DashboardPanel(username, catchManager, fishTypeManager, tripManager);
        cardPanel.add(dashboardPanel, "Dashboard");
        cardMap.put("Dashboard", dashboardPanel);

        // Fuel Cost Calculator placeholder
        JPanel fuelCostPanel = new JPanel();
        fuelCostPanel.setBackground(new Color(40, 40, 40));
        fuelCostPanel.setLayout(new GridBagLayout());
        JLabel comingSoon = new JLabel("Fuel Cost Calculator is not yet implemented.");
        comingSoon.setFont(new Font("Arial", Font.BOLD, 24));
        comingSoon.setForeground(Color.WHITE);
        fuelCostPanel.add(comingSoon);
        cardPanel.add(fuelCostPanel, "Fuel Cost Calculator");
        cardMap.put("Fuel Cost Calculator", fuelCostPanel);

        // Landing panel (introduction/hero/features)
        LandingPanel landingPanel = new LandingPanel(username, isLoggedIn,
            () -> {
                if (isLoggedIn) {
                    showCard("Dashboard");
                } else {
                    new LoginFrame().setVisible(true);
                    this.dispose();
                }
            },
            () -> new InstructionsFrame().setVisible(true)
        );
        cardPanel.add(landingPanel, "Landing");
        cardMap.put("Landing", landingPanel);

        // Trip Planner panel (was Environmental Impact)
        TripPlannerPanel tripPlannerPanel = new TripPlannerPanel(username, tripManager, catchManager);
        cardPanel.add(tripPlannerPanel, "Trip Planner");
        cardMap.put("Trip Planner", tripPlannerPanel);

        // Catch Manager panel (now passes tripPlannerPanel)
        CatchManagerPanel catchManagerPanel = new CatchManagerPanel(username, catchManager, fishTypeManager, tripManager, tripPlannerPanel);
        cardPanel.add(catchManagerPanel, "Catch Manager");
        cardMap.put("Catch Manager", catchManagerPanel);

        // Add card panel to center
        add(cardPanel, BorderLayout.CENTER);

        // NavBar with navigation callback
        JPanel navBar = NavBarFactory.createNavBar(username, () -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        }, isLoggedIn, this::showCard);
        add(navBar, BorderLayout.NORTH);

        // Show landing by default
        cardLayout.show(cardPanel, "Landing");
    }

    /**
     * Switches the visible card/panel in the main content area.
     * @param cardName The name of the card to show
     */
    private void showCard(String cardName) {
        if (cardMap.containsKey(cardName)) {
            // Refresh dashboard or catch manager if needed
            if ("Dashboard".equals(cardName)) {
                DashboardPanel dashboardPanel = (DashboardPanel) cardMap.get("Dashboard");
                dashboardPanel.refreshData();
            } else if ("Catch Manager".equals(cardName)) {
                CatchManagerPanel catchManagerPanel = (CatchManagerPanel) cardMap.get("Catch Manager");
                catchManagerPanel.refreshData();
            }
            cardLayout.show(cardPanel, cardName);
        }
    }

    /**
     * Exposes the FishTypeManager for dialog access.
     * @return the FishTypeManager instance
     */
    public FishTypeManager getFishTypeManager() {
        return fishTypeManager;
    }
}
