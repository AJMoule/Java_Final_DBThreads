package moule_finalproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookThread implements Runnable {
	protected String name = null;
	protected Connection connection = null;
	protected ArrayList<Book> bookList = null;
	protected Statement s = null;
	
	public BookThread(String id, Connection con, ArrayList<Book> theList) {
		name = id;
		this.connection = con;
		bookList = theList;
	}//BookThread
	
	@Override
	public void run() {
		int someResult = -1;
		synchronized(connection) 
		{
			try 
			{
				//Insert into Book Table - Book_Code, Title, Publisher_Code, Type, Price, Is_It_Paperback
				//"INSERT INTO javatable VALUES (1, 'Andrew the Wise and Great', 32, 10234.35)";
				String statement;
				s = connection.createStatement();
				for(Book b : bookList ) 
				{
					statement = "INSERT INTO BOOK VALUES('" +b.getCode()+"','"+b.getTitle()+"','"+b.getPublisher_Code()+"','"+b.getType()+"',"+b.getPrice()+","+b.getIs_It_Paperback()+")";
					someResult = s.executeUpdate(statement);
				}//for								
			}catch(SQLException se) 
			{
				System.out.println(Thread.currentThread().getName() + " " + name + " " + se + " " + someResult);
			}//catch
		}//synchronized
	}//run
}//BookThread