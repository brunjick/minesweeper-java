package minesweeper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Game {
    Zone[][] zone;
    private MouseListener mouseListener;
    private int width;
    private int height;
    private int zoneSize;
    private int numberOfMines;

    public Game() {
        width = 10;
        height = 10;
        zoneSize = 25;
        numberOfMines = 20;

        mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                processClick(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            public void processClick(MouseEvent e) {
                Object buttonObj = e.getSource();
                Zone _zone = (Zone) buttonObj;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    openZone(_zone);
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (!_zone.zoneRevealed) {
                        markZone(_zone);
                    }
                }
            }
        };
    }

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getZoneSize() { return zoneSize; }
    public int getNumberOfMines() { return numberOfMines; }
    // Setters
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setZoneSize(int zoneSize) { this.zoneSize = zoneSize; }
    public void setNumberOfMines(int numberOfMines) { this.numberOfMines = numberOfMines; }


    public void Start() {
        createBoard();
        placeMines();
        placeNumbers();
    }

    private void createBoard() {
        JFrame board = new JFrame("Minesweeper");
        board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = board.getContentPane();
        pane.setLayout(null);

        // Create buttons
        zone = new Zone[width+1][height+1];

        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                zone[i][j] = new Zone(i, j, zoneSize);
                pane.add(zone[i][j]);
                zone[i][j].addMouseListener(mouseListener);
            }
        }

        pane.setPreferredSize(new Dimension(width * zoneSize, height * zoneSize));
        board.pack();
        board.setResizable(false);
        board.setVisible(true);
    }

    private void placeMines() {
        int randomX, randomY;
        while (numberOfMines > 0) {
            randomX = randInt(1, width);
            randomY = randInt(1, height);
            if (!zone[randomX][randomY].zoneMined) {
                zone[randomX][randomY].zoneMined = true;
                numberOfMines--;
            }
        }
    }

    private void placeNumbers() {
        int mines;
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= width; j++) {
                if (zone[i][j].zoneMined) continue;

                mines = 0;
                ArrayList<Zone> neighbours = getNeighbours(zone[i][j]);

                for (Zone neighbour : neighbours) {
                    if (neighbour.zoneMined) {
                        mines++;
                    }
                }

                zone[i][j].zoneValue = mines;
            }
        }
    }

    private void openZone(Zone _zone) {
        if (_zone.zoneMined) {
            gameOver();
            return;
        }
        if (!_zone.zoneMarked && !_zone.zoneRevealed) {
            _zone.zoneRevealed = true;
            _zone.repaint();
            if (_zone.zoneValue == 0) {
                openNeighbours(_zone);
            }
        }
    }

    private void openNeighbours(Zone _zone) {
        ArrayList<Zone> neighbours = getNeighbours(_zone);

        for (Zone neighbour : neighbours) {
            if (!neighbour.zoneRevealed) {
                openZone(neighbour);
            }
        }
    }

    private void markZone(Zone _zone) {
        if (_zone.zoneMarked) {
            _zone.zoneMarked = false;
        }
        else {
            _zone.zoneMarked = true;
        }
        _zone.repaint();
    }

    private void gameOver() {
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                if (zone[i][j].zoneMined) {
                    zone[i][j].setColorMined();
                }
            }
        }
    }

    private ArrayList<Zone> getNeighbours(Zone _zone) {
        ArrayList<Zone> neighbours = new ArrayList<Zone>();
        int X = _zone.zoneX - 1;
        int Y = _zone.zoneY - 1;

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (i == 1 && j == 1) continue;
                if (X+i > 0 && Y+j > 0 && X+i <= width && Y+j <= height) {
                    neighbours.add(zone[X+i][Y+j]);
                }
            }
        }

        return neighbours;
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
