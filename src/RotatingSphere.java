import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class RotatingSphere extends JPanel {
    private final int WIDTH = 1200;
    private final int HEIGHT = 900;

    private BufferedImage image;
    private int[] pixels;

    private double rotX = 0;
    private double rotY = 0;

    private static final int BLACK = new Color(0, 0, 0).getRGB();

    public RotatingSphere() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        Timer timer = new Timer(30, e -> renderAndRepaint());
        timer.start();
    }

    private void renderAndRepaint() {
        Arrays.fill(pixels, BLACK);

        double radius = 400;
        double DistToCam = 500;
        double K1 = WIDTH * DistToCam / (8 * 2 * radius);

        // Шаги по сферическим координатам
        for (double theta = 0; theta < 2 * Math.PI; theta += 0.07) {     // азимут
            for (double phi = 0; phi < 2 * Math.PI; phi += 0.07) {       // полярный угол
                double cosTheta = Math.cos(theta), sinTheta = Math.sin(theta);
                double cosPhi = Math.cos(phi), sinPhi = Math.sin(phi);
                double cosA = Math.cos(rotX), sinA = Math.sin(rotX);
                double cosB = Math.cos(rotY), sinB = Math.sin(rotY);

                // 3D координаты на сфере
                double x = radius * sinPhi * cosTheta;
                double y = radius * sinPhi * sinTheta;
                double z = radius * cosPhi;

                // Вращение
                double x2 = x * cosB - z * sinB;
                double z2 = x * sinB + z * cosB;
                x = x2;
                z = z2;

                double y2 = y * cosA - z * sinA;
                z = y * sinA + z * cosA;
                y = y2;

                double ooz = 1 / (z + DistToCam);
                int xp = (int) (WIDTH / 2 + K1 * ooz * x);
                int yp = (int) (HEIGHT / 2 - K1 * ooz * y);

                // Освещение (скалярное произведение нормали и направления света)
                double L = cosPhi * cosTheta * sinB - cosA * cosTheta * sinPhi -
                        sinA * sinTheta + cosB * (cosA * sinTheta - cosTheta * sinA * sinPhi);

                L = Math.max(0, L);

                if (ooz > 0 && xp >= 0 && xp < WIDTH && yp >= 0 && yp < HEIGHT) {
                    int luminance = (int) (L * 12);
                    luminance = Math.min(15, luminance);

                    int r = Math.min(255, luminance * 15 + 50);
                    int g = Math.min(255, luminance * 20 + 100);
                    int b = Math.min(255, luminance * 25 + 150);

                    pixels[yp * WIDTH + xp] = new Color(r, g, b).getRGB();
                }
            }
        }

        rotX += 0.04;
        rotY += 0.02;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}