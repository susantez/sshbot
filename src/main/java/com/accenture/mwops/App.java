package com.accenture.mwops;

import com.accenture.mwops.entity.RemoteMachine;
import com.accenture.mwops.util.InputParser;
import com.accenture.mwops.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by susantez, 11.10.2017
 * SSH Bot!
 * It used to run multiple scripts on multiple remote machines via ssh and logs output
 * Input[0] = file, remote machine list {host:port}
 * Input[1] = scripts to be executed {script1;script2;...}
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) {
        try {
            /*debug mode
            if (args == null || args.length < 1) {
                args = new String[2];
                args[0] = "C:\\Users\\serkan.susantez\\Desktop\\remoteMachines.txt";
                args[1] = "C:\\Users\\serkan.susantez\\Desktop\\scripts.txt";
            }
            */
            if (Validator.validateInputs(args)) {
                logger.info("host|script|resultCode|result");
                List<RemoteMachine> remoteMachines = InputParser.parseRemoteMachines(args[0]);
                List<String> scripts = InputParser.parseScripts(args[1]);
                ShellExecutor executor = new ShellExecutor();
                for (RemoteMachine remoteMachine : remoteMachines) {
                    executor.setRemoteMachine(remoteMachine);
                    executor.executeFile(scripts);
                }
            }
        } catch (Exception t){
            logger.error(t);
        }
    }
}
