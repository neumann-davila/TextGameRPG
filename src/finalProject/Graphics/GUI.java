/*
 * Author: Neumann Davila
 * Date:   Apr 17, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.EventStructure.Event;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI{

    private JFrame main = new JFrame("TextRPG");

    private PlayerInfo playerInfo = new PlayerInfo();
    private EventPanel event = new EventPanel();
    GridBagConstraints cons = new GridBagConstraints();


    public void setEvent(Event event){
    this.event.setEvent(event);
    }
    public void updatePlayer() {
        playerInfo.updatePlayer();
    }

    public void updateInventory(){
        playerInfo.updateInventory();
        main.revalidate();
    }

    public void addExtra(JPanel panel){
        cons.gridy = 1;
        main.add(panel, cons);
        revalidate();
    }

    public void removeExtra(){
        main.remove(1);
    }
    public void revalidate() {
        main.revalidate();
    }
    public GUI (){
        main.setSize(800, 700);

        main.setLayout(new GridBagLayout());
        cons.ipady = 10;
        cons.ipadx = 10;

        cons.gridy = 0;
        main.add(event, cons);

        cons.gridy = 2;
        main.add(playerInfo, cons);

        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // or 3, to stop the application from running when it is closed
        main.setVisible(true);
    }
}
