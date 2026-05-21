import java.sql.*;
import javax.swing.*;

public class ReportPanel {

    public ReportPanel(int userId) {

        String month = JOptionPane.showInputDialog("Enter Month:");
        String year = JOptionPane.showInputDialog("Enter Year:");

        try {
            Connection con = DBConnection.getConnection();

            String sql =
            "SELECT category, SUM(amount) FROM transactions WHERE user_id=? AND MONTH(date)=? AND YEAR(date)=? GROUP BY category";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, userId);
            pst.setInt(2, Integer.parseInt(month));
            pst.setInt(3, Integer.parseInt(year));

            ResultSet rs = pst.executeQuery();

            String result = "Report:\n";

            while (rs.next()) {
                result += rs.getString(1) + " : " + rs.getDouble(2) + "\n";
            }

            JOptionPane.showMessageDialog(null, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}