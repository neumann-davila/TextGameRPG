/*
 * Author: Neumann Davila
 * Date:   Nov 18, 2022
 * Description:
 *
 *
 */

package finalProject.Items;

public class Armor extends Item{
    private int healthInc;
    private int armorType;

    /*
     * 1 - helmet
     * 2 - chestplate
     * 3 - leggings
     * 4 - boots
     * TODO split charm from armor?
     * 5 - charm
     *
     */

    public int getArmorType() {
        return armorType;
    }


    public Armor(String name, int armorType, int healthInc, int price) {
        this.name = name;
        this.healthInc = healthInc;
        this.price = price;
        this.armorType = armorType;
    }

    public int getInc() {
        return healthInc;
    }


}
