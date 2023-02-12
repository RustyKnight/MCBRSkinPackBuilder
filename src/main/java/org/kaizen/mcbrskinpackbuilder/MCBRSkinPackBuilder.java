/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.kaizen.mcbrskinpackbuilder;

import org.kaizen.mcbrskinpackbuilder.ui.BuilderPane;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author shane.whitehead
 */
public class MCBRSkinPackBuilder {
    public static void main(String[] args) {
        new MCBRSkinPackBuilder();
    }
    
    public MCBRSkinPackBuilder() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("Minecraft Bedrock, Skin Pack Builder");
                frame.add(new BuilderPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
