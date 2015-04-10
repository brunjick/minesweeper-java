package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Zone extends JButton {
    private static Color colorNormalLight = new Color(0x6870ff);
    private static Color colorNormalDark = new Color(0x656ef9);
    private static Color colorRevealedLight = new Color(0xfafafa);
    private static Color colorRevealedDark = new Color(0xf5f5f5);
    private static Color colorMarkedLight = new Color(0xffd048);
    private static Color colorMarkedDark = new Color(0xfacb46);
    private static Color colorMinedLight = new Color(0xff5b08);
    private static Color colorMinedDark = new Color(0xec5408);

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
        reset();
        this.zoneX = zoneX;
        this.zoneY = zoneY;
    }

    /**
     * Overrides default swing method
     * Sets zone colors or values according to their state and position
     * @param g This is default parameter
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color colorCurrent = new Color(0x000000);

        // True, if the zone's X and Y positions are both odd or even numbers
        boolean crossingPosition = (zoneX % 2 != 0 && zoneY % 2 != 0) || (zoneX % 2 == 0 && zoneY % 2 == 0);

        // Yeah, I know this is a whole mess, but ...
        colorCurrent = (!zoneRevealed && !zoneMarked) ? (crossingPosition ? colorNormalLight : colorNormalDark) : colorCurrent;
        colorCurrent = (zoneRevealed && !zoneMined) ? (crossingPosition ? colorRevealedLight : colorRevealedDark) : colorCurrent;
        colorCurrent = (zoneMarked) ? (crossingPosition ? colorMarkedLight : colorMarkedDark) : colorCurrent;
        colorCurrent = (zoneRevealed && zoneMined) ? (crossingPosition ? colorMinedLight : colorMinedDark) : colorCurrent;

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

    /**
     * Resets all zone properties to defaults
     */
    public void reset() {
        zoneMined = false;
        zoneRevealed = false;
        zoneMarked = false;
        zoneValue = 0;
    }
}