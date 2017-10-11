package com.accenture.mwops.entity;

public class RemoteMachine {
    private String username;
    private String password;
    private String host;
    private int port;

    public RemoteMachine() {    }
    public RemoteMachine(String host, String port) {
        setHost(host);
        setPort(Integer.parseInt(port));
    }

    public RemoteMachine(String username, String password, String host, int port) {
        setUsername(username);
        setPassword(password);
        setHost(host);
        setPort(port);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
