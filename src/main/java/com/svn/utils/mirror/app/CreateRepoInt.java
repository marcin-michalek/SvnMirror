package com.svn.utils.mirror.app;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public interface CreateRepoInt {
    void onRepoCreated(SVNURL svnurl);
    void onRepoCreationException(SVNException e);
}
