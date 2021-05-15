import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class serves as the model of the MVC. This model contains the data for the stones
 * in a particular pit at the moment, which is held in 2 arrays. One array contains the current state of the board while the other contains the previous
 * state of the board to allow a user a certain amount of undo moves per turn.  An ArrayList is used to hold the ChangeListeners that perform an
 * action once the model notifies them of changes. This class also contains accessors and mutator methods for the data and states of the game.
 * @team Sharks
 *  @author Aria Rostami, Aryan Vaid, Hieu Hoang
 * @version 1.0
 */

public class MancalaBoardModel {


    private int[] previousBoard;
    private int[] currentBoard;
    private boolean lastStoneOnBoard;
    private ArrayList<ChangeListener> arrayOfListeners; //will only contain View Listener
    private int numberOfUndos;
    private static final int NUMBER_OF_PITS = 14;

    //Position of Mancala A and B in the Array
    private static final int MANCALA_A = 6;
    private static final int MANCALA_B = 13;


    /**
     * Constructs an empty MancalaBoardModel.
     */
    public MancalaBoardModel() {
        currentBoard = new int[NUMBER_OF_PITS];
        previousBoard = currentBoard.clone(); // or null?
        lastStoneOnBoard = false;
        arrayOfListeners = new ArrayList<>();
        numberOfUndos = 0;
    }

    /**
     * Sets the number of stones in each pit in the mancala board, excluding the mancalas.
     * @param stonesPerPit the number of stones in each pit
     */
    public void initializeTheBoard(int stonesPerPit) {

        for (int i = 0; i < currentBoard.length; i++) {
            if (i == MANCALA_A || i == MANCALA_B) {
                currentBoard[i] = 0;
            } else {
                currentBoard[i] = stonesPerPit;
            }
        }

        previousBoard = currentBoard.clone();
    }


    /**
     * Constructs a Mancala Board with specified number of stones in each pit, and 0 stones in Mancalas
     * @param stonesPerPit the number of stones each pit initially contains
     */
    public MancalaBoardModel(int stonesPerPit) {

        /*
         * index 0 - 6: player A, index 7 - 13: B
         * index 6 and 13 correspond to Mancalas
         * Layout of the Board:
         * A1 - A6 - Mancala A
         * B1 - B6 - Mancala B 
         * */

        currentBoard = new int[] { stonesPerPit, stonesPerPit, stonesPerPit, stonesPerPit, stonesPerPit, stonesPerPit, 0,
                stonesPerPit, stonesPerPit, stonesPerPit, stonesPerPit, stonesPerPit, stonesPerPit, 0 };

        previousBoard = currentBoard.clone();
        lastStoneOnBoard = false;
        arrayOfListeners = new ArrayList<>();
        numberOfUndos = 0;
    }

    /**
     * Gets the current board.
     * @return the current board
     */
    public int[] getCurrentBoard() {
        return currentBoard.clone();
    }

    /**
     * Gets the previous board.
     * @return the previous board
     */
    public int[] getPreviousBoard() {
        return previousBoard.clone();
    }
    /**
     * Gets the number of stones in specified pit number.
     * @param pitNumber the pit number
     * @return the number of stones in the specified pit number
     */
    public int getAmountInPit(int pitNumber) {
        return currentBoard[pitNumber];
    }

    /**
     * Checks if the last stone was dropped in a Mancala.
     * @return the boolean value of lastStoneInMancala; true allows player another turn
     */
    public boolean isLastStoneOnBoard() //we will use this in Control to prompt player to go again
    {
        return lastStoneOnBoard;
    }


