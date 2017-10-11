package com.accenture.mwops.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Validator {
    private static final Logger logger = LogManager.getLogger(Validator.class);

    private Validator () throws IllegalAccessException {
        throw new IllegalAccessException("Validator Class");
    }

    public static boolean validateInputs(String[] args) {
        if (args == null || args.length < 2) {
            logger.error(Constants.constNullArgs);
            return false;
        }
        if(!checkFile(args[0])) {
            logger.error(Constants.constIvalidRemoteMachineFile);
            return false;
        }
        if(!checkFile(args[1])) {
            logger.error(Constants.constIvalidScriptFile);
            return false;
        }
        return true;
    }

    private static boolean checkFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }
}
