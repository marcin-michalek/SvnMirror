package com.svn.utils.mirror.gui.worker;

import com.svn.utils.mirror.app.Mirror;
import com.svn.utils.mirror.app.Revision;
import com.svn.utils.mirror.app.remote.RemoteMirror;
import com.svn.utils.mirror.gui.main.RevisionListSetter;
import org.tmatesoft.svn.core.SVNException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-19.
 */
public class RefreshRepoStatusWorker extends SwingWorker<List<Revision>, List<Revision>> {
    private RevisionListSetter revisionListSetter;
    private Mirror mirror;
    private RemoteMirror remoteMirror;
    private boolean shouldWork;
    private List<Revision> currentRevisionList;

    public RefreshRepoStatusWorker(RevisionListSetter revisionListSetter, Mirror mirror) {
        this.revisionListSetter = revisionListSetter;
        this.mirror = mirror;
        this.shouldWork = true;
        this.currentRevisionList = new ArrayList<>();
    }
    public RefreshRepoStatusWorker(RevisionListSetter revisionListSetter, RemoteMirror mirror) {
        this.revisionListSetter = revisionListSetter;
        this.remoteMirror = mirror;
        this.shouldWork = true;
        this.currentRevisionList = new ArrayList<>();
    }


    @Override
    protected List<Revision> doInBackground() throws Exception {
        while (shouldWork) {
            sleep(3000);
            if (newRevisionsAdded()) {
                if(mirror != null)
                    publish(mirror.getRevisions());
                else
                    publish(remoteMirror.getRevisions());
            }
        }
        if(mirror != null)
            return mirror.getRevisions();
        else
             return remoteMirror.getRevisions();
    }

    private void sleep(int i) {
        try {
            synchronized (this) {
                this.wait(i);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void process(List<List<Revision>> chunks) {
        try {
            publishCurrentRevisionList();
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    private void publishCurrentRevisionList() throws SVNException {
        if(mirror != null) {
            currentRevisionList = mirror.getRevisions();
            revisionListSetter.setRevisionList(currentRevisionList);
        }
        else {
            currentRevisionList = remoteMirror.getRevisions();
            revisionListSetter.setRevisionList(currentRevisionList);
        }

    }

    private boolean newRevisionsAdded() throws SVNException {
        if(mirror != null) {
            return currentRevisionList.size() != mirror.getRevisions().size();
        }
        else {
            return currentRevisionList.size() != remoteMirror.getRevisions().size();
        }

    }

    public void setShouldWork(boolean shouldWork) {
        this.shouldWork = shouldWork;
    }
}
