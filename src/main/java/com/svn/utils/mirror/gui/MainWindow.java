package com.svn.utils.mirror.gui;

import com.svn.utils.mirror.app.Mirror;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class MainWindow extends JFrame {
    private JPanel rootPanel;
    private JButton createRepoButton;
    private JTextPane logTextPane;

    public MainWindow(String title) throws HeadlessException {
        super(title);
        initAndShowMainWindow();
        addListeners();
    }

    private void addListeners() {
        createRepoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Mirror.createRepo()) {
                    appendToPane(logTextPane, "Repo created!", Color.GREEN);
                }
            }
        });
    }

    private void initAndShowMainWindow() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void appendToPane(JTextPane jTextPane, String message, Color color) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.FontFamily, "Lucida Console");
        attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        int len = jTextPane.getDocument().getLength();
        jTextPane.setCaretPosition(len);
        jTextPane.setCharacterAttributes(attributeSet, false);
        jTextPane.replaceSelection(message);
    }
}
