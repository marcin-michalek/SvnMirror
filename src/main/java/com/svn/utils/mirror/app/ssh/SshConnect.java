package com.svn.utils.mirror.app.ssh;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mar on 14.06.15.
 */
public class SshConnect {

    private Session session;

    public SshConnect(String user, String password, String host) throws JSchException {
        JSch jSch = new JSch();
        session = jSch.getSession(user, host);
        session.setPassword(password);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    public List<String> executeCommands(List<String> commands) {
        List<String> result = new ArrayList<>(commands.size());
        try {

            session.connect();
            for (String command : commands) {
                Channel channel = session.openChannel("exec");
                result.add(execute(channel, command));

            }
            session.disconnect();


        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isConnected() {
        return session.isConnected();
    }

    public String execute(Channel channel, String command) throws JSchException, IOException {
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();
        channel.connect();
        byte[] tmp = new byte[1024];
        String returnMessage = null;
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                returnMessage += new String(tmp, 0, i);
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
        }
        channel.disconnect();
        return returnMessage;
    }

    public void close() {
        session.disconnect();
    }

    public Session getSession() {
        return session;
    }
}
