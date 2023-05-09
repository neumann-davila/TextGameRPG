/*
 * Author: Neumann Davila
 * Date:   Apr 18, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.CharacterTypes.*;
import finalProject.Items.Item;
import finalProject.TextGame;

import javax.sql.rowset.spi.TransactionalWriter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayerInfo extends JComponent {

    private Player player = TextGame.player;

    private JLabel name = new JLabel("Name Unknown");

    private JPanel mainPanel = new JPanel();
    private JPanel genInfo = new JPanel();
    private JPanel inventory = new JPanel();

    private JLabel health = new JLabel("*");
    private JLabel level = new JLabel("*");
    private JLabel exp = new JLabel("*");
    private JLabel strength = new JLabel("*");
    private JLabel dexterity = new JLabel("*");
    private JLabel charisma = new JLabel("*");

    private ItemDisplay equippedHead = new ItemDisplay();
    private ItemDisplay equippedChest = new ItemDisplay();
    private ItemDisplay equippedLeg = new ItemDisplay();
    private ItemDisplay equippedBoot = new ItemDisplay();
    private ItemDisplay equippedWeapon = new ItemDisplay();

    private JLabel coins = new JLabel("*");

    GridBagConstraints cons = new GridBagConstraints();


    public void updatePlayer() {
        StatManager stats = player.getStats();
        Inventory inventory = player.getInventory();

        ItemDisplay[] armor = new ItemDisplay[4];
        armor[0] = equippedHead;
        armor[1] = equippedChest;
        armor[2] = equippedLeg;
        armor[3] = equippedBoot;

        name.setText(player.getName());
        health.setText(player.healthBar());
        coins.setText("" + inventory.getMoney());

        strength.setText("" + stats.getStrength().getStat());
        charisma.setText("" + stats.getCharisma().getStat());
        dexterity.setText("" + stats.getDexterity().getStat());
        exp.setText("" + stats.getXp().getStat());
        level.setText("" + stats.getLevel());

        for (int i = 1; i <= 4; i++) {
            if (inventory.getArmor(i) != null) {
                armor[i - 1].setItem(inventory.getArmor(i));
            }
        }
    }

    public void clearInventory(){
        for(int i = 0;i < 8; i++){
            inventory.remove(0);
        }
    }
    public void updateInventory() {
        ArrayList<Item> playerInventory = player.getInventory().getInventory();

        clearInventory();


        for(int i = 0; i < 8; i++){
            try{
                Item tempItem = playerInventory.get(i);

                ItemDisplay temp = new ItemDisplay(tempItem);
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ItemPanel panel = new ItemPanel(tempItem);
                        JButton exit = new JButton("Exit");
                        exit.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                remove(panel);
                                TextGame.graphics.updateInventory();
                            }
                        });

                        cons.anchor = GridBagConstraints.NORTHWEST;

                        panel.add(exit);

                        remove(inventory);
                        cons.gridy = 1;
                        cons.gridx = 1;
                        add(panel, cons);
                        TextGame.graphics.revalidate();
                    }
                });

                this.inventory.add(temp);
            }
            catch(IndexOutOfBoundsException e) {
                this.inventory.add(new ItemDisplay());
            }
            catch(Exception e){
                System.out.println(e);
            }

            cons.gridy = 1;
            cons.gridx = 1;
            add(this.inventory, cons);
        }
    }
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

        cons.anchor = GridBagConstraints.WEST;

        mainPanel.add(genInfo);


                //  ---Gen Info---  \\

        genInfo.setLayout(new GridBagLayout());
        genInfo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

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
        cons.gridy = 4;
        genInfo.add(new JLabel("Head: "), cons);

        cons.gridy = 5;
        genInfo.add(new JLabel("Chest: "), cons);

        cons.gridy = 6;
        genInfo.add(new JLabel("Legs: "), cons);

        cons.gridy = 7;
        genInfo.add(new JLabel("Feet: "), cons);

            // values that correlate to col 1


        cons.gridx = 1;
        cons.gridy = 0;
        genInfo.add(health, cons);

        cons.gridx = 1;
        cons.gridy = 1;
        genInfo.add(level, cons);

        cons.gridx = 1;
        cons.gridy = 2;
        genInfo.add(exp, cons);

        cons.gridy = 4;
        genInfo.add(equippedHead, cons);

        cons.gridy = 5;
        genInfo.add(equippedChest, cons);

        cons.gridy = 6;
        genInfo.add(equippedLeg, cons);

        cons.gridy = 7;
        genInfo.add(equippedBoot, cons);

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

        cons.gridy = 3;
        genInfo.add(coins, cons);

        cons.gridy = 4;
        genInfo.add(equippedWeapon, cons);



        //  Inventory Menu



        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
        inventory.add(new ItemDisplay());
    }

    public PlayerInfo() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBorder(new EmptyBorder(10, 10 ,10, 10));
        cons.anchor = GridBagConstraints.WEST;


        setLayout(new GridBagLayout());

        cons.gridy = 0;
        add(name, cons);

        cons.gridx = 1;
        cons.anchor = GridBagConstraints.CENTER;
        add(new JLabel("Inventory"), cons);

        cons.gridy = 1;
        cons.gridx = 0;
        add(mainPanel, cons);

        cons.gridy = 1;
        cons.gridx = 1;
        add(inventory, cons);

        inventory.setLayout(new GridLayout(4,2));
        inventory.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buildMainPanel();

    }

}
