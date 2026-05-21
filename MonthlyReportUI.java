import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MonthlyReportUI extends JFrame {

    JTable table;

    public MonthlyReportUI(int userId) {

        setTitle("Monthly Report");
        setSize(700, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // 🔵 Header Panel
        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        JLabel title = new JLabel("📋 Monthly Transactions Report");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title);

        add(header, BorderLayout.NORTH);

        // 🔵 Table
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Description", "Category", "Amount", "Type"}, 0
        );

        table = new JTable(model);

        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 🔵 Load Data
        loadData(userId, model);

        setVisible(true);
    }

    // 🎨 Table Styling
    private void styleTable() {

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(46, 204, 113));
        table.setSelectionForeground(Color.WHITE);

        // Center alignment for numbers
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
    }

    // 📥 Load Data
    private void loadData(int userId, DefaultTableModel model) {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id=?"
            );

            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("type")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}