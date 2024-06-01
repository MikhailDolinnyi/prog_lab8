package ru.mikhail.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class VtPanel extends JPanel {
    private final BufferedImage img;

    public VtPanel() {
        super();
        // Загрузка изображения
        try {
            this.img = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("icons/duck.jpeg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Отрисовка текста
        String text = "ВО СЛАВУ ВТ";
        Font font = new Font("Arial", Font.BOLD, 24);
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        int textX = (getWidth() - textWidth) / 2;
        int textY = (getHeight() - textHeight) / 2 - img.getHeight() / 2 - 10;
        g2.drawString(text, textX, textY);

        // Отрисовка изображения
        int imageX = (getWidth() - img.getWidth()) / 2;
        int imageY = (getHeight() - img.getHeight()) / 2;
        g2.drawImage(img, imageX, imageY, this);
    }
}
