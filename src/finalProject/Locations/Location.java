/*
 * Author: Neumann Davila
 * Date:   Oct 4, 2022
 * Description:
 *
 * Location objects store different values that are held in order to better sort events.
 * These Store Multiple Events that can take place in this Event as well as
 * NPC's that are always at this location.
 * 
 */

package finalProject.Locations;

import java.util.ArrayList;

import finalProject.CharacterTypes.NPC;
import finalProject.EventStructure.*;
import finalProject.TextGame;

public class Location {
	protected String name;
	protected ArrayList<Event> locationEvents =  new ArrayList<Event>();
	protected NPC[] LocationNPCs;
	protected int eventIndex;

	public String toString() {
		return this.name;
	}

			// Adds an array of choices that
	public void setNearbyLocations(Choice[] nearbyLocations) {
		for (Event locationEvent : locationEvents) {
			if(locationEvent.isDefault()) {
				for (Choice nearbyLocation : nearbyLocations) {
					locationEvent.addChoice(nearbyLocation);
				}
			}
		}
	}
	
		 // Method displays information when the character arrives at this location
	public void addEvent(Event newEvent) {
		locationEvents.add(newEvent);
	}
		//	create a way to get the next event by yourself loser
	public void nextEvent(int eventNum) {
		locationEvents.get(eventNum).displayEvent();
	}
		
	public Event getEvent(int eventNum) {
		return locationEvents.get(eventNum);
	}
	
		//	Object Constructor
	public Location() {
	}
	
	public Location(String name) {
		this.name = name;
	}
}

