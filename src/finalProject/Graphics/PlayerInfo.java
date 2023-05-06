/*
 * Author: Neumann Davila
 * Date:   Apr 18, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PlayerInfo extends JComponent {

    private JLabel name = new JLabel("Name Unknown");

    JPanel mainPanel = new JPanel();
    JPanel genInfo = new JPanel();
    JPanel inventory = new JPanel();

    JLabel health = new JLabel("*");
    JLabel level = new JLabel("*");
    JLabel exp = new JLabel("*");
    JLabel strength = new JLabel("*");
    JLabel dexterity = new JLabel("*");
    JLabel charisma = new JLabel("*");

    private void buildMainPanel(){

        /*
         * Main Panel contains two seperate Panels
         *
         * 1.) Gen Info:
         *      This contains general and current player data, like stats, equipped items, health, etc...
         *
         * 2.) Inventory:
         *      Contains an interactive inventory system that will change depending on what items are being equipped
         */

        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();

        mainPanel.add(genInfo);


                //  ---Gen Info---  \\

        genInfo.setLayout(new GridBagLayout());


            //  Start of Column 1
        cons.gridx = 0;
        cons.gridy = 0;
        genInfo.add(new JLabel("Health: "), cons);

        cons.gridx = 0;
        cons.gridy = 1;
        genInfo.add(new JLabel("Level: "), cons);

        cons.gridx = 0;
        cons.gridy = 2;
        genInfo.add(new JLabel("Exp: "), cons);

        cons.gridx = 0;
        cons.gridy = 3;
        genInfo.add(new JLabel("Head: "), cons);

        cons.gridy = 4;
        genInfo.add(new JLabel("Chest: "), cons);

        cons.gridy = 5;
        genInfo.add(new JLabel("Legs: "), cons);

        cons.gridy = 6;
        genInfo.add(new JLabel("Feet: "), cons);

            // values that correlate to col 1

        cons.ipadx = 30;

        cons.gridx = 1;
        cons.gridy = 0;
        genInfo.add(health, cons);

        cons.gridx = 1;
        cons.gridy = 1;
        genInfo.add(level, cons);

        cons.gridx = 1;
        cons.gridy = 2;
        genInfo.add(exp, cons);

        cons.ipadx = 0;

            // Start of Column 2

        cons.gridx = 2;
        cons.gridy = 0;
        genInfo.add(new JLabel("Strength: "), cons);

        cons.gridx = 2;
        cons.gridy = 1;
        genInfo.add(new JLabel("Dexterity: "), cons);

        cons.gridx = 2;
        cons.gridy = 2;
        genInfo.add(new JLabel("Charisma: "), cons);

        cons.gridx = 2;
        cons.gridy = 3;
        genInfo.add(new JLabel("Coins: "), cons);

        cons.gridy = 4;
        genInfo.add(new JLabel("Weapon: "), cons);


                // Start of Values relating to col2

        cons.gridx = 3;
        cons.gridy = 0;
        genInfo.add(strength, cons);

        cons.gridx = 3;
        cons.gridy = 1;
        genInfo.add(dexterity, cons);

        cons.gridx = 3;
        cons.gridy = 2;
        genInfo.add(charisma, cons);


        inventory.setLayout(new GridLayout(4, 2));



    }

    public PlayerInfo() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBorder(new EmptyBorder(10, 10 ,10, 10));
        GridBagConstraints mainCons = new GridBagConstraints();
        mainCons.anchor = GridBagConstraints.WEST;
        mainCons.ipadx = 10;

        setLayout(new GridBagLayout());

        mainCons.gridy = 0;
        add(name, mainCons);

        mainCons.gridy = 1;
        add(mainPanel, mainCons);

        buildMainPanel();


    }

}
