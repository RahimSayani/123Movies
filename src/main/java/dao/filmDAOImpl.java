package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Film;

public class filmDAOImpl implements filmDAO {

	private String dbPath;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}
	
	public filmDAOImpl(String dbPath) {
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
	public List<Film> findAllFilms() {
		Connection connection = null;
		String query = "SELECT * FROM FILM";
		
		List<Film> result = new ArrayList<Film>();
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Film f = new Film();
				f.setId(Integer.parseInt(resultSet.getString("id")));
				f.setName(resultSet.getString("name"));
				f.setDirector(resultSet.getString("director"));
				f.setStarring(resultSet.getString("starring"));
				f.setStudio(resultSet.getString("studio"));
				f.setReleaseDate(resultSet.getString("releaseDate"));
				f.setGenre(resultSet.getString("genre"));
				f.setDescription(resultSet.getString("description"));
				f.setRating(Double.parseDouble(resultSet.getString("rating")));
				f.setQuantity(Integer.parseInt(resultSet.getString("quantity")));
				f.setPrice(Double.parseDouble(resultSet.getString("price")));
				
				result.add(f);
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
	public Film findFilmByName(String name) {
		Connection connection = null;
		String query = "SELECT * FROM FILM WHERE name=?";
		Film f = null;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setString(1, name);
			
			ResultSet resultSet = statement.executeQuery();
			
			f = new Film();
			f.setId(Integer.parseInt(resultSet.getString("id")));
			f.setName(resultSet.getString("name"));
			f.setDirector(resultSet.getString("director"));
			f.setStarring(resultSet.getString("starring"));
			f.setStudio(resultSet.getString("studio"));
			f.setReleaseDate(resultSet.getString("releaseDate"));
			f.setGenre(resultSet.getString("genre"));
			f.setDescription(resultSet.getString("description"));
			f.setRating(Double.parseDouble(resultSet.getString("rating")));
			f.setQuantity(Integer.parseInt(resultSet.getString("quantity")));
			f.setPrice(Double.parseDouble(resultSet.getString("price")));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return f;
	}

	@Override
	public List<Film> sortFilms(String column, String order, String condition) {
		Connection connection = null;
		String query = "SELECT * FROM FILM WHERE " + condition + " ORDER BY " + column + " " + order;
		System.out.println(query);
		List<Film> result = new ArrayList<Film>();

		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Film f = new Film();
				f.setId(Integer.parseInt(resultSet.getString("id")));
				f.setName(resultSet.getString("name"));
				f.setDirector(resultSet.getString("director"));
				f.setStarring(resultSet.getString("starring"));
				f.setStudio(resultSet.getString("studio"));
				f.setReleaseDate(resultSet.getString("releaseDate"));
				f.setGenre(resultSet.getString("genre"));
				f.setDescription(resultSet.getString("description"));
				f.setRating(Double.parseDouble(resultSet.getString("rating")));
				f.setQuantity(Integer.parseInt(resultSet.getString("quantity")));
				f.setPrice(Double.parseDouble(resultSet.getString("price")));
				
				result.add(f);
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
	public void decrementQuantity(int filmID, int quantity) {
		Connection connection = null;
		String query = "UPDATE FILM SET quantity = quantity - ? WHERE id = ?";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, quantity);
			statement.setInt(2, filmID);
			
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
	}

}
