import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Horizontal Style Board
 *  @author Aria Rostami, Aryan Vaid, Hieu Hoang
 */

public class HorizontalStyleBoard implements BoardOrientation {

    // JButton for Mancala A
    JButton buttonMancalaA = new JButton();

    // JButton for Mancala B
    JButton buttonMancalaB = new JButton();

    /**
     * Create a vertical style board
     * Dimension 1000x320
     * @return the board as an Icon
     */

    @Override
    public Icon createBoard() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {

                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(160, 160, 160));

                Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, getIconWidth(), getIconHeight());
                g2.fill(rectangle);
            }

            @Override
            public int getIconWidth() {
                return 1000;
            }

            @Override
            public int getIconHeight() {
                return 320;
            }
        };
    }

    /**
     * Add stones to the pits 
     * @param pits - all JButtons of pits
     * @param pitsData - number of stones in each pit
     */
    @Override
    public void addStonesToPits(ArrayList<JButton> pits, int [] pitsData) {
        for(int i = 0; i < pitsData.length; i++) {
            Stone stones = new Stone(pitsData[i]);
            stones.setIconHeight(50);
            stones.setIconWidth(90);

            // Mancala A
            if(i == 6)
                buttonMancalaA.setIcon(stones);
            // Mancala B
            else if(i == 13)
                buttonMancalaB.setIcon(stones);
            // Other Pits
            else
            if(i > 6)
                pits.get(i - 1).setIcon(stones);
            else
                pits.get(i).setIcon(stones);
        }
    }

    /**
     * Add all the pits and labels to the board
     * @param pits - list of all JButtons of pits
     * @param label - main Mancala Board
     */
    @Override
    public void addPitsToBoard(ArrayList<JButton> pits, JLabel label)
    {

        //Create Mancala A and B pits as Buttons
        buttonMancalaA.setBackground(new Color(0, 76, 153));
        buttonMancalaA.setPreferredSize(new Dimension(100,140));
        buttonMancalaA.setOpaque(true);
        buttonMancalaA.setBorderPainted(false);

        buttonMancalaB.setBackground(new Color(0, 76, 153));
        buttonMancalaB.setPreferredSize(new Dimension(100,140));
        buttonMancalaB.setOpaque(true);
        buttonMancalaB.setBorderPainted(false);


        //Create Mancala A and B
        JButton mancalaA = new JButton("A");
        mancalaA.setFont(new Font("Arial", Font.PLAIN, 30));
        mancalaA.setBackground(new Color(160, 160, 160));
        mancalaA.setOpaque(true);
        mancalaA.setBorderPainted(false);

        JButton mancalaB = new JButton("B");
        mancalaB.setFont(new Font("Arial", Font.PLAIN, 30));
        mancalaB.setBackground(new Color(160, 160, 160));
        mancalaB.setOpaque(true);
        mancalaB.setBorderPainted(false);


        //Set layout for the board and add Mancala A and Mancala B
        label.setLayout(new FlowLayout());
        label.add(mancalaB);
        label.add(buttonMancalaB);

        //Panel to hold A and B pits + labels
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridLayout(4, 6, 10, 10));

        // Add pit labels for B to JPanel
        for(int i = 6; i > 0; i --) {
            JButton pitLabels = new JButton("B" + i);
            pitLabels.setFont(new Font("Arial", Font.PLAIN, 30));
            pitLabels.setBackground(new Color(160, 160, 160));;
            pitLabels.setOpaque(true);
            pitLabels.setBorderPainted(false);
            mainPanel.add(pitLabels);
        }


        //Add pits for B as a Button
        for (int i = pits.size() - 1; i >= 6; i--) {
            mainPanel.add(pits.get(i));
        }

        //Add pits for A as a Button
        for (int i = 0; i < 6; i++) {
            mainPanel.add(pits.get(i));
        }

        

        // Add pit labels for A to JPanel
        for (int i = 1; i <= 6; i++) {
            JButton pitLabels = new JButton("A" + i);
            pitLabels.setFont(new Font("Arial", Font.PLAIN, 30));
            pitLabels.setBackground(new Color(160, 160, 160));
            pitLabels.setOpaque(true);
            pitLabels.setBorderPainted(false);
            mainPanel.add(pitLabels);
        }
        
        label.add(mainPanel);
        label.add(buttonMancalaA);
        label.add(mancalaA);
    }

}