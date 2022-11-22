/*
 * Author: Neumann Davila
 * Date:   Nov 21, 2022
 * Description:
 *
 *
 */
package finalProject.CharacterTypes;

import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.Items.*;

import java.util.Scanner;
import java.util.ArrayList;

public class Inventory {
    private Scanner input = new Scanner(System.in);

    private int lastEmptyCell;
    private Item[] inventory = new Item[8];
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    private Weapon equippedWeapon = null;
    private int money = 0;

    public String toString() {
        String summary = "";
        for (int i = 0; i < lastEmptyCell; i++) {
            summary += (1 + i) + ": " + this.inventory[i] + "\n";
        }
        return summary;
    }

    public Item getItem(int index) {
        return inventory[index];
    }

    public void removeItem(int index) {
        if(inventory[index - 1] instanceof Weapon) {
            if(this.equippedWeapon == inventory[index - 1]) {
                weapons.remove(inventory[index - 1]);
                inventory[index - 1] = null;
                EquipWeapon();
            }
            else {
                weapons.remove(inventory[index - 1]);
                inventory[index - 1] = null;
            }
        }
        else {
            inventory[index - 1] = null;
        }

        for(int i = 0; i < lastEmptyCell; i++) {
            if (inventory[i] == null) {
                inventory[i] = inventory[i + 1];
                inventory[i + 1] = null;
            }
        }
        lastEmptyCell--;
    }


    public void addItem(Item newItem) {

        if(!newItem.isStackable()) {
            if(lastEmptyCell < 8) {
                this.inventory[this.lastEmptyCell] = newItem;
                this.lastEmptyCell += 1;


                if(newItem instanceof Weapon) {
                    weapons.add((Weapon) newItem);
                    if(weapons.size() == 1) {
                        equippedWeapon = (Weapon) newItem;
                    }
                }

            }
            else {
                System.out.println("You have no space in your inventory!");
            }
        }
        else {
            boolean found = false;
            for(int i = 0; i < lastEmptyCell; i++) {
                if(newItem.getName().equals(inventory[i].getName())) {
                    inventory[i].adjustAmount(newItem.getAmount());
                    found = true;
                    break;
                }
            }
            if(!found) {
                this.inventory[this.lastEmptyCell] = newItem;
                this.lastEmptyCell += 1;
            }
        }
    }



    public Weapon getEquippedWeapon() {
        return this.equippedWeapon;
    }



    public ArrayList<Weapon> getWeapons() {
        return this.weapons;
    }

    public void setEquippedWeapon(Weapon weapon) {
        equippedWeapon = weapon;
    }

    public void purchaseItem(Item item) {
        if (item.getPrice() > this.money) {
            System.out.println("You do not have enough money for this");
        }
        else {
            adjustMoney(-(item.getPrice()));
            addItem(item);
            System.out.println("added" + item);
        }
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public void adjustMoney(int money) {
        this.money += money;
    }

    public Inventory() {

    }
}
