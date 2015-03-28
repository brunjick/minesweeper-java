package minesweeper;

public class Minesweeper {

    public static void main(String[] args) {
        Game minesweeper = new Game();

        minesweeper.setWidth(10);
        minesweeper.setHeight(10);
        minesweeper.setNumberOfMines(15);
        minesweeper.setZoneSize(40);

        minesweeper.Start();
    }

}
