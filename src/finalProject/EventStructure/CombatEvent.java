/*
 * Author: Neumann Davila
 * Date:   Dec 06, 2022
 * Description:
 *
 *
 */
package finalProject.EventStructure;


/*
 *								---Combat System---
 *
 * The combat system is going to have multiple steps in its entirety
 * This will be a turn based combat system with the ability to react to current actions
 * The main idea is to create a strategy like combat system similar to pokemon
 *
 *  Questions to answer before starting:
 *
 * 		- Where should the method be located?
 *              - There will be individual combatMethods for NPC's and Player's
 *                  - !This would not work as I need to pass the instance of the NPC!
 *                      !From itself to the player which is not possible!
 *              - Create a CombatEvent class that will run everything for you once created
 *                  - Thie can contain methods that are for all cases in an organized way
 *
 * 		- Should NPC's be able to fight each other?
 *              - NPC combat is something that I plan to add but for now this system is what I need to work on
 *
 * 1.) Combat starts on the player's turn:
 * 		During this turn the player will have 4 main options
 * 			- Open inventory
 * 				- Use consumable items
 * 				- Equip other Items
 * 			- Attack with equipped weapon
 * 			- Use surroundings (TBD)
 * 				- in any location there will be random event that can
 * 					run based of stat rolls
 * 			- Run
 * 				- Dexterity rol that determines the ability to escape
 *		All of these actions will use an entire turn to run
 *
 * 2.) If an attack is made the enemy will have a chance to react
 * 			- Counter Attack
 * 				- only possible if reacting from a melee weapon
 * 				- runs as a normal attack would
 * 				- half Damage
 * 			- Dodge
 * 				- since Blocking damage is passively done with attacks this will need to be unique
 *				- Potentially a dex roll to see if you can dodge all damage
 *
 * 3.) Combat Cycle continues but now it is the NPC turn
 *		Some type of AI ay need to be made to decide the actions of mor complex NPC
 *
 * 4.)  Player will have a chance to react similarly to the NPC
 */

import finalProject.CharacterTypes.*;
import finalProject.CharacterTypes.Character;
import finalProject.Items.Weapons.RangedWeapon;

import java.util.Locale;


public class CombatEvent {
    private Player player;

        // Runs the Combat turn for the player
    public void playerCombatTurn(NPC enemy) {
            //  Checks to see if anyone has died
        if(player.getHealth() <= 0) {
            player.displayDeathEvent();
            return;
        }
        else if(enemy.getHealth() <= 0) {
            enemy.displayDeathEvent();
            return;
        }

        Event combat = new Event("Health: " + player.healthBar());

            // Checks the equipped Weapon and creates a choice based Weapon type
        combat.addChoice(new Choice("Attack: " + player.getEquippedWeapon(), () -> {

                // If the Weapon is ranged then ammo is required
            if (player.getEquippedWeapon() instanceof RangedWeapon) {
                RangedWeapon weapon = (RangedWeapon) player.getEquippedWeapon();
                if (player.getInventory().contains(weapon.getAmmoName())) {
                    int damage = player.attack(enemy);
                    player.getInventory().useItem(weapon.getAmmoName());

                    npcCounter(enemy, player, damage);
                }
                else {
                        //If the player has no ammo the turn simply restarts
                    System.out.println(player + " does not have any" + weapon.getAmmoName() + " in their inventory");
                    sleep(700);
                    playerCombatTurn(enemy);
                }
            }
            else {
                // Melee Weapon attack
                int damage = player.attack(enemy);
                npcCounter(enemy, player, damage);
            }
        }));

        combat.addChoice(new Choice("Run", () -> {
            if (player.getStats().rollDexterity(enemy.getStats().getDexterity())) {
                System.out.println(player + " ran");
            } else {
                System.out.println(player + " failed to run from " + enemy);
            }
            sleep(700);
        }));

        combat.displayEvent();
        npcCombatTurn(enemy);
    }

