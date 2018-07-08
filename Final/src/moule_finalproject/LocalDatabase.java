package moule_finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LocalDatabase {
	//Local Database URL, Driver, User name and Password (MySQL)
	private static final String LOCAL_DB_URL = "jdbc:mysql://localhost:3306/";
	private static final String LOCAL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String LOCAL_USER_NAME = "root";
	private static final String LCOAL_PASSWORD = "password";
	private static final String JAVA_DB_NAME = "moulejavafinal";
	
	//Want to create a Connection Object to actually initiate the connection to the database
	protected static Connection c = CreateConnection();	
	//ResultSet gets back the results of our Statement/Query
	protected static ResultSet rs = null;
	//This is the query statement we are going to generate to get back some information from the database
	protected static Statement st = null;
	//Know if all db stuff was done successfully
	protected boolean wasItDoneSuccessfully = false;
	private int countTimeChecking = 0;
	
	public LocalDatabase(WebDatabase webDB)
	{
		boolean doesDBExist = DoesTheDatabaseAlreadyExists();
		if(doesDBExist) 
		{
			//the database already exists check to see if all the tables are there
			//if they are present drop all the tables and then rebuild them from the web db info
			//otherwise create the tables
			SwitchConnectionStrings();
			DropAllTables();
			CreatePublisherTable(webDB.getMyPublisherArrayList()); 
			CreateBookTable(webDB.getMyBookArrayList()); //Has Publisher_Code FK need that Publisher table created first
			CreateAuthorTable(webDB.getMyAuthorArrayList());			
			CreateWroteTable(webDB.getMyWroteArrayList()); //Last Table to build Book_Code and Author_NUM are FK
			wasItDoneSuccessfully = DoesTheDatabaseAlreadyExists();
			while(!wasItDoneSuccessfully) 
			{
				wasItDoneSuccessfully = DoesTheDatabaseAlreadyExists();
			}//while
			CloseDownEverythingInDatabase();
		}//if
		else {
			//the database does not exist need to create it and all the tables
			CreateDatabase();
			SwitchConnectionStrings();
			CreatePublisherTable(webDB.getMyPublisherArrayList()); 
			CreateBookTable(webDB.getMyBookArrayList()); //Has Publisher_Code FK need that Publisher table created first
			CreateAuthorTable(webDB.getMyAuthorArrayList());			
			CreateWroteTable(webDB.getMyWroteArrayList()); //Last Table to build Book_Code and Author_NUM are FK
			wasItDoneSuccessfully = DoesTheDatabaseAlreadyExists();
			while(!wasItDoneSuccessfully) 
			{
				wasItDoneSuccessfully = DoesTheDatabaseAlreadyExists();
			}//while
			CloseDownEverythingInDatabase();
		}//else
	}//LocalDatabase
		
	private static Connection CreateConnection() 
	{
		Connection con; //holds connection to database
		try {
			Class.forName(LOCAL_JDBC_DRIVER);
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(LOCAL_DB_URL,LOCAL_USER_NAME,LCOAL_PASSWORD);
		}catch(SQLException se) {
			System.out.println("You got this SQL error: " +se);
			con = null;
		}catch(Exception e){
			System.out.println("You got this error: " +e);
			con = null;
		}
		return con;
	}//CreateConnection
	
	private static Connection CreateConnection(String newDBURL) 
	{
		Connection con; //holds connection to database
		try {
			Class.forName(LOCAL_JDBC_DRIVER);
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(newDBURL,LOCAL_USER_NAME,LCOAL_PASSWORD);
		}catch(SQLException se) {
			System.out.println("You got this SQL error: " +se);
			con = null;
		}catch(Exception e){
			System.out.println("You got this error: " +e);
			con = null;
		}
		return con;
	}//CreateConnection
		
	private static void CreateDatabase() 
	{
		//Creates the database only if it does not exist should this occur.
		System.out.println("Creating database...");
		String statemnt;
		statemnt = "CREATE DATABASE " + JAVA_DB_NAME;
		ExecuteStatements(statemnt);
	}//CreateDatabase
	
	private static void DropAllTables() 
	{
		String[] tableName = {"WROTE", "BOOK", "AUTHOR", "PUBLISHER"};
		for(int i = 0; i < tableName.length; i++) 
		{
			String statement = "DROP TABLE " +tableName[i];
			System.out.println(tableName[i]);
			ExecuteStatements(statement);
		}//for
	}//DropAllTables
	
	private static void CreateBookTable(ArrayList<Book> websArrayList) 
	{
		//Book_Code is PK, Publisher_Code is a FK to Publisher Table
		System.out.println("Creating Book Table....");
		String statemnt;
		statemnt = "CREATE TABLE BOOK "+
					"(Book_Code VARCHAR(10) PRIMARY KEY, " +
					"Title VARCHAR(1000)," +
					"Publisher_Code VARCHAR(10)," +
					"Type VARCHAR(10),"+
					"Price DOUBLE,"+
					"Is_It_Paperback BOOLEAN,"+
					"CONSTRAINT FK_PUB_BOOKTABLE FOREIGN KEY(Publisher_Code) REFERENCES Publisher(Publisher_Code))";
		ExecuteStatements(statemnt);
		Thread bookThread = new Thread(new BookThread("bookThread",c,websArrayList));
		bookThread.start();
	}//CreateBookTable
	
	private static void CreateAuthorTable(ArrayList<Author> websArrayList) 
	{
		//Creating the Author Table
		System.out.println("Creating Author Table....");
		String statemnt;
		statemnt = "CREATE TABLE AUTHOR "+
					"(Author_Num INT PRIMARY KEY, " +
					"Author_Last VARCHAR(100)," +
					"Author_First VARCHAR(100))";
		ExecuteStatements(statemnt);
		Thread authorThread = new Thread(new AuthorThread("authorThread",c,websArrayList));
		authorThread.start();
	}//CreateAuthorTable
	
	private static void CreatePublisherTable(ArrayList<Publisher> websArrayList) 
	{
		System.out.println("Creating Publisher Table....");
		String statemnt;
		statemnt = "CREATE TABLE Publisher "+
					"(Publisher_Code char(2) PRIMARY KEY, " +
					"Publisher_Name VARCHAR(50)," +
					"City VARCHAR(50))";
		ExecuteStatements(statemnt);
		//Thread thread1 = new Thread(new MyClass("thread1: "));
		Thread pubThread = new Thread(new PublisherThread("pubThread",c,websArrayList));
		pubThread.start();
	}//CreatePublisherTable
	
	private static void CreateWroteTable(ArrayList<Wrote> websArrayList) 
	{
		//Book_Code is FK to Book Table
		//Atuhor_Num is FK to Author Table
		System.out.println("Creating Wrote Table....");
		String statemnt;
		statemnt = "CREATE TABLE WROTE "+
					"(Book_Code VARCHAR(10), " +
					"Author_Num INT," +
					"Sequence INT," +
					"CONSTRAINT FK_BOOKCODE_WROTETABLE FOREIGN KEY(Book_Code) REFERENCES BOOK(Book_Code)," +
					"CONSTRAINT FK_AUTHORNUM_WROTETABLE FOREIGN KEY(Author_Num) REFERENCES AUTHOR(Author_Num))";		
		ExecuteStatements(statemnt);
		Thread wroteThread = new Thread(new WroteThread("wroteThread",c,websArrayList));
		wroteThread.start();
	}//CreateWroteTable
	
	public static void ExecuteStatements(String sttmnt) 
	{
		int result = -1;
		if(c != null) {
			try {
				st = c.createStatement();
				result = st.executeUpdate(sttmnt);
			}catch(SQLException se) {
				System.out.println("Could not execute update query to create Table.");
				System.out.println(se);
				System.out.println(result);
			}//catch
		}//if
	}//ExecuteStatements
	
	private boolean DoesTheDatabaseAlreadyExists() 
	{
		boolean theAnswer = false;
		if(countTimeChecking > 0) 
		{
			//This checks for the db existing and all tables existing before you can close the Connection
			try {
				//This goes out and gets all the database at the local host
				rs = c.getMetaData().getCatalogs();
				if(rs != null) 
				{
					while(rs.next()) 
					{
						String databaseName = rs.getString(1);
						if(databaseName.equals(JAVA_DB_NAME)) 
						{
							String[] tableName = {"WROTE", "BOOK", "AUTHOR", "PUBLISHER"};
							int tempCount = 0;
				            ResultSet tempRS = c.getMetaData().getTables(null, null, "%", null);
				            while(tempRS.next()) 
				            {
				            	for(int i = 0; i < tableName.length; i++) 
								{
				            		String tempTName = tempRS.getString("TABLE_NAME").toUpperCase();
									if(tableName[i].equals(tempTName))
									{
										tempCount++;
									}//if
								}//for				            	
				            }//while
				            if(tempCount == 4) 
				            {
				            	theAnswer = true;
								countTimeChecking++;
				            }//if				  
						}//if
					}//while
				}//if
			}catch(SQLException se) {}//catch
		}//if
		else 
		{
			try {
				//This goes out and gets all the database at the local host
				rs = c.getMetaData().getCatalogs();
				if(rs != null) 
				{
					while(rs.next()) 
					{
						String databaseName = rs.getString(1);
						if(databaseName.equals(JAVA_DB_NAME)) 
						{
							theAnswer = true;
							countTimeChecking++;
						}//if
					}//while
				}//if
			}catch(SQLException se) {}//catch
		}//else
		return theAnswer;
	}//DoesTheDatabaseAlreadyExists
	
	private void SwitchConnectionStrings() 
	{
		String newURL = LOCAL_DB_URL + JAVA_DB_NAME;
		CloseDownEverythingInDatabase(); //Need to close everything and open under the new Connection URL
		c = CreateConnection(newURL);
	}//SwitchConnectionStrings
	
	private static void CloseDownEverythingInDatabase() 
	{
		try 
		{
			if(st != null) {st.close();}//if
			if(rs != null) {rs.close();}//if
			if(c != null) {c.close();}//if
			System.out.println("Hey I'm closing the connection to the database....bye!");
		}catch(SQLException se) {System.out.println("Oh no you got this error: " + se);}//catch	
		catch(Exception except) {System.out.println("Oh no you got this error: " + except);}//catch		
	}//CloseConnection
}//LocalDatabase