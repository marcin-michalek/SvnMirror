package com.svn.utils.mirror.gui.main;

import com.svn.utils.mirror.app.CreateRepoInt;
import com.svn.utils.mirror.app.Mirror;
import com.svn.utils.mirror.app.Revision;
import com.svn.utils.mirror.app.remote.Configuration;
import com.svn.utils.mirror.app.remote.RemoteMirror;
import com.svn.utils.mirror.gui.enums.RepoAction;
import com.svn.utils.mirror.gui.enums.RepositoryType;
import com.svn.utils.mirror.gui.enums.SynchronizationStatus;
import com.svn.utils.mirror.gui.logger.DetailsLogger;
import com.svn.utils.mirror.gui.logger.StatusLogger;
import com.svn.utils.mirror.gui.model.RepositoryModel;
import com.svn.utils.mirror.gui.model.RevisionModel;
import com.svn.utils.mirror.gui.view.SynchronizationStatusComponent;
import com.svn.utils.mirror.gui.worker.RefreshRepoStatusWorker;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class MainWindow extends JFrame implements CreateRepoInt, RevisionListSetter {

    private JPanel rootPanel;
    private JButton mirrorButton;
    private JTextPane logTextPane;
    private JTextField sourceRepositoryPathTextField;
    private JTextField destinationRepositoryPathTextField;
    private JComboBox repositoryTypeComboBox;
    private JTextField loginTextFields;
    private JPasswordField passwordField;
    private JPanel rootServerLoginPanel;
    private JTextPane detailsPane;
    private JPanel synchronizationStatusPanel;
    private JList revisionsList;
    private JTextField hostField;
    private StatusLogger statusLogger;
    private RepoAction repoAction;
    private SynchronizationStatusComponent synchronizationStatusComponent;
    private Mirror mirror;
    private RemoteMirror remoteMirror;
    private RevisionModel revisionModel;
    private DetailsLogger detailsLogger;
    private RefreshRepoStatusWorker refreshRepoStatusWorker;
    private final boolean isRemote;

    public MainWindow(String title, RepoAction repoAction, boolean isRemote) throws HeadlessException {
        super(title);
        this.isRemote = isRemote;
        statusLogger = new StatusLogger(logTextPane);
        detailsLogger = new DetailsLogger(detailsPane);
        this.repoAction = repoAction;
        initView();
        initFormBasedOnRepoAction();
        addListeners();


        if (isRemote) {
            remoteMirror = RemoteMirror.getInstance();
            refreshRepoStatusWorker = new RefreshRepoStatusWorker(this, remoteMirror);
            destinationRepositoryPathTextField.setText("Repository name");
            sourceRepositoryPathTextField.setText("Source repository path");
        } else {
            mirror = Mirror.getInstance();
            refreshRepoStatusWorker = new RefreshRepoStatusWorker(this, mirror);
            rootServerLoginPanel.setVisible(false);
        }


    }

    private void initView() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(rootPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        initSynchronizationStatusComponent();
        if(!isRemote){
            rootServerLoginPanel.setVisible(false);
        }
    }



    public void setRevisionList(List<Revision> revisionList) {
        DefaultListModel<Revision> revisionDefaultListModel = new DefaultListModel<>();
        for (Revision revision : revisionList) {
            revisionDefaultListModel.addElement(revision);
        }
        revisionsList.setModel(revisionDefaultListModel);
        updateSynchronizationStatusComponent();
    }

    private void initSynchronizationStatusComponent() {
        synchronizationStatusComponent = new SynchronizationStatusComponent();
        synchronizationStatusComponent.setSynchronizationStatus(SynchronizationStatus.WAITING_FOR_FEEDBACK);
        synchronizationStatusPanel.add(synchronizationStatusComponent);
    }

    private void initFormBasedOnRepoAction() {
        // @TODO hide & show GUI parts
        switch (repoAction) {
            case CONNECT_TO_EXISTING_REPO_MIRROR:
                mirrorButton.setText("Attach");
                break;
            case CREATE_NEW_REPO_MIRROR:
                break;
        }
        setVisible(true);
    }

    private void addListeners() {
        addMirrorButtonClickListener();
        addRevisionListSelectionListener();
    }

    private void addRevisionListSelectionListener() {
        revisionsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    detailsLogger.logRevisionDetails((Revision) revisionsList.getSelectedValue());
                }
            }
        });
    }



    private void addMirrorButtonClickListener() {
        mirrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRemote) {
                    remoteMirror.initializeAndCreateRepoMirroring(loginTextFields.getText(), passwordField.getText(),
                            hostField.getText(), sourceRepositoryPathTextField.getText(), destinationRepositoryPathTextField.getText(),
                            MainWindow.this);
                } else {
                    mirror.initializeAndCreateRepoMirroring(sourceRepositoryPathTextField.getText(),
                            destinationRepositoryPathTextField.getText(),
                            MainWindow.this);
                }
                updateSynchronizationStatusComponent();
            }
        });
    }

    private void updateSynchronizationStatusComponent() {
        if (isRemote) {
            if (remoteMirror.isSynced(MainWindow.this)) {
                synchronizationStatusComponent.setSynchronizationStatus(SynchronizationStatus.SYNCHRONIZED);
            } else {
                synchronizationStatusComponent.setSynchronizationStatus(SynchronizationStatus.NOT_SYNCHRONIZED);
            }
        } else {
            if (mirror.isSynced(MainWindow.this)) {
                synchronizationStatusComponent.setSynchronizationStatus(SynchronizationStatus.SYNCHRONIZED);
            } else {
                synchronizationStatusComponent.setSynchronizationStatus(SynchronizationStatus.NOT_SYNCHRONIZED);
            }
        }

    }

    private void startRefreshingStatusWorker() {
        new Thread(refreshRepoStatusWorker).start();
    }

    @Override
    public void onRepoCreated(SVNURL svnurl) {
        statusLogger.logSuccess("Repo created: " + svnurl.getPath());
    }

    @Override
    public void onRepoCreationException(SVNException e) {
        statusLogger.logError("Repo creation failed: " + e.getMessage());
    }

    @Override
    public void onInitialization(Boolean b) {
        statusLogger.logSuccess("Mirror initialized");
    }

    @Override
    public void onInitializationException(SVNException e) {
        statusLogger.logError("Mirror initialization failed: " + e.getMessage());

    }

    @Override
    public void onSynchronization(Boolean b) {
        statusLogger.logSuccess("Repos synchronized");
    }

    @Override
    public void onSynchronizationException(SVNException e) {
        statusLogger.logError("Repo synchronization failed: " + e.getMessage());
    }

    @Override
    public void onHooksCreated(Boolean b) {
        statusLogger.logSuccess("Hooks created");
        startRefreshingStatusWorker();
    }

    @Override
    public void onHooksCreatedException(IOException e) {
        statusLogger.logError("Hooks creation failed" + e.getMessage());
    }

    @Override
    public void onIsSyncedException(SVNException e) {
        statusLogger.logError("Hooks creation failed" + e.getMessage());

    }

    @Override
    public void onException(Exception e) {
        statusLogger.logError("Error : " + e.getMessage());
    }
}
