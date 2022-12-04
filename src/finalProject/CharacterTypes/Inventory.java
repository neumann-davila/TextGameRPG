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
import java.util.Random;

public class Inventory {
    private Random rand = new Random();

    private ArrayList<Item> inventory = new ArrayList<Item>();
    private Event displayInventory = new Event("What would you like to do in your inventory", false);
    Choice exit = new Choice("Exit","Exiting Inventory",  () -> {});
    Choice unequip = new Choice("Unequip Item", () -> {unequipEvent();});


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
        displayInventory.addChoice(unequip);

        System.out.println( "\033[0;32mEquipped Helmet \033[0;93m\n" + equippedHelm +
                            "\n\033[0;32mEquipped Chestplate \033[0;93m\n" + equippedChest +
                            "\n\033[0;32mEquipped Leggings \033[0;93m\n" + equippedLeg +
                            "\n\033[0;32mEquipped Boots \033[0;93m\n" + equippedBoot +
                            "\n\033[0;32mEquipped Weapon \033[0;93m\n" + equippedWeapon +
                            "\n\033[0;32mCoins: \033[0;93m" + money);

        displayInventory.displayEvent();

        displayInventory.removeChoice(unequip);
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
        if(equippedChest != null) {
            totalIncrease += equippedChest.getInc();
        }
        if(equippedLeg != null) {
            totalIncrease += equippedLeg.getInc();
        }
        if(equippedBoot != null) {
            totalIncrease += equippedBoot.getInc();
        }
        if(totalIncrease <= 0) {
            return 0;
        }

        int finalIncrease = rand.nextInt(totalIncrease);

        return finalIncrease;
    }
    public void unequipEvent() {
        Event unequip = new Event("What item do you want to unequip");
        if (equippedWeapon != null){
           unequip.addChoice(new Choice("Unequip " + equippedWeapon, () -> {unequip(equippedWeapon);}));
        }
        if (equippedHelm != null) {
            unequip.addChoice(new Choice("Unequip " + equippedHelm, () -> {unequip(equippedHelm);}));
        }
        if (equippedChest != null) {
            unequip.addChoice(new Choice("Unequip " + equippedChest, () -> {unequip(equippedChest);}));
        }
        if (equippedLeg != null) {
            unequip.addChoice(new Choice("Unequip " + equippedLeg, () -> {unequip(equippedLeg);}));
        }
        if (equippedBoot != null) {
            unequip.addChoice(new Choice("Unequip " + equippedBoot, () -> {unequip(equippedBoot);}));
        }
        unequip.addChoice(new Choice("Back", () -> {}));

        unequip.displayEvent();
    }
    public void unequip(Item item) {
        if(item instanceof Weapon) {
            equippedWeapon = null;
        }
        else if(item instanceof Armor) {
            Armor armor = (Armor)item;
            if(armor.getArmorType() == 1) {
                equippedHelm = null;
            }
            else if (armor.getArmorType() == 2) {
                equippedChest = null;
            }
            else if (armor.getArmorType() == 3) {
                equippedLeg = null;
            }
            else if (armor.getArmorType() == 4) {
                equippedBoot = null;
            }
        }
        addItem(item);
    }
    public void equipArmor(Armor newArmor) {
        if(newArmor.getArmorType() == 1) {
            equippedHelm = newArmor;
        }
        else if (newArmor.getArmorType() == 2) {
            equippedChest = newArmor;
        }
        else if (newArmor.getArmorType() == 3) {
            equippedLeg = newArmor;
        }
        else if (newArmor.getArmorType() == 4) {
            equippedBoot = newArmor;
        }
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
