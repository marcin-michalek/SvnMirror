package com.svn.utils.mirror.app.util;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.svn.utils.mirror.app.remote.Configuration;
import com.svn.utils.mirror.app.remote.ssh.CommandNotFoundException;
import com.svn.utils.mirror.app.remote.ssh.SshConnect;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mar on 25.06.15.
 */
public class SvnCreateRepoTest {

    @Test
    public void svnCreateRepoTest() throws ConfigurationException, JSchException, CommandNotFoundException, SftpException, IOException {
        File configuration = new Configuration().addRepoName("mojaNazwaZajebistegoRepo").addSVNServerPath("/home/mar/exampleRepository").addHookConfig(
                "#!bin/bash" +
                "echo \"działam\"","pre-commit").build();

        SshConnect connect = new SshConnect("mar","*****","127.0.0.1");
        connect.sendFile(configuration);
        List<String> result = connect.executeCommands(new ArrayList<String>(){
            {
                add("sh ./conf.sh");
                add("rm conf.sh");
            }
        });
        result.forEach(System.out::println);

    }

}
