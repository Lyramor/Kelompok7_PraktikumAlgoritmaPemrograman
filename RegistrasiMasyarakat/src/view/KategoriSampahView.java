package view;

import javax.swing.*;
import java.awt.*;

public class KategoriSampahView extends JFrame {
    public KategoriSampahView() {
        // Set up the frame
        setTitle("Kategori Sampah");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("Kategori Sampah", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));

        mainPanel.add(lblTitle, BorderLayout.NORTH);

        add(mainPanel);
    }
}
