package com.svn.utils.mirror.app;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

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

    public void loadBaseRepostory(SVNURL url) throws SVNException {
        baseRepository = SVNRepositoryFactory.create(url);
        //authenticate(mirrorRepository, name, password);

    }

    public void loadBaseRepositoryURL(String url) throws SVNException {
        baseRepositoryURL = SVNURL.fromFile(new File(url));
    }

    public void loadMirrorRepositoryURL(String url) throws SVNException {
        mirrorRepositoryURL = SVNURL.fromFile(new File(url));
    }

    public void loadMirrorRepostory(SVNURL url, String name, String password) throws SVNException {
        mirrorRepository = SVNRepositoryFactory.create(url);
        //authenticate(mirrorRepository, name, password);

    }

    public void authenticate(SVNRepository repository, String name, String password) {
        ISVNAuthenticationManager authManager =
                SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
    }

    private void setupSvnRepositoryFactory() {
        SVNRepositoryFactoryImpl.setup();
    }

    private void setupHttpRepositoryFactory() {
        DAVRepositoryFactory.setup();
    }

    private void setupFileRepositoryFactory() {
        FSRepositoryFactory.setup();
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

    public void createRepo(String name, CreateRepoInt createRepoInt) {
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
}
