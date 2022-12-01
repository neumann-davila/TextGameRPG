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
import finalProject.Locations.*;

public class TextGame {
	static Scanner input = new Scanner(System.in);

		//Basic Items: Item("Name" , price, (optional) amount)
	static Item stick = new Item("Stick", 0);

		//	Declared Ammunition - Ammunition("Name" , amount)
	static Ammunition arrows = new Ammunition("Arrows", 10);
	
		//	Declared Items - Weapons("Name" , minDamage, maxDamage, hit%, price)
	static Weapon cane = new Weapon("Cane", 1, 3, 35, 1);
	static Weapon oldSword = new Weapon("Old Sword", 5, 7 , 63, 1);
	static Weapon oldAxe = new Weapon("Old Axe", 8, 10, 45, 1);
	static Weapon oldBow = new Weapon("Old Bow", 6, 9, 55, arrows, 1);
	
		//	Main Player declaration
	public static Character player =  new Character();
		
								//	---Character Creation Methods---\\
	
		/* 
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
		
		oldMan.addItem(cane);
		
		player.getStats().getFriendStat(oldMan).setStat(80);
		
		oldMan.addDialogue("I love you", 1);
		
		oldMan.addDialogue("Hello young whipper snapper", 0);
		oldMan.addDialogue("...Oh did you say something", 0);
		
		oldMan.addDialogue("I hate you", -1);
		
		Event death = new Event("You see the corpse of the man you just killed\nWhat wold you like to do", false);
		death.addChoice(new Choice("Loot Body", () -> {}));
		death.addChoice(new Choice("Leave", () -> {}));
		oldMan.setDeathEvent(death);
				
		return oldMan;
	}
	
	
								//	---Location Creation Methods---	\\
	
		/*
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
		enterCampsite.addChoice(new Choice("Write your name in your journal", "Type your Name",() -> {player.setName(input.nextLine());forest.nextEvent(1);}));
		
		forest.addEvent(enterCampsite);
			//	Event Index 1
		Event getWeapon = new Event("You also find an old Backpack with a...", false);
		getWeapon.addChoice(new Choice("Axe", () -> {player.addItem(oldAxe);player.getInventory().setEquippedWeapon(oldAxe);forest.nextEvent(2);}));
		getWeapon.addChoice(new Choice("Sword", () -> {player.addItem(oldSword);player.getInventory().setEquippedWeapon(oldSword);forest.nextEvent(2);}));
		getWeapon.addChoice(new Choice("A bow with 10 arrows", () -> {player.addItem(oldBow);player.getInventory().setEquippedWeapon(oldBow);player.addItem(arrows);forest.nextEvent(2);}));
		
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

		tavern.addItem(stick);

		tavern.enterShop();

		return tavern;
	}
	
	
	public static void run() {
		player.setMaxHealth(20);
		player.getStats().resetGame();

		Event gameOver = new Event("You Died", false);
		gameOver.addChoice(new Choice("Restart Game", () -> {TextGame.run();}));
		gameOver.addChoice(new Choice("Quit", () -> {}));
		player.setDeathEvent(gameOver);


		createPrisonWall();
	}
	
	public static void main (String[] args) {
		run();
	}
	
}

