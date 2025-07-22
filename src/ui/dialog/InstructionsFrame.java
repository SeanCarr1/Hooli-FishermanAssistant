// InstructionsFrame: shows a window with instructions and help for using the app.
package ui.dialog;

import java.awt.*;
import javax.swing.*;

public class InstructionsFrame extends JFrame {
    // Make the instructions window
    public InstructionsFrame() {
        super("Instructions");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(35, 35, 35));
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("How to Use Hooli", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        JTextArea instructions = new JTextArea();
        instructions.setEditable(false);
        instructions.setBackground(new Color(45, 45, 45));
        instructions.setForeground(Color.LIGHT_GRAY);
        instructions.setFont(new Font("Arial", Font.PLAIN, 16));
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setText(
            "Welcome to Hooli!\n\n" +
            "--- Application Overview ---\n" +
            "Hooli is a fisherman assistant app designed to help you log catches, manage trips, and analyze your fishing performance.\n\n" +
            "--- Main Components ---\n" +
            "1. Dashboard:\n" +
            "   - Overview of your fishing activity, recent catches, and statistics.\n" +
            "   - Quick access to key features.\n" +
            "2. Catch Manager:\n" +
            "   - Log new catches with details (date, fish type, weight, location, etc.).\n" +
            "   - Edit or delete existing catch records.\n" +
            "   - View a list of all your catches.\n" +
            "3. Trip Planner:\n" +
            "   - Plan and record fishing trips.\n" +
            "   - Track trip details (date, location, participants, notes).\n" +
            "   - Review past trips.\n" +
            "4. Fish Types:\n" +
            "   - View a list of supported fish species.\n" +
            "   - Learn about each species' properties.\n" +
            "   - (Note: Adding new fish types may be restricted.)\n" +
            "5. Revenue Estimator:\n" +
            "   - Estimate potential revenue from your catches.\n" +
            "6. User Management:\n" +
            "   - Login, register, and logout.\n\n" +
            "--- How To ---\n" +
            "- To log in: Click 'Login' in the top right and enter your credentials.\n" +
            "- To register: Click 'Register' and fill in the required information.\n" +
            "- To add a catch: Go to 'Catch Manager' and click 'Add Catch'. Fill in the details and save.\n" +
            "- To plan a trip: Go to 'Trip Planner' and click 'Add Trip'. Enter trip details and save.\n" +
            "- To view statistics: Visit the Dashboard for charts and summaries.\n" +
            "- To log out: Click 'Logout' in the top right.\n\n" +
            "--- Tips & Notes ---\n" +
            "- You must be logged in to access most features.\n" +
            "- Use the navigation bar at the top to switch between components.\n" +
            "- For help, click 'Instructions' at any time.\n" +
            "- Only supported fish types can be logged in catches.\n" +
            "- Data is saved locally. Remember to back up your records if needed.\n"
        );
        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }
}
