package minesweeper;

import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // Set system L&F
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            System.out.println(e.getStackTrace());
            System.exit(0);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getStackTrace());
            System.exit(0);
        }
        catch (InstantiationException e) {
            System.out.println(e.getStackTrace());
            System.exit(0);
        }
        catch (IllegalAccessException e) {
            System.out.println(e.getStackTrace());
            System.exit(0);
        }

        int width = 0;
        int height = 0;
        int numberOfMines = 0;

        // This code is taken from oracle examples' page
        Object[] options = {"Easy",
                "Medium",
                "Difficult"};
        int n = JOptionPane.showOptionDialog(null,
                "Choose game level",
                "Choose game level",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[2]);
        if (n == JOptionPane.YES_OPTION) { // Easy
            width = 10;
            height = 10;
            numberOfMines = 15;
        } else if (n == JOptionPane.NO_OPTION) { // Medium
            width = 15;
            height = 15;
            numberOfMines = 30;
        } else if (n == JOptionPane.CANCEL_OPTION) { // Difficult
            width = 20;
            height = 20;
            numberOfMines = 60;
        } else {
            System.exit(0);
        }

        Minesweeper minesweeper = new Minesweeper();

        minesweeper.setWidth(width);
        minesweeper.setHeight(height);
        minesweeper.setNumberOfMines(numberOfMines);
        minesweeper.setZoneSize(30); // Size in pixels

        minesweeper.Start();
    }

}