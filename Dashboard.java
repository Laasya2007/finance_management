import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Dashboard extends JFrame {

    int userId;

    JLabel incomeLabel, expenseLabel, savingsLabel;

    public Dashboard(int userId) {
        this.userId = userId;

        setTitle("💰 Finance Manager");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 47, 61));
        sidebar.setLayout(new GridLayout(10, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JLabel title = new JLabel(" MENU", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton addBtn = createSideButton("➕ Add");
        JButton importBtn = createSideButton("📂 Import");
        JButton reportBtn = createSideButton("📋 Report");
        JButton pieBtn = createSideButton("🥧 Pie");
        JButton barBtn = createSideButton("📊 Bar");
        JButton aiBtn = createSideButton("🤖 AI");
        JButton refreshBtn = createSideButton("🔄 Refresh");
        JButton logoutBtn = createSideButton("⏻ Logout");

        sidebar.add(title);
        sidebar.add(addBtn);
        sidebar.add(importBtn);
        sidebar.add(reportBtn);
        sidebar.add(pieBtn);
        sidebar.add(barBtn);
        sidebar.add(aiBtn);
        sidebar.add(refreshBtn);
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // ===== TOP CARDS =====
        JPanel topCards = new JPanel(new GridLayout(1, 3, 15, 15));
        topCards.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        incomeLabel = createCard("Income", new Color(46, 204, 113));
        expenseLabel = createCard("Expense", new Color(231, 76, 60));
        savingsLabel = createCard("Savings", new Color(52, 152, 219));

        topCards.add(incomeLabel);
        topCards.add(expenseLabel);
        topCards.add(savingsLabel);

        mainPanel.add(topCards, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

        // ===== BUTTON ACTIONS =====

        addBtn.addActionListener(e ->
                new AddTransactionUI(userId, this::refreshData)
        );

        importBtn.addActionListener(e ->
                new CSVImporter(userId, this::refreshData)
        );

        reportBtn.addActionListener(e ->
                new MonthlyReportUI(userId)
        );

        pieBtn.addActionListener(e ->
                new PieChartReport(userId)
        );

        barBtn.addActionListener(e ->
                new BarChartReport(userId)
        );

        aiBtn.addActionListener(e ->
                new AIDashboard(userId)
        );

        refreshBtn.addActionListener(e -> refreshData());

        logoutBtn.addActionListener(e -> {
            dispose();
            new Login();
        });

        // ===== INITIAL LOAD =====
        refreshData();

        setVisible(true);
    }

    // ===== SIDEBAR BUTTON STYLE =====
    JButton createSideButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    // ===== CARD STYLE =====
    JLabel createCard(String title, Color color) {
        JLabel label = new JLabel("", JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return label;
    }

    // ===== 🔥 LIVE REFRESH METHOD =====
    public void refreshData() {

        try {
            Connection con = DBConnection.getConnection();

            double income = 0;
            double expense = 0;

            // ===== INCOME =====
            PreparedStatement pst1 = con.prepareStatement(
                "SELECT IFNULL(SUM(amount),0) FROM transactions WHERE user_id=? AND LOWER(type)='income'"
            );
            pst1.setInt(1, userId);
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) income = rs1.getDouble(1);

            // ===== EXPENSE =====
            PreparedStatement pst2 = con.prepareStatement(
                "SELECT IFNULL(SUM(amount),0) FROM transactions WHERE user_id=? AND LOWER(type)='expense'"
            );
            pst2.setInt(1, userId);
            ResultSet rs2 = pst2.executeQuery();
            if (rs2.next()) expense = rs2.getDouble(1);

            double savings = income - expense;

            incomeLabel.setText("<html><center>💰 Income<br>₹" + income + "</center></html>");
            expenseLabel.setText("<html><center>💸 Expense<br>₹" + expense + "</center></html>");
            savingsLabel.setText("<html><center>🏦 Savings<br>₹" + savings + "</center></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}