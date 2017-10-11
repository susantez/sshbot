package com.accenture.mwops;

import com.accenture.mwops.entity.RemoteMachine;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ShellExecutor {

    private static final Logger logger = LogManager.getLogger(ShellExecutor.class);

    private RemoteMachine remoteMachine;

    public void setRemoteMachine(RemoteMachine machine) {
        remoteMachine = machine;
    }

    public void executeFile(List<String> scripts) throws IOException, JSchException {

        if (this.remoteMachine == null) {
            return;
        }
        remoteMachine.setUsername(scripts.get(0));
        remoteMachine.setPassword(scripts.get(1));
        try {

            /**
             * Create a new Jsch object
             * This object will execute shell commands or scripts on server
             */
            JSch jsch = new JSch();

         /*
         * Open a new session, with your username, host and port
         * Set the password and call connect.
         * session.connect() opens a new connection to remote SSH server.
         * Once the connection is established, you can initiate a new channel.
         * this channel is needed to connect to remotely execution program
         */
            Session session = jsch.getSession(remoteMachine.getUsername(), remoteMachine.getHost(), remoteMachine.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications",  "publickey,keyboard-interactive,password");
            session.setPassword(remoteMachine.getPassword());
            session.connect();

            /* First 2 lines are for username and password. Scripts starts with index 2 */
            for (int i = 2; i < scripts.size(); i++) {
                //create the excution channel over the session
                ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

                // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
                InputStream in = channelExec.getInputStream();

                // Set the command that you want to execute
                // In our case its the remote shell script
                channelExec.setCommand("sh " + scripts.get(i));

                // Execute the command
                channelExec.connect();

                // Read the output from the input stream we set above
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuffer result = new StringBuffer();

                //Read each line from the buffered reader and add it to result list
                // You can also simple print the result here
                while ((line = reader.readLine()) != null) {
                    result.append(" " + line);
                }

                //retrieve the exit status of the remote command corresponding to this channel
                int exitStatus = channelExec.getExitStatus();

                //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
                channelExec.disconnect();
                logger.info(remoteMachine.getHost() + "|" + scripts.get(i) + "|" + exitStatus + "|" + result.toString());
            }

            session.disconnect();

        } catch(Exception e) {
            throw e;
        }
    }
}
