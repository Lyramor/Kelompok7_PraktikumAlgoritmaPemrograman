package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import model.JenisModel;
import model.JenisMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;
import java.util.List;

public class JenisSampahView extends JFrame {
    private JButton btnKembali;
    private JTable tblJenisSampah;
    private DefaultTableModel tableModel;
    private List<JenisModel> jenisList;

    public JenisSampahView() {
        initializeComponents();
        loadJenisSampah();
    }

    private void initializeComponents() {
        setTitle("Jenis Sampah");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Back Button
        btnKembali = new JButton("← Kembali");
        btnKembali.setFont(new Font("Arial", Font.PLAIN, 12));
        btnKembali.setForeground(new Color(70, 130, 180));
        btnKembali.setBorderPainted(false);
        btnKembali.setContentAreaFilled(false);
        btnKembali.setFocusPainted(false);
        headerPanel.add(btnKembali, BorderLayout.WEST);

        // Title
        JLabel lblTitle = new JLabel("Daftar Jenis Sampah", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(51, 51, 51));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));

        // Initialize table model
        String[] columnNames = {"ID", "Nama Jenis", "Kategori"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Initialize table
        tblJenisSampah = new JTable(tableModel);
        customizeTable();

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblJenisSampah);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void customizeTable() {
        // Set table appearance
        tblJenisSampah.setRowHeight(30);
        tblJenisSampah.setFont(new Font("Arial", Font.PLAIN, 12));
        tblJenisSampah.setGridColor(new Color(230, 230, 230));
        tblJenisSampah.setSelectionBackground(new Color(70, 130, 180));
        tblJenisSampah.setSelectionForeground(Color.WHITE);
        tblJenisSampah.setShowVerticalLines(true);
        tblJenisSampah.setShowHorizontalLines(true);

        // Customize header
        tblJenisSampah.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblJenisSampah.getTableHeader().setBackground(new Color(70, 130, 180));
        tblJenisSampah.getTableHeader().setForeground(Color.WHITE);
        tblJenisSampah.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        TableColumnModel columnModel = tblJenisSampah.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // ID column
        columnModel.getColumn(1).setPreferredWidth(200); // Nama Jenis column
        columnModel.getColumn(2).setPreferredWidth(150); // Kategori column

        // Center align for ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
    }

    private void loadJenisSampah() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            JenisMapper mapper = session.getMapper(JenisMapper.class);
            jenisList = mapper.getAllJenis();

            // Clear existing table data
            tableModel.setRowCount(0);

            // Add data to table
            for (JenisModel jenis : jenisList) {
                Object[] rowData = {
                        jenis.getId(),
                        jenis.getNamaJenis(),
                        jenis.getNamaKategori()
                };
                tableModel.addRow(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading data: " + e.getMessage());
        }
    }

    public void refresh() {
        loadJenisSampah();
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    public void addBackButtonListener(ActionListener listener) {
        btnKembali.addActionListener(listener);
    }

    // Method to get selected jenis if needed
    public JenisModel getSelectedJenis() {
        int selectedRow = tblJenisSampah.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < jenisList.size()) {
            return jenisList.get(selectedRow);
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            // Set System Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JenisSampahView view = new JenisSampahView();
            view.setVisible(true);
        });
    }
}