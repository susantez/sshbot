package com.accenture.mwops.util;

public class Constants {
    private Constants () throws IllegalAccessException {
        throw new IllegalAccessException("Constants class");
    }

    public static final String constNullArgs = "Parameters missing! \n[0] -> remoteMachine list,\n" +
            "[1] -> scripts list\n";
    public static final String constIvalidRemoteMachineFile = "RemoteMachine parameter is not a file";
    public static final String constIvalidScriptFile = "Script parameter is not a file";
    public static final String constInvalidMachineInformation = "File contains invalid machine information";
    public static final String constInvalidSctipy= "All scripts must end with .sh";

    public static final String constPortDelimator = ":";
}
