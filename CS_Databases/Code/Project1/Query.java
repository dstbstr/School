/*******************************************************
	CSCD 327 RELATIONAL DATABASE SYSTEMS
					Project
			Student Name: ......... ADD YOUR NAME HERE
 *******************************************************/
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.*;
import java.lang.String;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Query {

	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet result;
	private Scanner scanner = new Scanner(System.in);

	public Query(Connection c) throws SQLException
	{
		conn = c;
	}

	public void updateAuthor() {

	}

	public void deleteAuthor() {

	}

	public void listMultiAuthors() {

	}

	public void listTopShippers() {

	}

	public void findBooksByPrice() {

	}

	public void getOrderValue() {

	}

	public void getPopularRegions() {

	}

	public void getCustomerPaid() {

	}

	public void getAveragePrices() {

	}

	public void getCoauthors() {
		
	}
	/*
	public void exampleQuery1() throws IOException, SQLException
	{
		// Take user input
		System.out.println("\nEnter the book id:");
		int bookID = scanner.nextInt();

		// Prepare the SQL statement
		String query  = "select * from Book where book_id =?";
		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input book id
		stmt.setInt(1, bookID);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nQuery output:");
		System.out.println("-------------");

		if(!result.next()) {
			System.out.println("No results exist for this input");
			return;
		}

		else
			do {
				// It is possible to get the columns using the column names or using the column number, which starts at 1. The example below uses both
				String row = result.getString(1) + "\t\t" + result.getString("title")+ "\t\t" + result.getString(3) + "\t\t" + result.getString(4) + "\t\t" + result.getString(5);
				System.out.println(row);
			} while (result.next());
	}

	public void exampleQuery2() throws IOException, SQLException
	{
		// Take user inputs
		System.out.println("\nEnter the member id:");
		int memberID = scanner.nextInt();
		scanner.nextLine();
		
		System.out.println("\nEnter the member's name:");
		String memberName = scanner.nextLine();
				
		System.out.println("\nEnter the member's city:");
		String city = scanner.nextLine();
		
		System.out.println("\nEnter the joining date:");
		String joinDate = scanner.nextLine();

		// Prepare the SQL statement
		String query  = "insert into Member values (?, ?, ?, ?)";
		stmt = conn.prepareStatement(query);

		// Replace the '?'s in the above statement with the inputs
		stmt.setInt(1, memberID);
		stmt.setString(2, memberName);
		stmt.setString(3, city);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		try { 
			Date joinDateFormatted = dateFormat.parse(joinDate);
			java.sql.Date sqlDate = new java.sql.Date(joinDateFormatted.getTime());
			stmt.setDate(4, sqlDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		};

		

		// Insert data with the query and print status
		if (stmt.executeUpdate() == 1)
			System.out.println("\nData added successfully.");

	}

	public void exampleQuery3() throws IOException, SQLException
	{
		// Take user input
		System.out.println("\nEnter the member id:");
		int memberID = scanner.nextInt();

		// Prepare the SQL statement
		String query  = "select title from Member join Borrow using (member_id) join Book using(book_id) where member_id =?";
		stmt = conn.prepareStatement(query);

		// Replace the '?' in the above statement with the input member id
		stmt.setInt(1, memberID);

		// Retrieve data with the query
		result = stmt.executeQuery();

		// Print the retrieved data
		System.out.println("\nQuery output:");
		System.out.println("-------------");

		if(!result.next()) {
			System.out.println("No results exist for this input");
			return;
		}

		else
			do {
				String row = result.getString(1);
				System.out.println(row);
			} while (result.next());
	}
	*/
}