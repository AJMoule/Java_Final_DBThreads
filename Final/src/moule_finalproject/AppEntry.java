package moule_finalproject;

public class AppEntry {

	public static void main(String[] args) {
		
		WebDatabase myWebDB = new WebDatabase(); //call all the web database tables to get them
		Thread outPutFileThread = new Thread(new OutputIntoFile("print",myWebDB));
		outPutFileThread.start();
		LocalDatabase myLocalDB = new LocalDatabase(myWebDB);//create &/or drop web db's tables and create/re-create them 
	}//main
}//AppEntry