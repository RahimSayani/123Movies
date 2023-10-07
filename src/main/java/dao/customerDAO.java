package dao;

import java.util.List;

import model.Customer;

public interface customerDAO {

	public List<Customer> findAllCustomers();
	
	public Customer findCustomerById(int id);
	
	public void addCustomer(Customer c);
	
	public int generateCustomerID();
	
	public Customer findCustomerBySignIn(String email, String password);

}
