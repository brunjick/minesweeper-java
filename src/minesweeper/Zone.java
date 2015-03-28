package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Zone extends JButton {
    private static Color colorOddNormal = new Color(0x6870ff);
    private static Color colorEvenNormal = new Color(0x656ef9);
    private static Color colorOddRevealed = new Color(0xfafafa);
    private static Color colorEvenRevealed = new Color(0xf5f5f5);
    private static Color colorOddMarked = new Color(0xffd048);
    private static Color colorEvenMarked = new Color(0xfacb46);
    private static Color colorOddMined = new Color(0xff5b08);
    private static Color colorEvenMined = new Color(0xec5408);
    public boolean zoneMined;

    public boolean zoneRevealed;
    public boolean zoneMarked;
    public int zoneValue;
    public int zoneX;
    public int zoneY;
    public int zoneSize;

    public Zone(int zoneX, int zoneY, int zoneP) {
        super();
        zoneSize = zoneP;
        setBounds((zoneX - 1) * zoneSize, (zoneY - 1) * zoneSize, zoneSize, zoneSize);
        zoneMined = false;
        zoneRevealed = false;
        zoneMarked = false;
        zoneValue = 0;
        this.zoneX = zoneX;
        this.zoneY = zoneY;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color colorCurrent = new Color(0x000000);

        if ((zoneX % 2 != 0 && zoneY % 2 != 0) || (zoneX % 2 == 0 && zoneY % 2 == 0)) {
            colorCurrent = (!zoneRevealed && !zoneMarked) ? colorOddNormal : colorCurrent;
            colorCurrent = (zoneRevealed && !zoneMined) ? colorOddRevealed : colorCurrent;
            colorCurrent = (zoneMarked) ? colorOddMarked : colorCurrent;
            colorCurrent = (zoneRevealed && zoneMined) ? colorOddMined : colorCurrent;
        }
        else {
            colorCurrent = (!zoneRevealed && !zoneMarked) ? colorEvenNormal : colorCurrent;
            colorCurrent = (zoneRevealed && !zoneMarked) ? colorEvenRevealed : colorCurrent;
            colorCurrent = (zoneMarked) ? colorEvenMarked : colorCurrent;
            colorCurrent = (zoneRevealed && zoneMined) ? colorEvenMined : colorCurrent;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, colorCurrent, zoneSize, zoneSize, colorCurrent);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        if (zoneRevealed) {
            if (zoneValue > 0) {
                String value = Integer.toString(zoneValue);
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(value, g2);
                int x = (this.getWidth() - (int) r.getWidth()) / 2;
                int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
                g.drawString(value, x, y);
            }
        }
    }

    public void setColorMined() {
        zoneRevealed = true;
        repaint();
    }
}
