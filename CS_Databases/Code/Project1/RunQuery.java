/*******************************************************
	CSCD 327 RELATIONAL DATABASE SYSTEMS
					Project
			Student Name: Dustin Randall
 *******************************************************/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Scanner;

public class RunQuery {

	public static void main(String[] args) {
		int selection = 0;
		Scanner scanner = new Scanner(System.in);
		Connection conn = null;

		try {

			conn = getConnection();
			Query db = new Query(conn);

			System.out.println("Welcome!!!");

			while (true) {
				System.out.println("\nChoose from the following options");
				System.out.println("1. updateAuthor");
				System.out.println("2. deleteAuthor");
				System.out.println("3. listMultiAuthors");
				System.out.println("4. listTopShippers");
				System.out.println("5. findBooksByPrice");
				System.out.println("6. getOrderValue");
				System.out.println("7. getPopularRegions");
				System.out.println("8. getCustomerPaid");
				System.out.println("9. getAveragePrices");
				System.out.println("10. getCoauthors");
				System.out.println();
				
				selection = scanner.nextInt();
				switch(selection) {
				case 1: db.updateAuthor(); break;
				case 2: db.deleteAuthor(); break;
				case 3: db.listMultiAuthors(); break;
				case 4: db.listTopShippers(); break;
				case 5: db.findBooksByPrice(); break;
				case 6: db.getOrderValue(); break;
				case 7: db.getPopularRegions(); break;
				case 8: db.getCustomerPaid(); break;
				case 9: db.getAveragePrices(); break;
				case 10: db.getCoauthors(); break;
				default: System.out.println("\nInvalid choice..."); break;
				}

				System.out.println("\nEnter 1 to CONTINUE, enter any other number to QUIT");
				if(scanner.nextInt() != 1) {
					break;
				}
			}
			
			System.out.println("\nExiting...Bye!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// catch (IOException e) {
		// 	e.printStackTrace();
		// }
	}

	public static Connection getConnection() throws SQLException {
		
		// Create connection to the database
		Connection connection;
		String serverName = "10.219.0.50:3306";
		String database = "w26drandall3_projectDB";
		String url = "jdbc:mysql://" + serverName + "/" + database;
		String username = "w26drandall3";
		String password = "Dstbstr2026!!"; // TODO: Remove password before submission
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
}