import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.*;
import javax.swing.*;

public class BarChartReport {

public BarChartReport(int userId) {
    try {
            Connection con = DBConnection.getConnection();

            // ===== DATASET =====
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            PreparedStatement pst = con.prepareStatement(
                "SELECT category, SUM(amount) FROM transactions WHERE user_id=? AND LOWER(type)='expense' GROUP BY category"
            );

            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String category = rs.getString(1);
                double amount = rs.getDouble(2);

                dataset.addValue(amount, "Expense", category);
            }

            // ===== CHART =====
            JFreeChart chart = ChartFactory.createBarChart(
                "Category-wise Expenses",
                "Category",
                "Amount",
                dataset
            );

            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();

            ChartFrame frame = new ChartFrame("Bar Chart", chart);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating Bar Chart");
        }
    }
}