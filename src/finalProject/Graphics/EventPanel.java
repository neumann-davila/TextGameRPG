/*
 * Author: Neumann Davila
 * Date:   May 09, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.EventStructure.*;
import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.TextGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EventPanel extends JPanel {


    JPanel choices = new JPanel();
    ButtonGroup group = new ButtonGroup();
    ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();
    ArrayList<Choice> eventChoices;

    JButton submit = new JButton();

    GridBagConstraints cons = new GridBagConstraints();

    public void slowText(String[] strings) {
        cons.gridy = 0;
        cons.gridx = 0;

        for(String temp: strings){
            add(new JLabel(temp));

            TextGame.graphics.revalidate();
            try{
                Thread.sleep(60);
            }
            catch(Exception e){
                System.out.println(e + "\n SLOW TEXT");
            }
            cons.gridy++;
            cons.gridx++;

        }
    }

    public void revealLines(){
        // Possibly flashes lines aws the appear
    }

    public EventPanel(Event event){
        setLayout(new GridBagLayout());
        cons.anchor = GridBagConstraints.NORTHWEST;

        eventChoices = event.getChoices();
        for(Choice choice: eventChoices){
            choices.setLayout(new GridLayout(eventChoices.size(), 1));

            JRadioButton temp = new JRadioButton(choice.getDescription());
            choices.add(temp);
            buttons.add(temp);
            group.add(temp);
        }

        slowText(event.getDescription());

        cons.gridy++;
        cons.gridx++;
        add(choices, cons);

        cons.gridy++;
        cons.gridx++;
        cons.anchor = GridBagConstraints.CENTER;
        add(submit);


        TextGame.graphics.revalidate();

    }
}
