/*
 * Author: Neumann Davila
 * Date:   Jan 11, 2023
 * Description:
 *
 *
 */
package finalProject.Items;

import finalProject.EventStructure.Consequence;

public class Consumables extends Item {
    private Consequence outcome;
    String name;
    String description;

    @Override
    public String toString() {
        return super.toString() + "\n     " + description;
    }

    public void use() {
        outcome.react();
    }

   public Consumables(String name, String description, Consequence outcome) {
       this.name =  name;
       this.description = description;
       this.outcome = outcome;
   }
}
