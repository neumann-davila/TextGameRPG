/*
 * Author: Neumann Davila
 * Date:   Dec 06, 2022
 * Description:
 *
 *
 */
package finalProject.Items.Weapons;


public class RangedWeapon extends Weapon {
    private String ammoName;
        //TODO

    @Override
    public String toString() {
        return super.toString() + "\n     Ammo: " + ammoName;
    }

    @Override
    public String getDisplayDescription() {
        return super.getDisplayDescription() + "\n     Ammo: " + ammoName;
    }

    public String getAmmoName() {
        return ammoName;
    }

    public RangedWeapon(String name, String ammoName, int damageMin, int damageMax, int hitChance, int price) {
        super(name, damageMin, damageMax, hitChance, price);
        this.ammoName = ammoName;
    }
}
