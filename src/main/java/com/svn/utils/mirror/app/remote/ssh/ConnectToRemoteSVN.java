package com.svn.utils.mirror.app.remote.ssh;

import com.svn.utils.mirror.app.Revision;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by mar on 19.06.15.
 */
public class ConnectToRemoteSVN {

    private SVNRepository repository;

    public ConnectToRemoteSVN(String user, String password, String url) throws URISyntaxException, SVNException {
        repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user,password);
        repository.setAuthenticationManager(authManager);
    }

    public List<Revision> getRevisions() throws SVNException {
        List<Revision> list = new ArrayList<>();
        Collection logEntries = null;
        logEntries = repository.log(new String[]{""}, null, 0, -1, true, true);
        for (Iterator entries = logEntries.iterator(); entries.hasNext(); ) {
            Revision revision = new Revision();
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
            revision.setId(logEntry.getRevision());
            revision.setAuthor(logEntry.getAuthor());
            revision.setDate(logEntry.getDate());
            revision.setMessage(logEntry.getMessage());
            if (logEntry.getChangedPaths().size() > 0) {
                String info = "";
                Set changedPathsSet = logEntry.getChangedPaths().keySet();

                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext(); ) {
                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                    String a = " "
                            + entryPath.getType()
                            + " "
                            + entryPath.getPath()
                            + ((entryPath.getCopyPath() != null) ? " (from "
                            + entryPath.getCopyPath() + " revision "
                            + entryPath.getCopyRevision() + ")" : "");
                    info += a;
                }
                revision.setChanges(info);
            }
            list.add(revision);
        }
        return list;
    }




}
