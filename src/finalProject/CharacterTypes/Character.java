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

	//TODO add defense STAT?
	protected String name;
	protected int maxHealth = 0;
	protected int tempHealth;
		//	Stat Manager
	protected StatManager stats = new StatManager();
		//	inventory variables
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
	
	public void setHealth(int health) {
		this.tempHealth = health;
	}
	
	public void adjustHealth(int userIn) {
		this.tempHealth += userIn;
	}
	
	public int getHealth() {
		return this.tempHealth;
	}
	
	public void attack(Character enemy) {
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

	public void EquipWeapon() {
		ArrayList<Weapon> weapons = inventory.getWeapons();

		if(weapons.size() == 0) {
			inventory.setEquippedWeapon(new Weapon());
		}
		else {
			for(int i = 0; i < weapons.size(); i++) {
				System.out.println((i + 1) + ": " + weapons.get(i));
			}
			System.out.println("Choose the weapon you would like to equip:");
			inventory.setEquippedWeapon(weapons.get(input.nextInt() - 1));
		}
	}

	public void addItem(Item item) {
		inventory.addItem(item);
	}

	public void discardItem() {
		System.out.println("What Item would you like to discard?\n9: Exit");
		int discardIndex = input.nextInt();

		if(discardIndex < 9) {
			Event confirmDiscard = new Event("Are you sure you want to discard:\n" + inventory.getItem(discardIndex - 1), false);
			confirmDiscard.addChoice(new Choice("Yes", () -> {inventory.removeItem(discardIndex);}));
			confirmDiscard.addChoice(new Choice("No", () -> {}));
			confirmDiscard.displayEvent();

		}
		else if(discardIndex == 9) {

		}
		else {
			System.out.println("Invalid Input");
			discardItem();
		}
	}
	
	public void giveItem(NPC recipiant) {
		System.out.println(inventory + "What Item would you like to give to " + recipiant + "?\n9: Exit");
		int discardIndex = input.nextInt();
		
		if(discardIndex < 9){
			Item tempItem = inventory.getItem(discardIndex - 1);
			
			Event confirmGive = new Event("Are you sure you want to give:\n" + tempItem, false);
			confirmGive.addChoice(new Choice("Yes", () -> {recipiant.receiveItem(stats.getFriendStat(recipiant), tempItem);inventory.removeItem(discardIndex);}));
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
		if(recipiant.getStats().rollDexterity(this.stats.getDexterity()) == true) {
			System.out.println(inventory + "What would you like to take?\n9: Exit");
			int stealIndex = input.nextInt();
			
			if(stealIndex < 9 && stealIndex > 0) {
				recipiant.addItem(inventory.getItem(stealIndex - 1));
				inventory.removeItem(stealIndex);
				
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

								//	---Constructors---  \\
	
	public Character() {
		this.stats.getXp().setStat(0);
	}
	
}

