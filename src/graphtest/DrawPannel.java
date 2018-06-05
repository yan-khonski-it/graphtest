/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtest;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Yan
 */
class DrawPannel extends JPanel {

    public DrawPannel() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ProcessingFrame.draw((Graphics2D) g);
    }
}