/*
 * Author: Neumann Davila
 * Date:   Jan 12, 2023
 * Description:
 *
 *
 */
package finalProject.Items.Consumable;

import finalProject.TextGame;

import java.util.Random;

public class HealthPot extends Consumables{


    public HealthPot(String name, String description, int healthMin, int healthMax) {
        super(name, description, () -> {
            Random rand = new Random();
            TextGame.player.adjustHealth(rand.nextInt(healthMax - healthMin) + healthMin);});
    }
}
