package com.svn.utils.mirror.app.util;

import com.svn.utils.mirror.app.remote.ssh.ConnectToRemoteSVN;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import java.net.URISyntaxException;

/**
 * Created by mar on 24.06.15.
 */
public class statusRepoTest {

    @Test
    public void repoTest() {
        try {
            ConnectToRemoteSVN connectToRemoteSVN = new ConnectToRemoteSVN("svn", "svn", "svn://192.168.2.111/stosowana/");
            System.out.println(connectToRemoteSVN.getRevisions());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }
}
