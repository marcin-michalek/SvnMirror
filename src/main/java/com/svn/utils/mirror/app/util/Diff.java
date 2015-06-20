package com.svn.utils.mirror.app.util;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mar on 19.06.15.
 */
public class Diff {

    private SVNClientManager manager;

    public Diff(File repoRoot){
        manager = SVNClientManager.newInstance();

    }

    public String getDiff(File repo) throws SVNException {
        File baseDirectory = new File("/home/mar");
        File wcRoot = new File(baseDirectory, "exampleWC/iota");





            SVNClientManager clientManager = SVNClientManager.newInstance();
            //SVNWCClient wcClient = SVNClientManager.newInstance().getWCClient();
            //wcClient.doSetProperty(new File(wcRoot, "A/B"), "spam", SVNPropertyValue.create("egg"), false,
            //        SVNDepth.EMPTY, null, null);

            //now run diff the working copy against the repository
            SVNDiffClient diffClient = clientManager.getDiffClient();
            /*
             * This corresponds to 'svn diff -rHEAD'.
             */
            diffClient.doDiff(wcRoot, SVNRevision.WORKING, SVNRevision.PREVIOUS, SVNRevision.HEAD,
                    SVNDepth.INFINITY, true, System.out, null);
        return null;
    }


}
