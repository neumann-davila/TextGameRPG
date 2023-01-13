/*
 * Author: Neumann Davila
 * Date:   Nov 14, 2022
 * Description:
 *
 * This shop functions like a Location but has the added a list of Items that can
 * be purchased at the shop when the shop is displayed
 * 
 */

package finalProject.Locations;

import java.util.ArrayList;
import java.util.Scanner;

import finalProject.TextGame;
import finalProject.EventStructure.*;
import finalProject.Items.Item;

public class Shop extends Location{
	private Scanner input = new Scanner(System.in);
	private ArrayList<Item> forSale = new ArrayList<Item>();
	private Event shop;
	
	public void addItem(Item item) {
		forSale.add(item);
	}
	
	public void displayShop() {
		for (Item tempItem:forSale) {
			tempItem.setAmount(1);
		}
		Event displayShop = new Event("What would you like to buy?");
		for(Item tempItem: forSale) {
			displayShop.addChoice(new Choice("" + tempItem, () -> {
				if(tempItem.isStackable()) {
					System.out.println("How many would you like to purchase?");
					try {
						int tempInt = Integer.parseInt(input.nextLine().strip());
						tempItem.adjustAmount(tempInt - 1);
					}
					catch(Exception e) {
						System.out.println("Invalid Input");
						displayShop();
					}
				}
				TextGame.player.purchaseItem(tempItem);
			}));
		}
		displayShop.addChoice(new Choice("Back", () -> {}));
		displayShop.displayEvent();
		
		shop.displayEvent();
	}
	public void sell() {
		Event sell = new Event("What would you like to sell?", false);

		for(Item tempItem : TextGame.player.getInventory().getInventory()) {
			sell.addChoice(new Choice("" + tempItem, () -> {
				if (!tempItem.isStackable()) {
					TextGame.player.getInventory().adjustMoney(tempItem.getPrice());
					TextGame.player.getInventory().remove(tempItem);
					return;
				}

				System.out.println("How many would you like to sell?");

				try {
					int tempInt = Integer.parseInt(input.nextLine().strip());
					if (tempInt > tempItem.getAmount()) {
						System.out.println("You do not have " + tempInt + " of that item");
						sell.displayEvent();
					} else {
						TextGame.player.getInventory().adjustMoney(tempInt * tempItem.getPrice());
						TextGame.player.getInventory().remove(tempItem, tempInt);
					}

				} catch (Exception e) {
					System.out.println("Invalid Input");
				}
			}));
		}
	}

	@Override
	public void setNearbyLocations(Choice[] nearbyLocations) {
		for(Choice nearbyLocation : nearbyLocations) {
			shop.addChoice(nearbyLocation);
		}
		super.setNearbyLocations(nearbyLocations);
	}

	@Override
	public void nextEvent(int eventNum) {
		if(eventNum == 0) {
			enterShop();
		}
		else {
			super.nextEvent(eventNum - 1);
		}
	}

	public void enterShop() {
		shop.displayEvent();
	}
	

	public Shop(String shopName) {
		super(shopName);
		this.shop = new Event("You enter " + shopName);
		shop.addChoice(new Choice("Buy", () -> {displayShop();}));
		shop.addChoice(new Choice("Sell", () -> {sell();}));
		shop.addChoice(new Choice("Info", () -> {
			System.out.println("");
		}));
	}
}