    /**
     * Determines the winner of the game by comparing the number of stones collected in each players Mancala after
     * all the remaining stones in the board are properly distributed.
     * The winner has the greater number of stones.
     * @param emptyPitFlag value passed in that dictates the player that receives the remaining stones on the board
     * @return 1 if winner is A or
     *         2 if the winner is B or
     *         3 if there is a tie
     */
    public int checkWinner(int emptyPitFlag){

        if (emptyPitFlag == 1){ //only all A pits are empty
            moveStonesToMancala(7, MANCALA_B); //move leftover stones to Mancala B
        }
        else if (emptyPitFlag == 2){ //only all B pits are empty
            moveStonesToMancala(0, MANCALA_A); //move leftover stones to Mancala A
        }

        //Compare number of stones in two mancalas
        if (currentBoard[MANCALA_A] > currentBoard[MANCALA_B])
            return 1;
        else if (currentBoard[MANCALA_A] < currentBoard[MANCALA_B])
            return 2;
        else return 3;
    }


    /**
     * Checks if the game is over when pits belonging to either player are all empty.
     * @return  0 if game is not over
     *          1 if game is over and only A pits are empty
     *          2 if game is over and only B pits are empty
     */
    public int checkIfGameOver() {

        boolean mancala_A_Empty = false, mancala_B_Empty = false;

        //Check if all A's pits are empty
        for (int pitA = 0; pitA < MANCALA_A; pitA++){
            if (currentBoard[pitA] != 0){
                mancala_A_Empty = false;
                break;
            } else mancala_A_Empty = true;
        }

        //Check if all B's pits are empty
        for (int pitB = 7; pitB < MANCALA_B; pitB++){
            if (currentBoard[pitB] != 0){
                mancala_B_Empty = false;
                break;

            } else mancala_B_Empty = true;
        }
        return mancala_A_Empty? 1: (mancala_B_Empty? 2 : 0);
//        if (mancala_A_Empty) return 1;
//        else if (mancala_B_Empty) return 2;

//        return 0;
    }



