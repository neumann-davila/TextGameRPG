/*
 * Author: Neumann Davila
 * Date:   Apr 18, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JComponent {

    private JLabel name = new JLabel("Name Unknown");

    private JTabbedPane inventoryTabs = new JTabbedPane();

    JPanel genInfo = new JPanel();
    JPanel equippedItems = new JPanel();
    JPanel inventory = new JPanel();

    private void buildGenInfo(){

    }
    public PlayerInfo() {
        setSize(200, 100);
        GridBagConstraints mainCons = new GridBagConstraints();
        setLayout(new GridBagLayout());

        mainCons.gridy = 0;
        add(name, mainCons);

        mainCons.gridy = 1;
        add(genInfo, mainCons);

        mainCons.gridy = 2;
        add(inventoryTabs, mainCons);

        inventoryTabs.addTab("Equipped Items", equippedItems);
        inventoryTabs.addTab("Inventory", inventory);




    }

}
