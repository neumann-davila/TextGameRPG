/*
 * Author: Neumann Davila
 * Date:   Oct 4, 2022
 * Description:
 *
 * This is a text adventure RPG game that I am using for my final project
 * The game is played by typing a number that corresponds to a certain choice
 * When you press enter the input wil be taken and the choice will run.
 */

package finalProject;

import java.util.Scanner;

import finalProject.CharacterTypes.*;
import finalProject.CharacterTypes.Character;
import finalProject.EventStructure.*;
import finalProject.Items.*;
import finalProject.Items.Weapons.Weapon;
import finalProject.Locations.*;

public class TextGame {
	static Scanner input = new Scanner(System.in);
		//	Main Player declaration
	public static Player player =  new Player();

		/*
		 * 						---Needed Constructors---
		 *
		 *
		 * Item name = new Item("Name", int price, (optional) int amount);
		 * 		- Creates Basic Items with no real immediately usable function
		 *
		 * Weapon name = new Weapon("Name", int minDamage, int maxDamage, int hit%, int price);
		 *
		 * Armor name = new Armor("Name", int armorType, int defense, int price);
		 * 		- Armor Types
		 * 			- 1 = Helmet
		 * 			- 2 = Chest plate
		 * 			- 3 = Leggings
		 * 			- 4 = Boots
		 */



	
		/*
		 *						---Character Creation Methods---
		 *
		 *
		 * NPC = Character("Name", health, XP gained)
		 * 		- Dialogue must be added within functions
		 * 				- AddDialogue("STRING", int)
		 * 					- int = 1: positiveReaction; 0: neutral; -1: negative
		 * 		- Stats must be added within function
		 * 		- Potential side quest must be added within the function
		 * 		- All Items must be added within the function
		 * 		
		 */
		
	public static NPC createOldMan() {
		NPC oldMan = new NPC("Old man", 6, 20);

		oldMan.addMoney(5);
		oldMan.getInventory().setEquippedWeapon(new Weapon("Cane", 1, 3, 47, 1));
		
		player.getStats().getFriendStat(oldMan).setStat(80);
		
		oldMan.addDialogue("I love you", 1);
		
		oldMan.addDialogue("Hello young whipper snapper", 0);
		oldMan.addDialogue("...Oh did you say something", 0);
		
		oldMan.addDialogue("I hate you", -1);
		
		Event death = new Event("You see the corpse of the man you just killed\nWhat wold you like to do", false);
		death.addChoice(new Choice("Loot Body", () -> {oldMan.loot(player); death.displayEvent();}));
		death.addChoice(new Choice("Leave", () -> {}));
		oldMan.setDeathEvent(death);
				
		return oldMan;
	}
	

	
		/*
		 *						---Location Creation Methods---
		 *
		 *
		 * These Methods are used to fill the locations with events and NPC's
		 * otherwise the Location will not be interactive
		 *
		 * Location name = new Location();
		 * 		- Add nearby Locations
		 * 		- Must add Events & choices in method
		 * 		- location.nextEvent(eventIndex);
		 * 		- location.addEvent(Event);
		 * 
		 */


		//	These indexes help keep track of the event that should happen when arriving

	
	public static Choice goTo(String name, Consequence locationMethod) {
		return new Choice("Go to " + name, locationMethod);
	}

	static int prisonWallIndex = 0;

	public static Location createPrisonWall() {
		Location prisonWall = new Location();
		
		Event escape = new Event("You finally got over the wall unnoticed... for now.", false);
		escape.addChoice(new Choice("Search", "You find nothing",() -> {player.displayDeathEvent();}));
		escape.addChoice(new Choice("Wait", "You wait and get Captured", () -> {player.displayDeathEvent();}));
		escape.addChoice(new Choice("Run", "You run and run for miles, until you finally see a forest in the distance.\n" +
											"Hopefully you will be able to hide in there.",() -> {System.out.println();forestIndex = 0;createForest();}));
		
		prisonWall.addEvent(escape);

		prisonWall.nextEvent(prisonWallIndex);

		return prisonWall;
	}

	static int forestIndex = 0;

	public static Location createForest() {
		Location forest =  new Location();
		Choice[] nearbyLocations = {goTo("Prison Wall", () -> {createPrisonWall();}), goTo("Tavern", () -> {createTavern();})};
		
			//	Event Index 0
		Event enterCampsite = new Event("As you get deeper into the forest you find a campsite that was abandoned long ago.\n" +
										"There are an assortment of items left behind... hopefully one wants them back.\n" +
										"You find a journal and decide to write your name", false);
		enterCampsite.addChoice(new Choice("Write your name in your journal", "Type your Name",() -> {player.setName(input.nextLine());forestIndex = 1;forest.nextEvent(forestIndex);}));
		
		forest.addEvent(enterCampsite);
			//	Event Index 1
		Event getWeapon = new Event("You also find an old Backpack with a...", false);
		getWeapon.addChoice(new Choice("Axe", () -> {player.getInventory().setEquippedWeapon(new Weapon("Old Axe", 8, 10, 48, 1));forestIndex = 2;forest.nextEvent(forestIndex);}));
		getWeapon.addChoice(new Choice("Sword", () -> {player.getInventory().setEquippedWeapon(new Weapon("Old Sword", 5, 7 , 63, 1));forestIndex = 2; forest.nextEvent(forestIndex);}));

		forest.addEvent(getWeapon);
			//	Event Index 2
		Event test = new Event("test");
		test.addNPC(createOldMan());
		
		forest.addEvent(test);
		
		forest.setNearbyLocations(nearbyLocations);
		forest.nextEvent(forestIndex);

		return forest;
	}

	static int tavernIndex = 0;

	public static Shop createTavern() {
		Shop tavern = new Shop("Tavern");
		Choice[] nearbyLocations = {goTo("Forest", () -> {createForest();})};

		tavern.addItem(new Item("Stick", 0));

		tavern.setNearbyLocations(nearbyLocations);
		tavern.nextEvent(tavernIndex);

		return tavern;
	}
	
	public static void createPlayer() {
		player.setMaxHealth(20);
		player.getStats().resetGame();

		Event gameOver = new Event("You Died", false);
		gameOver.addChoice(new Choice("Restart Game", TextGame::run));
		gameOver.addChoice(new Choice("Quit", () -> {System.exit(0);}));
		player.setDeathEvent(gameOver);

		player.equip(new Armor("Prison Uniform", 2, 8, 0));
		player.equip(new Armor("Prison Uniform", 3, 0, 0));
	}
	public static void run() {
		createPlayer();

		createPrisonWall();
	}
	
	public static void main (String[] args) {
		run();
	}
	
}

