import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AddTransactionUI extends JFrame {

    int userId;
    Runnable refreshCallback;

    JTextField descField, amountField, dateField;
    JComboBox<String> typeBox, categoryBox;

    public AddTransactionUI(int userId, Runnable refreshCallback) {
        this.userId = userId;
        this.refreshCallback = refreshCallback;

        setTitle("➕ Add Transaction");
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 247, 250));
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        descField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();

        typeBox = new JComboBox<>(new String[]{"income", "expense"});
        categoryBox = new JComboBox<>(new String[]{
                "Food", "Travel", "Shopping", "Bills", "Income", "Others"
        });

        JButton addBtn = createButton("Add");
        JButton cancelBtn = createButton("Cancel");

        panel.add(new JLabel("Description:"));
        panel.add(descField);

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel("Type:"));
        panel.add(typeBox);

        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);

        panel.add(addBtn);
        panel.add(cancelBtn);

        add(panel);

        addBtn.addActionListener(e -> addTransaction());
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(46, 204, 113));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    void addTransaction() {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO transactions(user_id,description,category,amount,date,type) VALUES(?,?,?,?,?,?)"
            );

            pst.setInt(1, userId);
            pst.setString(2, descField.getText());
            pst.setString(3, (String) categoryBox.getSelectedItem());
            pst.setDouble(4, Double.parseDouble(amountField.getText()));
            pst.setString(5, dateField.getText());
            pst.setString(6, (String) typeBox.getSelectedItem());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Added");

            if (refreshCallback != null) {
                refreshCallback.run();
            }

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error");
        }
    }
}