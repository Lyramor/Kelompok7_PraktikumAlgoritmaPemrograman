package view;

import model.KategoriModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class KategoriSampahView extends JFrame {
    private JButton btnKembali;
    private JPanel buttonPanel;

    public KategoriSampahView(List<KategoriModel> kategoriList) {
        // Setup frame
        setTitle("Kategori Sampah");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(230, 230, 230));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(230, 230, 230));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnKembali = new JButton("← Kembali");
        btnKembali.setFont(new Font("Arial", Font.PLAIN, 12));
        btnKembali.setForeground(new Color(1, 88, 88));
        btnKembali.setBorderPainted(false);
        btnKembali.setContentAreaFilled(false);
        btnKembali.setFocusPainted(false);
        headerPanel.add(btnKembali, BorderLayout.WEST);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(230, 230, 230));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblTitle = new JLabel("Kategori Sampah", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Button Panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(new Color(230, 230, 230));
        buttonPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        if (kategoriList != null && !kategoriList.isEmpty()) {
            for (KategoriModel kategori : kategoriList) {
                JButton button = createStyledButton(kategori.getNamaKategori());
                buttonPanel.add(button, gbc);
                gbc.gridy++;
            }
        } else {
            JLabel noDataLabel = new JLabel("Tidak Ada Kategori Sampah", SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noDataLabel.setForeground(new Color(100, 100, 100));
            buttonPanel.add(noDataLabel, gbc);
        }

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 45));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(1, 88, 88));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    public void addBackButtonListener(ActionListener listener) {
        btnKembali.addActionListener(listener);
    }
}