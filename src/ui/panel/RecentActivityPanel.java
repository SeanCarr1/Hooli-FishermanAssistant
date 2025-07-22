package ui.panel;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import manager.CatchManager;
import model.CatchRecord;

public class RecentActivityPanel extends JPanel {
    public RecentActivityPanel(String username, CatchManager catchManager) {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));
        setBorder(BorderFactory.createTitledBorder(null, "Recent Activity", 0, 0, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        JTable recentTable = createRecentActivityTable(username, catchManager);
        JScrollPane tableScroll = new JScrollPane(recentTable);
        add(tableScroll, BorderLayout.CENTER);
    }

    private JTable createRecentActivityTable(String username, CatchManager catchManager) {
        String[] columns = {"Date", "Fish Type", "Weight (kg)", "Location"};
        List<CatchRecord> records = catchManager.getRecordsForUser(username);
        records.sort((a, b) -> b.getDate().compareTo(a.getDate())); // Most recent first
        int rowCount = Math.min(records.size(), 10);
        Object[][] data = new Object[rowCount][4];
        for (int i = 0; i < rowCount; i++) {
            CatchRecord rec = records.get(i);
            data[i][0] = rec.getDate();
            data[i][1] = rec.getFishType();
            data[i][2] = rec.getAmount();
            data[i][3] = rec.getLocation();
        }
        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(50, 50, 50));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(28);
        return table;
    }
}
