package com.svn.utils.mirror.gui.model;

import com.svn.utils.mirror.app.Revision;

import java.util.List;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-17.
 */
public class RevisionModel {
    private List<Revision> revisions;

    public RevisionModel(List<Revision> revisions) {
        this.revisions = revisions;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }
}
