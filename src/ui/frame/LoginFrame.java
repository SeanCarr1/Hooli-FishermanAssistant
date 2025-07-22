package ui.frame;

import java.awt.*;
import javax.swing.*;
import manager.UserManager;
import model.User;
import util.RememberMeUtil;

public class LoginFrame extends JFrame {
	private UserManager userManager = new UserManager(); // Uses default users.txt

	public LoginFrame() {
		setTitle("Fisherman Assistant - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350, 200);
		setLocationRelativeTo(null);

		// Create UI components
		JLabel userLabel = new JLabel("Username:");
		JTextField userField = new JTextField(15);
		JLabel passLabel = new JLabel("Password:");
		JPasswordField passField = new JPasswordField(15);
		JCheckBox rememberMeBox = new JCheckBox("Remember Me");
		String remembered = RememberMeUtil.loadUsername();
		if (remembered != null) {
			userField.setText(remembered);
			rememberMeBox.setSelected(true);
		}
		JButton loginButton = new JButton("Login");
		JButton registerButton = new JButton("Register");

		// Add login logic
		loginButton.addActionListener(e -> {
			String username = userField.getText().trim();
			String password = new String(passField.getPassword());
			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			User user = userManager.loginUser(username, password);
			if (user != null) {
				if (rememberMeBox.isSelected()) {
					RememberMeUtil.saveUsername(username);
				} else {
					RememberMeUtil.clear();
				}
				JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + ".");
				MainFrame mainFrame = new MainFrame(username);
				mainFrame.setVisible(true);
				this.dispose(); // Close the login window
			} else {
				JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// Add registration logic
		registerButton.addActionListener(e -> {
			String username = userField.getText().trim();
			String password = new String(passField.getPassword());
			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			boolean registered = userManager.registerUser(username, password);
			if (registered) {
				JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
			} else {
				JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.",
						"Registration Failed", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Layouts
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(userLabel, gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(userField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(passLabel, gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(passField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(rememberMeBox, gbc);
		gbc.gridy = 3;
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(loginButton);
		buttonPanel.add(registerButton);
		panel.add(buttonPanel, gbc);

		add(panel);
	}
}
