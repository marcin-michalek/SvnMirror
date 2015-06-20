package com.svn.utils.mirror.app.remote.ssh;

/**
 * Created by mar on 20.06.15.
 */
public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(String msg){
        super(msg);
    }
}
