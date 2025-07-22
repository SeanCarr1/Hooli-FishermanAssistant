// CreditsFrame: shows a window with information about the developer and course.
package ui.dialog;

import java.awt.*;
import javax.swing.*;

/**
 * Dialog window that shows credits/about the developer.
 */
public class CreditsFrame extends JFrame {
    /**
     * Shows a dialog with information about the developer and project.
     */
    public CreditsFrame() {
        super("Credits");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 35, 35));

        JLabel title = new JLabel("About the Developer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JTextArea credits = new JTextArea();
        credits.setEditable(false);
        credits.setBackground(new Color(45, 45, 45));
        credits.setForeground(Color.LIGHT_GRAY);
        credits.setFont(new Font("Arial", Font.PLAIN, 16));
        credits.setLineWrap(true);
        credits.setWrapStyleWord(true);
        credits.setText(
            "Hooli Fisherman Assistant\n\n" +
            "Developed by Sean Carr Geoffrey M. Tenedero.\n\n" +
            "Course & Section: BSCPE 2A IE\n" +
            "Subject: CPE05\n" +
            "Instructor: Engr. Donna Castro\n" +
            "Date Submitted: June 6, 2025\n" +
            "This application was created as a modern tool for fishermen to log, manage, and analyze their fishing activities.\n\n" +
            "Thank you for using Hooli!"
        );
        JScrollPane scrollPane = new JScrollPane(credits);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }
}
