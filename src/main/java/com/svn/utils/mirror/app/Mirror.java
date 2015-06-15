package com.svn.utils.mirror.app;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.wc.admin.SVNAdminArea;
import org.tmatesoft.svn.core.internal.wc.admin.SVNAdminArea16;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import java.io.File;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class Mirror {
    public static void createRepo(String name, CreateRepoInt createRepoInt) {
        try {
            createRepoInt.onRepoCreated(SVNRepositoryFactory.createLocalRepository(new File("repos/" + name), true, true));
        } catch (SVNException e) {
            createRepoInt.onRepoCreationException(e);
        }
    }
}
