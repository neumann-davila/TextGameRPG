/*
 * Author: Neumann Davila
 * Date:   May 08, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.CharacterTypes.Inventory;
import finalProject.Items.*;
import finalProject.Items.Weapons.Weapon;
import finalProject.TextGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemPanel extends JPanel {
    Inventory inventory = TextGame.player.getInventory();

    private JPanel buttons = new JPanel();
    private JButton discard = new JButton("Discard");
    private JButton equip = new JButton("Equip");

    public void add(JButton button){
        buttons.add(button);
    }
    public ItemPanel(Item item) {
        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();

        cons.anchor = GridBagConstraints.WEST;
        cons.gridy= 0;
        add(new JLabel("What would you like to do with " + item.getDisplayName()), cons);


        buttons.setLayout(new GridLayout(3, 1));

        cons.gridy = 1;
        cons.ipady = 5;
        add(buttons, cons);
        discard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventory.remove(item);
            }
        });

        buttons.add(discard);

        if(item instanceof Weapon){

            equip.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inventory.unequip(inventory.getEquippedWeapon());
                    inventory.remove(item);
                    inventory.setEquippedWeapon((Weapon) item);
                    TextGame.graphics.updatePlayer();
                    TextGame.graphics.updateInventory();
                }
            });
            buttons.add(equip);

        }
        else if(item instanceof Armor) {
            Armor armor = (Armor) item;

            equip.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inventory.unequip(inventory.getArmor(armor.getArmorType()));
                    inventory.remove(item);
                    inventory.equipArmor(armor);
                    TextGame.graphics.updatePlayer();
                    TextGame.graphics.updateInventory();
                }
            });

            buttons.add(equip);
        }


    }
}
