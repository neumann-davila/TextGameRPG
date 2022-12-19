/*
 * Author: Neumann Davila
 * Date:   Dec 06, 2022
 * Description:
 *
 * The player class stores specific Methods that are only useful for the player
 * as they require user input and terminal prompts that will be different from
 * NPC's. This includes Giving; PickPocketing, and combat
 *
 */
package finalProject.CharacterTypes;

import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.Items.*;

import java.util.ArrayList;

public class Player extends Character{


    public int attack(NPC enemy) {
        int damage = inventory.getEquippedWeapon().attack();
        if(damage > 0) {
            int extra = rand.nextInt(stats.getStrength().getStat());


            System.out.println("You do " + damage + " + " + extra +  " damage to the " + enemy + ".");
            return damage + extra;
        }
        else {
            System.out.println("You miss!");
            return 0;
        }
    }

    public void giveItem(NPC recipiant) {
        System.out.println(inventory + "What Item would you like to give to " + recipiant + "?\n9: Exit");
        int discardIndex = input.nextInt();

        if(discardIndex < 9){
            Item tempItem = inventory.getItem(discardIndex - 1);

            Event confirmGive = new Event("Are you sure you want to give:\n" + tempItem, false);
            confirmGive.addChoice(new Choice("Yes", () -> {recipiant.receiveItem(stats.getFriendStat(recipiant), tempItem);inventory.remove(tempItem);}));
            confirmGive.addChoice(new Choice("No", () -> {}));

            confirmGive.displayEvent();
        }
        else if(discardIndex == 9) {

        }
        else {
            System.out.println("Invalid Input: Give");
            giveItem(recipiant);
        }
    }

    public boolean pickPocket(NPC victim) {
        //TODO rewrite pickPocket

         if(stats.rollDexterity(victim.getStats().getDexterity())) {
          Event pickPocket = new Event("Select one item to steal" , false);
          for(Item tempItem:victim.getInventory().getInventory()) {
              pickPocket.addChoice(new Choice("" + tempItem, () -> {
                  ArrayList<Item> inventory = victim.getInventory().getInventory();
                  if(tempItem.isStackable()) {
                      for(int i = 0; i < inventory.size(); i++) {
                          if(tempItem.getName().equals(inventory.get(i).getName())) {
                              inventory.get(i).adjustAmount(-1);
                              addItem(tempItem);
                          }

                      }
                  }
                  else {
                      addItem(tempItem);
                      inventory.remove(tempItem);
                  }

              }));
          }

            return true;
        }
        else {
            System.out.println("PickPocket Failed");
            return false;
        }
    }

    public Player() {
        super();
    }
}
