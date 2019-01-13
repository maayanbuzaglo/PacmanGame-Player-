package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySql {
	public static void main(String[] args) {
		String jdbcUrl = "jdbc:mysql://ariel-oop.xyz:3306/oop"; //?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
		String jdbcUser = "student";
		String jdbcPassword = "student";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
			
			
			Statement statement = connection.createStatement();
			
			//select data
			String allCustomersQuery = "SELECT AVG(Point) FROM logs where FirstID = 314882077 and SomeDouble = 2128259830;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println(resultSet.getDouble(1));
			}
			
			allCustomersQuery = "SELECT AVG(Point) FROM logs where SomeDouble = 2128259830";
			resultSet.next();
			
			resultSet.close();		
			statement.close();		
			connection.close();		
		}
		
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
