package com.svn.utils.mirror.app.remote;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNStatus;

/**
 * Created by mar on 19.06.15.
 */
public class HandleSvnStatus implements ISVNStatusHandler {
    @Override
    public void handleStatus(SVNStatus status) throws SVNException {
        //to do get info
    }
}
