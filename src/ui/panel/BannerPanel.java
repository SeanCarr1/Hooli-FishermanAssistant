package ui.panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class BannerPanel extends JPanel {
    private BufferedImage backgroundImg;

    public BannerPanel(BufferedImage img) {
        this.backgroundImg = img;
        setLayout(new GridBagLayout()); // For centering overlay text
        setPreferredSize(new Dimension(900, 320));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            // Scale image to fill the panel (may distort aspect ratio)
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
