// RevenueEstimatorPanel: panel for estimating revenue based on catches and user-input prices.
package ui.panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import model.CatchRecord;

public class RevenueEstimatorPanel extends JPanel {
    // Make the revenue estimator panel
    public RevenueEstimatorPanel(String username, manager.CatchManager catchManager) {
        setBackground(new Color(60, 60, 60));
        setBorder(BorderFactory.createTitledBorder(null, "Revenue Estimator", 0, 0, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        List<String> fishTypes = new ArrayList<>();
        for (CatchRecord rec : catchManager.getRecordsForUser(username)) {
            if (!fishTypes.contains(rec.getFishType())) {
                fishTypes.add(rec.getFishType());
            }
        }
        Map<String, JTextField> priceFields = new HashMap<>();
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(60, 60, 60));
        inputPanel.setLayout(new GridLayout(fishTypes.size(), 2, 10, 5));
        // Build input fields for each fish type
        for (String type : fishTypes) {
            inputPanel.add(new JLabel(type + " price/kg:") {{ setForeground(Color.WHITE); }});
            JTextField field = new JTextField();
            priceFields.put(type, field);
            inputPanel.add(field);
        }
        add(inputPanel);
        JButton calcBtn = new JButton("Calculate Revenue");
        JLabel resultLabel = new JLabel(" ");
        resultLabel.setForeground(new Color(0x4FC3F7));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        // Calculate revenue when button is pressed
        calcBtn.addActionListener(e -> {
            double total = 0.0;
            for (CatchRecord rec : catchManager.getRecordsForUser(username)) {
                String type = rec.getFishType();
                try {
                    double price = Double.parseDouble(priceFields.get(type).getText());
                    total += rec.getAmount() * price;
                } catch (Exception ex) {
                    // ignore parse errors
                }
            }
            resultLabel.setText("Estimated Revenue: PHP " + Math.round(total * 100.0) / 100.0);
        });
        add(Box.createVerticalStrut(10));
        add(calcBtn);
        add(Box.createVerticalStrut(10));
        add(resultLabel);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
    }
}
