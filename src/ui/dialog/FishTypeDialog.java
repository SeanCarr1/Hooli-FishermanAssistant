// FishTypeDialog: dialog window for adding or editing a fish type.
package ui.dialog;

import java.awt.*;
import javax.swing.*;
import model.FishType;

public class FishTypeDialog extends JDialog {
    private JTextField nameField;
    private boolean confirmed = false;
    private FishType fishType;
    private String username;

    // Make a new dialog for adding or editing a fish type
    public FishTypeDialog(Frame owner, String username, FishType fishType) {
        super(owner, true);
        this.username = username;
        this.fishType = fishType;
        setTitle(fishType == null ? "Add Fish Type" : "Edit Fish Type");
        setSize(350, 120);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
        form.add(new JLabel("Fish Type Name:"));
        nameField = new JTextField(fishType != null ? fishType.getName() : "");
        form.add(nameField);
        add(form, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            if (validateFields()) {
                confirmed = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());
    }

    // Validate the input fields
    private boolean validateFields() {
        String nameText = nameField.getText().trim();
        if (nameText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fish type name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Check if the dialog was confirmed
    public boolean isConfirmed() { return confirmed; }

    // Get the new or edited fish type
    public FishType getFishType() {
        if (!confirmed) return null;
        return new FishType(username, nameField.getText().trim(), ""); // No notes
    }
}


