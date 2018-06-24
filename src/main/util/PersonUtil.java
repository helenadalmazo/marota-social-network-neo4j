package main.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import application.Main;
import main.person.Person;

public class PersonUtil {
	
	static public String RELATIONSHIP = "ISFRIENDSWITH";

	static public Long getNextCodigo() {
		Long codigoCount = null;
		
        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            sql = "MATCH (n:Person {}) RETURN MAX(n.codigo)";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
            	codigoCount = resultSet.getLong("MAX(n.codigo)");
            }
            
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return codigoCount + 1L;
	}	
	

}
