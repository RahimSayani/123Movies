package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.customerDAO;
import dao.customerDAOImpl;
import dao.filmDAO;
import dao.filmDAOImpl;
import dao.addressDAO;
import dao.addressDAOImpl;
import model.Address;
import model.Customer;
import model.Film;
import dao.poDAO;
import dao.poDAOImpl;
import model.PurchaseOrder;


/**
 * Servlet implementation class FilmSiteController
 */
@WebServlet("/landing_page")
public class FilmSiteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	filmDAO filmDao;
	poDAO poDao;
	customerDAO cDao;
	addressDAO aDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmSiteController() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		ServletContext context = config.getServletContext();
		String dbPath = context.getRealPath("/WEB-INF/sqlitedb/Film.db");
		
		filmDao = new filmDAOImpl(dbPath);
		poDao = new poDAOImpl(dbPath);
		cDao = new customerDAOImpl(dbPath);
		aDao = new addressDAOImpl(dbPath);
		
		List<Film> filmList = filmDao.findAllFilms();
		context.setAttribute("filmList", filmList);
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		String view = "/jsp/home.jsp";		// default view
		
		//TODO: implement admin system (ideas: special sign on key for admin), decrease quantity of film after checkout, checkout regex for confirmation, add more films to the page.
		
		// CLIENT INPUT
		if (action != null) {
			
			if (action.equalsIgnoreCase("sales history")) {
				HttpSession session = request.getSession();
				List<PurchaseOrder> sales = poDao.findAllPurchaseOrders();
				
				session.setAttribute("sales", sales);
				view = "/jsp/saleshistory.jsp";
			}
			
			if (action.equalsIgnoreCase("purchase")) {
				
				// retrieve customer cart
				HttpSession session = request.getSession();
				@SuppressWarnings("unchecked")
				List<PurchaseOrder> cart = (List<PurchaseOrder>) session.getAttribute("cart");
				
				for (PurchaseOrder po : cart) {
					// mark as complete
					poDao.markPurchaseOrderComplete(po.getId());
					
					// decrement film quantity by purchase order quantity
					filmDao.decrementQuantity(po.getFilmID(), po.getQuantity());
				}
				session.setAttribute("cart", new ArrayList<PurchaseOrder>());
				
				ServletContext context = request.getServletContext();
				context.setAttribute("filmList", filmDao.findAllFilms());
				
				view = "/jsp/thankyou.jsp";
			}
			
			else if (action.equalsIgnoreCase("Confirm")) {
				boolean validCheckoutDetails = validateCheckout(request, response);
				
				if (validCheckoutDetails) {
					
					view = "/jsp/orderSummary.jsp";
				}
				else {
					request.setAttribute("message", "Invalid Payment or Shipping Details.");
					request.setAttribute("redirect", "Checkout");
					view = "/jsp/response.jsp";
				}
			}
			
			
			// CUSTOMER CHECKOUT
			else if (action.equalsIgnoreCase("Checkout")) {
				view = "/jsp/checkout.jsp";
			}
			// DECREMENT PURCHASE ORDER
			else if (action.equalsIgnoreCase("-")) {
				int poID = Integer.parseInt(request.getParameter("poID"));
				poDao.decrementPurchaseOrderQuantity(poID);
				poDao.removeEmptyPurchaseOrders();
				
				// UPDATE CART
				HttpSession session = request.getSession();
				Customer c = (Customer) session.getAttribute("account");
				List<PurchaseOrder> cart = poDao.findPurchaseOrdersByCustomerId(c.getId());
				session.setAttribute("cart", cart);
				
				double filmPrice = Double.parseDouble(request.getParameter("filmPrice"));
				double oldPrice = (double) session.getAttribute("total");
				double newPrice = Math.round((oldPrice - filmPrice) * 100.0) / 100.0;
				
				session.setAttribute("total", newPrice);
				
				view = "/jsp/cart.jsp";
			}
			// INCREMENT PURCHASE ORDER
			else if (action.equalsIgnoreCase("+")) {
				int poID = Integer.parseInt(request.getParameter("poID"));
				
				boolean available = checkAvailability(poID);
				
				if (available) {
					poDao.incrementPurchaseOrderQuantity(poID);
					
					// UPDATE CART
					HttpSession session = request.getSession();
					Customer c = (Customer) session.getAttribute("account");
					List<PurchaseOrder> cart = poDao.findPurchaseOrdersByCustomerId(c.getId());
					session.setAttribute("cart", cart);
					
					double filmPrice = Double.parseDouble(request.getParameter("filmPrice"));
					double oldPrice = (double) session.getAttribute("total");
					double newPrice = Math.round((oldPrice + filmPrice) * 100.0) / 100.0;
					
					session.setAttribute("total", newPrice);
					
					view = "/jsp/cart.jsp";
				}
				else {
					request.setAttribute("message", "We cannot accomodate your order due to supply shortages.");
					request.setAttribute("redirect", "Back to Cart");
					view = "/jsp/response.jsp";
				}
				
			}
			
			// SORTING AND FILTERING FILMS
			else if (action.equalsIgnoreCase("Sort Films") || action.equalsIgnoreCase("Filter Genre") || action.equalsIgnoreCase("Filter Studio")) {		// || filter genre || filter brand
				
				String column;
				String order;
				String condition;
				
				if (request.getParameter("sort").equalsIgnoreCase("none")) {
					column = "id";
					order = "ASC";
				}
				else {
					String [] sortBy = request.getParameter("sort").split(" ");
					
					column = sortBy[0];
					order = sortBy[1];
					
					if (order.equalsIgnoreCase("ascending")) {
						order = "ASC";
					}
					else {
						order = "DESC";
					}
				}
				
				if (request.getParameter("genre").equalsIgnoreCase("none")) {
					condition = "1 = 1";
				}
				else {
					String genre = request.getParameter("genre");
					condition = "genre = '" + genre + "'";
				}
				
				if (request.getParameter("studio").equalsIgnoreCase("none")) {
					condition += " AND 1 = 1";
				}
				else {
					String studio = request.getParameter("studio");
					condition += " AND studio = '" + studio + "'"; 
				}
								
				List<Film> sortedFilmList = filmDao.sortFilms(column, order, condition);
				
				ServletContext context = request.getServletContext();
				context.setAttribute("filmList", sortedFilmList);
			}
			
			// GO TO ACCOUNT PAGE
			else if (action.equalsIgnoreCase("account")) {
				view = "/jsp/account.jsp";
			}
			
			// GO TO REGISTER PAGE
			else if (action.equalsIgnoreCase("Register an account")) {
				view = "/jsp/register.jsp";
			}
			// REGISTER ACCOUNT
			else if (action.equalsIgnoreCase("Register")) {
				
				addCustomer(request, response);
				
				view = "/jsp/response.jsp";
			}
			
			// GO TO SIGN-IN PAGE
			else if (action.equalsIgnoreCase("Sign-in to your account")) {
				view = "/jsp/sign-in.jsp";
			}
			// SIGN IN
			else if (action.equalsIgnoreCase("Sign-in")) {
				signIn(request, response);
								
				view = "/jsp/response.jsp";
				
			}
			
			// SIGN OUT
			else if (action.equalsIgnoreCase("Sign-out of your account")) {
				signOut(request, response);
				view = "/jsp/response.jsp";
			}
			
			// FILM SELECTION
			else if (action.split("-")[0].equalsIgnoreCase("film")) {
				// iterate through all films
				String film = action.split("-")[1];
				
				ServletContext context = request.getServletContext();
				@SuppressWarnings("unchecked")
				List<Film> filmList = (List<Film>) context.getAttribute("filmList");
				
				for (Film f : filmList) {
					if (film.equalsIgnoreCase(f.getName())) {
						view = "/jsp/film_focus.jsp";
						request.setAttribute("film_focus", f);
					}
				}
			}
			
			// GO TO CART PAGE
			else if (action.equalsIgnoreCase("view cart") || action.equalsIgnoreCase("back to cart")) {
				HttpSession session = request.getSession();
				
				if (session.getAttribute("account") == null) {
					view = "/jsp/sign-in.jsp";
				}
				else if (session.getAttribute("account") != null){
					
					view = "/jsp/cart.jsp";
				}
			}
			// ADD FILM TO CART
			else if (action.split(" ")[0].equalsIgnoreCase("add")) {
				
				HttpSession session = request.getSession();
				
				if (session.getAttribute("total") == null) {
					session.setAttribute("total", 0.0);
				}
				Customer account = (Customer) session.getAttribute("account");
				
				// REDIRECT TO SIGN IN IF CUSTOMER NOT SIGNED IN
				if (account == null) {
					view = "/jsp/sign-in.jsp";
				}
				
				// ADD FILM TO CART AND PO
				else if (account != null){					
					
					// FINDING THE SELECTED FILM
					String filmName = action.substring(4, action.length()-8);
					Film film = filmDao.findFilmByName(filmName);
					
					if (film.getQuantity() == 0) {
						request.setAttribute("message", "This film is no longer available. We are currently sold out.");
						request.setAttribute("redirect", "Browse Other Films");
						view = "/jsp/response.jsp";
					}
					
					else {
						// INCREASING TOTAL PRICE
						double filmPrice = film.getPrice();
						double oldPrice = (double) session.getAttribute("total");
						double newPrice = Math.round((oldPrice + filmPrice) * 100.0) / 100.0;
						
						session.setAttribute("total", newPrice);
						
						// checking if the film is already in the cart
						boolean inCart = false;
						@SuppressWarnings("unchecked")
						List<PurchaseOrder> cart = (List<PurchaseOrder>) session.getAttribute("cart");
						for (PurchaseOrder p : cart) {
							if (p.getFilmID() == film.getId()) {
								p.setQuantity(p.getQuantity() + 1);
								view = "/jsp/cart.jsp";
								inCart = true;
							}
						}
						
						if (!inCart) {
							// create new purchase order (incomplete / pending)
							PurchaseOrder po = new PurchaseOrder();
							
							po.setId(poDao.generatePurchaseOrderID());
							po.setCustomerID(account.getId());
							po.setFilmID(film.getId());
							po.setDateOfPurchase("2023-08-14");
							po.setPurchased(0);
							po.setPrice(film.getPrice());
							po.setQuantity(1);
							po.setFilmName(film.getName());
							
							// add purchase order to database
							poDao.addPurchaseOrder(po);
							
							// add purchase order to cart
							cart.add(po);
							
							// update cart
							session.setAttribute("cart", cart);
							
							// cart view
							view="/jsp/cart.jsp";
						}
					}
					
				}
				
			}
			else if (action.equalsIgnoreCase("clear cart")) {
				
				// clearing session cart
				HttpSession session = request.getSession();
				session.setAttribute("cart", new ArrayList<PurchaseOrder>());
				session.setAttribute("total", 0.0);
				
				// clearing cart elements from purchase order db
				Customer account = (Customer) session.getAttribute("account");
				poDao.clearCustomerCart(account.getId());
				
				view = "/jsp/cart.jsp";
			}
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher(view);
		dispatch.forward(request, response);
	}
	
	private boolean validateCheckout(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			// NAME: firstname lastname
			String name = request.getParameter("name");
			if (!regexTest("^\\w+\\s{1}\\w+$", name)) {
				return false;
			}
			
			// NUMBER: 1234 5495 5443 3123
			String number = request.getParameter("number").trim().replaceAll(" ", "");
			if (!regexTest("^\\d{16}$", number)) {
				return false;
			}
			
			// EXPIRY: MM/YY
			String expiry = request.getParameter("expiry").trim();
			if (!regexTest("^\\d{2}/\\d{2}$", expiry) || !(Integer.parseInt(expiry.split("/")[1]) > 23)) {
				return false;
			}
			
			// CVV: 123
			String cvv = request.getParameter("cvv");
			if (!regexTest("^\\d{3}$", cvv)) {
				return false;
			}
			
			// STREET: ## streetName lane/ave/drive
			String street = request.getParameter("street");
			if (!regexTest("^\\d+\\s{1}\\w+\\s{1}\\w+$", street)) {
				request.setAttribute("message", "Invalid Street Input");
				return false;
			}
			
			// PROVINCE: non-digit
			String province = request.getParameter("province");
			if (!regexTest("^\\D+$", province)) {
				request.setAttribute("message", "Invalid Province Input");
				return false;
			}
			
			// COUNTRY: non-digit
			String country = request.getParameter("country");
			if (!regexTest("^\\D+$", country)) {
				request.setAttribute("message", "Invalid Country Input");
				return false;
			}
			
			// ZIP: 6-digit alphanumeric
			String zip = request.getParameter("zip").replace(" ", "");
			if (!regexTest("^[a-zA-Z0-9]{6}$", zip)) {
				request.setAttribute("message", "Invalid ZIP Input");
				return false;
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}

	private double findTotalPrice(List<PurchaseOrder> cart) {
		double total = 0.0;
		
		for (PurchaseOrder p : cart) {
			total += p.getPrice() * p.getQuantity();
		}
		
		total = Math.round(total * 100.0) / 100.0;
		
		return total;
	}
	
	private boolean checkAvailability(int poID) {
		
		// FIND PURCHASE ORDER FROM ID
		PurchaseOrder po = poDao.findPurchaseOrderById(poID);
		
		// FIND FILM FROM PURCHASE ORDER
		Film film = filmDao.findFilmByName(po.getFilmName());
		
		if (film.getQuantity() - po.getQuantity() > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	private void signOut(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		if (session.getAttribute("account") == null) {
			request.setAttribute("message", "Sign-out Failed. You are not currently signed into an account.");
		}
		else {
			session.setAttribute("account", null);
			session.setAttribute("cart", null);
			request.setAttribute("message", "Sign-out Successful. You are no longer signed-into your account.");
		}
		request.setAttribute("redirect", "Back to Home");
		
	}
	
	
	private void signIn(HttpServletRequest request, HttpServletResponse response) {
		
		// if login_status true redirect to sign out
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Customer c = cDao.findCustomerBySignIn(email, password);
		
		HttpSession session = request.getSession();

		if (c != null) {
			session.setAttribute("account", c);
			request.setAttribute("message", "Sign-in Successful.");
			request.setAttribute("redirect", "Back to Home");
			
			// retrieve customer cart
			List<PurchaseOrder> cart = poDao.findPurchaseOrdersByCustomerId(c.getId());
			session.setAttribute("cart", cart);
			session.setAttribute("total", findTotalPrice(cart));
		}
		else {
			request.setAttribute("message", "Sign-in Failed. Incorrect E-mail or Password.");
			request.setAttribute("redirect", "Sign-in to your account");
		}
		
		
	}
	
	
	private boolean regexTest(String regex, String input) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		return matcher.matches();
	}
	
	
	private void addCustomer(HttpServletRequest request, HttpServletResponse response) {
		
		
		try {
			request.setAttribute("redirect", "Register an account");
			
			// FIRST NAME: non-digit
			String firstName = request.getParameter("firstName");
			if (!regexTest("^\\D+$", firstName)) {
				request.setAttribute("message", "Invalid First Name Input");
				return;
			}
			
			// LAST NAME: non-digit
			String lastName = request.getParameter("lastName");
			if (!regexTest("^\\D+$", lastName)) {
				request.setAttribute("message", "Invalid Last Name Input");
				return;
			}
			
			// STREET: ## streetName lane/ave/drive
			String street = request.getParameter("street");
			if (!regexTest("^\\d+\\s{1}\\w+\\s{1}\\w+$", street)) {
				request.setAttribute("message", "Invalid Street Input");
				return;
			}
			
			// PROVINCE: non-digit
			String province = request.getParameter("province");
			if (!regexTest("^\\D+$", province)) {
				request.setAttribute("message", "Invalid Province Input");
				return;
			}
			
			// COUNTRY: non-digit
			String country = request.getParameter("country");
			if (!regexTest("^\\D+$", country)) {
				request.setAttribute("message", "Invalid Country Input");
				return;
			}
			
			// PHONE: ###-###-#####
			String phone = request.getParameter("phone");
			if (!regexTest("^\\d{3}-\\d{3}-\\d{4}", phone)) {
				request.setAttribute("message", "Invalid Phone Input");
				return;
			}
			
			// ZIP: 6-digit alphanumeric
			String zip = request.getParameter("zip").replace(" ", "");
			if (!regexTest("^[a-zA-Z0-9]{6}$", zip)) {
				request.setAttribute("message", "Invalid ZIP Input");
				return;
			}
			
			// EMAIL: name @ domain . ca/com/eu/...
			String email = request.getParameter("email");
			if (!regexTest("^[A-Za-z0-9._]+@[A-Za-z0-9._]+\\.\\w{2,}$", email)) {
				request.setAttribute("message", "Invalid E-mail Input");
				return;
			}
			
			// PASSWORD: any alphanumeric
			String password = request.getParameter("password");
			if (!regexTest("^\\S{4,}$", password)) {
				request.setAttribute("message", "Invalid Password Input");
				return;
			}
			
			Address a = new Address();
			a.setStreet(street);
			a.setProvince(province);
			a.setCountry(country);
			a.setPhone(phone);
			a.setZip(zip);
			a.setId(aDao.generateAddressID());
			
			Customer c = new Customer();
			c.setFirstName(firstName);
			c.setLastName(lastName);
			c.setAddressID(a.getId());
			c.setId(cDao.generateCustomerID());
			c.setAddressID(a.getId());
			c.setEmail(email);
			c.setPassword(password);
			
			aDao.addAddress(a);
			cDao.addCustomer(c);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("message", "Account Registered.");
		request.setAttribute("redirect", "Sign-in to your account");
	}
	
	


}
