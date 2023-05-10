/*
 * Author: Neumann Davila
 * Date:   May 09, 2023
 * Description:
 *
 *
 */
package finalProject.Graphics;

import finalProject.EventStructure.Choice;
import finalProject.EventStructure.Event;
import finalProject.TextGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EventPanel extends JPanel {

   JPanel descriptions = new JPanel();
    JPanel choices = new JPanel();
    ButtonGroup group = new ButtonGroup();
    ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();
    ArrayList<Choice> eventChoices;

    JButton submit = new JButton("Submit Choice");

    GridBagConstraints cons = new GridBagConstraints();



    public void setEvent(Event event){

        System.out.println("Event run");

        eventChoices.clear();
        buttons.clear();


        eventChoices = event.getChoices();

        cons.anchor = GridBagConstraints.NORTHWEST;

        cons.gridy = 0;
        add(descriptions);
        slowText(event.getDescription());

        for(Choice choice: eventChoices){
            choices.setLayout(new GridLayout(eventChoices.size(), 1));

            JRadioButton temp = new JRadioButton(choice.getDescription());
            choices.add(temp);
            buttons.add(temp);
            group.add(temp);
        }


        cons.gridy++;

        add(choices, cons);
        System.out.println("Event run");

        cons.gridy++;
        cons.anchor = GridBagConstraints.CENTER;
        add(submit, cons);
        TextGame.graphics.revalidate();

    }

    public void slowText(String[] strings) {
        cons.gridy = 0;
        descriptions.setLayout(new GridBagLayout());

        for(String temp: strings){
            descriptions.add(new JLabel(temp), cons);

            TextGame.graphics.revalidate();
//            try{
//                Thread.sleep(200);
//            }
//            catch(Exception e){
//                System.out.println(e + "\n SLOW TEXT");
//            }
            cons.gridy++;

        }
    }

    public EventPanel(){

        Event mainMenu = new Event("Welcome to Eriador!", false);

        mainMenu.addChoice(new Choice("Start Game", () -> {TextGame.run();}));
        mainMenu.addChoice(new Choice("Tutorial", () -> {TextGame.tutorial();}));

        eventChoices = mainMenu.getChoices();

        setLayout(new GridBagLayout());
        cons.anchor = GridBagConstraints.CENTER;

        cons.gridy = 0;
        add(descriptions, cons);
        descriptions.add(new JLabel("Welcome to Eriador"));

        cons.anchor = GridBagConstraints.NORTHWEST;

        for(Choice choice: eventChoices){
            choices.setLayout(new GridLayout(eventChoices.size(), 1));

            JRadioButton temp = new JRadioButton(choice.getDescription());
            choices.add(temp);
            buttons.add(temp);
            group.add(temp);
        }


        cons.gridy++;

        add(choices, cons);

        cons.gridy++;
        cons.anchor = GridBagConstraints.CENTER;
        add(submit, cons);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(JRadioButton button: buttons) {
                    if(button.isSelected()){
                        for(Choice choice: eventChoices){
                            if(choice.getDescription().equals(button.getText())){

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for(Component component: choices.getComponents()){
                                            choices.remove(component);
                                        }
                                        for(Component component: descriptions.getComponents()) {
                                            descriptions.remove(component);
                                        }
                                        remove(choices);
                                        remove(submit);
                                        remove(descriptions);
                                        TextGame.graphics.revalidate();
                                        choice.choiceRun();
                                    }
                                });

                                thread.start();

                            }
                        }
                    }
                }

            }
        });

    }
}
