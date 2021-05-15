import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The View and Controller of the Game.
 *  @author Hieu Hoang
 */

public class MancalaBoardView extends JFrame implements ChangeListener {

    private static final int LAST_PIT_OF_A = 5;
    private static final int LAST_PIT_OF_B = 11;
    private static final int FIRST_PIT_OF_B = 6;

    private static final int A_TURN = 0;
    private static final int B_TURN = 1;

    private static final int MAX_NUM_OF_UNDOS = 3;

    private MancalaBoardModel theModel;
    private BoardOrientation boardOrientation;	// general strategy
    private ArrayList<JButton> pits = new ArrayList<>();// JButtons Representing Pits

    private boolean mancalaHasReached = false;
    private int undoMoveA;
    private int undoMoveB;
    private int currentTurn; //0 - A; 1 - B

    /**
     * Constructs a MancalaBoardView with no model
     */
    public MancalaBoardView() {
        this.theModel = null;
        undoMoveA = MAX_NUM_OF_UNDOS;
        undoMoveB = MAX_NUM_OF_UNDOS;
        currentTurn = A_TURN;
        DisplayMainMenu();
    }
    
    /**
     * Constructs a MancalaBoardView with model 
     * @param theModel the model
     */
    public MancalaBoardView(MancalaBoardModel theModel) {
        this.theModel = theModel;
        undoMoveA = MAX_NUM_OF_UNDOS;
        undoMoveB = MAX_NUM_OF_UNDOS;
        currentTurn = A_TURN;
        DisplayMainMenu();
    }

