import java.io.*;
import java.sql.*;
import javax.swing.*;

public class CSVImporter {

    public CSVImporter(int userId, Runnable onComplete) {

        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(null);

        if (res == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                Connection con = DBConnection.getConnection();

                while ((line = br.readLine()) != null) {

                    line = line.trim();

                    // 🔥 Only process main transaction lines
                    if (!(line.contains("CREDIT") || line.contains("DEBIT")))
                        continue;

                    try {
                        // ✅ DATE
                        String date = extractDate(line);

                        // ✅ TYPE
                        String type = line.contains("CREDIT") ? "income" : "expense";

                        // ✅ DESCRIPTION
                        String desc = extractDescription(line);

                        // ✅ AMOUNT (MAIN FIX)
                        double amount = extractAmount(line);

                        if (amount <= 0) continue;

                        String category = getCategory(desc);

                        String sql = "INSERT INTO transactions(user_id, description, category, amount, date, type) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement pst = con.prepareStatement(sql);

                        pst.setInt(1, userId);
                        pst.setString(2, desc);
                        pst.setString(3, category);
                        pst.setDouble(4, amount);
                        pst.setString(5, date);
                        pst.setString(6, type);

                        pst.executeUpdate();

                        System.out.println("✔ Inserted: " + desc + " | " + amount);

                    } catch (Exception inner) {
                        System.out.println("❌ Skipped: " + line);
                    }
                }

                JOptionPane.showMessageDialog(null, "✅ Import Completed!");

                if (onComplete != null) onComplete.run();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 🔥 DATE FIX
    String extractDate(String line) {
        try {
            // Example: Mar 29, 2026
            String part = line.substring(0, 12).trim();

            String[] p = part.replace(",", "").split(" ");
            String day = p[1];
            String year = p[2];
            String month = getMonth(p[0]);

            return year + "-" + month + "-" + day;

        } catch (Exception e) {
            return "2026-01-01";
        }
    }

    String getMonth(String m) {
        switch (m) {
            case "Jan": return "01";
            case "Feb": return "02";
            case "Mar": return "03";
            case "Apr": return "04";
            case "May": return "05";
            case "Jun": return "06";
            case "Jul": return "07";
            case "Aug": return "08";
            case "Sep": return "09";
            case "Oct": return "10";
            case "Nov": return "11";
            case "Dec": return "12";
        }
        return "01";
    }

    // 🔥 DESCRIPTION FIX
    String extractDescription(String line) {
        try {
            if (line.contains("Paid to")) {
                return line.split("Paid to")[1].split("DEBIT")[0].trim();
            }

            if (line.contains("Received from")) {
                return line.split("Received from")[1].split("CREDIT")[0].trim();
            }

        } catch (Exception e) {}

        return "Unknown";
    }

    // 🔥 PERFECT AMOUNT EXTRACTION
    double extractAmount(String line) {
        try {
            // Example: DEBIT             ?1,000

            String[] parts = line.split("\\s+");

            // Take LAST element
            String amtStr = parts[parts.length - 1];

            // Remove ₹, ?, commas
            amtStr = amtStr.replaceAll("[^0-9.]", "");

            return Double.parseDouble(amtStr);

        } catch (Exception e) {
            return 0;
        }
    }

    // 🔥 CATEGORY AUTO DETECTION
    String getCategory(String desc) {

        desc = desc.toLowerCase();

        if (desc.contains("restaurant") || desc.contains("food"))
            return "Food";

        if (desc.contains("uber") || desc.contains("ola"))
            return "Travel";

        if (desc.contains("amazon") || desc.contains("flipkart"))
            return "Shopping";

        if (desc.contains("salary") || desc.contains("received"))
            return "Income";

        return "Others";
    }
}