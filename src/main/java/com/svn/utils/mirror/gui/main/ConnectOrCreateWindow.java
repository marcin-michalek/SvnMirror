package com.svn.utils.mirror.gui.main;

import com.svn.utils.mirror.gui.enums.RepoAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-16.
 */
public class ConnectOrCreateWindow extends JFrame {
    private JButton connectWithExistingButton;
    private JButton createMirrorButton;
    private JPanel rootPanel;
    private JCheckBox isRemote;

    public ConnectOrCreateWindow(String title) throws HeadlessException {
        super(title);
        initAndShowMainWindow();
        addListeners();
    }

    private void addListeners() {
        createMirrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainWindow("Create mirroring", RepoAction.CREATE_NEW_REPO_MIRROR,isRemote.isSelected());

                setVisible(false);
            }
        });
        connectWithExistingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainWindow("Attach to existing configuration", RepoAction.CONNECT_TO_EXISTING_REPO_MIRROR,isRemote.isSelected());
                setVisible(false);
            }
        });
    }

    private void initAndShowMainWindow() {
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
