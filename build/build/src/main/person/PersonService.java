package main.person;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import application.Main;
import main.util.DataUtil;
import main.util.PersonUtil;


public class PersonService {

	public static PersonService instance = new PersonService();

	public List<Person> getTodasAsPessoasDoSistema() {
		List<Person> pessoas = new ArrayList<>();

        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            sql = "MATCH (all:Person) RETURN all.codigo, all.nome, all.email, "
            		+ "all.dataNascimento, all.cidadeNascimento, all.cidadeResidencia "
            		+ "ORDER BY all.codigo";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Person pessoa = new Person();
                pessoa.setCodigo(resultSet.getLong("all.codigo"));
                pessoa.setNome(resultSet.getString("all.nome"));
                pessoa.setEmail(resultSet.getString("all.email"));
                pessoa.setDataNascimento(resultSet.getString("all.dataNascimento"));
                pessoa.setCidadeNascimento(resultSet.getString("all.cidadeNascimento"));
                pessoa.setCidadeResidencia(resultSet.getString("all.cidadeResidencia"));
                
                pessoa.setAmigos(getAmigos(pessoa.getCodigo().toString()));
                pessoas.add(pessoa);
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pessoas;
    }


    public Person getPessoa(String codigo) {
        Person pessoa = null;

        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            sql = "MATCH (pessoa:Person {codigo: " + codigo + "}) RETURN pessoa.codigo, pessoa.nome, pessoa.email, "
            		+ "pessoa.dataNascimento, pessoa.cidadeNascimento, pessoa.cidadeResidencia";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                pessoa = new Person();
                pessoa.setCodigo(resultSet.getLong("pessoa.codigo"));
                pessoa.setNome(resultSet.getString("pessoa.nome"));
                pessoa.setEmail(resultSet.getString("pessoa.email"));
                pessoa.setDataNascimento(resultSet.getString("pessoa.dataNascimento"));
                pessoa.setCidadeNascimento(resultSet.getString("pessoa.cidadeNascimento"));
                pessoa.setCidadeResidencia(resultSet.getString("pessoa.cidadeResidencia"));
                pessoa.setAmigos(getAmigos(codigo));
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pessoa;
    }

    public List<Person> getAmigos(String codigo) {
        List<Person> amigos = new ArrayList<>();

        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            sql = "MATCH (pessoa:Person {codigo: " + codigo + "})-[relacao:ISFRIENDSWITH {}]->(amigos) "
            		+ "RETURN amigos.codigo, amigos.nome, amigos.email, "
            		+ "amigos.dataNascimento, amigos.cidadeNascimento, amigos.cidadeResidencia";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Person pessoa = new Person();
                pessoa.setCodigo(resultSet.getLong("amigos.codigo"));
                pessoa.setNome(resultSet.getString("amigos.nome"));
                pessoa.setEmail(resultSet.getString("amigos.email"));
                pessoa.setDataNascimento(resultSet.getString("amigos.dataNascimento"));
                pessoa.setCidadeNascimento(resultSet.getString("amigos.cidadeNascimento"));
                pessoa.setCidadeResidencia(resultSet.getString("amigos.cidadeResidencia"));

                amigos.add(pessoa);
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return amigos;
    }

    public void insertPerson(Person pessoa) throws Exception {
        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            sql = "CREATE (:Person {codigo: ?, nome: ?, email: ?, "
            		+ "dataNascimento: ?, cidadeNascimento: ?, cidadeResidencia: ?})";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, PersonUtil.getNextCodigo());
            stmt.setString(2, pessoa.getNome());
            stmt.setString(3, pessoa.getEmail());

            // a conversão da data que o usuário digitou para gregoriancalendar
            // a data que o usuário digitou precisa ser instanciável como gregoriancalendar, 
            // se não gera uma exception
            GregorianCalendar date = DataUtil.instance.stringToGregorianCalendar(pessoa.getDataNascimento());
            stmt.setString(4, pessoa.getDataNascimento());
            
            stmt.setString(5, pessoa.getCidadeNascimento());
            stmt.setString(6, pessoa.getCidadeResidencia());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePerson(Person pessoa) throws Exception {
        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
    	sql = "MATCH (pessoa:Person {codigo: ?})"
    			+ "SET pessoa.nome = ?, "
    			+ "pessoa.email = ?, "
    			+ "pessoa.dataNascimento = ?, "
    			+ "pessoa.cidadeNascimento = ?, "
    			+ "pessoa.cidadeResidencia = ?";
    	PreparedStatement stmt = connection.prepareStatement(sql);
    	stmt.setLong(1, pessoa.getCodigo());
        stmt.setString(2, pessoa.getNome());
        stmt.setString(3, pessoa.getEmail());
        
        GregorianCalendar date = DataUtil.instance.stringToGregorianCalendar(pessoa.getDataNascimento());
        stmt.setString(4, pessoa.getDataNascimento());
        
        stmt.setString(5, pessoa.getCidadeNascimento());
        stmt.setString(6, pessoa.getCidadeResidencia());
        
        
        deleteAllFriendsOfThePerson(pessoa);
        
        for(Person amigo : pessoa.getAmigos()) {
        	insertFriend(pessoa, amigo);
        }
        
        stmt.execute();
        stmt.close();
    }
    
    public void deletePessoa(Person pessoa) {
        Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            Statement statement = connection.createStatement();

            //DETACH - remove o nó e seus relacionamentos
            sql = "MATCH (pessoa:Person {codigo: " + pessoa.getCodigo().toString() + "}) "
            		+ "DETACH DELETE pessoa";
            statement.execute(sql);

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void insertFriend(Person pessoa, Person amigo) throws Exception {
    	Connection connection =  Main.connectionManager.getConnection();
        String sql = null;

        sql = "MATCH (pessoa:Person {codigo: ?}) "
        		+ "MATCH (amigo:Person {codigo: ?}) "
        		+ "CREATE (pessoa)-[relacao:ISFRIENDSWITH {dataInicioAmizade: ''}]->(amigo)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, pessoa.getCodigo());
        stmt.setLong(2, amigo.getCodigo());
        stmt.execute();
        stmt.close();
    }
    
    public void deleteFriend(Person pessoa, Person amigo) {
    	Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            Statement statement = connection.createStatement();

            sql = "MATCH (pessoa:Person {codigo: ?}) "
            		+ "MATCH (amigo:Person {codigo: ?}) "
            		+ "MATCH (pessoa)-[relacao:ISFRIENDSWITH]->(amigo) "
            		+ "DELETE relacao";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, pessoa.getCodigo());
            stmt.setLong(2, amigo.getCodigo());
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteAllFriendsOfThePerson(Person pessoa) {
    	Connection connection =  Main.connectionManager.getConnection();
        String sql = null;
        try {
            sql = "MATCH (pessoa:Person {codigo: " + pessoa.getCodigo().toString() + " }) "
            		+ "-[relacao:ISFRIENDSWITH {}]->(amigos)"
            		+ "DELETE relacao";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
