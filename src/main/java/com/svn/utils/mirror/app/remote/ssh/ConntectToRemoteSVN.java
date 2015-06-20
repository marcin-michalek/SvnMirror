package com.svn.utils.mirror.app.remote.ssh;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by mar on 19.06.15.
 */
public class ConntectToRemoteSVN {

    public ConntectToRemoteSVN(String user, String password, String url) throws URISyntaxException, SVNException {
        SVNStatusClient statusClient = new SVNStatusClient(new BasicAuthenticationManager(user, password), null);
        SVNStatus status = statusClient.doStatus(new File(new URI(url)), true);
    }


}
