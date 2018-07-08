package moule_finalproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AuthorThread implements Runnable {
	protected String name = null;
	protected Connection connection = null;
	protected ArrayList<Author> authorList = null;
	protected Statement s = null;
	
	public AuthorThread(String id, Connection con, ArrayList<Author> theList) {
		name = id;
		this.connection = con;
		authorList = theList;
	}//AuthorThread
	
	@Override
	public void run() {
		int someResult = -1;
		synchronized(connection) 
		{
			try 
			{
				//Insert into Author Table - Author_Num, Author_Last, Author_First
				//"INSERT INTO javatable VALUES (1, 'Andrew the Wise and Great', 32, 10234.35)";
				String statement;
				s = connection.createStatement();
				for(Author a : authorList) 
				{
					statement = "INSERT INTO AUTHOR VALUES("+a.getAuthor_Number()+",'"+a.getLast_Name().replaceAll("\'", "")+"','"+a.getFirst_Name()+"')";
					someResult = s.executeUpdate(statement);
				}//for								
			}catch(SQLException se) 
			{
				
				System.out.println(Thread.currentThread().getName() + " " + name + " " + se + " " + someResult);
			}//catch
		}//synchronized
	}//run
}//AuthorThread