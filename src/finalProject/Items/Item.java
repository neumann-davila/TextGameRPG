/*
 * Author: Neumann Davila
 * Date:   Oct 6, 2022
 * Description:
 *
 * Item Objects are used as more of a blanket class in order to store
 * different Item types in one array. This also allows me to create general
 * variables and methods that are accessible for all items.
 * 
 */

package finalProject.Items;

public class Item {
	protected String name;
	protected int amount;
	protected int price;
	protected boolean stackable = true;

	// TODO make potions maybe armor or charm/amulet
	public String toString() {
		if(amount > 1) {
			return this.name + " x" + amount +
								"\n     Price: " + price + " coins";
		}
		else {
			return this.name + ": \n" +
					           "	 Price: " + price + " coins";
		}
	}
	
	public String getName() {
		return this.name;
	}
	public int getPrice() {
		return this.price;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isStackable() {
		return stackable;
	}
	public int getAmount() {
		return this.amount;
	}
	
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void addAmount(int amount) {
		this.amount = amount;
	}
	
	public void adjustAmount(int amount) {
		this.amount += amount;
	}
	
	public Item() {
		this.name = "none";
		this.amount = 1;
		this.stackable = true;
	}
	
	public Item(String name, int price) {
		this.name = name;
		this.price = price;
		this.amount = 1;
		this.stackable = true;
	}
	
	public Item(String name, int price, int amount) {
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.stackable = true;
	}
}

