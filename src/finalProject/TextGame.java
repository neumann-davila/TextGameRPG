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
import finalProject.EventStructure.*;
import finalProject.Items.*;
import finalProject.Items.Weapons.Weapon;
import finalProject.Locations.*;

public class TextGame {
	static Scanner input = new Scanner(System.in);
		//	Main Player declaration
	public static Player player =  new Player();

	public static void pause() {
		try{
			Thread.sleep(700);
		}
		catch(Exception e ) {

		}
	}

		/*
		 * 						---Needed Constructors---
		 *
		 *
		 * Item name = new Item("Name",(optional) int amount, int price);
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

	public static boolean oldManDead = false;
	public static NPC createOldMan() {
		NPC oldMan = new NPC("Old man", 6, 20);

		oldMan.addMoney(5);
		oldMan.getInventory().setEquippedWeapon(new Weapon("Cane", 1, 3, 47, 1));
		
		player.getStats().getFriendStat(oldMan).setStat(80);
		
		oldMan.addDialogue("Yer such a nice youn' lad", 1);
		
		oldMan.addDialogue("'ello youn' whipper snapper", 0);
		oldMan.addDialogue("...Sorry didn' hear yeh", 0);
		
		oldMan.addDialogue("GET ER WAY FROM ME", -1);
		
		Event death = new Event(new String[] {"You see the corpse of the man you just killed","What wold you like to do"}, false);
		death.addChoice(new Choice("Loot Body", () -> {oldMan.loot(player); death.displayEvent();}));
		death.addChoice(new Choice("Leave", () -> {oldManDead = true;}));
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

	public static Location createPrisonWall(int index) {
		Location prisonWall = new Location();
		prisonWallIndex = index;
		
		Event escape = new Event(new String[] {"You finally got over the wall unnoticed... for now."}, false);
		escape.addChoice(new Choice("Search", "As you search the ground for any goodies you find the boot of you captor.... then the cell you just escaped",() -> {player.displayDeathEvent();}));
		escape.addChoice(new Choice("Wait", "You sit down in wait until you get thrown in prison again. What were you waiting for, Christmas?", () -> {player.displayDeathEvent();}));
		escape.addChoice(new Choice("Run", "You flee captivity into a forest, hopefully you can find some new clothes to replace your prisoner's uniform",() -> {createForest(0);}));
		
		prisonWall.addEvent(escape);

		prisonWall.nextEvent(prisonWallIndex);

		return prisonWall;
	}

	static int forestIndex = 0;

	public static Location createForest(int index) {
		Location forest =  new Location();

		forestIndex = index;
		
			//	Event Index 0
		 Event enterCampsite = new Event(new String[] {"As you run you spot an old backpack near an old fire pit.","The person who must have owned this is probably dead, so I am sure they wouldn't mind if you took some things"}, false);
		enterCampsite.addChoice(new Choice("Search Backpack", "In the backpack You find an old hatchet, some worn clothes and 15 gold coins",() -> {
			player.addItem(new Weapon("Old Hatchet", 5, 6, 48, 1));
			player.addItem(new Armor("Worn Shirt", 2, 0, 1));
			player.addItem(new Armor("Worn Pants", 3, 0, 1));
			player.addMoney(15);

			forestIndex = 1;
			forest.nextEvent(1);
		}));

		forest.addEvent(enterCampsite);

		//	Event Index 1
		Event exploreCampsite = new Event(new String[] {"It may be best to play it safe for now and stay in the forest", "Set up camp"});
		Choice buildCampfire = new Choice("Light a fire", () -> {
			if(player.useItem("Stick", 6)) {
				exploreCampsite.getChoices().remove(1);
				System.out.println("");
			}
			exploreCampsite.displayEvent();
		});
		exploreCampsite.addChoice(new Choice("Examine fire pit", "The fire pit has grass growing inside of it.\nThere clearly hasn't been a fire in a long time", () -> {
			exploreCampsite.getChoices().remove(1);
			exploreCampsite.getChoices().add(1,buildCampfire);
			exploreCampsite.displayEvent();
		}));
		exploreCampsite.addChoice(new Choice("Search campsite", "You find a stick.... and then another stick.... there sure are a lot of sticks in this forest\n6 sticks added to your backpack", () -> {
			player.addItem(new Item("Stick", 6, 0));
			exploreCampsite.displayEvent();
		}));

		forest.addEvent(exploreCampsite);

			//	Event Index 2
		Event test = new Event(new String[] {"test"});
		test.addNPC(createOldMan(), oldManDead);
		
		forest.addEvent(test);

		forest.nextEvent(forestIndex);

		return forest;
	}

	static int tavernIndex = 0;

	public static Shop createTavern(int index) {
		Shop tavern = new Shop("Tavern");
		tavernIndex = index;

		tavern.addItem(new Item("Stick", 0));

		tavern.nextEvent(tavernIndex);

		return tavern;
	}
	
	public static void createPlayer() {
		player.setMaxHealth(20);
		player.getStats().resetGame();

		Event gameOver = new Event(new String []{"You Died"}, false);
		gameOver.addChoice(new Choice("Restart Game", TextGame::run));
		gameOver.addChoice(new Choice("Quit", () -> {System.exit(0);}));
		player.setDeathEvent(gameOver);

		player.equip(new Armor("Prison Uniform", 2, 8, 0));
		player.equip(new Armor("Prison Uniform", 3, 0, 0));
	}
	public static void run() {
		createPlayer();

		createPrisonWall(0);

		System.out.println("That is all that I have created.\nThank you for playing!");
	}

	public static void tutorial() {
		System.out.println("If you are new to the game press enter to play the tutorial or type any character and press enter");

		if(!input.nextLine().equals("")) {
			return;
		}

		System.out.println("This is a Text Adventure Game that uses numbered choices to progress the game");
		pause();
		Event test = new Event(new String[] {"This is the basic Structure for an event", "Select one of the choices by typing a number and the clicking enter"}, false);

		test.addChoice(new Choice("Continue","Continuing ",  () -> {}));
		test.addChoice(new Choice("Proceed","Proceeding",  () -> {}));

		test.displayEvent();

		pause();
		System.out.println( "\033[0;94mThere are many more things to understand in this game, but this is the most basic mechanic of this game.\n" +
							"Have fun!");
		pause();
	}
	public static void main (String[] args){
		System.out.println("\033[0;94mWelcome to NAME_TBD\nPress enter to start");
		input.nextLine();
		tutorial();

		run();
	}
	
}

