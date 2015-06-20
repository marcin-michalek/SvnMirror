package com.svn.utils.mirror.app.util;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.svn.utils.mirror.app.remote.Configuration;
import com.svn.utils.mirror.app.remote.ssh.CommandNotFoundException;
import com.svn.utils.mirror.app.remote.ssh.SshConnect;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mar on 20.06.15.
 */

public class SshConnectTest {

    private static final String USERNAME = "mar";
    private static final String PASSWORD = "******";
    private static final String HOST = "127.0.0.1";

    @Test
    public void executeCommandInSsh() throws JSchException, CommandNotFoundException {
        SshConnect connect = new SshConnect(USERNAME,PASSWORD,HOST);
        List<String> result = connect.executeCommands(new ArrayList<String>(){
            {
                add("ls");
            }
        });
        result.forEach((System.out::println));
        Assert.assertTrue("List should be greather than 0", result.size() > 0);
    }

    @Test
    public void fileTransferTest() throws JSchException, FileNotFoundException, SftpException {
        SshConnect connect = new SshConnect(USERNAME,PASSWORD,HOST);
        connect.sendFile(new Configuration());
    }

    @Test(expected = CommandNotFoundException.class)
    public void commandNotFound() throws JSchException, CommandNotFoundException {
        SshConnect connect = new SshConnect(USERNAME,PASSWORD,HOST);
        List<String> result = connect.executeCommands(new ArrayList<String>(){
            {
                add("lsfff");
            }
        });
        result.forEach((System.out::println));
        Assert.assertTrue("List should be greather than 0", result.size() > 0);
    }
}
