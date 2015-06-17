package com.svn.utils.mirror.app;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.*;
import java.util.*;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public final class Mirror {
    private SVNRepository baseRepository;
    private SVNURL baseRepositoryURL;
    private SVNRepository mirrorRepository;
    private SVNURL mirrorRepositoryURL;
    private SVNClientManager svnClientManager;
    private static Mirror mirror;

    public static Mirror getInstance() {
        if (mirror == null) {
            mirror = new Mirror();
            mirror.init();
        }
        return mirror;
    }

    private Mirror() {
    }

    public void init() {
        svnClientManager = SVNClientManager.newInstance();
    }

    public SVNURL createRepo(String name) throws SVNException {
        SVNURL url = SVNRepositoryFactory.createLocalRepository(new File("repos/" + name), true, true);
        return url;
    }

    public SVNURL createBaseRepo(String name) throws SVNException {
        baseRepositoryURL = createRepo(name);
        return baseRepositoryURL;
    }

    public SVNURL createMirrorRepo(String name) throws SVNException {
        mirrorRepositoryURL = createRepo(name);
        return mirrorRepositoryURL;
    }

    public void loadBaseRepository(SVNURL url) throws SVNException {
        baseRepository = SVNRepositoryFactory.create(url);
        //authenticate(mirrorRepository, name, password);
    }

    public void loadMirrorRepository(SVNURL url) throws SVNException {
        mirrorRepository = SVNRepositoryFactory.create(url);
        //authenticate(mirrorRepository, name, password);
    }

    public void loadBaseRepositoryURL(String url) throws SVNException {
        baseRepositoryURL = SVNURL.fromFile(new File(url));
        loadBaseRepository(baseRepositoryURL);
    }

    public void loadMirrorRepositoryURL(String url) throws SVNException {
        mirrorRepositoryURL = SVNURL.fromFile(new File(url));
        loadMirrorRepository(mirrorRepositoryURL);
    }

    public void authenticate(SVNRepository repository, String name, String password) {
        ISVNAuthenticationManager authManager =
                SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
    }

    private void initializeRepository(SVNURL fromRepos, SVNURL toRepos) throws SVNException {
        svnClientManager.getAdminClient().doInitialize(baseRepositoryURL, mirrorRepositoryURL);
    }

    public void initializeRepository() throws SVNException {
        initializeRepository(baseRepositoryURL, mirrorRepositoryURL);
    }

    public void synchronize(SVNURL fromRepos, SVNURL toRepos) throws SVNException {
        svnClientManager.getAdminClient().doSynchronize(toRepos);
    }

    public void synchronize() throws SVNException {
        synchronize(baseRepositoryURL, mirrorRepositoryURL);
    }

    public void createHooks() throws IOException {
        String text = "#!/bin/sh \n" +
                "svnsync --non-interactive sync " +
                "file://" + mirrorRepositoryURL.getURIEncodedPath();
        File file = new File(baseRepositoryURL.getURIEncodedPath() + "/hooks/post-commit");
        file.createNewFile();
        file.setExecutable(true);
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) output.close();
        }

    }

    public void createBaseRepo(String name, CreateRepoInt createRepoInt) {
        try {
            SVNURL svnURL = createRepo(name);
            createRepoInt.onRepoCreated(svnURL);
        } catch (SVNException e) {
            createRepoInt.onRepoCreationException(e);
        }
    }

    public void createMirrorRepo(String name, CreateRepoInt createRepoInt) {
        try {
            SVNURL svnURL = createMirrorRepo(name);
            createRepoInt.onRepoCreated(svnURL);
        } catch (SVNException e) {
            createRepoInt.onRepoCreationException(e);
        }
    }

    long getRevison(SVNRepository repository) throws SVNException {
        return repository.getLatestRevision();
    }

    long getMirrorRevison() throws SVNException {
        return mirrorRepository.getLatestRevision();
    }

    long getBaseRevison() throws SVNException {
        return baseRepository.getLatestRevision();
    }

    boolean isSynced() throws SVNException {
        return getBaseRevison() == getMirrorRevison();

    }

    public String listEntries(String path) throws SVNException {
        Collection entries = baseRepository.getDir(path, -1, null, (Collection) null);
        Iterator iterator = entries.iterator();
        String output = "";
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            output += ("/" + (path.equals("") ? "" : path + "/") + entry.getName() +
                    " ( author: '" + entry.getAuthor() + "'; revision: " + entry.getRevision() +
                    "; date: " + entry.getDate() + ")\n");
            if (entry.getKind() == SVNNodeKind.DIR) {
                listEntries((path.equals("")) ? entry.getName() : path + "/" + entry.getName());
            }
        }
        return output;
    }

    String getHistory(long startRevision, long endRevision) throws SVNException {
        String history = "";
        Collection logEntries = null;

        logEntries = baseRepository.log(new String[]{""}, null, startRevision, endRevision, true, true);
        for (Iterator entries = logEntries.iterator(); entries.hasNext(); ) {
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
            history += "\n---------------------------------------------\n";
            history += "\nrevision: " + logEntry.getRevision();
            history += "\nauthor: " + logEntry.getAuthor();
            history += "\ndate: " + logEntry.getDate();
            history += "\nlog message: " + logEntry.getMessage();
            if (logEntry.getChangedPaths().size() > 0) {
                history += "\nchanged paths:";
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
                    history += a;
                }
            }
        }
        return history;
    }

    List<Revision> getRevisions() throws SVNException {
        List<Revision> list = new ArrayList<>();
        Collection logEntries = null;
        logEntries = baseRepository.log(new String[]{""}, null, 0, -1, true, true);
        for (Iterator entries = logEntries.iterator(); entries.hasNext(); ) {
            Revision revision = new Revision();
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
            revision.setId(logEntry.getRevision());
            revision.setAuthor(logEntry.getAuthor());
            revision.setDate(logEntry.getDate());
            revision.setMessage(logEntry.getMessage());
            if (logEntry.getChangedPaths().size() > 0) {
                String info= "";
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

    String getHistory() throws SVNException {
        return getHistory(0, -1);
    }

    public static void main(String[] args) throws SVNException {
        Mirror mirror = Mirror.getInstance();
        mirror.loadBaseRepositoryURL("repos/base/");
        mirror.loadMirrorRepositoryURL("repos/mirror");
        for(Revision r:mirror.getRevisions()){
            System.out.println(r);
        }
    }


}
