package com.accenture.mwops;

import com.accenture.mwops.entity.RemoteMachine;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

public class ShellExecutor {

    private static final Logger logger = LogManager.getLogger(ShellExecutor.class);

    private RemoteMachine remoteMachine;

    public void setRemoteMachine(RemoteMachine machine) {
        remoteMachine = machine;
    }

    public void runCommands(List<String> scripts) throws InterruptedException, JSchException, IOException {
        if (this.remoteMachine == null) {
            return;
        }
        remoteMachine.setUsername(scripts.get(0));
        remoteMachine.setPassword(scripts.get(1));

        Session session = null;
        ChannelExec channelExec = null;
        try {
            session = getSession(remoteMachine);
            //create the excution channel over the session
            channelExec = (ChannelExec) session.openChannel("exec");
            if (remoteMachine.getRootUser() == null || remoteMachine.getRootUser().isEmpty()) {
                for (int i = 2; i < scripts.size(); i++) {
                    executeCommand(channelExec, scripts.get(i));
                }
            } else {
                executeAsSudo(channelExec, scripts);
            }
            session.disconnect();

        } catch (JSchException | InterruptedException | IOException ex) {
            throw (ex);
        } finally {
            if (channelExec != null && channelExec.isConnected()) {
                channelExec.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public void executeCommand(ChannelExec channelExec, String command) throws IOException, JSchException {
        // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
        InputStream in = channelExec.getInputStream();

        // Set the command that you want to execute
        // In our case its the remote shell script
        channelExec.setCommand("" + command);

        // Execute the command
        channelExec.connect();
        // Read the output from the input stream we set above
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;

        //Read each line from the buffered reader and add it to result list
        // You can also simple print the result here
        while ((line = reader.readLine()) != null) {
            logger.info(remoteMachine.getHost() + "|" + line);
        }
    }

    public void executeAsSudo(ChannelExec channelExec, List<String> scripts) throws JSchException, IOException, InterruptedException {
        // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
        channelExec.setCommand("dzdo su - " + remoteMachine.getRootUser());
        channelExec.setPty(true);
        channelExec.connect();
        InputStream inputStream = channelExec.getInputStream();
        OutputStream out = channelExec.getOutputStream();
        out.write(("\n").getBytes());
        out.flush();
        Thread.sleep(1000);
            /* First 2 lines are for username and password. Scripts starts with index 2 */
        for (int i = 2; i < scripts.size(); i++) {
            out.write((scripts.get(i) + "\n").getBytes());
            out.flush();
            Thread.sleep(1000);
        }
        logInfo(inputStream);
        Thread.sleep(1000);
        out.write(("logout" + "\n").getBytes());
        out.flush();
        Thread.sleep(1000);

        out.write(("exit" + "\n").getBytes());
        out.flush();
        out.close();
    }

    private Session getSession(RemoteMachine remoteMachine) throws JSchException {
        /*
         * Open a new session, with your username, host and port
         * Set the password and call connect.
         * session.connect() opens a new connection to remote SSH server.
         * Once the connection is established, you can initiate a new channel.
         * this channel is needed to connect to remotely execution program
         */
        JSch jsch = new JSch();
        Session session = jsch.getSession(remoteMachine.getUsername(), remoteMachine.getHost(), remoteMachine.getPort());
        session.setPassword(remoteMachine.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        session.connect();
        return session;
    }

    private void logInfo(InputStream in) {
        StringBuilder result = new StringBuilder();
        try {
            byte[] tmp = new byte[1024];
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0)
                    break;
                result.append(new String(tmp, 0, i));
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.info(remoteMachine.getHost() + "\n" + result.toString());
        }
    }
}
