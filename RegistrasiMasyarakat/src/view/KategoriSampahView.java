package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import model.KategoriModel;
import model.KategoriMapper;
import org.apache.ibatis.session.SqlSession;
import util.MyBatisUtil;
import java.util.List;

public class KategoriSampahView extends JFrame {
    private JButton btnKembali;
    private JTable tblKategoriSampah;
    private DefaultTableModel tableModel;
    private List<KategoriModel> kategoriList;

    public KategoriSampahView() {
        initializeComponents();
        loadKategoriSampah();
    }

    private void initializeComponents() {
        setTitle("Kategori Sampah");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
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
        JLabel lblTitle = new JLabel("Daftar Kategori Sampah", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(51, 51, 51));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));

        // Initialize table model
        String[] columnNames = {"ID", "Nama Kategori"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Initialize table
        tblKategoriSampah = new JTable(tableModel);
        customizeTable();

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblKategoriSampah);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void customizeTable() {
        // Set table appearance
        tblKategoriSampah.setRowHeight(30);
        tblKategoriSampah.setFont(new Font("Arial", Font.PLAIN, 12));
        tblKategoriSampah.setGridColor(new Color(230, 230, 230));
        tblKategoriSampah.setSelectionBackground(new Color(70, 130, 180));
        tblKategoriSampah.setSelectionForeground(Color.WHITE);
        tblKategoriSampah.setShowVerticalLines(true);
        tblKategoriSampah.setShowHorizontalLines(true);

        // Customize header
        tblKategoriSampah.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblKategoriSampah.getTableHeader().setBackground(new Color(70, 130, 180));
        tblKategoriSampah.getTableHeader().setForeground(Color.WHITE);
        tblKategoriSampah.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        TableColumnModel columnModel = tblKategoriSampah.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);    // ID column
        columnModel.getColumn(1).setPreferredWidth(250);   // Nama Kategori column

        // Center align for ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
    }

    private void loadKategoriSampah() {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            KategoriMapper mapper = session.getMapper(KategoriMapper.class);
            kategoriList = mapper.getAllKategori();

            // Clear existing table data
            tableModel.setRowCount(0);

            // Add data to table
            for (KategoriModel kategori : kategoriList) {
                Object[] rowData = {
                        kategori.getId(),
                        kategori.getNamaKategori()
                };
                tableModel.addRow(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading data: " + e.getMessage());
        }
    }

    public void refresh() {
        loadKategoriSampah();
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

    // Method to get selected kategori if needed
    public KategoriModel getSelectedKategori() {
        int selectedRow = tblKategoriSampah.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < kategoriList.size()) {
            return kategoriList.get(selectedRow);
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            KategoriSampahView view = new KategoriSampahView();
            view.setVisible(true);
        });
    }
}