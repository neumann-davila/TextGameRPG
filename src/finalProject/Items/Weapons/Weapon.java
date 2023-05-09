/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 *
 * Weapons act like Items but have a few extra functions. These include a damage range,
 * and a hit percentage as well as a way to determine range
 *
 */

package finalProject.Items.Weapons;

import finalProject.Items.Item;

import java.util.Random;

public class Weapon extends Item {
	protected int damageMin;
	protected int damageMax;
	protected int hitChance;


	@Override public String toString() {
		String summary = "";
		
		summary += "\n";
		summary += "     Damage: " + damageMin + " - " + damageMax + "\n";
		summary += "     Hit Chance: " + hitChance + "%";
		
		return (super.toString() + summary);
	}

	@Override public String getDisplayDescription() {
		String summary = "";

		summary += "<html>Price: " + price + " Coins<br />"+
				"     Damage: " + damageMin + " - " + damageMax + "<br />" +
				"     Hit Chance: " + hitChance + "%<html/>";

		return summary;
	}
		//	creates an attack using random values between the min and max
	public int attack() {
		Random rand = new Random();
		int hitPerc = rand.nextInt(99);
		
		if(hitPerc < hitChance - 1){
				return rand.nextInt(damageMax - damageMin) + damageMin;
		}
		else {
			return 0;
		}
	}
	
											//	---Constructors---  \\
	public Weapon() {
		this.name = "Fist";
		this.damageMax = 2;
		this.damageMin = 1;
		this.hitChance = 10;
		this.stackable = false;
	}
		
	public Weapon(String name, int damageMin, int damageMax, int hitChance, int price) {
		this.name = name;
		this.price = price;
		this.damageMin = damageMin;
		this.damageMax = damageMax;
		this.hitChance = hitChance;
		this.stackable = false;
	}
	
}

