import java.sql.*;
import javax.swing.*;

public class Register extends JFrame {

    JTextField name, email, budget;
    JPasswordField pass;

    public Register() {
        setTitle("Register");
        setSize(400, 350);
        setLayout(null);

        name = new JTextField();
        email = new JTextField();
        pass = new JPasswordField();
        budget = new JTextField();

        addLabel("Name", 30);
        add(name).setBounds(150, 30, 200, 30);

        addLabel("Email", 80);
        add(email).setBounds(150, 80, 200, 30);

        addLabel("Password", 130);
        add(pass).setBounds(150, 130, 200, 30);

        addLabel("Budget", 180);
        add(budget).setBounds(150, 180, 200, 30);

        JButton btn = new JButton("Register");
        btn.setBounds(120, 240, 150, 30);
        add(btn);

        btn.addActionListener(e -> registerUser());

        setVisible(true);
    }

    JLabel addLabel(String text, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(30, y, 100, 30);
        add(l);
        return l;
    }

    void registerUser() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO users(name,email,password,budget) VALUES(?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name.getText());
            pst.setString(2, email.getText());
            pst.setString(3, new String(pass.getPassword()));
            pst.setDouble(4, Double.parseDouble(budget.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registered!");
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}