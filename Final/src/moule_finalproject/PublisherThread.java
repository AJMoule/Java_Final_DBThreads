package moule_finalproject;

import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;

public class PublisherThread implements Runnable {
	protected String name = null;
	protected Connection connection = null;
	protected ArrayList<Publisher> publisherList = null;
	protected Statement s = null;
	
	public PublisherThread(String id, Connection con, ArrayList<Publisher> theList) {
		name = id;
		this.connection = con;
		publisherList = theList;
	}//PublisherThread
	
	@Override
	public void run() {
		int someResult = -1;
		synchronized(connection) 
		{
			try 
			{
				//Insert into Publisher Table - Publisher_Code, Publisher_Name, City
				//"INSERT INTO javatable VALUES (1, 'Andrew the Wise and Great', 32, 10234.35)";
				String statement;
				s = connection.createStatement();
				for(Publisher p : publisherList) 
				{
					statement = "INSERT INTO PUBLISHER VALUES('" +p.getCode()+"','"+p.getName()+"','"+p.getCity()+"')";
					someResult = s.executeUpdate(statement);
				}//for								
			}catch(SQLException se) 
			{
				
				System.out.println(Thread.currentThread().getName() + " " + name + " " + se + " " + someResult);
			}//catch
		}//synchronized
	}//run
}//PublisherThread