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
    public ItemDisplay (Item item) {

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
                BorderFactory.createEmptyBorder(0 ,20, 0, 20)
        ));

    }

}
