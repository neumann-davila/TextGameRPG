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
			Thread.sleep(1500);
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
		NPC oldMan = new NPC("Old man", 15, 20);

		oldMan.addMoney(5);
		oldMan.getInventory().setEquippedWeapon(new Weapon("Cane", 1, 3, 47, 1));
		
		player.getStats().getFriendStat(oldMan).setStat(80);
		
		oldMan.addDialogue("Yer such a nice youn' lad", 1);
		
		oldMan.addDialogue("'ello youn' whipper snapper", 0);
		oldMan.addDialogue("...Sorry didn' hear yeh", 0);
		
		oldMan.addDialogue("GET ER WAY FROM ME", -1);
		
		Event death = new Event("You see the corpse of the man you just killed`What wold you like to do", false);
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
		
		Event escape = new Event("You finally got over the wall unnoticed... for now.", false);
		escape.addChoice(new Choice("Search", "As you search the ground for any goodies you find the boot of you captor.... then the cell you just escaped",() -> {escape.displayEvent();}));
		escape.addChoice(new Choice("Wait", "You sit down in wait until you get thrown in prison again. What were you waiting for, Christmas?", () -> {escape.displayEvent();}));
		escape.addChoice(new Choice("Run", "You flee captivity into a forest, hopefully you can find some new clothes to replace your prisoner's uniform",() -> {createForest(0);}));
		
		prisonWall.addEvent(escape);

		prisonWall.nextEvent(prisonWallIndex);

		return prisonWall;
	}

	static int forestIndex = 0;
	static boolean foundBook = false;
	public static Location createForest(int index) {
		Location forest =  new Location();
		forestIndex = index;

										//	---Enter Campsite Event---	\\

			/*
			 *	Event Index 0
			 *
			 * Initial entry of the player gaining default items
			 */

		 Event enterCampsite = new Event("As you run you spot an old backpack near an old fire pit.`The person who must have owned this is probably dead, so I am sure they wouldn't mind if you took some things", false);
		enterCampsite.addChoice(new Choice("Search Backpack", "In the backpack You find an old hatchet, some worn clothes and 15 gold coins",() -> {
			player.addItem(new Weapon("Old Hatchet", 5, 6, 48, 1));
			player.addItem(new Armor("Worn Shirt", 2, 0, 1));
			player.addItem(new Armor("Worn Pants", 3, 0, 1));
			player.addMoney(15);

			forestIndex = 1;
			forest.nextEvent(1);
		}));

		forest.addEvent(enterCampsite);


										//	---Explore Campsite Event---  \\

		/*
		 * Event Index 1
		 *
		 * Campsite Event requiring player to better learn how to play the game - Understanding the inventory system
		 */

		Event exploreCampsite = new Event("It may be best to play it safe for now and stay in the forest`Set up camp");

			// added after campfire is built and only works when player changes out of prison uniform
		Choice sleep = new Choice("Rest for the night", () -> {
			if(player.getInventory().getArmor(2).getName().equals("Prison Uniform")){
				System.out.println("You should probably change out of your prison uniform before you go to sleep");
				pause();
				System.out.println("You don't want to wake up back in prison");
				pause();
				exploreCampsite.displayEvent();
			}
			else{
				forestIndex = 2;
				forest.nextEvent(forestIndex);
			}
		});

			// This choice is added after the Examine fire pit choice is selected
		Choice buildCampfire = new Choice("Light a fire", () -> {
			if(player.useItem("Stick", 6)) {
				exploreCampsite.getChoices().remove(1);
				exploreCampsite.addChoice(sleep);
			}
			exploreCampsite.displayEvent();
		});


		exploreCampsite.addChoice(new Choice("Examine fire pit", "There clearly hasn't been a fire in a long time\nHopefully I can get one going", () -> {
			exploreCampsite.getChoices().remove(1);
			exploreCampsite.getChoices().add(1,buildCampfire);
			exploreCampsite.displayEvent();
		}));
		exploreCampsite.addChoice(new Choice("Search campsite", "You find a stick.... and then another stick.... there sure are a lot of sticks in this forest, but it is to dark to see anything else\n6 Sticks added to your backpack", () -> {
			player.addItem(new Item("Stick", 6, 0));
			exploreCampsite.getChoices().remove(2);
			exploreCampsite.displayEvent();
		}));

		forest.addEvent(exploreCampsite);


			/*
			 * Event Index 2
			 *
			 * The next morning the player has multiple choices a\
			 */
		Event campsiteMorning = new Event("You wake up as the sun begins to rise`Nothing seems out of the ordinary");

		campsiteMorning.addChoice(new Choice("Search Campsite", "You found a small brown book with writings of an outlaw's haven... Corellon. Flipping through the pages you almost notice a repetitive  mention of \"The Guild\"", () -> {
			pause();
			foundBook = true;
			System.out.println("Maybe this guild can help you get to this Corellon");
			campsiteMorning.displayEvent();
		}));
		campsiteMorning.addChoice(new Choice("Explore the forest", "You find a path going east and west to the south of you, and a barn in a small clearing to the east of you. Both seem pretty sketchy", () -> {
			campsiteMorning.getChoices().remove(2);
			campsiteMorning.addChoice(new Choice("Go to barn", () -> {
				createBarn(0);
			}));
		}));

		forest.addEvent(campsiteMorning);

		forest.nextEvent(forestIndex);

		return forest;
	}


	static int barnIndex;
	public static Location createBarn(int index){
		Location barn = new Location("Barn");

		Event campToBarn = new Event("Test Event");

		campToBarn.addNPC(createOldMan(),oldManDead);

		campToBarn.addChoice(new Choice("Go to Tavern", () -> {
			createTavern(0);
		}));

		barn.nextEvent(barnIndex);
		return barn;
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
		Event setName = new Event("What is your name", false);
		setName.addChoice(new Choice("", () -> {
			String name = input.nextLine();
			Event confirm = new Event("Your name is " + name + "?", false);
			confirm.addChoice(new Choice("Yes", () -> {
				System.out.println("Thus begins the great epic of " + name + " the great outlaw");
				player.setName(name);
			}));
			confirm.addChoice(new Choice("No", setName::displayEvent));

		}));

		player.setMaxHealth(20);
		player.getStats().resetGame();

		Event gameOver = new Event("You Died", false);
		gameOver.addChoice(new Choice("Restart Game", TextGame::run));
		gameOver.addChoice(new Choice("Quit", () -> {System.exit(0);}));
		player.setDeathEvent(gameOver);

		player.equip(new Armor("Prisoner's Shirt", 2, 8, 0));
		player.equip(new Armor("Prisoner's Pants", 3, 0, 0));
		player.equip(new Armor("Old Boots", 4, 0, 1));
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
		Event test = new Event("This is the basic Structure for an event`Select one of the choices by typing a number and the clicking enter", false);

		test.addChoice(new Choice("Continue","Continuing ",  () -> {}));
		test.addChoice(new Choice("Proceed","Proceeding",  () -> {}));

		test.displayEvent();

		pause();
		System.out.println( "\033[0;94mThere are many more things to understand in this game, but this is the most basic mechanic of this game.\n" +
							"Have fun!");
		pause();
	}
	public static void main (String[] args){
		System.out.println("\033[0;94mWelcome to Eriador\nPress enter to start");
		input.nextLine();
		tutorial();

		run();

	}
	
}

