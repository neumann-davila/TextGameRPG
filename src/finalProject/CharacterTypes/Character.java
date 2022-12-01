/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 * Stores Player Data --> Name; Health; Inventory; StatManger
 *
 * 
 */

package finalProject.CharacterTypes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.Items.*;


public class Character {
	Scanner input = new Scanner(System.in);
	Random rand = new Random();

	protected String name;
	protected int maxHealth = 0;
	protected int tempHealth;

	//TODO add defense STAT?
	protected StatManager stats = new StatManager();
	Inventory inventory = new Inventory();
		//	death events
	protected Event death;
		
										//	---MISC---  \\
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}
	
	public StatManager getStats() {
		return stats;
	}	
	
	
								//	---Health Methods---  \\
	
	public String healthBar() {
		return tempHealth + " / " + maxHealth;
	}
	
	public void adjustMaxHealth(int adjustment) {
		maxHealth += adjustment;
		tempHealth = maxHealth;
	}
	public void setMaxHealth(int health) {
		this.maxHealth = health;
		this.tempHealth = maxHealth;
	}

	public void adjustHealth(int userIn) {
		this.tempHealth += userIn;
	}
	
	public int getHealth() {
		return this.tempHealth;
	}
	
	public void attack(NPC enemy) {
		int damage = inventory.getEquippedWeapon().attack();
		if(damage > 0) {
			int extra = rand.nextInt(stats.getStrength().getStat());
			
			enemy.adjustHealth(-(damage + extra));
			
			System.out.println("You do " + damage + " + " + extra +  " damage to the " + enemy + ".");

		}
		else {
			System.out.println("You miss!");
		}
	}

	public void combat(NPC enemy) {
		Event combat = new Event("Health: " + healthBar());

		combat.addChoice(new Choice("Attack: " + getEquippedWeapon(), () -> {attack(enemy); }));
	}
	
	public void setDeathEvent(Event event) {
		this.death = event;
	}

	public Event getDeathEvent() {
		return this.death;
	}
	
	public void displayDeathEvent() {
		death.displayEvent();
	}


										//	---Inventory Methods---	\\


	public Inventory getInventory() {
		return inventory;
	}
	public Weapon getEquippedWeapon() {
		return inventory.getEquippedWeapon();
	}
	public void removeItem(int index) {
			inventory.removeItem(index -1);
	}



	public void addItem(Item item) {
		inventory.addItem(item);
	}

	public void giveItem(NPC recipiant) {
		System.out.println(inventory + "What Item would you like to give to " + recipiant + "?\n9: Exit");
		int discardIndex = input.nextInt();
		
		if(discardIndex < 9){
			Item tempItem = inventory.getItem(discardIndex - 1);
			
			Event confirmGive = new Event("Are you sure you want to give:\n" + tempItem, false);
			confirmGive.addChoice(new Choice("Yes", () -> {recipiant.receiveItem(stats.getFriendStat(recipiant), tempItem);removeItem(discardIndex);}));
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
			//	finalize stat system idea
			//	create an if statement that allows for a chance of failure
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

	public void purchaseItem(Item item) {
		inventory.purchaseItem(item);
	}

								//	---Constructors---  \\
	
	public Character() {
		this.stats.getXp().setStat(0);
	}
	
}

