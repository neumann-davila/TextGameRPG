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

    public void pickPocket(Character recipiant) {
        //TODO rewrite pickPocket

         if(recipiant.getStats().rollDexterity(this.stats.getDexterity())) {
            System.out.println(inventory + "What would you like to take?\n9: Exit");
            int stealIndex = input.nextInt();

            if(stealIndex < 9 && stealIndex > 0) {
                recipiant.addItem(inventory.getItem(stealIndex - 1));
                removeItem(stealIndex);

            }
            else if (stealIndex == 9) {

            }
            else {
                System.out.println("Invalid Input: Steal");
                pickPocket(recipiant);
            }
        }
        else {
            System.out.println("Failed");
        }
    }

    public Player() {
        super();
    }
}
