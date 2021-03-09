package de.bethibande.bperms.utils;

import de.bethibande.bperms.core.BPerms;
import lombok.Getter;

import java.sql.*;

public class Sql {

    public static final String default_port = "3306";

    @Getter
    private final String host;
    @Getter
    private final String port;
    @Getter
    private final String user;
    @Getter
    private final String password;
    @Getter
    private final String db;

    private Connection connection;

    public Sql(String host, String port, String user, String password, String db) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.db = db;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            BPerms.getInstance().getLogger().logError("An error occurred while loading the mysql driver!");
        }
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.db + "?characterEncoding=utf8&autoReconnect=true", this.user, this.password);
            BPerms.getInstance().getLogger().log("§aSuccessfully connected to mysql!");
        } catch (SQLException e) {
            e.printStackTrace();
            BPerms.getInstance().getLogger().logError("Could not connect to mysql server!");
        }
    }

    public void disconnect() {
        try {
            if(this.connection != null) {
                this.connection.close();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        this.connection = null;
    }

    public void update(String cmd) {
        if(!this.isConnected()) this.connect();
        try {
            Statement s = this.connection.createStatement();
            s.execute(cmd);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        if(!this.isConnected()) this.connect();
        try {
            Statement s = this.connection.createStatement();
            return s.executeQuery(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

}