import java.awt.*;
import javax.swing.*;

public class UIUtils {

    public static boolean darkMode = true;

    public static Color bgColor = new Color(30, 30, 30);
    public static Color panelColor = new Color(45, 45, 45);
    public static Color textColor = Color.WHITE;
    public static Color buttonColor = new Color(52, 152, 219);
    public static Color hoverColor = new Color(41, 128, 185);

    public static void stylePanel(JPanel panel) {
        panel.setBackground(bgColor);
    }

    public static void styleLabel(JLabel label) {
        label.setForeground(textColor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(panelColor);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public static void styleButton(JButton btn) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(buttonColor);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                btn.setBackground(buttonColor.darker());
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
        });
    }

    // Fade animation
    public static void fadeIn(JFrame frame) {
        try {
            frame.setOpacity(0f);
            new Thread(() -> {
                try {
                    for (float i = 0f; i <= 1f; i += 0.05f) {
                        frame.setOpacity(i);
                        Thread.sleep(20);
                    }
                } catch (Exception ignored) {}
            }).start();
        } catch (Exception e) {
            // opacity not supported → ignore
        }
    }
}