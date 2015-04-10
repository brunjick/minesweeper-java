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

        Minesweeper minesweeper = new Minesweeper();

        minesweeper.setWidth(10);
        minesweeper.setHeight(10);
        minesweeper.setNumberOfMines(20);
        minesweeper.setZoneSize(40); // Size in pixels

        minesweeper.Start();
    }

}