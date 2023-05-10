/*
 * Author: Neumann Davila
 * Date:   Apr 20, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.Items.*;

import javax.swing.*;
import java.awt.*;

public class ItemDisplay extends JButton {
    Item item = null;

    public Item getItem(){
        return this.item;
    }

    public void setItem(Item item) {
        if(item != null) {
            this.item = item;
            setText(item.getDisplayDescription());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(item.getDisplayName()),
                    BorderFactory.createEmptyBorder(0, 20, 0, 20)
            ));
        }
        else{
            setText("N/A");
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Empty"),
                    BorderFactory.createEmptyBorder(0 ,30, 0, 30)
            ));
        }
    }
    public ItemDisplay (Item item) {
        this.item = item;
        setText(item.getDisplayDescription());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(item.getDisplayName()),
                BorderFactory.createEmptyBorder(0 ,20, 0, 20)
        ));
    }

    public ItemDisplay() {

        setText("N/A");
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Empty"),
                BorderFactory.createEmptyBorder(0 ,30, 0, 30)
        ));

    }

}
