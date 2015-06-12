package com.svn.utils.mirror.gui;

import javax.swing.*;
import java.awt.*;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class MainWindow extends JFrame {
    private JButton helloButton;
    private JButton button1;
    private JPanel rootPanel;

    public MainWindow(String title) throws HeadlessException {
        super(title);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }
}
