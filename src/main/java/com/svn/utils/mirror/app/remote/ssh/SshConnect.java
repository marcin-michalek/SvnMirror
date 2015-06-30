package com.svn.utils.mirror.app.remote.ssh;

import com.jcraft.jsch.*;
import com.svn.utils.mirror.app.remote.Configuration;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mar on 14.06.15.
 */
public class SshConnect {

    private Session session;
    private static final String CAN_CREATE_REPO = "svnadmin --version";
    private final String user;
    private final String host;
    private final String password;

    public SshConnect(String user, String password, String host) throws JSchException {
        this.password = password;
        this.host = host;
        this.user = user;
    }
    private void createSession() throws JSchException {
        if(session!=null)
            session.disconnect();
        JSch jSch = new JSch();
        session = jSch.getSession(user, host);
        session.setPassword(password);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }
    public void sendFile(File configuration) throws JSchException, IOException, SftpException, CommandNotFoundException, ConfigurationException {
        createSession();
        session.connect();
        Channel channel = session.openChannel("exec");
        channel.connect();
        execute(channel,CAN_CREATE_REPO);
        channel.disconnect();
        channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp channelSftp = (ChannelSftp)channel;
        File configurationFile = configuration;
        channelSftp.put(new FileInputStream(configurationFile),configuration.getName());
        channel.disconnect();
        session.disconnect();

    }
    public List<String> executeCommands(List<String> commands) throws CommandNotFoundException {
        List<String> result = new ArrayList<>(commands.size());
        try {
            createSession();
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

    private String execute(Channel channel, String command) throws JSchException, IOException, CommandNotFoundException {
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
                if(channel.getExitStatus() != 0)
                    throw new CommandNotFoundException("Command " + command + " on remote host");
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
