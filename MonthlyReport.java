import java.sql.*;
import javax.swing.*;

public class MonthlyReport {

    // ✅ METHOD 1 → Used by Dashboard button (popup)
    public static void generateReport(int userId) {

        double[] data = getReport(userId);

        double income = data[0];
        double expense = data[1];
        double savings = data[2];

        JOptionPane.showMessageDialog(null,
                "📊 Monthly Report\n\n" +
                "Income: ₹" + income +
                "\nExpense: ₹" + expense +
                "\nSavings: ₹" + savings);
    }

    // ✅ METHOD 2 → Used by MonthlyReportUI (table/chart)
    public static double[] getReport(int userId) {

        double income = 0;
        double expense = 0;

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT " +
                    "SUM(CASE WHEN type='income' THEN amount ELSE 0 END) AS income, " +
                    "SUM(CASE WHEN type='expense' THEN amount ELSE 0 END) AS expense " +
                    "FROM transactions WHERE user_id=?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                income = rs.getDouble("income");
                expense = rs.getDouble("expense");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        double savings = income - expense;

        return new double[]{income, expense, savings};
    }
}