import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AIDashboard extends JFrame {

    int userId;

    JLabel insightLabel, suggestionLabel, predictionLabel;

    public AIDashboard(int userId) {
        this.userId = userId;

        setTitle("AI Smart Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        insightLabel = new JLabel();
        suggestionLabel = new JLabel();
        predictionLabel = new JLabel();

        add(createCard("📊 Insights", insightLabel));
        add(createCard("💡 Suggestions", suggestionLabel));
        add(createCard("🔮 Prediction", predictionLabel));

        generateAIReport();

        setVisible(true);
    }

    JPanel createCard(String title, JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    void generateAIReport() {
        try {
            Connection con = DBConnection.getConnection();

            // Total income
            PreparedStatement incomeStmt = con.prepareStatement(
                "SELECT SUM(amount) FROM transactions WHERE user_id=? AND type='income'");
            incomeStmt.setInt(1, userId);
            ResultSet rs1 = incomeStmt.executeQuery();
            double income = rs1.next() ? rs1.getDouble(1) : 0;

            // Total expense
            PreparedStatement expenseStmt = con.prepareStatement(
                "SELECT SUM(amount) FROM transactions WHERE user_id=? AND type='expense'");
            expenseStmt.setInt(1, userId);
            ResultSet rs2 = expenseStmt.executeQuery();
            double expense = rs2.next() ? rs2.getDouble(1) : 0;

            double savings = income - expense;

            // ===== AI INSIGHT =====
            if (expense > income) {
                insightLabel.setText("⚠️ You are overspending!");
            } else {
                insightLabel.setText("✅ Your spending is under control");
            }

            // ===== AI SUGGESTION =====
            if (savings < 1000) {
                suggestionLabel.setText("💡 Try to reduce unnecessary expenses");
            } else {
                suggestionLabel.setText("💡 Good savings! Keep it up");
            }

            // ===== AI PREDICTION =====
            double predictedExpense = expense * 1.1; // simple 10% growth
            predictionLabel.setText("🔮 Next month expense ~ ₹" + predictedExpense);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}