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
import finalProject.Items.Consumable.Consumables;
import finalProject.Items.Weapons.Weapon;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Inventory {
    private Random rand = new Random();
    private Scanner input = new Scanner(System.in);

    private ArrayList<Item> inventory = new ArrayList<Item>();
    private Event displayInventory = new Event("What would you like to do in your inventory", false);
    private Choice exit = new Choice("Exit","Exiting Inventory",  () -> {});
    private Choice unequip = new Choice("Unequip Item", () -> {unequipEvent();});
    private Choice info = new Choice("Info", () -> {info();});


                            //  ---Equippable Items---  \\
    private Weapon equippedWeapon = new Weapon();
    private Armor equippedHelm;
    private Armor equippedChest;
    private Armor equippedLeg;
    private Armor equippedBoot;

                                    //  ---Money---  \\
    private int money = 0;

                            //      ---Main Function Methods---     \\
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

        interact.addChoice(new Choice("Discard",() -> {remove(newItem);displayInventory.removeChoice(exit);displayInventory.removeChoice(unequip);display();}));

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

        interact.addChoice(new Choice("Exit", () -> {displayInventory.removeChoice(exit);displayInventory.removeChoice(unequip);display();}));

        interact.displayEvent();
    }

    // Displays the Inventory & adds the exit choice inventory Event and later removes it
    public void display() {
        displayInventory.addChoice(exit);
        displayInventory.addChoice(unequip);
        displayInventory.addChoice(info);

        System.out.println( "\033[0;32mEquipped Helmet \033[0;93m\n" + equippedHelm);
        pause();
        System.out.println( "\n\033[0;32mEquipped Chestplate \033[0;93m\n" + equippedChest);
        pause();
        System.out.println( "\n\033[0;32mEquipped Leggings \033[0;93m\n" + equippedLeg);
        pause();
        System.out.println( "\n\033[0;32mEquipped Boots \033[0;93m\n" + equippedBoot);
        pause();
        System.out.println( "\n\033[0;32mEquipped Weapon \033[0;93m\n" + equippedWeapon);
        pause();
        System.out.println( "\n\033[0;32mCoins: \033[0;93m" + money);

        displayInventory.displayEvent();

        displayInventory.removeChoice(unequip);
        displayInventory.removeChoice(exit);
        displayInventory.removeChoice(info);
    }

    private void info() {
        Event info = new Event("What item type do you want more info on?", false);

        info.addChoice(new Choice("Melee Weapons", () -> {
            System.out.println("\033[0;94mMelee Weapons are Weapons used in close combat.");
            pause();
            System.out.println("These Weapons tend to do more damage than ranged weapons, but they are unable to counter attack against enemies using ranged weapons");
            pause();
        }));

        info.addChoice(new Choice("Ranged Weapons", () -> {
            System.out.println("\033[0;94mRanged Weapons are Weapons used in ranged combat.");
            pause();
            System.out.println("These Weapons tend to do less damage than melee weapons, but are always able to counter attack enemies in combat");
            pause();
        }));

        info.addChoice(new Choice("Armor", () -> {
            System.out.println("\033[0;94mIn combat Armor has a chance to negate damage");
            pause();
            System.out.println("This is done with your defense stat and a bit of rng");
            pause();
        }));

        info.addChoice(new Choice("Consumables", () -> {
            System.out.println("\033[0;94mConsumable Items can be used inside and outside of combat");
            pause();
            System.out.println("These items usually will have a description on their effect and once they are used there is no getting it back");
            pause();
        }));
    }
    public Item getItem(int index) {
        return inventory.get(index);
    }

    public void removeItem(int index) {
        inventory.remove(index);
    }

        //for stackable items to remove specific amounts
    public void remove(Item item) {

        for (int i = 0; i < inventory.size(); i++) {
            if (item.getName().equals(inventory.get(i).getName())) {
                for (int j = 0; j < displayInventory.getChoices().size(); j++) {
                    if (("" + item).equals(displayInventory.getChoices().get(j).getDescription())) {
                        displayInventory.getChoices().remove(j);
                    }
                }
                if(item.getAmount() == 1) {
                    inventory.remove(i);
                }
                else {
                    System.out.println("How many would you like to remove?");
                    try {
                        int tempInt = Integer.parseInt(input.nextLine().strip());
                        inventory.get(i).adjustAmount(-tempInt);
                        if(0 < inventory.get(i).getAmount()) {

                            displayInventory.addChoice(new Choice("" + item, () -> {
                                interact(item);
                            }));
                        }
                        else {
                            inventory.remove(i);
                        }
                    }
                    catch(Exception e) {
                        System.out.println("Invalid Input");
                        remove(item);
                    }
                }
                break;
            }
        }
    }

    public void remove(Item item, int amount) {
        if(item.isStackable()) {
            for (int i = 0; i < inventory.size(); i++) {
                if (item.getName().equals(inventory.get(i).getName())) {

                inventory.get(i).adjustAmount(-amount);

                    if(1 < inventory.get(i).getAmount()) {

                        displayInventory.addChoice(new Choice("" + item, () -> {
                            interact(item);
                        }));
                    }
                    else {
                        inventory.remove(i);
                    }
                }
            }
        }
        else {
            System.out.println("Cannot Remove nonStackable Item");
        }
    }


    public void addItem(Item newItem) {
        if(inventory.size() < 8) {
                //  checks if the item is stackable
            if(newItem.isStackable()) {
                boolean found = false;
                    // cycles through the inventory to find if the item is already in the inventory
                for (int i = 0; i < inventory.size(); i++) {
                        //  compares the names of each Item
                    if ((newItem.getName()).equals(inventory.get(i).getName())) {
                        Item tempItem = inventory.get(i);

                        inventory.set(i, new Item(newItem.getName(), tempItem.getAmount() + newItem.getAmount() , newItem.getPrice()));
                        displayInventory.getChoices().set(i, new Choice("" + inventory.get(i), () -> {interact(newItem);}));
                        found = true;
                    }
                }
                if(!found) {
                    inventory.add(newItem);
                    displayInventory.addChoice(new Choice("" + newItem, () -> {interact(newItem);}));
                }
            }
            else {
                inventory.add(newItem);
                displayInventory.addChoice(new Choice("" + newItem,() -> {interact(newItem);}));
           }
        }
        else {
            System.out.println("No space in Inventory");
        }
    }

    public boolean contains(String itemName) {
        for(Item tempItem:inventory) {
            if(tempItem.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void useItem(String itemName) {
        for(Item tempItem:inventory) {
            if(tempItem.getName().equals(itemName)) {
                 tempItem.adjustAmount(-1);
            }
        }
    }

    public void useItem(String itemName, int amount) {
        for(Item tempItem:inventory) {
            if(tempItem.getName().equals(itemName)) {
                Consumables item = (Consumables) tempItem;
                item.use();

            }
        }
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }
                         //          ---Equippable Item Methods---          \\

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
    public void unequipAll() {
        if(equippedWeapon != null) {
            addItem(equippedWeapon);
        }
        if(equippedHelm != null) {
            addItem(equippedHelm);
        }
        if(equippedChest != null) {
            addItem(equippedChest);
        }
        if(equippedLeg != null) {
            addItem(equippedLeg);
        }
        if(equippedBoot != null) {
            addItem(equippedBoot);
        }

        equippedWeapon = null;
        equippedHelm = null;
        equippedChest = null;
        equippedLeg = null;
        equippedBoot = null;
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

                            //      ---Money Methods---        \\
    public void purchaseItem(Item item) {
        if (item.getPrice() > this.money) {
            System.out.println("You do not have enough money for this");
        }
        else {
            adjustMoney(-(item.getPrice()));
            addItem(item);
        }
    }

    public int getMoney() {
        return this.money;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public void adjustMoney(int money) {
        this.money += money;
    }

                            //      ---MISC---      \\

    private void pause() {
        try{
            Thread.sleep(1500);
        }
        catch(Exception e) {

        }
    }

    public Inventory() {
    }
}
