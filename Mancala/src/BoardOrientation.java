import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Interface representing strategies
 * @author Hieu Hoang, Aria, Aryan
 */
public interface BoardOrientation {
    /**
     * Create the board for the Mancala game
     * @return the board as an Icon
     */
    Icon createBoard();
    /**
     * Add stones to the pits
     * @param pits - Arraylist of JButtons i.e. pits that need to be added to the board
     * @param pitsData - Array of integers of number of stones in each pit
     */
    void addStonesToPits(ArrayList<JButton> pits, int[] pitsData);

    /**
     * Add all the pits and labels to the board 
     * @param pits - Arraylist of JButtons i.e. pits that need to be added to the board
     * @param label - the main board
     */
    void addPitsToBoard(ArrayList<JButton> pits, JLabel label);
}



