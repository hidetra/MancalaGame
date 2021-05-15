/**
 *  Dr. Kim - Object Oriented Design
 *  @project Mancala Board Game
 *  @team Shark
 *  @author Aria Rostami, Aryan Vaid, Hieu Hoang
 *  @date 04/12/2020
 *  @version 1.0
 */

/**
 * Main class to run the Mancala Game
 */
public class MancalaBoardGameTester {
    public static void main(String[] args) {
        MancalaBoardModel mancalaBoard = new MancalaBoardModel();
        MancalaBoardView view = new MancalaBoardView(mancalaBoard);
        mancalaBoard.attach(view);
    }
}