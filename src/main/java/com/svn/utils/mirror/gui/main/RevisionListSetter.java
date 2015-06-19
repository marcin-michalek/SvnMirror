package com.svn.utils.mirror.gui.main;

import com.svn.utils.mirror.app.Revision;

import java.util.List;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-19.
 */
public interface RevisionListSetter {
    void setRevisionList(List<Revision> revisionList);
}
