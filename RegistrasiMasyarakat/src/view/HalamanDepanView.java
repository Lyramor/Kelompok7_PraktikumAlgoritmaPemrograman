package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HalamanDepanView extends JFrame {
    // Deklarasi komponen
    private JButton closeButton;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel headerPanel;
    private JPanel contentPanel;

    // Konstruktor
    public HalamanDepanView() {
        initializeComponents(); // Memanggil metode untuk mengatur komponen
    }

    // Metode untuk inisialisasi dan pengaturan komponen
    private void initializeComponents() {
        // Inisialisasi komponen
        headerPanel = new JPanel();
        closeButton = new JButton("X");
        contentPanel = new JPanel();
        titleLabel = new JLabel("Welcome to eWaste!");
        subtitleLabel = new JLabel("Recycle. Reward. Repeat.");
        loginButton = new JButton("LOGIN");
        registerButton = new JButton("REGISTER");

        // Pengaturan frame utama
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Menggunakan layout BorderLayout

        // Pengaturan panel header
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setPreferredSize(new Dimension(360, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Pengaturan tombol "Close"
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.addActionListener((ActionEvent evt) -> dispose()); // Menutup aplikasi saat tombol diklik
        headerPanel.add(closeButton);

        // Tambahkan headerPanel ke frame
        add(headerPanel, BorderLayout.NORTH);

        // Pengaturan panel konten
        contentPanel.setBackground(new Color(0, 153, 153));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Pengaturan label judul
        titleLabel.setFont(new Font("Tw Cen MT", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(50)); // Spasi atas
        contentPanel.add(titleLabel);

        // Pengaturan label subjudul
        subtitleLabel.setFont(new Font("Dubai Medium", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(10)); // Spasi antar elemen
        contentPanel.add(subtitleLabel);

        // Pengaturan tombol "Login"
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(40)); // Spasi antar elemen
        contentPanel.add(loginButton);

        // Pengaturan tombol "Register"
        registerButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(10)); // Spasi antar elemen
        contentPanel.add(registerButton);

        // Tambahkan contentPanel ke frame
        add(contentPanel, BorderLayout.CENTER);

        // Pengaturan ukuran dan posisi frame
        setSize(360, 440);
        setLocationRelativeTo(null); // Memposisikan frame di tengah layar
    }

    // Metode utama untuk menjalankan aplikasi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HalamanDepanView().setVisible(true);
        });
    }
}
