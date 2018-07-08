package moule_finalproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OutputIntoFile implements Runnable {
	private Locale locale = getLocale();
	private String Name;
	private static ArrayList<PrintOutObject> printOut = new ArrayList<PrintOutObject>();
	private static WebDatabase Web;
	
	public OutputIntoFile(String name, WebDatabase webDB) 
	{
		/* **************************************************************************************
		 * We want to display the Price of the Books in the current Locale.  					*
		 * Display the Title in proper case sensitivity.					 					*
		 * Display the correct Author First and Last name together in proper case sensitivity.	*
		 * Display the name of the Publisher in proper case sensitivity. 						*
		 * Display the price based on locale													*
		 * Output all to a text file.															*
		 * **************************************************************************************/
		setName(name);
		setWeb(webDB);
	}//OutputIntoFile
	
	@Override
	public void run() {	
		//set all the book properties of PrintOutObject
		insertBookInfo();
		insertPublisherInfo();
		insertAuthorInfo();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
		//Time to print out the file.
		File sourceFile = new File("Moule_Java_Final_Output.txt");
		try 
		{
			FileWriter fw = new FileWriter(sourceFile);
			PrintWriter pw = new PrintWriter(fw);
			int count = 1;
			for(PrintOutObject poo : printOut) 
			{
				pw.println("#"+count + " Book");				
				pw.println("Book Tile: " + toTitleCase(poo.getBook_Title()));
				pw.println("Book Author: " +toTitleCase(poo.getAuthor_FullName()));
				pw.println("Book Publisher: " +toTitleCase(poo.getPublisher_Name()));
				pw.println("Book Price: " + currencyFormatter.format(poo.getBook_Price()).toString());
				pw.println("-----------------------------------------------------------------------");
				count++;
			}//for
			//flush and close stream
			pw.close();
		}//try
		catch(IOException ioe) {System.out.println(ioe);}//catch
	}//run
	
	private static String toTitleCase(String input) 
	{
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char thisChar : input.toCharArray()) {
	        if (Character.isSpaceChar(thisChar)) 
	        {
	            nextTitleCase = true;
	        }//if 
	        else if (nextTitleCase) 
	        {
	        	thisChar = Character.toTitleCase(thisChar);
	            nextTitleCase = false;
	        }//else if
	        titleCase.append(thisChar);
	    }//for
	    return titleCase.toString();
	}//toTitleCase
	
	private void insertBookInfo() 
	{
		ArrayList<Book> bookList = OutputIntoFile.getWeb().getMyBookArrayList();
		for(Book b : bookList) 
		{
			PrintOutObject p = new PrintOutObject();
			p.setBook_Code(b.getCode());
			p.setBook_Title(b.getTitle());
			p.setBook_Price(b.getPrice());
			p.setPublisher_Code(b.getPublisher_Code());
			printOut.add(p);
		}//for
	}//insertBookInfo
	
	private void insertPublisherInfo() 
	{
		ArrayList<Publisher> pubList = OutputIntoFile.getWeb().getMyPublisherArrayList();
		for(PrintOutObject p : printOut) 
		{
			String pubCode = p.getPublisher_Code();
			for(Publisher pub : pubList) 
			{
				if(pub.getCode().equals(pubCode))
				{
					p.setPublisher_Name(pub.getName());
				}//if
			}//for
		}//for
	}//insertPublisherInfo
	
	private void insertAuthorInfo() 
	{
		ArrayList<Author> authorList = OutputIntoFile.getWeb().getMyAuthorArrayList();
		ArrayList<Wrote> wroteList = OutputIntoFile.getWeb().getMyWroteArrayList();
		for(PrintOutObject p : printOut) 
		{
			String bookCode = p.getBook_Code();
			for(Wrote w : wroteList) 
			{
				if(w.getBook_Code().equals(bookCode)) 
				{
					p.setAuthor_Num(w.getAuthor_Number());
				}//if
			}//for
			String authorNumber = p.getAuthor_Num();
			for(Author a : authorList) 
			{
				if(a.getAuthor_Number().equals(authorNumber)) 
				{
					p.setAuthor_FullName(a.getFirst_Name() + " " + a.getLast_Name());
				}//if
			}//for
		}//for
	}//insertAuthorInfo
	
	private Locale getLocale() 
	{
	    if (this.locale == null) {
	        this.locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
	    }//if
	    return this.locale;
	}//Locale

	private static WebDatabase getWeb() {
		return Web;
	}//WebDatabase

	private static void setWeb(WebDatabase web) {
		OutputIntoFile.Web = web;
	}//setWeb

	private void setName(String name) 
	{
		Name = name;
	}//setName
}//OutputIntoFile