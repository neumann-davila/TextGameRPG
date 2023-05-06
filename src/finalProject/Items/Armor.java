/*
 * Author: Neumann Davila
 * Date:   Nov 18, 2022
 * Description:
 *
 *
 */

package finalProject.Items;

public class Armor extends Item{
    private int deffenseInc;
    private int armorType;

    /*
     * 1 - helmet
     * 2 - chest-plate
     * 3 - leggings
     * 4 - boots
     *
     */

    @Override public String toString(){
        String summary = "";
        summary +=  "\n" +
                    "     Defense: " + deffenseInc;

        return super.toString() + summary;
    }

    @Override
    public String getDisplayDescription(){
        String summary = "";
        summary +=  "\n" +
                "     Defense: " + deffenseInc;
        return super.getDisplayDescription() + summary;
    }
    public int getArmorType() {
        return armorType;
    }

    public int getInc() {
        return deffenseInc;
    }

    public Armor(String name, int armorType, int deffenseInc, int price) {
        this.name = name;
        this.deffenseInc = deffenseInc;
        this.armorType = armorType;
        this.price = price;
        this.stackable = false;
    }
}
