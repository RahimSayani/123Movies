package dao;

import java.util.List;

import model.Address;

public interface addressDAO {
	
	public List<Address> findAllAddresses ();
	
	public Address findAddressById(int id);
	
	public boolean addAddress(Address a);
	
	public int generateAddressID();
}
