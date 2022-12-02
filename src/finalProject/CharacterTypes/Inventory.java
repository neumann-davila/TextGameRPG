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

import java.util.ArrayList;
import java.util.Scanner;
    //TODO Stack duplication
public class Inventory {
    private Scanner input = new Scanner(System.in);

    private ArrayList<Item> inventory = new ArrayList<Item>();
    private Event displayInventory = new Event("What would you like to do in your inventory", false);
    Choice exit = new Choice("Exit","Exiting Inventory",  () -> {});


                        //  ---Equippable Items---  \\
    private Weapon equippedWeapon = new Weapon();
    private Armor equippedHelm;
    private Armor equippedChest;
    private Armor equippedLeg;
    private Armor equippedBoot;

                        //  ---Money---  \\
    private int money = 0;

    public String toString() {
        String summary = "";
        for (int i = 0; i < inventory.size(); i++) {
            summary += (1 + i) + ": " + this.inventory.get(i) + "\n";
        }
        return summary;
    }

    public void interact(Item newItem) {
        Event interact = new Event("What would you like to do with " + newItem, false);
                //  Creates discard method
        Event confirmDis = new Event("Are you sure you want to discard: " + newItem, false);
        confirmDis.addChoice(new Choice("Yes", () -> {inventory.remove(newItem);}));
        confirmDis.addChoice(new Choice("No", () -> {}));

        interact.addChoice(new Choice("Discard",() -> {confirmDis.displayEvent();interact(newItem);}));

        System.out.println(newItem);
        if(newItem instanceof Weapon) {
            interact.addChoice(new Choice("Equip", () -> {setEquippedWeapon((Weapon) newItem);}));
        }
        else if (newItem instanceof Armor) {
            if(((Armor) newItem).getArmorType() == 1) {
                interact.addChoice(new Choice("Equip", () -> {equippedHelm = (Armor)newItem;remove(newItem);}));
            }
            else if(((Armor) newItem).getArmorType() == 2) {
                interact.addChoice(new Choice("Equip", () -> {equippedChest = (Armor)newItem;remove(newItem);}));
            }
            else if(((Armor) newItem).getArmorType() == 3) {
                interact.addChoice(new Choice("Equip", () -> {equippedLeg = (Armor)newItem;remove(newItem);}));
            }
            else if(((Armor) newItem).getArmorType() == 4) {
                interact.addChoice(new Choice("Equip", () -> {equippedBoot = (Armor)newItem;remove(newItem);}));
            }
        }

        interact.addChoice(new Choice("Exit", () -> {displayInventory.removeChoice(exit);display();}));

        interact.displayEvent();
    }

    // Displays the Inventory & adds the exit choice inventory Event and later removes it
    public void display() {
        displayInventory.addChoice(exit);

        System.out.println( "\033[0;32mEquipped Weapon \033[0;93m\n" + equippedWeapon +
                            "");

        displayInventory.displayEvent();

        displayInventory.removeChoice(exit);
    }
    public Item getItem(int index) {
        return inventory.get(index);
    }

    public void removeItem(int index) {
        inventory.remove(index);
    }

    public void remove(Item item) {
        for(int i = 0; i < inventory.size(); i++) {
            if(item.equals(inventory.get(i))) {
                for(int j = 0; j < displayInventory.getChoices().size(); j++) {
                    if(("" + item).equals(displayInventory.getChoices().get(j).getDescription())) {
                        displayInventory.getChoices().remove(j);
                    }
                }
                inventory.remove(i);

                break;
            }
        }
    }

    public void addItem(Item newItem) {
        if(inventory.size() < 8) {
                //  checks if the item is stackable
            if(newItem.isStackable()) {
                    // cycles through the inventory to find if the item it already in th
                for (int i = 0; i < inventory.size(); i++) {
                        //  compares the names of each Item
                    if (("" + newItem).equals("" + inventory.get(i))) {
                            // Item choice is removed
                        for(int j = 0; j < displayInventory.getChoices().size(); j++) {
                            if(("" + newItem).equals(displayInventory.getChoices().get(j).getDescription())) {
                                displayInventory.getChoices().remove(j);
                            }
                        }
                            //   item amount is adjusted
                        inventory.get(i).adjustAmount(newItem.getAmount());

                    }
                }
            }
                    inventory.add(newItem);
                    displayInventory.addChoice(new Choice("" + newItem,() -> {interact(newItem);}));
        }
        else {
            System.out.println("No space in Inventory");
        }
    }

    public int getArmorIncrease() {
        int totalIncrease = 0;

        if(equippedHelm != null) {
            totalIncrease += equippedHelm.getInc();
        }
        else if(equippedChest != null) {
            totalIncrease += equippedChest.getInc();
        }
        else if(equippedLeg != null) {
            totalIncrease += equippedLeg.getInc();
        }
        else if(equippedBoot != null) {
            totalIncrease += equippedBoot.getInc();
        }

        return totalIncrease;
    }

    public Weapon getEquippedWeapon() {
        return this.equippedWeapon;
    }


    public void setEquippedWeapon(Weapon weapon) {
        equippedWeapon = weapon;
        remove(weapon);
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
