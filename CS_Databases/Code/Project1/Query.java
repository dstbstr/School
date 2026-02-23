/*******************************************************
	CSCD 327 RELATIONAL DATABASE SYSTEMS
					Project
			Student Name: Dustin Randall
 *******************************************************/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.lang.String;
import java.util.Scanner;

public class Query {

	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet result;
	private Scanner scanner = new Scanner(System.in);

	private static final String UPDATE_AUTHOR_QUERY = "UPDATE author SET firstName = ?, lastName = ? WHERE authorID = ?;";
	private static final String DELETE_AUTHOR_QUERY = "DELETE FROM author WHERE authorID = ?;";
	private static final String LIST_MULTI_AUTHORS_QUERY = """
				SELECT firstName, lastName, COUNT(*) AS bookCount FROM author NATURAL JOIN book_author
				GROUP BY authorID 
				HAVING bookCount > 1;""";

	private static final String LIST_TOP_SHIPPERS_QUERY = """
		SELECT ROUND(AVG(shipCost), 2) AS AvgCost, firstName, lastName 
		FROM orders NATURAL JOIN customer
		GROUP BY customerID
		HAVING AvgCost = (
		  SELECT MAX(AvgCost) As AvgCost
		  FROM (
		        SELECT AVG(shipCost) AS AvgCost
		        FROM orders
		        GROUP BY customerID
		  ) AS T
		);""";

	private static final String FIND_BOOKS_BY_PRICE = """
		SELECT title, name from book NATURAL JOIN publisher
		WHERE price >= ? AND price <= ?;""";

	private static final String GET_ORDER_VALUE = """
		SELECT SUM(IF(shipCost, shipCost, 0)) + SUM(IF(price, price, 0) * quantity) AS totalCost 
		FROM orders NATURAL JOIN items NATURAL JOIN book
		WHERE orderID = ?;""";

	private static final String GET_POPULAR_REGIONS = """
		SELECT region, COUNT(*) AS orderCount 
		FROM orders NATURAL JOIN customer
		GROUP BY region
		HAVING orderCount = (
  			SELECT MAX(orderCount)
  			FROM (
        		SELECT region, COUNT(*) AS orderCount
        		FROM orders NATURAL JOIN customer
        		GROUP BY region
  			) AS T
		);""";

	private static final String GET_CUSTOMER_PAID = """
		SELECT firstName, lastName, SUM(price * quantity) AS totalPrice
		FROM customer NATURAL JOIN orders NATURAL JOIN items NATURAL JOIN book
		WHERE firstName = ? AND lastName = ?;""";

	private static final String GET_AVERAGE_PRICES = """
		SELECT category, GetAvgPriceByCategory(category) AS avgPrice
		FROM book
		GROUP BY category;""";

	/*
	author(authorID, firstName, lastName)
	publisher(publisherID, name, contact, phone)
	customer(customerID, firstName, lastName, street, city, state, zip, region)
	book(ISBN, title, publicationDate, publisherID, price, category)
	book_author(ISBN, authorID, ranks)
	orders(orderID, customerID, orderDate, shipCost)
	items(orderID, itemNumber, quantity, ISBN)

	*/
	public Query(Connection c) throws SQLException
	{
		conn = c;
	}

	public void updateAuthor() throws SQLException {
		String authorId = getInput("Enter the author ID:", String.class);
		String firstName = getInput("Enter First Name:", String.class);
		String lastName = getInput("Enter Last Name:", String.class);

		stmt = conn.prepareStatement(UPDATE_AUTHOR_QUERY);
		stmt.setString(1, firstName);
		stmt.setString(2, lastName);
		stmt.setString(3, authorId);
		boolean success = stmt.executeUpdate() == 1;
		System.out.println(success ? "Success" : "Failed");
	}

	public void deleteAuthor() throws SQLException {
		stmt = conn.prepareStatement(DELETE_AUTHOR_QUERY);
		stmt.setString(1, getInput("Enter the author ID:", String.class));
		boolean success = stmt.executeUpdate() == 1;
		System.out.println(success ? "Success" : "Failed");
	}

	public void listMultiAuthors() throws SQLException{
		stmt = conn.prepareStatement(LIST_MULTI_AUTHORS_QUERY);
		result = stmt.executeQuery();
		printResultSet(result, "firstName", "lastName", "bookCount");
	}

	public void listTopShippers() throws SQLException {
		stmt = conn.prepareStatement(LIST_TOP_SHIPPERS_QUERY);
		result = stmt.executeQuery();
		printResultSet(result, "AvgCost", "firstName", "lastName");
	}

	public void findBooksByPrice() throws SQLException {
		stmt = conn.prepareStatement(FIND_BOOKS_BY_PRICE);
		stmt.setInt(1, getInput("Min Price", Integer.class));
		stmt.setInt(2, getInput("Max Price", Integer.class));
		result = stmt.executeQuery();
		printResultSet(result, "title", "name");
	}

	public void getOrderValue() throws SQLException {
		stmt = conn.prepareStatement(GET_ORDER_VALUE);
		stmt.setInt(1, getInput("Enter Order ID:", Integer.class));
		result = stmt.executeQuery();
		printResultSet(result, "totalCost");
	}

	public void getPopularRegions() throws SQLException {
		stmt = conn.prepareStatement(GET_POPULAR_REGIONS);
		result = stmt.executeQuery();
		printResultSet(result, "region", "orderCount");

	}

	public void getCustomerPaid() throws SQLException {
		stmt = conn.prepareStatement(GET_CUSTOMER_PAID);
		System.out.println("Enter Customer Name:");
		String name = scanner.nextLine();
		String[] nameParts = name.split(" ");
		stmt.setString(1, nameParts[0].toUpperCase());
		stmt.setString(2, nameParts[1].toUpperCase());
		result = stmt.executeQuery();
		printResultSet(result, "firstName", "lastName", "totalPrice");
	}

	public void getAveragePrices() throws SQLException {
		stmt = conn.prepareStatement(GET_AVERAGE_PRICES);
		result = stmt.executeQuery();
		printResultSet(result, "category", "avgPrice");
	}

	public void getCoauthors() throws SQLException{
		CallableStatement stmt = conn.prepareCall("{call GetAuthorPairs()}");
		stmt.execute();
		result = stmt.getResultSet();
		printResultSet(result, "authors");
	}

	private <T> T getInput(String prompt, Class<T> type) {
		System.out.println(prompt);
		String input = scanner.nextLine();
		if(type == Integer.class) return (T) Integer.valueOf(input);
		else if(type == Double.class) return (T) Double.valueOf(input);
		else return (T) input;
	}

	private void printResultSet(ResultSet result, String... columnNames) throws SQLException {
		for(int i = 0; i < columnNames.length; i++) {
			System.out.printf("%-35s", columnNames[i]);
		}
		System.out.println();

		while(result.next()) {
			for(int i = 0; i < columnNames.length; i++) {
				System.out.printf("%-35s", result.getString(columnNames[i]));
			}
			System.out.println();
		}
	}
}