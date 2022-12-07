/*
 * Author: Neumann Davila
 * Date:   Dec 06, 2022
 * Description:
 *
 *
 */
package finalProject.EventStructure;


/* TODO
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
 * 				- half Damage? <---------------------
 * 				- chance to block? <-----------------
 * 			- Block or dodge
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


public class CombatEvent {
    private Player player;
    private NPC enemy;
    private NPC enem1;

    public void PlayerCombatTurn(NPC enemy) {
        player.getStats().getFriendStat(enemy).adjustStat(-50);
        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            Event combat = new Event("Health: " + player.healthBar());
            combat.addChoice(new Choice("Attack: " + player.getEquippedWeapon(), () -> {

                if (player.getEquippedWeapon() instanceof RangedWeapon) {
                    RangedWeapon weapon = (RangedWeapon) player.getEquippedWeapon();
                    if (player.getInventory().contains(weapon.getAmmoName())) {
                        player.attack(enemy);
                        player.getInventory().useItem(weapon.getAmmoName());

                    } else {
                        System.out.println("You do not have any" + weapon.getAmmoName() + " in your inventory");
                    }
                } else {
                    player.attack(enemy);

                    int damageDone = enemy.attack();
                    int damageBlocked = player.getInventory().getArmorIncrease();
                    int damageTaken = damageDone - damageBlocked;
                    if (damageTaken < 0 && damageBlocked > 0) {
                        System.out.println(damageBlocked + " damage blocked by armor");
                    } else if (damageBlocked > 0 && damageDone < 0) {
                        System.out.println("Damage blocked by armor");
                    }
                    player.adjustHealth(damageTaken);
                }
            }));

            combat.addChoice(new Choice("Run", () -> {
                if (player.getStats().rollDexterity(enemy.getStats().getDexterity())) {
                    System.out.println("You ran");
                } else {
                    System.out.println("You failed to run from " + enemy);
                    player.adjustHealth(enemy.attack());
                }
            }));
            combat.displayEvent();
        }
    }

    public void NPCCounter() {

    }




    public CombatEvent(Player player, NPC enemy) {
            this.player = player;
            this.enemy = enemy;
            PlayerCombatTurn(enemy);
    }
    public CombatEvent(NPC enemy, NPC enemy1) {
        this.enemy = enemy;

    }
}
