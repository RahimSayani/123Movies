package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Address;

public class addressDAOImpl implements addressDAO {

	private String dbPath;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}
	
	public addressDAOImpl(String dbPath) {
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
	public List<Address> findAllAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address findAddressById(int id) {
		Connection connection = null;
		
		String query = "SELECT * FROM ADDRESS WHERE id=?";
		
		Address a = null;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			
			a = new Address();
			
			a.setId(resultSet.getInt("id"));
			a.setStreet(resultSet.getString("street"));
			a.setProvince(resultSet.getString("province"));
			a.setCountry(resultSet.getString("country"));
			a.setZip(resultSet.getString("zip"));
			a.setPhone(resultSet.getString("phone"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return a;
	}

	@Override
	public boolean addAddress(Address a) {
		Connection connection = null;
		
		int id = a.getId();
		String street = a.getStreet();
		String province = a.getProvince();
		String country = a.getCountry();
		String zip = a.getZip();
		String phone = a.getPhone();
		
		
		String query = "INSERT INTO ADDRESS (id, street, province, country, zip, phone) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, id);
			statement.setString(2, street);
			statement.setString(3, province);
			statement.setString(4, country);
			statement.setString(5, zip);
			statement.setString(6, phone);
			
			int r = statement.executeUpdate();
			statement.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return false;
	}

	@Override
	public int generateAddressID() {
		Connection connection = null;
		String query = "SELECT MAX(id) AS max_id FROM ADDRESS";
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

}
