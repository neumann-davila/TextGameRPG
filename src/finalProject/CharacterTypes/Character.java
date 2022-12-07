/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 * Stores Player Data --> Name; Health; Inventory; StatManger
 *
 * 
 */

package finalProject.CharacterTypes;

import java.util.Random;
import java.util.Scanner;

import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.Items.*;
import finalProject.Items.Weapons.*;


public class Character {
	Scanner input = new Scanner(System.in);
	Random rand = new Random();

	protected String name;
	protected int maxHealth = 0;
	protected int tempHealth;

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

	public void equip(Item item) {
		if(item instanceof Weapon) {
			inventory.setEquippedWeapon((Weapon) item);
		}
		else if (item instanceof Armor) {
			inventory.equipArmor((Armor) item);
		}
	}

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


	public void purchaseItem(Item item) {
		inventory.purchaseItem(item);
	}

								//	---Constructors---  \\
	
	public Character() {
		this.stats.getXp().setStat(0);
	}
	
}

