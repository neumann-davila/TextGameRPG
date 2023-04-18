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

    private Panel playerInfo;
    private Panel event;
    private Panel location = new Panel();


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public GUI (){
        main.setSize(500, 300);

        main.setLayout(new GridBagLayout());
        GridBagConstraints mainCons = new GridBagConstraints();

        JLabel balls = new JLabel("Balls");

        location.add(balls);
        mainTop.add(location);
        mainCons.gridy = 1;
        main.add(mainTop, mainCons);

        balls.setText("cum");

        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // or 3, to stop the application from running when it is closed
        main.setVisible(true);
    }
}
