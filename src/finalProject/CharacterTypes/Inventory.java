/*
 * Author: Neumann Davila
 * Date:   Nov 21, 2022
 * Description:
 *
 * This object stores Item objects for the player or NPC so that I can keep track of
 * what the Character has on their person. This also manages money weapons, and armor that
 * linked to the character
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
    private Event displayInventory = new Event("What would you like to do in your inventory", false);
    Choice exit = new Choice("Exit", () -> {});

    //TODO create item interaction feature to eliminate unnecessary ArrayLists

                        //  ---Equippable Items---  \\
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    private Weapon equippedWeapon = null;

                        //  ---Money---  \\
    private int money = 0;

    public String toString() {
        String summary = "";
        for (int i = 0; i < lastEmptyCell; i++) {
            summary += (1 + i) + ": " + this.inventory[i] + "\n";
        }
        return summary;
    }

    public void interact(int index) {
        Item tempItem = inventory[index];
        System.out.println(index);
        Event interact = new Event("What would you like to do with " + tempItem, false);
                //  Creates discard method
        Event confirmDis = new Event("Are you sure you want to discard: " + tempItem, false);
        confirmDis.addChoice(new Choice("Yes", () -> {removeItem(index);}));
        confirmDis.addChoice(new Choice("No", () -> {}));

        interact.addChoice(new Choice("Discard",() -> {confirmDis.displayEvent();interact(index);}));

        System.out.println(tempItem);
        if(tempItem instanceof Weapon) {
            interact.addChoice(new Choice("Equip " + tempItem, () -> {setEquippedWeapon((Weapon) tempItem);}));
        }
        else if (tempItem instanceof Armor) {

        }

        interact.addChoice(new Choice("Exit", () -> {displayInventory.removeChoice(exit);display();}));

        interact.displayEvent();
    }

    // Displays the Inventory & adds the exit choice inventory Event and later removes it
    public void display() {
        displayInventory.addChoice(exit);

        displayInventory.displayEvent();

        displayInventory.removeChoice(exit);
    }
    public Item[] getInventory() {
        return this.inventory;
    }
    public Item getItem(int index) {
        return inventory[index];
    }

    public void removeItem(int index) {
        if(inventory[index] instanceof Weapon) {
            if(this.equippedWeapon == inventory[index]) {
                weapons.remove(inventory[index]);
                inventory[index - 1] = null;
            }
            else {
                weapons.remove(inventory[index]);
                inventory[index] = null;
            }
        }
        else {
            inventory[index] = null;
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
                System.out.println("" + newItem);
                this.inventory[this.lastEmptyCell] = newItem;
                int tempInt = lastEmptyCell;
                displayInventory.addChoice(new Choice("" + newItem,() -> {interact(tempInt);}));
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
