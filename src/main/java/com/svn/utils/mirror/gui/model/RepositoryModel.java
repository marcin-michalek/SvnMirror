package com.svn.utils.mirror.gui.model;

import com.svn.utils.mirror.gui.enums.RepositoryType;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-16.
 */
public class RepositoryModel {
    private RepositoryType repositoryType;

    public RepositoryModel(RepositoryType repositoryType) {
        this.repositoryType = repositoryType;
    }

    public RepositoryType getRepositoryType() {
        return repositoryType;
    }

    @Override
    public String toString() {
        return repositoryType.toString();
    }
}
