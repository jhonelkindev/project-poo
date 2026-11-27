package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelImage extends JPanel {

    private Image image;

    public PanelImage(String route) {

        URL imageURL = getClass().getResource(route);
        if (imageURL != null) {
            image = new ImageIcon(imageURL).getImage();
        }else {
            JOptionPane.showMessageDialog(null, "No se encontro el route");
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0, 0,getWidth(),getHeight(), this);
        }
    }
}