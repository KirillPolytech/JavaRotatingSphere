import javax.swing.*;
import java.awt.*;

void main() {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Rotating Sphere");
        frame.add(new RotatingSphere());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    });
}