package moule_finalproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WroteThread implements Runnable {
	protected String name = null;
	protected Connection connection = null;
	protected ArrayList<Wrote> wroteList = null;
	protected Statement s = null;
	
	public WroteThread(String id, Connection con, ArrayList<Wrote> theList) {
		name = id;
		this.connection = con;
		wroteList = theList;
	}//PublisherThread
	
	@Override
	public void run() {
		int someResult = -1;
		synchronized(connection) 
		{
			try 
			{
				//Insert into Wrote Table - Book_Code, Author_Num, Sequence
				//"INSERT INTO javatable VALUES (1, 'Andrew the Wise and Great', 32, 10234.35)";
				String statement;
				s = connection.createStatement();
				for(Wrote w : wroteList) 
				{
					statement = "INSERT INTO WROTE VALUES('" +w.getBook_Code()+"',"+w.getAuthor_Number()+","+w.getSequence_Number()+")";
					someResult = s.executeUpdate(statement);
				}//for								
			}catch(SQLException se) 
			{
				
				System.out.println(Thread.currentThread().getName() + " " + name + " " + se + " " + someResult);
			}//catch
		}//synchronized
	}//run
}//WroteThread