// LandingPanel: the welcome/landing page with hero image, features, and action buttons.
package ui.panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class LandingPanel extends JPanel {
    private JButton getStartedBtn;
    private JButton instructionsBtn;
    private JButton creditsBtn; // New Credits button
    private JButton exitBtn; // New Exit button

    /**
     * Constructs the landing page with hero image, features, and action buttons.
     * @param username The current user (if any)
     * @param isLoggedIn Whether the user is logged in
     * @param onGetStarted Callback for Get Started button
     * @param onInstructions Callback for Instructions button
     */
    // Make the landing page panel
    public LandingPanel(String username, boolean isLoggedIn, Runnable onGetStarted, Runnable onInstructions) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        // Hero section (banner image and overlay text)
        try {
            java.net.URL imgUrl = getClass().getResource("/images/fishing.jpg");
            if (imgUrl != null) {
                BufferedImage heroImg = javax.imageio.ImageIO.read(imgUrl);
                BannerPanel bannerPanel = new BannerPanel(heroImg);
                // Overlay text panel (centered)
                JPanel overlayPanel = new JPanel();
                overlayPanel.setOpaque(false);
                overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));
                JLabel titleLabel = new JLabel("Welcome to Hooli");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JLabel subtitleLabel = new JLabel("Your modern fishing logbook and assistant");
                subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 26));
                subtitleLabel.setForeground(Color.WHITE);
                subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JPanel textBg = new JPanel();
                textBg.setBackground(new Color(0,0,0,140));
                textBg.setOpaque(true);
                textBg.setLayout(new BoxLayout(textBg, BoxLayout.Y_AXIS));
                textBg.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
                textBg.add(titleLabel);
                textBg.add(Box.createVerticalStrut(15));
                textBg.add(subtitleLabel);
                overlayPanel.add(textBg);
                bannerPanel.add(overlayPanel, new GridBagConstraints());
                add(bannerPanel, BorderLayout.NORTH);
            }
        } catch (Exception e) {
            // fallback: no image
        }

        // Features section (cards)
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new GridLayout(1, 3, 30, 10));
        featuresPanel.setBackground(new Color(35, 35, 35));
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        featuresPanel.add(createFeaturePanel("\uD83D\uDCCB", "Track Catches", "Easily log and review your fishing records."));
        featuresPanel.add(createFeaturePanel("\uD83D\uDC1F", "Manage Fish Types", "Organize and learn about different fish species."));
        featuresPanel.add(createFeaturePanel("\uD83D\uDCCA", "View Statistics", "Visualize your fishing trends and performance."));

        // Call to action section (buttons)
        JPanel ctaPanel = new JPanel();
        ctaPanel.setBackground(new Color(50, 50, 50));
        ctaPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JLabel ctaLabel = new JLabel("Start managing your catches now!");
        ctaLabel.setFont(new Font("Arial", Font.BOLD, 22));
        ctaLabel.setForeground(Color.WHITE);
        getStartedBtn = new JButton("Get Started");
        getStartedBtn.setFont(new Font("Arial", Font.BOLD, 18));
        getStartedBtn.setFocusPainted(false);
        getStartedBtn.addActionListener(e -> {
            if (onGetStarted != null) onGetStarted.run();
        });
        instructionsBtn = new JButton("Instructions");
        instructionsBtn.setFont(new Font("Arial", Font.BOLD, 18));
        instructionsBtn.setFocusPainted(false);
        instructionsBtn.addActionListener(e -> {
            if (onInstructions != null) onInstructions.run();
        });
        creditsBtn = new JButton("Credits"); // Initialize Credits button
        creditsBtn.setFont(new Font("Arial", Font.BOLD, 18));
        creditsBtn.setFocusPainted(false);
        creditsBtn.addActionListener(e -> {
            new ui.dialog.CreditsFrame().setVisible(true);
        });
        exitBtn = new JButton("Exit"); // Initialize Exit button
        exitBtn.setFont(new Font("Arial", Font.BOLD, 18));
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> System.exit(0));
        ctaPanel.add(ctaLabel);
        ctaPanel.add(Box.createHorizontalStrut(20));
        ctaPanel.add(getStartedBtn);
        ctaPanel.add(Box.createHorizontalStrut(10));
        ctaPanel.add(instructionsBtn);
        ctaPanel.add(Box.createHorizontalStrut(10));
        ctaPanel.add(creditsBtn); // Add Credits button to panel
        ctaPanel.add(Box.createHorizontalStrut(10));
        ctaPanel.add(exitBtn); // Add Exit button to panel

        // Footer section
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(25, 25, 25));
        JLabel credits = new JLabel("Developed by Sean Carr Geoffrey M. Tenedero");
        credits.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(credits);

        // Main vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.add(featuresPanel);
        mainPanel.add(ctaPanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(footerPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Hide only the Get Started button if already logged in
        if (isLoggedIn) {
            if (getStartedBtn != null) getStartedBtn.setVisible(false);
            // Instructions, Credits, and Exit remain visible
        }
    }

    /**
     * Hides only the Get Started button if user is already logged in.
     */
    public void hideActionButtons() {
        if (getStartedBtn != null) getStartedBtn.setVisible(false);
        // Instructions, Credits, and Exit remain visible
    }

    /**
     * Helper to create a feature card panel.
     */
    private JPanel createFeaturePanel(String icon, String title, String desc) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 180px;'>" + desc + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(Color.LIGHT_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descLabel);
        return panel;
    }
}
