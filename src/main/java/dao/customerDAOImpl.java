package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;

public class customerDAOImpl implements customerDAO{

	private String dbPath;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}
	
	public customerDAOImpl(String dbPath) {
		this.dbPath = dbPath;
	}
	
	private Connection getConnection() throws SQLException {
		 return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
	}

	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} catch (SQLException ex) {
		}
	}

	@Override
	public List<Customer> findAllCustomers() {
		
		List<Customer> result = new ArrayList<Customer>();
		
		Connection connection = null;
		
		String query = "SELECT * FROM CUSTOMER";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				
				Customer c = new Customer();
				
				// populate book and author beans with needed info, and then set author to book
				
				c.setFirstName(resultSet.getString("firstName"));
				c.setLastName(resultSet.getString("lastName"));
				c.setId(Integer.parseInt(resultSet.getString("id")));
				c.setAddressID(Integer.parseInt(resultSet.getString("addressID")));
				c.setEmail(resultSet.getString("email"));
				c.setPassword(resultSet.getString("password"));
				System.out.println(resultSet.getString("firstName"));
				result.add(c);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return result;
	}

	@Override
	public Customer findCustomerById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCustomer(Customer c) {
		Connection connection = null;
		
		int id = c.getId();
		String firstName = c.getFirstName();
		String lastName =c.getLastName();
		int addressID = c.getAddressID();
		String email = c.getEmail();
		String password = c.getPassword();
		
		String query = "INSERT INTO CUSTOMER (id, firstName, lastName, addressID, email, password)"
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, id);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setInt(4, addressID);
			statement.setString(5, email);
			statement.setString(6, password);
			
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
	}

	@Override
	public int generateCustomerID() {
		Connection connection = null;
		String query = "SELECT MAX(id) AS max_id FROM CUSTOMER";
		int id = 0;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			id = Integer.parseInt(resultSet.getString("max_id")) + 1;
		}
		catch (Exception e){
			e.printStackTrace();
			return 0;
		}
		finally {
			closeConnection(connection);
		}
		return id;
	}

	@Override
	public Customer findCustomerBySignIn(String email, String password) {
		Connection connection = null;
		String query = "SELECT * FROM CUSTOMER WHERE email=? AND password=?";
		Customer c = null;

		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setString(1, email);
			statement.setString(2, password);
			
			ResultSet resultSet = statement.executeQuery();
			
			c = new Customer();
			c.setFirstName(resultSet.getString("firstName"));
			c.setLastName(resultSet.getString("lastName"));
			c.setAddressID(Integer.parseInt(resultSet.getString("addressID")));
			c.setId(Integer.parseInt(resultSet.getString("id")));
			c.setEmail(resultSet.getString("email"));
			c.setPassword(resultSet.getString("password"));
		}
		catch (Exception e) {
			return null;
		}
		finally {
			closeConnection(connection);
		}
		return c;
	}
}