    public void DisplayMainMenu() {
        /**
         * JFrame show the menu list of button to choose the desired style of the Mancala Board.
         * Mancala board is visualized based on selected choice.
         */
        //Create Frame for selecting number of stones and style of board
        JFrame frame = new JFrame("Main Menu");
        frame.setLayout(new FlowLayout());
        /*
         * JButton to select style of the board and the number of stones
         */
        JButton horizontal_3_stones = new JButton("3 stones - Horizontal Display");
        JButton horizontal_4_stones = new JButton("4 stones - Horizontal Display");
        JButton vertical_3_stones = new JButton("3 stones - Vertical Display");
        JButton vertical_4_stones = new JButton("4 stones - Vertical Display");

        horizontal_3_stones.addActionListener(e -> {

            theModel.initializeTheBoard(3);
            boardOrientation = new HorizontalStyleBoard();
            frame.dispose();
            displayBoard();
        });


        horizontal_4_stones.addActionListener(e -> {
            theModel.initializeTheBoard(4);
            boardOrientation = new HorizontalStyleBoard();
            frame.dispose();
            displayBoard();
        });

        vertical_3_stones.addActionListener(e -> {
            theModel.initializeTheBoard(3);
            boardOrientation = new VerticalStyleBoard();
            frame.dispose();
            displayBoard();
        });

        vertical_4_stones.addActionListener(e -> {
            theModel.initializeTheBoard(4);
            boardOrientation = new VerticalStyleBoard();
            frame.dispose();
            displayBoard();
        });

        frame.add(horizontal_3_stones);
        frame.add(horizontal_4_stones);
        frame.add(vertical_3_stones);
        frame.add(vertical_4_stones);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    

    /**
     * Display the Mancala board.
     */
    public void displayBoard()
    {
        setLayout(new BorderLayout(20, 20));

        // Buttons representing pits belonging to player A
        JButton A_pit_1_Button = new JButton();
        JButton A_pit_2_Button = new JButton();
        JButton A_pit_3_Button = new JButton();
        JButton A_pit_4_Button = new JButton();
        JButton A_pit_5_Button = new JButton();
        JButton A_pit_6_Button = new JButton();

        // Buttons representing pits belonging to player B
        JButton B_pit_1_Button = new JButton();
        JButton B_pit_2_Button = new JButton();
        JButton B_pit_3_Button = new JButton();
        JButton B_pit_4_Button = new JButton();
        JButton B_pit_5_Button = new JButton();
        JButton B_pit_6_Button = new JButton();

        // Add JButtons belonging to player A to ArrayList of pits
        pits.add(A_pit_1_Button);
        pits.add(A_pit_2_Button);
        pits.add(A_pit_3_Button);
        pits.add(A_pit_4_Button);
        pits.add(A_pit_5_Button);
        pits.add(A_pit_6_Button);

        // Add JButtons belonging to player B to ArrayList of pits
        pits.add(B_pit_1_Button);
        pits.add(B_pit_2_Button);
        pits.add(B_pit_3_Button);
        pits.add(B_pit_4_Button);
        pits.add(B_pit_5_Button);
        pits.add(B_pit_6_Button);

        //northPanel
        JTextField textFieldMessage = new JTextField(40);
        textFieldMessage.setText("Game on!");

        for(int i = 0; i < pits.size(); i ++) {

            // Set the background of each pit JButton to light brown
            pits.get(i).setBackground(new Color(102, 102, 0));
            pits.get(i).setPreferredSize(new Dimension(100,60));
            pits.get(i).setBorderPainted(false);

            // Add a listener to pits and update model if button is pressed
            pits.get(i).addMouseListener(new PitMouseListener(i) {
                public void mousePressed(MouseEvent e) {
                    int mouseID = this.getMouseListenerID();
                    //prevents player A from going on player B's turn
                    if(currentTurn == B_TURN && mouseID <= LAST_PIT_OF_A) {
                        textFieldMessage.setText("It's B's turn.");
                    }
                    //prevents player B from going on player A's turn
                    else if(currentTurn == A_TURN && mouseID <= LAST_PIT_OF_B && mouseID >= FIRST_PIT_OF_B) {
                        textFieldMessage.setText("It's A's turn.");
                    }
                    else if (mouseID >= 6 && theModel.getAmountInPit(mouseID + 1) == 0) {
                        textFieldMessage.setText("Oops, no stones in this pit!");
                    }
                    else if (mouseID < 6 && theModel.getAmountInPit(mouseID) == 0) {
                        textFieldMessage.setText("Oops, no stones in this pit!");
                    }
                    else if(currentTurn == A_TURN && mouseID <= LAST_PIT_OF_A) {
                        theModel.move(mouseID);
                        if(theModel.isLastStoneOnBoard()) {
                            currentTurn = A_TURN;
                            textFieldMessage.setText("Try again!");
                            mancalaHasReached = true;
                        } else {
                            currentTurn = B_TURN;
                            mancalaHasReached = false;
                        }
                        undoMoveB = MAX_NUM_OF_UNDOS;
                    }
                    else if(currentTurn == B_TURN && mouseID <= LAST_PIT_OF_B && mouseID >= FIRST_PIT_OF_B) {
                        theModel.move(mouseID);
                        if(theModel.isLastStoneOnBoard()) {
                            currentTurn = B_TURN;
                            textFieldMessage.setText("go again!");
                            mancalaHasReached = true;
                        } else {
                            currentTurn = A_TURN;
                            mancalaHasReached = false;
                        }
                        undoMoveA = MAX_NUM_OF_UNDOS;
                    }


                    //Check if the game is over
                    int gameOverFlag = theModel.checkIfGameOver();

                    if (gameOverFlag > 0) {
                        int winner = theModel.checkWinner(gameOverFlag);
                        if (winner == 1)
                            textFieldMessage.setText("Congratulation, Player A won!");
                        else if (winner == 2)
                            textFieldMessage.setText("Congratulation, Player B won!");
                        else if (winner == 3)
                            textFieldMessage.setText("It's a tie, go again!");
                    }
                }
            });
        }

        int [] mancalaData = theModel.getCurrentBoard();

        // Center
        JLabel center = new JLabel(boardOrientation.createBoard());
        boardOrientation.addStonesToPits(pits, mancalaData);
        boardOrientation.addPitsToBoard(pits, center);
        add(center, BorderLayout.CENTER);

        // North
        JPanel northPanel = new JPanel();
        northPanel.add(textFieldMessage);
        add(northPanel, BorderLayout.NORTH);

        // South
        JPanel southPanel = new JPanel();

        JTextField undoCountText = new JTextField(20);
        undoCountText.setText("Number of undos: 3");

        JButton undoButton = new JButton("undo");
        undoButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {

                if(Arrays.equals(theModel.getCurrentBoard(), theModel.getPreviousBoard())) {
                    undoCountText.setText("Oops! No move to undo");
                }

                else if (currentTurn == B_TURN && undoMoveA <= 0) {
                    undoCountText.setText("Oops! Undo max has been reached.");
                }
                else if (currentTurn == A_TURN && undoMoveB <= 0) {
                    undoCountText.setText("Oops! Undo max has been reached.");
                }
                else if (mancalaHasReached && currentTurn == B_TURN) {
                    undoMoveB--;
                    undoCountText.setText("Number of undos: " + undoMoveB);
                    theModel.undoMove();
                    if(Arrays.equals(theModel.getCurrentBoard(), theModel.getPreviousBoard())) {
                        currentTurn = B_TURN;//still B's turn since B got a free turn for reaching own Mancala
                    }
                    mancalaHasReached = false;
                }
                else if (mancalaHasReached && currentTurn == A_TURN) {
                    undoMoveA--;
                    undoCountText.setText("Number of undos: " + undoMoveA);
                    theModel.undoMove();
                    if(Arrays.equals(theModel.getCurrentBoard(), theModel.getPreviousBoard())) {
                        currentTurn = A_TURN;//still A's turn since A got a free turn for reaching own Mancala
                    }
                    mancalaHasReached = false;
                }
                else if (currentTurn == A_TURN) { //was B's turn and A did not reach Mancala, then B undo so turn goes back to B
                    undoMoveB--;
                    undoCountText.setText("Number of undos: " + undoMoveB);
                    theModel.undoMove();
                    currentTurn = B_TURN;
                }
                else if (currentTurn == B_TURN) { //was A's turn and A did not reach Mancala, then A undo so turn goes back to A
                    undoMoveA--;
                    undoCountText.setText("Number of undos: " + undoMoveA);
                    theModel.undoMove();
                    currentTurn = A_TURN;
                }
            }
        });

        southPanel.add(undoCountText);
        southPanel.add(undoButton);
        add(southPanel, BorderLayout.SOUTH);

        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Called when the data in the model is changed.
     * @param e the event representing the change
     */
    public void stateChanged(ChangeEvent e) {
        int[] mancalaData= theModel.getCurrentBoard();
        boardOrientation.addStonesToPits(pits, mancalaData);
        repaint();
    }

}