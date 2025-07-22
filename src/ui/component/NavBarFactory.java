// NavBarFactory: builds the navigation bar for the app, with logo, navigation buttons, and user actions.
package ui.component;

import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import ui.dialog.InstructionsFrame;
import ui.frame.LoginFrame;

public class NavBarFactory {
    /**
     * Creates a navigation bar with logo, navigation buttons, and user actions.
     * @param username The current username (if logged in)
     * @param onLogout Callback for logout action
     * @param isLoggedIn Whether the user is logged in
     * @param onNavigate Callback for navigation button clicks
     * @return JPanel representing the navigation bar
     */
    public static JPanel createNavBar(String username, Runnable onLogout, boolean isLoggedIn, Consumer<String> onNavigate) {
        // Force mnemonic underlines to show on Windows
        UIManager.put("Button.showMnemonics", Boolean.TRUE);

        JPanel navBar = new JPanel(new BorderLayout());
        // Left: logo and brand
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Placeholder for icon
        JLabel iconLabel = new JLabel();
        // You can replace this with an actual image icon later
        iconLabel.setText("\uD83D\uDC1F"); // Unicode fish emoji as placeholder
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        leftPanel.add(iconLabel);
        // Brand name
        JLabel brandLabel = new JLabel("Hooli");
        brandLabel.setFont(new Font("Arial", Font.BOLD, 18));
        leftPanel.add(brandLabel);
        navBar.add(leftPanel, BorderLayout.WEST);

        // Center: navigation
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] navItems = {"Home", "Dashboard", "Catch Manager", "Trip Planner"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFocusPainted(false);
            switch (item) {
                case "Home": navButton.setMnemonic('H'); break;
                case "Dashboard": navButton.setMnemonic('D'); break;
                case "Catch Manager": navButton.setMnemonic('C'); break;
                case "Trip Planner": navButton.setMnemonic('T'); break;
            }
            if (onNavigate != null) {
                navButton.addActionListener(e -> {
                    if (item.equals("Home")) {
                        onNavigate.accept("Landing"); // Show the landing page card
                    } else if (isLoggedIn) {
                        onNavigate.accept(item);
                    } else {
                        JOptionPane.showMessageDialog(null, "You must be logged in to access '" + item + "'.", "Login Required", JOptionPane.WARNING_MESSAGE);
                    }
                });
            }
            centerPanel.add(navButton);
        }
        // Instructions button
        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setFocusPainted(false);
        instructionsButton.addActionListener(e -> {
            new InstructionsFrame().setVisible(true);
        });
        centerPanel.add(instructionsButton);
        navBar.add(centerPanel, BorderLayout.CENTER);

        // Right: user actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (!isLoggedIn) {
            JButton loginRegisterButton = new JButton("Login/Register");
            loginRegisterButton.setMnemonic('L'); // Alt+L for mnemonic
            loginRegisterButton.setToolTipText("Login or Register (Ctrl+L)");
            loginRegisterButton.setFocusable(true);
            // Ctrl+L accelerator
            loginRegisterButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control L"), "loginRegister");
            loginRegisterButton.getActionMap().put("loginRegister", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Window parent = SwingUtilities.getWindowAncestor(navBar);
                    if (parent != null) parent.dispose();
                    new LoginFrame().setVisible(true);
                }
            });
            loginRegisterButton.addActionListener(e -> {
                Window parent = SwingUtilities.getWindowAncestor(navBar);
                if (parent != null) parent.dispose();
                new LoginFrame().setVisible(true);
            });
            rightPanel.add(loginRegisterButton);
        } else {
            JButton logoutButton = new JButton("Logout");
            logoutButton.setMnemonic('L'); // Alt+L for mnemonic
            logoutButton.setToolTipText("Logout (Ctrl+L)");
            logoutButton.setFocusable(true);
            // Ctrl+L accelerator
            logoutButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control L"), "logout");
            logoutButton.getActionMap().put("logout", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (onLogout != null) onLogout.run();
                }
            });
            if (onLogout != null) {
                logoutButton.addActionListener(e -> onLogout.run());
            }
            rightPanel.add(logoutButton);
        }
        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }
}

// No code change needed here for your fish type restriction request. The NavBar is already correct.
// The next step is to update CatchDialog so users cannot add a new fish type from there.
