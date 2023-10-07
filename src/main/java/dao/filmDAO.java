package dao;

import java.util.List;

import model.Film;

public interface filmDAO {

	public List<Film> findAllFilms();
	
	public Film findFilmByName(String name);
	
	public List<Film> sortFilms(String column, String order, String condition);

	public void decrementQuantity(int filmID, int quantity);
}
