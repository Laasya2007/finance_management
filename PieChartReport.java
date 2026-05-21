import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.*;
import javax.swing.*;

public class PieChartReport {

    // ✅ ONLY constructor (with userId)
    public PieChartReport(int userId) {

        double income = 0;
        double expense = 0;

        try {
            Connection con = DBConnection.getConnection();

            // Income
            PreparedStatement pst1 = con.prepareStatement(
                "SELECT IFNULL(SUM(amount),0) FROM transactions WHERE user_id=? AND LOWER(type)='income'"
            );
            pst1.setInt(1, userId);
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) income = rs1.getDouble(1);

            // Expense
            PreparedStatement pst2 = con.prepareStatement(
                "SELECT IFNULL(SUM(amount),0) FROM transactions WHERE user_id=? AND LOWER(type)='expense'"
            );
            pst2.setInt(1, userId);
            ResultSet rs2 = pst2.executeQuery();
            if (rs2.next()) expense = rs2.getDouble(1);

            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Income", income);
            dataset.setValue("Expense", expense);

            JFreeChart chart = ChartFactory.createPieChart(
                    "Income vs Expense",
                    dataset,
                    true,
                    true,
                    false
            );

            ChartFrame frame = new ChartFrame("Pie Chart", chart);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating Pie Chart");
        }
    }
}