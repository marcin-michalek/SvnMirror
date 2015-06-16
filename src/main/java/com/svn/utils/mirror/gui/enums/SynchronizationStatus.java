package com.svn.utils.mirror.gui.enums;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-16.
 */
public enum SynchronizationStatus {
    SYNCHRONIZED,
    NOT_SYNCHRONIZED,
    WAITING_FOR_FEEDBACK;


    @Override
    public String toString() {
        if (this == WAITING_FOR_FEEDBACK) {
            return "WAITING";
        } else {
            return super.toString();
        }
    }
}
