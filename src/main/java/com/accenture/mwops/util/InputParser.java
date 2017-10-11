package com.accenture.mwops.util;

import com.accenture.mwops.entity.RemoteMachine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by susantez, 11.10.2017
 * it is used to parse both remote machine list and scripts list to be executed
 * remoteMachine: each line represents a remote machine {host:port}
 * scripts: first line contains user and password that has proper authorization to execute scripts on remote
 *          each row has one script to be executed.
 */
public class InputParser {

    public static List<RemoteMachine> parseRemoteMachines(String fileName) throws Exception {
        List<RemoteMachine> remoteMachines = new ArrayList<>();
        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] arr = line.split(Constants.constPortDelimator);
            if(arr.length < 2) {
                throw new Exception(Constants.constInvalidMachineInformation + " - " + line);
            }
            remoteMachines.add(new RemoteMachine(arr[0],arr[1]));
        }
        return remoteMachines;
    }

    public static List<String> parseScripts(String fileName) throws Exception {
        List<RemoteMachine> remoteMachines = new ArrayList<>();
        Path path = Paths.get(fileName);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        int index = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (i > 1 && !lines.get(i).endsWith(".sh")) {
                throw new Exception(Constants.constInvalidSctipy + " - " + lines.get(i));
            }
        }
        return lines;
    }
}
