package com.hotmail.solntsev_igor.GUI;

import com.hotmail.solntsev_igor.util.ImageComparator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by solncevigor on 9/13/17.
 */
public class Guibuilder {
    //method to buil GUI interface
    public static void giuBuilder(){
        ImageComparator im = new ImageComparator();

        File image1 = new File("image1.png");
        File image2 = new File("image2.png");
        File image3 = new File("image3.png");

        final JFrame myJFrame = new JFrame("Image Comparator");
        final JPanel panel = new JPanel();
        final Container cp = myJFrame.getContentPane();

        JButton button1 = new JButton("Compare images");
        JButton button2 = new JButton("Check result");

        panel.setLayout(new GridLayout(5,5));

        //Button result action
        ActionListener al_2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File("image3.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ImageIcon icon=new ImageIcon(img);
                JFrame frame=new JFrame();
                frame.setLayout(new FlowLayout());
                frame.setSize(1000,1000);
                JLabel lbl=new JLabel();
                lbl.setIcon(icon);
                frame.add(lbl);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        };


        //Button compare action
        ActionListener al_3 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                im.compareImages(image1,image2,image3);
            }
        };

        button1.addActionListener(al_3);
        button2.addActionListener(al_2);

        cp.add(panel);

        panel.add(button1);
        panel.add(button2);

        myJFrame.setSize(600,600);
        //Puts the window in the middle of the screen.
        myJFrame.setLocationRelativeTo(null);
        myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myJFrame.setVisible(true);
    }
}
