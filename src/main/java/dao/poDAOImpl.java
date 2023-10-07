package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.PurchaseOrder;

public class poDAOImpl implements poDAO {

	private String dbPath;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}
	
	public poDAOImpl(String dbPath) {
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
	public int generatePurchaseOrderID() {
		Connection connection = null;
		String query = "SELECT MAX(id) AS max_id FROM PO";
		
		int id = 0;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.getString("max_id") == null) {
				id = 1;
			}
			else {
				id = Integer.parseInt(resultSet.getString("max_id"))+ 1;
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return id;
	}

	@Override
	public List<PurchaseOrder> findPurchaseOrdersByCustomerId(int customerID) {
		Connection connection = null;
		String query = "SELECT * FROM PO WHERE customerID=? AND purchased=?";

		List<PurchaseOrder> poList = new ArrayList<>();
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, customerID);
			statement.setInt(2, 0);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				 PurchaseOrder po = new PurchaseOrder();
				 po.setId(resultSet.getInt("id"));
				 po.setCustomerID(resultSet.getInt("customerID"));
				 po.setFilmID(resultSet.getInt("filmID"));
				 po.setDateOfPurchase(resultSet.getString("dateOfPurchase"));
				 po.setPurchased(resultSet.getInt("purchased"));
				 po.setPrice(resultSet.getDouble("price"));
				 po.setQuantity(resultSet.getInt("quantity"));
				 po.setFilmName(resultSet.getString("filmName"));
				 
				 if (po.getPurchased() == 0) {
					 poList.add(po);
				 }
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return poList;
	}

	@Override
	public void addPurchaseOrder(PurchaseOrder p) {
		Connection connection = null;
		String query = "INSERT INTO PO (id, customerID, filmID, filmName, dateOfPurchase, purchased, quantity, price) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, p.getId());
			statement.setInt(2, p.getCustomerID());
			statement.setInt(3, p.getFilmID());
			statement.setString(4, p.getFilmName());
			statement.setString(5, p.getDateOfPurchase());
			statement.setInt(6, p.getPurchased());
			statement.setInt(7, p.getQuantity());
			statement.setDouble(8, p.getPrice());
			
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
	public void clearCustomerCart(int customerID) {
		Connection connection = null;
		String query = "DELETE FROM PO WHERE customerID=?";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, customerID);
			
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
	public PurchaseOrder findPurchaseOrderById(int poID) {
		Connection connection = null;
		String query = "SELECT * FROM PO WHERE id = ?";
		
		PurchaseOrder po = null;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, poID);
			ResultSet resultSet = statement.executeQuery();
			
			po = new PurchaseOrder();
			po.setId(resultSet.getInt("id"));
			po.setCustomerID(resultSet.getInt("customerID"));
			po.setFilmID(resultSet.getInt("filmID"));
			po.setDateOfPurchase(resultSet.getString("dateOfPurchase"));
			po.setPurchased(resultSet.getInt("purchased"));
			po.setPrice(resultSet.getDouble("price"));
			po.setQuantity(resultSet.getInt("quantity"));
			po.setFilmName(resultSet.getString("filmName"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return po;
	}

	@Override
	public void decrementPurchaseOrderQuantity(int poID) {
		Connection connection = null;
		String query = "UPDATE PO SET quantity = quantity - 1 WHERE id = ?";
				
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, poID);
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
	public void incrementPurchaseOrderQuantity(int poID) {
		Connection connection = null;
		String query = "UPDATE PO SET quantity = quantity + 1 WHERE id = ?";
				
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, poID);
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
	public void removeEmptyPurchaseOrders() {
		Connection connection = null;
		String query = "DELETE FROM PO WHERE quantity = 0";
				
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
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
	public void markPurchaseOrderComplete(int id) {
		Connection connection = null;
		String query = "UPDATE PO SET purchased = 1 WHERE id = ?";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			
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
	public List<PurchaseOrder> findAllPurchaseOrders() {
		Connection connection = null;
		String query = "SELECT * FROM PO";
		
		List<PurchaseOrder> poList = null;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			poList = new ArrayList<PurchaseOrder>();
			
			while (resultSet.next()) {
				PurchaseOrder po = new PurchaseOrder();
				po.setId(resultSet.getInt("id"));
				po.setCustomerID(resultSet.getInt("customerID"));
				po.setFilmID(resultSet.getInt("filmID"));
				po.setDateOfPurchase(resultSet.getString("dateOfPurchase"));
				po.setPurchased(resultSet.getInt("purchased"));
				po.setPrice(resultSet.getDouble("price"));
				po.setQuantity(resultSet.getInt("quantity"));
				po.setFilmName(resultSet.getString("filmName"));
				
				poList.add(po);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return poList;
	}
}
