/*
 * Author: Neumann Davila
 * Date:   Apr 17, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener{

    private JFrame main = new JFrame("TextRPG");

    private Panel mainTop = new Panel();
    private Panel mainBottom = new Panel();

    private PlayerInfo playerInfo = new PlayerInfo();
    private Panel event;
    private Panel location = new Panel();

    private JLabel balls = new JLabel("tree");
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void updatePlayer() {
        playerInfo.updatePlayer();
    }

    public void updateInventory(){
        playerInfo.updateInventory();
        main.revalidate();
    }

    public void revalidate() {
        main.revalidate();
    }
    public GUI (){
        main.setSize(800, 500);

        main.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.ipady = 10;
        cons.ipadx = 10;


        JLabel location = new JLabel("");

        location.add(balls);
        mainTop.add(location);
        cons.gridy = 0;
        main.add(mainTop, cons);

        cons.gridy = 1;
        main.add(mainBottom, cons);

        cons.gridy = 0;
        cons.gridx = 1;
        mainBottom.add(playerInfo, cons);

        balls.setText("stuff");

        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // or 3, to stop the application from running when it is closed
        main.setVisible(true);
    }
}
