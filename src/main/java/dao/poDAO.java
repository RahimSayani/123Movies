package dao;

import java.util.List;

import model.PurchaseOrder;

public interface poDAO {
		
	public List<PurchaseOrder> findPurchaseOrdersByCustomerId (int id);
		
	public void addPurchaseOrder (PurchaseOrder p);
	
	public int generatePurchaseOrderID();
	
	public void clearCustomerCart(int customerID);
	
	public PurchaseOrder findPurchaseOrderById(int poID);

	public void decrementPurchaseOrderQuantity(int poID);

	public void incrementPurchaseOrderQuantity(int poID);
	
	public void removeEmptyPurchaseOrders();

	public void markPurchaseOrderComplete(int id);

	public List<PurchaseOrder> findAllPurchaseOrders();
	
}
