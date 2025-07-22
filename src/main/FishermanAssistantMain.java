// Main entry point for the Hooli Fisherman Assistant application
package main;

import com.formdev.flatlaf.FlatDarkLaf;

import ui.frame.MainFrame;

public class FishermanAssistantMain {
    // Launches the application, sets the look and feel, and shows the main frame
    public static void main(String[] args) {
        try {
            // Set FlatLaf look and feel
            javax.swing.UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf. Falling back to system look and feel.");
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Show the main application window (no user logged in by default)
            MainFrame frame = new MainFrame("");
            frame.setVisible(true);
        });
    }
}
