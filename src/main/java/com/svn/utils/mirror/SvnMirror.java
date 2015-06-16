package com.svn.utils.mirror;

import com.svn.utils.mirror.app.Mirror;
import com.svn.utils.mirror.gui.main.MainWindow;
import org.tmatesoft.svn.core.SVNException;

import java.io.IOException;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class SvnMirror {
    public static void main(String[] args) {

        //new MainWindow("HelloWorldSwing");
        Mirror mirror = Mirror.getInstance();
        try {
            //mirror.createBaseRepo("base");
            //mirror.createMirrorRepo("mirror");
            mirror.loadBaseRepositoryURL("repos/base");
            mirror.loadMirrorRepositoryURL("repos/mirror");
            //mirror.synchronize();
            mirror.createHooks();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }
}
