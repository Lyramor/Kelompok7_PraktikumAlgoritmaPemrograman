package view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {
    private JButton btnDataKategori;
    private JButton btnDataJenis;
    private JButton btnLogout;

    public DashboardView() {
        setTitle("Dashboard - E-Waste Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JLabel lblTitle = new JLabel("Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        buttonPanel.setBackground(new Color(245, 245, 245));

        btnDataKategori = new JButton("Data Kategori");
        btnDataKategori.setFont(new Font("Arial", Font.BOLD, 14));
        btnDataKategori.setBackground(new Color(70, 130, 180));
        btnDataKategori.setForeground(Color.WHITE);
        btnDataKategori.setFocusPainted(false);

        btnDataJenis = new JButton("Data Jenis");
        btnDataJenis.setFont(new Font("Arial", Font.BOLD, 14));
        btnDataJenis.setBackground(new Color(70, 130, 180));
        btnDataJenis.setForeground(Color.WHITE);
        btnDataJenis.setFocusPainted(false);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setBackground(new Color(255, 69, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        buttonPanel.add(btnDataKategori);
        buttonPanel.add(btnDataJenis);
        buttonPanel.add(btnLogout);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    public JButton getBtnDataKategori() {
        return btnDataKategori;
    }

    public JButton getBtnDataJenis() {
        return btnDataJenis;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }
}
