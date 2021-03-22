package com.eviro.assessment.grad001.tumelomalatji.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemDB {

	// Not loaded eagerly, but initialed on demand.
	private volatile static SystemDB INSTANCE = null;
	private static Scanner scanner;
	public final String url = "jdbc:sqlite::memory";
	static Connection connection;
	private static final Logger logger = LogManager.getLogger(SystemDB.class);

	private SystemDB() {
		if (INSTANCE != null) {
			// We know there is an object of this class created already.
			throw new RuntimeException("There object already exist. Please use getInstance() to get the instance.");
		}
		// TODO: Database memory map code logic here.
		// 1. Do the database creation
		logger.info("Opening Connection...");
		try {
			openConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logger.info("Connection Open...");

		logger.info("Creating table...");
		createTable();

		// 1. Read the file that contains the test accounts and populate the accounts
		// table
		readAccountsFile();

	}

	public static SystemDB getInstace() {
		if (INSTANCE == null) { // Check to see if the object of this class has been created.
			synchronized (SystemDB.class) { // Make it thread-safe by creating a lock
				if (INSTANCE == null) { // Do another check again to confirm that another thread is not busy creating
										// the object
					// Create the object.
					INSTANCE = new SystemDB();
				}
			}
		}
		return INSTANCE;
	}

	private void openConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(url);
		}
	}

	public static void readAccountsFile() {
		try {
			logger.info("Reading the accounts file...");
			scanner = new Scanner(new File("accounts.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Printing out the accounts file...");
		while (scanner.hasNextLine()) {
			// Load the file into memory database
			String accountString[] = scanner.nextLine().split(",");
			logger.info("AccountString Array is : [ " + accountString[0] + ", " + accountString[1] + ", "
					+ accountString[2] + ", " + accountString[4] + "  ]");
			try {
				// You can put the logic to check for the overdraft requirement in here before
				// you populate the table.
				populateTable(Integer.parseInt(accountString[0].strip()), Integer.parseInt(accountString[1].strip()),
						new BigDecimal(accountString[2].strip()), new BigDecimal(accountString[3].strip()),
						accountString[4].strip());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		scanner.close();
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}

	private void createTable() {
		try {
			final Statement statement = connection.createStatement();
			// Drop the table first because during testing the table is always there.
			String dropTableQuery = "DROP TABLE IF EXISTS ACCOUNT_T; ";
			logger.info("Executing query : " + dropTableQuery);
			statement.execute(dropTableQuery);
			logger.info("Table dropped...");

			String createTableQuery = "CREATE TABLE IF NOT EXISTS ACCOUNT_T" + " ( ID	integer PRIMARY KEY, "
					+ " ACCOUNT_NUM integer, " + " BALANCE	real, " + " OVERDRAFT real, " + " ACCOUNT_TYPE text ); ";
			logger.info("Executing query : " + createTableQuery);
			statement.execute(createTableQuery);
			logger.info("Table created...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void populateTable(int id, int accountNum, BigDecimal balance, BigDecimal overdraft,
			String accountType) throws SQLException {
		String query1 = "INSERT INTO ACCOUNT_T (ID, ACCOUNT_NUM, BALANCE, OVERDRAFT, ACCOUNT_TYPE ) " + "VALUES "
				+ "(?, ?, ?, ?, ?)";
		logger.info("Adding the following to the database : [ " + id + ", " + accountNum + ", " + balance + ", "
				+ overdraft + ", " + accountType + " ]");
		logger.info("Executing the query : " + query1);
		PreparedStatement preparedstatement = connection.prepareStatement(query1);
		preparedstatement.setInt(1, id);
		preparedstatement.setInt(2, accountNum);
		preparedstatement.setBigDecimal(3, balance);
		preparedstatement.setBigDecimal(4, overdraft);
		preparedstatement.setString(5, accountType);

		preparedstatement.executeUpdate();

	}

	public void showAllDB() {
		try {
			String query = "SELECT * FROM ACCOUNT_T;";
			final Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			while (rs.next()) {
				for (int i = 1; i <= numColumns; i++) {
					System.out.print(rs.getString(i) + "\t");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.getMessage();
		}
	}

	public String getAccount(int accountNum) {
		String queryResults = new String();
		try {
			String query = "SELECT * FROM ACCOUNT_T WHERE ACCOUNT_NUM = " + accountNum + "; ";
			logger.info("Search query is : [ " + query + " ]");
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);

			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			while (rs.next()) {
				// Build up the queryResult return string
				for (int i = 1; i <= numColumns; i++) {
					queryResults = queryResults.concat(rs.getString(i) + ",");
				}
				logger.info("The query result String is : [ " + queryResults + " ]");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryResults;
	}

	public void updateAccountBalance(int accountNum, BigDecimal newBalance) {
		// TODO Auto-generated method stub
		String updateAccountBalanceQuery = "UPDATE ACCOUNT_T SET BALANCE = " + newBalance + " WHERE ACCOUNT_NUM = "
				+ accountNum + "; ";
		logger.info("Update query is : [ " + updateAccountBalanceQuery + " ]");
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(updateAccountBalanceQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
