/*
 * Author: Neumann Davila
 * Date:   Oct 7, 2022
 * Description:
 *
 * This is the structure for Choices. These choices contain an interface object with a runnable
 * abstract method. This is so I can create and run a new method easily and with every iteration of
 * a Choice. This also includes a Choice description, which is displayed when initially in the Event,
 * and an outcome description, which is optional and is displayed while the choice is running.
 *
 */

package finalProject.EventStructure;
public class Choice {
	private String choiceDescription;
	private String outcomeDescription;
	private Consequence outcome;
		//	runs whatever effect is assigned to the react() method
	public String getDescription() {
		return this.choiceDescription;
	}
	public void choiceRun() {
		if(outcomeDescription != null) {
			System.out.println("\033[93m" + outcomeDescription);
		}
		else {
			System.out.println("");
		}
		outcome.react();
	}
	
	public String toString() {
		return this.choiceDescription;
	}
		//	Collects info necessary for the Choice
	public Choice(String choiceDescription, Consequence effect) {
		this.choiceDescription = choiceDescription;
		this.outcome = effect;
	}
		//	Creates a choice object the contains a String reaction that is displayed to the user
	public Choice(String choiceDescription, String outcomeDescription, Consequence effect) {
		this.choiceDescription = choiceDescription;
		this.outcomeDescription = outcomeDescription;
		this.outcome = effect;
	}
}

