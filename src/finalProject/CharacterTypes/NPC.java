/*
 * Author: Neumann Davila
 * Date:   Nov 4, 2022
 * Description:
 *
 * NPC's (Non-Player Characters) are intractable entities that act similarly to Characters.
 * Their main added function  is currently Dialogue, but also I have override multiple functions
 * in order to eliminate user prompts.
 * 
 */

package finalProject.CharacterTypes;

import java.util.ArrayList;

import finalProject.EventStructure.Choice;
import finalProject.TextGame;
import finalProject.EventStructure.Event;
import finalProject.Items.Item;

public class NPC extends Character {

	private ArrayList<String> positiveReacts = new ArrayList<String>();
	private ArrayList<String> neutralReacts = new ArrayList<String>();
	private ArrayList<String> negativeReacts = new ArrayList<String>();
	private ArrayList<Event> sideQuests = new ArrayList<Event>();
	private int interactions = 0;
	private int giftsReceived = 0;
	
								//	----Interaction---	\\
	
	public void addDialogue(String newDialogue, int type) {
		if(type == 1) {
			positiveReacts.add(newDialogue);
		}
		else if(type == 0) {
			neutralReacts.add(newDialogue);
		}
		else {
			negativeReacts.add(newDialogue);
		}
	}
	
	public String getDialogue(Stat friendStat) {
		
		if(interactions < 3) {
			TextGame.player.getStats().adjustFriendStat(friendStat, 5);
			interactions++;
		}
		
		int friendValue = friendStat.getStat();
		
		if(friendValue > 75) {
			return "\033[0;32m" + name + ":\033[0;33m \"" + positiveReacts.get(rand.nextInt(positiveReacts.size())) + "\" \033[0;33m";
		}
		else if(friendValue > 25) {
			return "\033[0;92m" + name + ":\033[0;33m \"" +neutralReacts.get(rand.nextInt(neutralReacts.size())) + "\" \033[0;33m";
		}
		else {
			return "\033[0;31m" + name + ":\033[0;33m \"" +negativeReacts.get(rand.nextInt(negativeReacts.size())) + "\" \033[0;33m";
		}
	}
	
	public void addSideQuest(Event newQuest) {
		sideQuests.add(newQuest);
	}
	
								//	---Misc---	\\
	
	public void receiveItem(Stat friendStat, Item item) {
		addItem(item);
		
		if (giftsReceived < 2) {
			TextGame.player.getStats().adjustFriendStat(friendStat, 15);
			giftsReceived++;
		}
	}


	public int attack() {
		int damage = inventory.getEquippedWeapon().attack();
		if(damage > 0) {
			int extra = rand.nextInt(stats.getStrength().getStat());
			System.out.println(name + " did " + damage + " + " + extra + " damage");
			return (damage + extra);
		}
		else {
			System.out.println(name + " missed");
			return 0;
		}
	}

	public void loot(Player player) {
		Event loot = new Event(new String[] {"Select what items you would like to take"});
		inventory.unequipAll();
		//Iterates through every item in npc inventory
		for (Item tempItem : inventory.getInventory()) {
			//Choice creation for Item in cycle
			loot.addChoice(new Choice("" + tempItem, () -> {
				ArrayList<Item> inventory = getInventory().getInventory();

				if (tempItem.isStackable()) {
					System.out.println("How many would you like to take");
					try{
						int tempInt = Integer.parseInt(input.nextLine().strip());
						getInventory().remove(tempItem, tempInt);
						tempItem.setAmount(tempInt);
						player.addItem(tempItem);
					}
					catch(Exception e) {
						System.out.println("Invalid Input");
						loot.displayEvent();
					}
				}
				else {
					player.addItem(tempItem);
					inventory.remove(tempItem);
				}
				loot(player);
			}));

		}

		if(inventory.getMoney() > 0) {
			loot.addChoice(new Choice(inventory.getMoney() + " Coins", () -> {
				player.getInventory().adjustMoney(inventory.getMoney());
				inventory.setMoney(0);

				loot(player);
			}));
		}

		loot.addChoice(new Choice("Exit", () -> {}));

		loot.displayEvent();
	}
	public void die(Player killer) {
		killer.getStats().addXp(getStats().getXp().getStat());
		getDeathEvent().displayEvent();
	}
	
	
	public NPC() {
		super();
	}
	
	public NPC(String name, int health, int xp) {
		this.name = name;
		adjustMaxHealth(health);
		this.stats.getXp().setStat(xp);
	}
}

