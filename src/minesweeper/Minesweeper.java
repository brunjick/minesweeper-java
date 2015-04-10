package minesweeper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    private Zone[][] zone;
    private JFrame board;
    private MouseListener mouseListener;
    private int width;
    private int height;
    private int zoneSize;
    private int numberOfMines;
    private int numberOfMarked;
    private int numberOfRevealed;

    public Minesweeper() {
        // Set default values
        width = 10;
        height = 10;
        zoneSize = 25;
        numberOfMines = 20;

        // Handle mouse events
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

            /**
             * Handles mouse events
             * @param e Generated MouseEvent object
             */
            public void processClick(MouseEvent e) {
                Zone _zone = (Zone) e.getSource(); // Downcast object to our Zone class

                if (!_zone.zoneRevealed) { // Check if zone is not already revealed
                    if (SwingUtilities.isLeftMouseButton(e)) openZone(_zone); // Open zone if left mouse button is clicked
                    else if (SwingUtilities.isRightMouseButton(e)) markZone(_zone); // Mark zone if right mouse button is clicked
                }
                // Check if user won the game
                checkWinStatus();
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


    /**
     * Starts the game and creates GUI
     * This method should be called once per Minesweeper object
     */
    public void Start() {
        createBoard();
        newGame();
    }

    /**
     * Creates actual GUI and places Zone objects on it
     * Note that .pack() method doesn't set correct size on Windows 8.1 (tested)
     */
    private void createBoard() {
        board = new JFrame("Minesweeper");
        board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container pane = board.getContentPane();
        pane.setLayout(null);

        // Create buttons
        zone = new Zone[width+1][height+1]; // I preferred to use 1 based indexes for Zone array

        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                zone[i][j] = new Zone(i, j, zoneSize);
                pane.add(zone[i][j]);
                zone[i][j].addMouseListener(mouseListener);
            }
        }

        pane.setPreferredSize(new Dimension(width * zoneSize, height * zoneSize));
        board.pack();
        board.setLocationRelativeTo(null); // Center the window position
        board.setResizable(false);
        board.setVisible(true);
    }

    /**
     * Resets all data from last game and starts new
     */
    private void newGame() {
        numberOfMarked = 0;
        numberOfRevealed = 0;
        resetZones();
        placeMines();
        placeNumbers();
    }

    /**
     * Resets all Zone objects to it's defaults
     */
    private void resetZones() {
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                zone[i][j].reset();
                zone[i][j].repaint();
            }
        }
    }

    /**
     * Calculate random positions for mines
     */
    private void placeMines() {
        int randomX, randomY;
        int n = numberOfMines;
        while (n > 0) {
            randomX = randInt(1, width);
            randomY = randInt(1, height);
            if (!zone[randomX][randomY].zoneMined) {
                zone[randomX][randomY].zoneMined = true;
                n--;
            }
        }
    }

    /**
     * Place numbers where required depending on mines' positions
     */
    private void placeNumbers() {
        int mines;
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
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

    /**
     * Marks or unmarks zone
     * @param _zone Zone object that should be marked or unmarked
     */
    private void markZone(Zone _zone) {
        if (_zone.zoneMarked) {
            numberOfMarked--;
        }
        else {
            numberOfMarked++;
        }
        _zone.zoneMarked = !_zone.zoneMarked;
        _zone.repaint();
    }

    /**
     * Opens Zone and it's neighbours
     * @param _zone Zone object that should be opened
     */
    private void openZone(Zone _zone) {
        // Check if user lost the game
        if (_zone.zoneMined) {
            gameOver(false);
            return;
        }

        // Open zone and it's neighbours if necessary
        if (!_zone.zoneMarked && !_zone.zoneRevealed) {
            numberOfRevealed++;
            _zone.zoneRevealed = true;
            _zone.repaint();
            if (_zone.zoneValue == 0) {
                openNeighbours(_zone);
            }
        }
    }

    /**
     * Opens neighbours of given Zone object
     * @param _zone Zone object whose neighbours should be opened
     */
    private void openNeighbours(Zone _zone) {
        ArrayList<Zone> neighbours = getNeighbours(_zone);

        for (Zone neighbour : neighbours) {
            if (!neighbour.zoneRevealed) {
                openZone(neighbour);
            }
        }
    }

    /**
     * Searches for given Zone's neighbours and returns them
     * @param _zone Zone object whose neighbours should be returned
     * @return ArrayList of Zone's containing all neighbours of given Zone
     */
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

    /**
     * Cheks if user won the game and if true, make proper behaviour
     */
    private void checkWinStatus() {
        if ((numberOfMarked + numberOfRevealed) == (width * height)) {
            gameOver(true);
        }
    }

    /**
     * Shows dialog displaying message about winning or losing the game, with restart possibility
     * @param won Boolean value to determine what message should be displayed
     */
    private void gameOver(boolean won) {
        String dialogTitle;
        String dialogMessage;
        if (won) {
            dialogTitle = "You won the game :)";
            dialogMessage = "Congratulations, you won";
        }
        else {
            dialogTitle = "You lost the game :(";
            dialogMessage = "Sorry, you lost";
        }
        // Set red color on mined zones
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                if (zone[i][j].zoneMined) {
                    zone[i][j].zoneRevealed = true;
                    zone[i][j].repaint();
                }
            }
        }

        // This code is taken from oracle examples' page
        Object[] options = {"Yes", "Exit"};
        int n = JOptionPane.showOptionDialog(board,
            dialogMessage + "\nDo you want to restart the game?",
            dialogTitle,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        if (n == JOptionPane.YES_OPTION) {
            newGame();
        } else if (n == JOptionPane.NO_OPTION) {
            System.exit(0);
        } else {
            System.exit(0);
        }
    }

    /**
     * Generates random number in min - max range
     * @param min Minimum value of generated random number
     * @param max Maximum value of generated random number
     * @return
     */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}