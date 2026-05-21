import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame {

    JTextField emailField;
    JPasswordField passField;

    public Login() {

        setTitle("🔐 Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        UIUtils.stylePanel(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 🔥 IMPORTANT: allows horizontal expansion
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ================= TITLE =================
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(JLabel.CENTER);
        UIUtils.styleLabel(title);

        // ================= INPUT FIELDS =================

        // 🔥 EMAIL FIELD (INCREASED LENGTH)
        emailField = new JTextField(30);   // 👈 increased length
        passField = new JPasswordField(20);

        // Optional extra force sizing (ensures visible width)
        emailField.setPreferredSize(new Dimension(260, 30));
        passField.setPreferredSize(new Dimension(200, 30));

        UIUtils.styleTextField(emailField);
        UIUtils.styleTextField(passField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        UIUtils.styleButton(loginBtn);
        UIUtils.styleButton(registerBtn);

        // ================= LAYOUT =================

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        // Email label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel l1 = new JLabel("Email:");
        UIUtils.styleLabel(l1);
        panel.add(l1, gbc);

        // Email field (WIDER)
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Password label
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel l2 = new JLabel("Password:");
        UIUtils.styleLabel(l2);
        panel.add(l2, gbc);

        // Password field
        gbc.gridx = 1;
        panel.add(passField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(loginBtn, gbc);

        // Register button
        gbc.gridx = 1;
        panel.add(registerBtn, gbc);

        add(panel);

        // ================= ACTIONS =================
        loginBtn.addActionListener(e -> loginUser());
        registerBtn.addActionListener(e -> registerUser());

        setVisible(true);
        UIUtils.fadeIn(this);
    }

    // ================= LOGIN =================
    void loginUser() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM users WHERE email=? AND password=?"
            );

            pst.setString(1, emailField.getText());
            pst.setString(2, new String(passField.getPassword()));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                new Dashboard(userId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= REGISTER =================
    void registerUser() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO users(email,password) VALUES(?,?)"
            );

            pst.setString(1, emailField.getText());
            pst.setString(2, new String(passField.getPassword()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Registered!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Email already exists");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}