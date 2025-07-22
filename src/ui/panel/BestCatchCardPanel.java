package ui.panel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import manager.CatchManager;
import model.CatchRecord;

public class BestCatchCardPanel extends JPanel {
    public BestCatchCardPanel(String username, CatchManager catchManager) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(60, 60, 60));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Best Catch");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        CatchRecord best = null;
        for (CatchRecord rec : records) {
            if (best == null || rec.getAmount() > best.getAmount()) {
                best = rec;
            }
        }
        if (best != null) {
            JLabel valueLabel = new JLabel(best.getAmount() + " kg");
            valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
            valueLabel.setForeground(new Color(0xFFD700));
            valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(valueLabel);
            JLabel fishTypeLabel = new JLabel(best.getFishType() + " - " + best.getDate());
            fishTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            fishTypeLabel.setForeground(Color.LIGHT_GRAY);
            fishTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(fishTypeLabel);
            JLabel locationLabel = new JLabel(best.getLocation());
            locationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            locationLabel.setForeground(Color.LIGHT_GRAY);
            locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(locationLabel);
        } else {
            JLabel valueLabel = new JLabel("No records yet.");
            valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
            valueLabel.setForeground(Color.LIGHT_GRAY);
            valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(valueLabel);
        }
    }
}
