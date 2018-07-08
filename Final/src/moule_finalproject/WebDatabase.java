package moule_finalproject;

import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebDatabase {
	//Web Database URL, Driver, User name and Password (Microsoft)
	private static final String WEB_DB_URL = "jdbc:sqlserver://bitweb3.nwtc.edu;databaseName=asp11";
	private static final String WEB_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String WEB_USER_NAME = "asp11";
	private static final String WEB_PASSWORD = "jcmE48KV";
	
	//Want to create a Connection Object to actually initiate the connection to the database
	protected static Connection c = CreateConnection();	
	//ResultSet gets back the results of our Statement/Query
	protected static ResultSet rs = null;
	//This is the query statement we are going to generate to get back some information
	//from the database
	protected static Statement st = null;
	
	//Create ArrayList to hold Book, Author, Wrote and Publisher class.
	private ArrayList<Book> myBookArrayList = new ArrayList<Book>();
	private ArrayList<Author> myAuthorArrayList = new ArrayList<Author>();
	private ArrayList<Wrote> myWroteArrayList = new ArrayList<Wrote>();
	private ArrayList<Publisher> myPublisherArrayList = new ArrayList<Publisher>();
	
	public WebDatabase() 
	{
		RetrieveBookTableData();
		RetrieveAuthorTableData();
		RetrievePublisherTableData();
		RetrieveWroteTableData();
		CloseDownEverythingInDatabase();
	}//WebDatabase
	
	private static Connection CreateConnection() 
	{
		Connection con; //holds connection to database
		try {
			Class.forName(WEB_JDBC_DRIVER);
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(WEB_DB_URL,WEB_USER_NAME,WEB_PASSWORD);
		}catch(SQLException se) {
			System.out.println("You got this SQL error: " +se);
			con = null;
		}catch(Exception e){
			System.out.println("You got this error: " +e);
			con = null;
		}
		return con;
	}//CreateConnection
	
	private static ResultSet GetResultSet(String sql)
	{
		if(c !=null)
		{
			System.out.println("Into the database to execute our query.");
			try {
				st = c.createStatement(); //Creates the ability for statement to be created
				rs = st.executeQuery(sql);//sql string holds the SQL we want a result set returned from
			}catch(SQLException se) {
				System.out.println("You got this SQL error: " +se);
				rs = null;
				st = null;
			}catch(Exception e){
				System.out.println("You got this error: " +e);
				rs = null;
				st = null;
			}
		}//if
		if(rs == null)
		{rs = null;}//if created to stop java from yelling about rs never being used.
		return rs;
	}//GetResults
	
	private void RetrieveBookTableData() 
	{
		//Holds the Book Data
		
		String sqlStatementToPass = "Select * From Book";
		rs = GetResultSet(sqlStatementToPass);
		if(rs != null) 
		{			
			//put in a try catch
			try {
				while(rs.next())//while there is something in rs
				{
					//Create temp Book object so we can set it and then set the ArrayList
					Book tempBook = new Book();
					tempBook.setCode(rs.getString("BOOK_CODE"));
					tempBook.setTitle(rs.getString("TITLE"));
					tempBook.setPublisher_Code(rs.getString("PUBLISHER_CODE"));
					tempBook.setType(rs.getString("TYPE"));
					tempBook.setPrice(rs.getDouble("PRICE"));
					tempBook.setIs_It_Paperback(rs.getString("PAPERBACK"));
					//add the tempBook to tempList
					myBookArrayList.add(tempBook);
				}//while
			}catch(SQLException se) {}//catch
		}//if
	}//RetrieveBookTableData
	
	private void RetrieveAuthorTableData() 
	{
		//Holds the Author Data
		
		String sqlStatementToPass = "Select * From Author";
		rs = GetResultSet(sqlStatementToPass);
		if(rs != null) 
		{			
			//put in a try catch
			try {
				while(rs.next())//while there is something in rs
				{
					Author tempAuthor = new Author();
					tempAuthor.setAuthor_Number(rs.getString("AUTHOR_NUM"));
					tempAuthor.setLast_Name(rs.getString("AUTHOR_LAST"));
					tempAuthor.setFirst_Name(rs.getString("AUTHOR_FIRST"));
					myAuthorArrayList.add(tempAuthor);
				}//while
			}catch(SQLException se){}//catch
		}
	}//RetrieveAuthorTableData
	
	private void RetrievePublisherTableData() 
	{
		//Holds the Publisher Data
		
		String sqlStatementToPass = "Select * From Publisher";
		rs = GetResultSet(sqlStatementToPass);
		if(rs != null) 
		{			
			//put in a try catch
			try {
				while(rs.next())//while there is something in rs
				{
					Publisher tempPublisher = new Publisher();
					tempPublisher.setCode(rs.getString("PUBLISHER_CODE"));
					tempPublisher.setName(rs.getString("PUBLISHER_NAME"));
					tempPublisher.setCity(rs.getString("CITY"));
					myPublisherArrayList.add(tempPublisher);
				}//while
			}catch(SQLException se) {}//catch
		}
	}//RetrievePublisherTableData
	
	private void RetrieveWroteTableData() 
	{
		//This is a bridge table between Book and Author
		String sqlStatementToPass = "Select * From Wrote";
		rs = GetResultSet(sqlStatementToPass);
		if(rs != null) 
		{			
			//put in a try catch
			try {
				while(rs.next())//while there is something in rs
				{
					Wrote tempWrote = new Wrote();
					tempWrote.setBook_Code(rs.getString("BOOK_CODE"));
					tempWrote.setAuthor_Number(rs.getString("AUTHOR_NUM"));
					tempWrote.setSequence_Number(rs.getString("SEQUENCE"));
					myWroteArrayList.add(tempWrote);
				}//while
			}catch(SQLException se) {}//catch
		}
	}//RetrieveWroteTableData
	
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

	public ArrayList<Book> getMyBookArrayList() {
		return myBookArrayList;
	}//getMyBookArrayList

	public ArrayList<Author> getMyAuthorArrayList() {
		return myAuthorArrayList;
	}//getMyAuthorArrayList
	
	public ArrayList<Publisher> getMyPublisherArrayList() {
		return myPublisherArrayList;
	}//getMyPublisherArrayList

	public ArrayList<Wrote> getMyWroteArrayList() {
		return myWroteArrayList;
	}//getMyWroteArrayList
}//WebDatabase