package com.svn.utils.mirror.gui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-17.
 */
public class RevisionModel {
    private List<Revision> revisions;

    public RevisionModel() {
        Revision r1 = new Revision("Commit " + new Date().getTime());
        Revision r2 = new Revision("Commit " + new Date().toString());

        revisions = new ArrayList<>();
        revisions.add(r1);
        revisions.add(r2);
    }

    public List<Revision> getRevisions() {
        return revisions;
    }
}
