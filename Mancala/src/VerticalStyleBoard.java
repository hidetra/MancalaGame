import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Vertical Style Board
 * @author Aria Rostami, Aryan Vaid, Hieu Hoang
 */
public class VerticalStyleBoard implements BoardOrientation
{
    JButton buttonMancalaA = new JButton();
    JButton buttonMancalaB = new JButton();

    /**
     * Create vertical board style
     * Dimension 600x600
     * @return the board as an Icon
     */
    @Override
    public Icon createBoard() {
        Icon board = new Icon() {

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(160, 160, 160));

                Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, getIconWidth(), getIconHeight());
                g2.fill(rectangle);
            }

            @Override
            public int getIconWidth() {
                return 600;
            }

            @Override
            public int getIconHeight() {
                return 600;
            }
        };

        return board;
    }

    /**
     * Add all the pits and labels to the board
     * @param pits - list of all JButtons of pits
     * @param label - main Mancala Board
     */
    @Override
    public void addPitsToBoard(ArrayList<JButton> pits, JLabel label)
    {
        //Create Mancala A and B
        JButton mancalaB = new JButton("Mancala B");
        mancalaB.setFont(new Font("Arial", Font.PLAIN, 30));
        mancalaB.setBackground(new Color(160, 160, 160));
        mancalaB.setOpaque(true);
        mancalaB.setBorderPainted(false);

        JButton mancalaA = new JButton("Mancala A");
        mancalaA.setFont(new Font("Arial", Font.PLAIN, 30));
        mancalaA.setBackground(new Color(160, 160, 160));
        mancalaA.setOpaque(true);
        mancalaA.setBorderPainted(false);

        //Create mancala A and B pits as Buttons
        buttonMancalaA.setPreferredSize(new Dimension(200, 70));
        buttonMancalaA.setBackground(new Color(0, 76, 153));
        buttonMancalaA.setOpaque(true);
        buttonMancalaA.setBorderPainted(false);

        buttonMancalaB.setPreferredSize(new Dimension(200, 70));
        buttonMancalaB.setBackground(new Color(0, 76, 153));
        buttonMancalaB.setOpaque(true);
        buttonMancalaB.setBorderPainted(false);

        //Set layout for the board and add two mancala
        label.setLayout(new GridBagLayout());
        GridBagConstraints gbcons = new GridBagConstraints();
        gbcons.gridwidth = 0; // 1 column
        gbcons.gridheight = 3; // 3 rows
        gbcons.weighty = 1;
        gbcons.anchor = GridBagConstraints.CENTER;

        label.add(mancalaB, gbcons);
        label.add(buttonMancalaB, gbcons);

        //Panel to hold A and B pits + labels
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(6, 4, 5, 5));
        centerPanel.setOpaque(false);

        for (int i = 1, j = 6, k = 11; i <= 6; i++, j--, k--){
            //Add Pit A Labels
            JButton pitALabel = new JButton("A" + i);
            pitALabel.setFont(new Font("Arial", Font.PLAIN, 30));
            pitALabel.setBackground(new Color(160, 160, 160));
            pitALabel.setOpaque(true);
            pitALabel.setBorderPainted(false);
            centerPanel.add(pitALabel);

            //Add pits for A as a Button
            centerPanel.add(pits.get(i-1));

            //Add pits for B as a Button
            centerPanel.add(pits.get(k));

            //Add Pit B Labels
            JButton pitBLabel = new JButton("B" + j);
            pitBLabel.setFont(new Font("Aria", Font.PLAIN, 30));
            pitBLabel.setBackground(new Color(160, 160, 160));
            pitBLabel.setOpaque(true);
            pitBLabel.setBorderPainted(false);
            centerPanel.add(pitBLabel);
        }

        gbcons.gridy = GridBagConstraints.RELATIVE;
        label.add(centerPanel, gbcons);

        gbcons.gridy = GridBagConstraints.PAGE_END;
        label.add(buttonMancalaA, gbcons);

        gbcons.gridy = GridBagConstraints.RELATIVE;
        label.add(mancalaA, gbcons);

    }

    /**
     * Add stones to the pits
     * @param pits - all JButtons of pits
     * @param pitsData - number of stones in each pit
     */
    @Override
    public void addStonesToPits(ArrayList<JButton> pits, int [] pitsData) {
        for(int i = 0; i < pitsData.length; i++) {
            Stone stones  = new Stone(pitsData[i]);
            stones.setIconHeight(50);
            stones.setIconWidth(90);

            // Mancala A
            if(i == 6)
                buttonMancalaA.setIcon(stones);
                // Mancala B
            else if(i == 13)
                buttonMancalaB.setIcon(stones);
                // other Pits
            else
            if(i > 6)
                pits.get(i - 1).setIcon(stones);
            else
                pits.get(i).setIcon(stones);
        }
    }
}