    /**
     * Sums up the remaining stones on the board and adds that to the mancala specified by the mancalaPos, 
     * then notifies the listeners of the change in the model's state.
     * @param pitPos the starting position of the summation.
     * @param mancalaPos the position of the mancala that will receive the rest of the stones.
     */
    public void moveStonesToMancala(int pitPos, int mancalaPos){
        for (int i = pitPos; i < mancalaPos; i++){
            currentBoard[mancalaPos] += currentBoard[i];
            currentBoard[i] = 0;
        }
        for (ChangeListener l : arrayOfListeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Given the pit number and the number of stones currently in the pit, the function redistributes the stones across
     * the board according to the rules of the mancala game.
     * First exception is when a player has their last stone dropped in their own Mancala, resulting in a free turn.
     * Second exception is when a player's last stone dropped is in an empty pit on their side of the board.
     * This results in the player getting to add the stones in the last pit and the stones in the pit across from that on
     * the opponent's side into their own Mancala.
     * @param pitNumber the pit number chosen by the user
     */
    public void move(int pitNumber) { //pit that is pressed by user

        boolean turnA = true;
        int ownPitNumber = pitNumber;
        if(pitNumber > 5) {
            turnA = false;
            pitNumber = pitNumber +1;
            ownPitNumber = pitNumber - 7;
        }
        if(currentBoard[pitNumber] != 0) {
            previousBoard = currentBoard.clone(); //save board prior to move to allow undo option
            lastStoneOnBoard = false;
            //save the number of stones in the pit number in variable stoneCount
            int stoneCount = currentBoard[pitNumber];
            int oPitNumber = pitNumber;
            int endingPit = pitNumber + stoneCount;

            boolean opponMancReached = false;
            if(ownPitNumber + stoneCount >= 13) {
                opponMancReached = true;
            }

            //If a player's last stone lands in their own Mancala, that player gets another turn, so set lastStoneInMancala = true
            if(ownPitNumber + stoneCount == 6) {
                lastStoneOnBoard = true;
                //System.out.println("You get another turn!");
            }

            //remove stones from chosen pit and redistribute them
            for(int i = 1; i <= stoneCount; i++) {
                if ((pitNumber + i) == 14) {
                    pitNumber = -1*i;//Looping around the board once b6 pit has been reached
                }
                currentBoard[pitNumber + i] = currentBoard[pitNumber + i] + 1;
            }
            //set the number of stones in specified pit number to 0
            currentBoard[oPitNumber] = 0;
		
			/*A's last stone dropped lands in an empty pit on A's side, so A gets to add the stones in that last pit and the stones 
			on the opponent's side that is across from that last pit, into A's Mancala.*/
            if(turnA && endingPit <= 5 && previousBoard[endingPit] == 0 && !opponMancReached) {
                currentBoard[endingPit] = 0;
                int opponStones = currentBoard[endingPit + (2*(6-endingPit))];
                currentBoard[MANCALA_A] = currentBoard[MANCALA_A] + opponStones + 1;
                currentBoard[endingPit + (2*(6-endingPit))] = 0;
            }
            /*This time, B's last stone dropped lands in an empty pit on B's side, the same rule applies here.*/
            if(!turnA && endingPit > 6 && endingPit < 13 && previousBoard[endingPit] == 0 && !opponMancReached) {
                currentBoard[endingPit] = 0;
                int opponStones = currentBoard[endingPit - (2*(endingPit - 6))];
                currentBoard[MANCALA_B] = currentBoard[MANCALA_B] + opponStones + 1;
                currentBoard[endingPit - (2*(endingPit - 6))] = 0;
            }

            //When A passes through opponents side and reaches A's side with the last stone, will require at least 8 stones in the starting pit.
            if(turnA && opponMancReached) {
                currentBoard[13] = previousBoard[13];
                int nextPitToGetStone = ownPitNumber + stoneCount + 1;
                if(nextPitToGetStone > 13) {
                    nextPitToGetStone = nextPitToGetStone - 14;
                }
                currentBoard[nextPitToGetStone] = currentBoard[nextPitToGetStone] + 1;
                //When A passes through opponent's side and also lands in an empty pit
                if(previousBoard[nextPitToGetStone] == 0) {
                    currentBoard[nextPitToGetStone] = 0;
                    int opponStones = currentBoard[nextPitToGetStone + (2*(6-nextPitToGetStone))];
                    currentBoard[MANCALA_A] = currentBoard[MANCALA_A] + opponStones + 1;
                    currentBoard[nextPitToGetStone + (2*(6-nextPitToGetStone))] = 0;
                }
            }
            //When B passes through opponents side and reaches B's side with the last stone
            if(!turnA && opponMancReached) {
                currentBoard[6] = previousBoard[6];
                int nextPitToGetStone = ownPitNumber + stoneCount + 1;
                if(nextPitToGetStone > 13) {
                    nextPitToGetStone = nextPitToGetStone - 14;
                }
                currentBoard[nextPitToGetStone + 7] = currentBoard[nextPitToGetStone + 7] + 1;
                //B passes through the opponent's side and also lands in an empty pit
                if(previousBoard[nextPitToGetStone + 7] == 0) {
                    currentBoard[nextPitToGetStone + 7] = 0;
                    int opponStones = currentBoard[nextPitToGetStone + 7 + (2*(6 - (nextPitToGetStone + 7)))];
                    currentBoard[MANCALA_B] = currentBoard[MANCALA_B] + opponStones + 1;
                    currentBoard[nextPitToGetStone + 7 + (2*(6 - (nextPitToGetStone + 7)))] = 0;
                }
            }

            //to alert listeners of change
            for (ChangeListener l : arrayOfListeners) {
                l.stateChanged(new ChangeEvent(this));
            }
        }
    }

    /**
     * Makes the current board equivalent to the previous board.
     */
    public void undoMove() {
        currentBoard = previousBoard.clone();
        //to alert listeners of change
        for (ChangeListener l : arrayOfListeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /*
     * Adds a listener to ArrayList of listeners in the model.
     */
    public void attach(ChangeListener l) {

        arrayOfListeners.add(l);
    }

}