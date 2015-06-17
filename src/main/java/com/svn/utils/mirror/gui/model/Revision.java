package com.svn.utils.mirror.gui.model;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-17.
 */
public class Revision {
    private String name;

    public Revision(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
