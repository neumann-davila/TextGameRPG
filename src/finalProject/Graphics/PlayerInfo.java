/*
 * Author: Neumann Davila
 * Date:   Apr 18, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.CharacterTypes.*;
import finalProject.Items.Armor;
import finalProject.Items.Item;
import finalProject.Items.Weapons.Weapon;
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

    private GridBagConstraints cons = new GridBagConstraints();



    public void createItemPanel(ItemDisplay button){
        Item item = button.getItem();

        for(ActionListener listener: button.getActionListeners()){
            button.removeActionListener(listener);
        }

        Inventory playerInventory = TextGame.player.getInventory();

        JPanel itemPanel = new JPanel();
        JPanel buttons = new JPanel();

        JButton discard = new JButton("Discard");
        JButton equip = new JButton("Equip");
        JButton exit = new JButton("Exit");

        cons.anchor = GridBagConstraints.NORTHWEST;



        JPanel confirmButtons = new JPanel();
        confirmButtons.setLayout(new GridLayout(2, 1));

        JPanel confirm = new JPanel();
        confirm.setLayout(new GridBagLayout());

        cons.gridy = 1;
        cons.gridx = 1;
        add(confirm, cons);


        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");

        discard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(itemPanel);


                SpinnerNumberModel numberModel = new SpinnerNumberModel(1, 1, item.getAmount(), 1);
                JSpinner amountDiscard = new JSpinner(numberModel);

                cons.gridy = 0;
                cons.gridx = 0;
                confirm.add(new JLabel("Are you sure you want to remove " + item.getDisplayName()), cons);

                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        remove(confirm);
                        TextGame.graphics.updateInventory();
                    }
                });

                    // Creates two separate question styles based on if there is only one object or multiple
                if(item.equals(playerInventory.getEquippedWeapon())){
                    yes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            playerInventory.setEquippedWeapon(new Weapon());
                            remove(confirm);
                            TextGame.graphics.updateInventory();
                            TextGame.graphics.revalidate();

                        }
                    });
                    confirm.remove(0);
                    cons.gridy = 1;
                    confirm.add(new JLabel("Are you sure you want to unequip and discard " + playerInventory.getEquippedWeapon().getName()), cons);

                }
                else if(item instanceof Armor && item.equals(playerInventory.getArmor(((Armor) item).getArmorType()))){
                    int armorType = ((Armor) item).getArmorType();
                    yes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            playerInventory.unequip(playerInventory.getArmor(armorType));
                            remove(confirm);
                            TextGame.graphics.updateInventory();
                            TextGame.graphics.revalidate();
                        }
                    });
                    confirm.remove(0);
                    cons.gridy = 1;
                    confirm.add(new JLabel("Are you sure you want to unequip and discard " + playerInventory.getArmor(armorType).getName()), cons);
                }
                else if(item.getAmount() == 1){
                    yes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            playerInventory.remove(item);
                            remove(confirm);
                            TextGame.graphics.updateInventory();
                        }
                    });

                }
                else if(item.getAmount() > 1){
                    yes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            playerInventory.remove(item, (Integer)amountDiscard.getValue());
                            remove(confirm);
                            TextGame.graphics.updateInventory();
                        }
                    });

                    cons.gridy = 1;
                    confirm.add(new JLabel("Please input the amount you want to remove"), cons);

                    cons.gridy = 2;
                    confirm.add(amountDiscard, cons);

                }
                cons.gridy++;

                confirm.add(confirmButtons, cons);
                confirmButtons.add(yes);
                confirmButtons.add(no);

                TextGame.graphics.revalidate();

            }
        });

        buttons.add(discard);

            //Determines if the Item requires and equip option

        if(item instanceof Weapon){

            equip.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(itemPanel);

                    cons.gridy = 0;
                    cons.gridx = 0;
                    confirm.add(new JLabel("Are you sure you want to equip " + item.getDisplayName()), cons);

                    if(!playerInventory.getEquippedWeapon().getName().equals(new Weapon().getName())){
                        cons.gridy++;
                        confirm.add(new JLabel("If you equip this you will unequip " + playerInventory.getEquippedWeapon().getDisplayName()), cons);
                    }


                    yes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            remove(confirm);
                            playerInventory.unequip(playerInventory.getEquippedWeapon());
                            playerInventory.remove(item);
                            playerInventory.setEquippedWeapon((Weapon) item);
                            TextGame.graphics.updatePlayer();
                            TextGame.graphics.updateInventory();
                        }
                    });

                    cons.gridy++;

                    confirm.add(confirmButtons, cons);
                    confirmButtons.add(yes);
                    confirmButtons.add(no);

                    TextGame.graphics.revalidate();

                }
            });
            buttons.add(equip);

        }
        else if(item instanceof Armor) {
            Armor armor = (Armor) item;

            equip.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(itemPanel);

                    cons.gridy = 0;
                    cons.gridx = 0;
                    confirm.add(new JLabel("Are you sure you want to equip " + armor.getDisplayName()), cons);

                    if(playerInventory.getArmor(armor.getArmorType()) != null){
                        cons.gridy++;
                        confirm.add(new JLabel("If you equip this you will unequip " + playerInventory.getArmor(armor.getArmorType()).getDisplayName()), cons);
                    }
                    yes.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            remove(confirm);
                            playerInventory.unequip(playerInventory.getArmor(armor.getArmorType()));
                            playerInventory.remove(item);
                            playerInventory.equipArmor(armor);
                            TextGame.graphics.updatePlayer();
                            TextGame.graphics.updateInventory();
                        }
                    });
                    cons.gridy++;

                    confirm.add(confirmButtons, cons);
                    confirmButtons.add(yes);
                    confirmButtons.add(no);

                    TextGame.graphics.revalidate();
                }
            });
            buttons.add(equip);
        }

            //  Adds the Exit option to the list, returning to the inventory view
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(itemPanel);
                TextGame.graphics.updateInventory();
            }
        });

        buttons.add(exit);

            //gives the ItemDisplay button an action listener that creates the panel when called
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(inventory);
                cons.gridy = 1;
                cons.gridx = 1;
                add(itemPanel, cons);
                TextGame.graphics.revalidate();
            }
        });


        itemPanel.setLayout(new GridBagLayout());

        buttons.setLayout(new GridLayout(3, 1));

        cons.gridy = 0;
        cons.gridx = 0;
        itemPanel.add(new JLabel("What would you like to do with " + button.getItem().getDisplayName()));

        cons.gridy = 1;
        itemPanel.add(buttons, cons);

    }

    public void updatePlayer() {
        StatManager stats = player.getStats();
        Inventory playerInventory = player.getInventory();

        ItemDisplay[] armor = new ItemDisplay[4];
        armor[0] = equippedHead;
        armor[1] = equippedChest;
        armor[2] = equippedLeg;
        armor[3] = equippedBoot;

        name.setText(player.getName());
        health.setText(player.healthBar());
        coins.setText("" + playerInventory.getMoney());

        strength.setText("" + stats.getStrength().getStat());
        charisma.setText("" + stats.getCharisma().getStat());
        dexterity.setText("" + stats.getDexterity().getStat());
        exp.setText("" + stats.getXp().getStat());
        level.setText("" + stats.getLevel());

        for (int i = 1; i <= 4; i++) {
            if (playerInventory.getArmor(i) != null) {
                armor[i - 1].setItem(playerInventory.getArmor(i));
                createItemPanel(armor[i - 1]);
            }
        }

        equippedWeapon.setItem(playerInventory.getEquippedWeapon());
        if(!equippedWeapon.getItem().getName().equals("Fist")){
            createItemPanel(equippedWeapon);
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
                ItemDisplay temp = new ItemDisplay(playerInventory.get(i));

                createItemPanel(temp);

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
