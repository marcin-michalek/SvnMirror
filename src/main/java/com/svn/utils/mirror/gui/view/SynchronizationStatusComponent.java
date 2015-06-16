package com.svn.utils.mirror.gui.view;

import com.svn.utils.mirror.gui.enums.SynchronizationStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-16.
 */
public class SynchronizationStatusComponent extends JComponent {
    private SynchronizationStatus synchronizationStatus;

    public void setSynchronizationStatus(SynchronizationStatus synchronizationStatus) {
        this.synchronizationStatus = synchronizationStatus;
        repaint();
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (synchronizationStatus) {
            case SYNCHRONIZED:
                g.setColor(Color.GREEN);
                break;
            case NOT_SYNCHRONIZED:
                g.setColor(Color.RED);
                break;
            case WAITING_FOR_FEEDBACK:
                g.setColor(Color.YELLOW);
                break;
        }

        Graphics2D g2d = (Graphics2D)g;
        Ellipse2D.Double circle = new Ellipse2D.Double(getX(),
                getY(),
                getHeight(),
                getHeight());
        g2d.fill(circle);
        g2d.setColor(Color.BLACK);
        g2d.drawString(synchronizationStatus.toString(), getX(), getY() + getHeight()/2);
    }
}
