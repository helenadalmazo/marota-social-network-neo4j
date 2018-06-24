package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;

import application.Main;
import main.person.Person;


public class ConnectionManager {
    //private String driver = "org.neo4j.jdbc.Driver";
    private String user; // = "neo4j";
    private String password; // = "helena";
    private String url = "jdbc:neo4j:bolt://"; //127.0.0.1:7687";
    private Connection connection;

    public ConnectionManager() { }

    public ConnectionManager(String host, String port, String user, String password) {

    	StringBuilder builderURL = new StringBuilder();
    	builderURL.append(this.url);
    	builderURL.append(host);
    	builderURL.append(":");
    	builderURL.append(port);
    	builderURL.append("/");

    	setUser(user);
    	setPassword(password);
    	setUrl(builderURL.toString());
    }

    public void open() throws SQLException, ClassNotFoundException {
        //Class.forName(driver);
        System.out.println("URL: " + url);
        System.out.println("Usuário: " + user);
        System.out.println("Senha: " + password);
        Connection connection = DriverManager.getConnection(url, user, password);
        setConnection(connection);
    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
