package com.svn.utils.mirror.gui.main;

import com.svn.utils.mirror.app.CreateRepoInt;
import com.svn.utils.mirror.app.Mirror;
import com.svn.utils.mirror.gui.logger.StatusLogger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class MainWindow extends JFrame implements CreateRepoInt {
    private JPanel rootPanel;
    private JButton createRepoButton;
    private JTextPane logTextPane;
    private JTextField nameTextField;
    private StatusLogger statusLogger;

    public MainWindow(String title) throws HeadlessException {
        super(title);
        statusLogger = new StatusLogger(logTextPane);
        initAndShowMainWindow();
        addListeners();
    }

    private void addListeners() {
        createRepoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mirror.createRepo(nameTextField.getText(), MainWindow.this);
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

    @Override
    public void onRepoCreated(SVNURL svnurl) {
        statusLogger.logSuccess("Repo created: " + svnurl.getPath());
    }

    @Override
    public void onRepoCreationException(SVNException e) {
        statusLogger.logError("Repo creation failed: " + e.getMessage());
    }
}
