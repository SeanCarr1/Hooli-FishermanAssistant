package ui.panel;

import java.awt.*;
import javax.swing.*;

public class SummaryCardPanel extends JPanel {
    public SummaryCardPanel(String title, String value) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(60, 60, 60));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(0x4FC3F7));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(valueLabel);
    }

    public void setValue(String value) {
        ((JLabel)getComponent(2)).setText(value);
    }
}
