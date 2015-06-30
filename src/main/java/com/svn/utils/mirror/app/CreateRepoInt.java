package com.svn.utils.mirror.app;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import java.io.IOException;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public interface CreateRepoInt {
    void onRepoCreated(SVNURL svnurl);
    void onRepoCreationException(SVNException e);
    void onInitialization(Boolean b);
    void onInitializationException(SVNException e);
    void onSynchronization(Boolean b);
    void onSynchronizationException(SVNException e);
    void onHooksCreated(Boolean b);
    void onHooksCreatedException(IOException e);
    void onIsSyncedException(SVNException e);
    void onException(Exception e);
}
