package com.svn.utils.mirror.app.util;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import java.io.File;

/**
 * Created by mar on 19.06.15.
 */
public class diffTest {

    private static final String REPO_PATH = "/home/mar/stosowana";

    @Test
    public void checkDiffOfRepo() throws SVNException {
        Diff diff = new Diff(new File(REPO_PATH));
        System.out.println(diff.getDiff(new File(REPO_PATH)));
    }
}
