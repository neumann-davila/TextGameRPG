/*
 * Author: Neumann Davila
 * Date:   Jan 11, 2023
 * Description:
 *
 *
 */
package finalProject.Items.Consumable;

import finalProject.EventStructure.Consequence;
import finalProject.Items.Item;

public class Consumables extends Item {
    protected Consequence outcome;
    protected String name;
    protected String description;

    @Override
    public String toString() {
        return super.toString() + "\n     " + description;
    }

    @Override
    public String getDisplayDescription() {
        return super.getDisplayDescription() + "\n     " + description;
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
