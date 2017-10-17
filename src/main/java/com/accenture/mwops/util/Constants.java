package com.accenture.mwops.util;

public class Constants {
    private Constants() throws IllegalAccessException {
        throw new IllegalAccessException("Constants class");
    }

    public static final String CONST_NULL_ARGS = "Parameters missing! \n[0] -> remoteMachine list,\n" +
                                                 "[1] -> scripts list\n";
    public static final String CONST_IVALID_REMOTE_MACHINE_FILE = "RemoteMachine parameter is not a file";
    public static final String CONST_IVALID_SCRIPT_FILE = "Script parameter is not a file";
    public static final String CONST_INVALID_MACHINE_INFORMATION = "File contains invalid machine information";
    public static final String CONST_PORT_DELIMATOR = ":";
}
