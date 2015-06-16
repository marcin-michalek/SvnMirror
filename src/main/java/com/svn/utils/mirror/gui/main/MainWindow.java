package com.svn.utils.mirror.gui.main;

import com.svn.utils.mirror.app.CreateRepoInt;
import com.svn.utils.mirror.app.Mirror;
import com.svn.utils.mirror.gui.enums.RepoAction;
import com.svn.utils.mirror.gui.enums.RepositoryType;
import com.svn.utils.mirror.gui.enums.SynchronizationStatus;
import com.svn.utils.mirror.gui.logger.StatusLogger;
import com.svn.utils.mirror.gui.model.RepositoryModel;
import com.svn.utils.mirror.gui.view.SynchronizationStatusComponent;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class MainWindow extends JFrame implements CreateRepoInt {
    private JPanel rootPanel;
    private JButton mirrorButton;
    private JTextPane logTextPane;
    private JTextField sourcePathTextField;
    private JTextField destinationRepositoryPathTextField;
    private JComboBox repositoryType;
    private JTextField loginTextFields;
    private JPasswordField passwordField;
    private JPanel rootServerLoginPanel;
    private JEditorPane detailsPane;
    private JPanel synchroStatusPanel;
    private StatusLogger statusLogger;
    private RepoAction repoAction;
    private SynchronizationStatusComponent synchronizationStatusComponent;

    public MainWindow(String title, RepoAction repoAction) throws HeadlessException {
        super(title);
        statusLogger = new StatusLogger(logTextPane);
        this.repoAction = repoAction;
        initializeView();
        initializeFormBasedOnRepoAction();
        addListeners();
    }

    private void initializeView() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        synchronizationStatusComponent = new SynchronizationStatusComponent();
        synchronizationStatusComponent.setSynchronizationStatus(SynchronizationStatus.WAITING_FOR_FEEDBACK);
        synchroStatusPanel.add(synchronizationStatusComponent);

        DefaultComboBoxModel<RepositoryModel> repositoryTypeComboBoxModel = new DefaultComboBoxModel<>();
        repositoryTypeComboBoxModel.addElement(new RepositoryModel(RepositoryType.REMOTE));
        repositoryTypeComboBoxModel.addElement(new RepositoryModel(RepositoryType.LOCAL));
        repositoryType.setModel(repositoryTypeComboBoxModel);
    }

    private void initializeFormBasedOnRepoAction() {
        // @TODO hide & show GUI parts
        switch (repoAction) {
            case CONNECT_TO_EXISTING_REPO_MIRROR:
                break;
            case CREATE_NEW_REPO_MIRROR:
                break;
        }
        setVisible(true);
    }

    private void addListeners() {
        mirrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mirror.createRepo(sourcePathTextField.getText(), MainWindow.this);
            }
        });

        repositoryType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (((RepositoryModel) e.getItem()).getRepositoryType() == RepositoryType.LOCAL) {
                        rootServerLoginPanel.setVisible(false);
                    } else {
                        rootServerLoginPanel.setVisible(true);
                    }
                }
            }
        });
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