    public void playerCounter(NPC npc) {

            // Runs player counter event
        Event counter = new Event(  "During the attack you try to... \n" +
                                    "Choose an action:", false);

            // Dodging attacks allows for damage negation on a successful dexterity roll
        counter.addChoice(new Choice("Dodge", () -> {

            if(player.getStats().rollDexterity(npc.getStats().getDexterity())) {
                System.out.println(player + " dodged the attack!");
                 sleep(700);
            }
            else {
                System.out.println("Dodge failed");
                sleep(700);
                int damage = npc.attack();
                if(damage != 0) {
                    System.out.println(" to " + player);
                }
                player.adjustHealth(-damage);
                sleep(700);
            }
        }));

            // Counter-attack allows the player to attack but only dealing half of its possible damage
        if(!(npc.getEquippedWeapon() instanceof RangedWeapon)) {
            counter.addChoice(new Choice("Counter attack", () -> {
                    //  gets the amount damage that can be blocked
                int blocked = npc.getInventory().getArmorIncrease();
                int playerBlocked = player.getInventory().getArmorIncrease();

                System.out.println(player + " counter attacked!");
                sleep(700);


                int counterDamage = player.counterAttack(npc) - blocked;
                System.out.println(npc + " blocked " + blocked + " damage with their armor.");
                sleep(700);
                if(counterDamage > 0) {
                    npc.adjustHealth(-counterDamage);
                }

                int damage = npc.attack() - playerBlocked;

                if(damage > 0) {
                    System.out.println(player + " blocked " + blocked + " damage with their armor.");
                    System.out.println(damage + " was done to " + player);
                }

                System.out.println("");
                player.adjustHealth(-damage);
            }));
        }

        counter.displayEvent();
    }

    public void npcCombatTurn(NPC npc) {
        if(player.getHealth() <= 0) {
            player.displayDeathEvent();
            return;
        }
        else if(npc.getHealth() <= 0) {
            npc.displayDeathEvent();
            return;
        }

        System.out.println(npc + " attacks with their " + npc.getEquippedWeapon().getName().toLowerCase() + "!");
        sleep(700);

        playerCounter(npc);
        playerCombatTurn(npc);
    }
    //TODO armor decrease not added everywhere
    public void npcCounter(NPC npc, Character enemy, int damage) {
        int damageBlocked = npc.getInventory().getArmorIncrease();
        int damageTaken = damage - damageBlocked;
        if((enemy.getEquippedWeapon() instanceof RangedWeapon || npc.getHealth() < enemy.getHealth()) && damageTaken > 0) {
            if(npc.getStats().rollDexterity(enemy.getStats().getDexterity())) {
                System.out.println(npc + " dodged the attack");
                return;
            }

        }
        else {
            System.out.println(npc + " counter attacked!");
            sleep(700);

            int counterDamage = npc.attack();
            if(counterDamage != 0) {
                System.out.println("to " + enemy);
            }

            counterDamage /= 2;
            player.adjustHealth(-counterDamage);
            sleep(700);
        }

        if (damage != 0) {
            System.out.println(npc + " blocked " + damageBlocked + " damage with their armor");
            sleep(700);
            System.out.println(npc + " took " + damageTaken + " damage");
            npc.adjustHealth(-damageTaken);
            sleep(700);
        }
    }

    private void sleep(long milliSecs) {
        try{
            Thread.sleep(milliSecs);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public CombatEvent(Player player, NPC npc) {
        this.player = player;
        System.out.println("You enter combat with " + npc);
        sleep(1000);
        if(npc.getStats().getDexterity().getStat() < player.getStats().getDexterity().getStat()) {

            playerCombatTurn(npc);
        }
        else{
            npcCombatTurn(npc);
        }
    }
    public CombatEvent(NPC enemy, NPC enemy1) {
        //TODO make NPC combat Events
    }
}
