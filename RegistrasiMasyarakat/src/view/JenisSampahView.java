package view;

import model.JenisModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class JenisSampahView extends JFrame {
    private JButton btnKembali;
    private JPanel buttonPanel;

    public JenisSampahView(List<JenisModel> jenisList) {
        // Setup frame
        setTitle("Jenis Sampah");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 660);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(230, 230, 230));

        // Header Panel with Back Button and Title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(230, 230, 230));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Back Button Panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(new Color(230, 230, 230));

        btnKembali = new JButton("← Kembali");
        btnKembali.setFont(new Font("Arial", Font.PLAIN, 12));
        btnKembali.setForeground(new Color(1, 88, 88));
        btnKembali.setBorderPainted(false);
        btnKembali.setContentAreaFilled(false);
        btnKembali.setFocusPainted(false);

        backButtonPanel.add(btnKembali);
        headerPanel.add(backButtonPanel);

        // Title Label Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(230, 230, 230));

        JLabel lblTitle = new JLabel("Jenis Sampah");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(51, 51, 51));

        titlePanel.add(lblTitle);
        headerPanel.add(titlePanel);

        // Content Panel for buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(230, 230, 230));
        buttonPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Add Buttons to Button Panel
        if (jenisList != null && !jenisList.isEmpty()) {
            for (JenisModel jenis : jenisList) {
                JButton button = createStyledButton(jenis.getNamaJenis());
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                buttonPanel.add(button);
                buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            JLabel noDataLabel = new JLabel("Tidak Ada Jenis Sampah", SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noDataLabel.setForeground(new Color(100, 100, 100));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(noDataLabel);
        }

        // Create a panel to ensure buttons stay at the top when scrolling
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(new Color(230, 230, 230));
        contentWrapper.add(buttonPanel, BorderLayout.NORTH);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.setBackground(new Color(230, 230, 230));

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(300, 45));
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