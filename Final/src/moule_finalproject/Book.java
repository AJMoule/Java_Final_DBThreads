package moule_finalproject;

public class Book {
	private String Code;
	private String Title;
	private String Publisher_Code;
	private String Type;
	private double Price;
	private boolean Is_It_Paperback;
	
	public String getCode() {
		return Code;
	}//getCode
	public void setCode(String code) {
		Code = code;
	}//setCode
	public String getTitle() {
		return Title;
	}//getTitle
	public void setTitle(String title) {
		Title = title;
	}//setTitle
	public String getPublisher_Code() {
		return Publisher_Code;
	}//getPublisher_Code
	public void setPublisher_Code(String publisher_Code) {
		Publisher_Code = publisher_Code;
	}//setPublisher_Code
	public String getType() {
		return Type;
	}//getType
	public void setType(String type) {
		Type = type;
	}//setType
	public double getPrice() {
		return Price;
	}//getPrice
	public void setPrice(double price) {
		Price = price;
	}//setPrice
	public boolean getIs_It_Paperback() {
		return Is_It_Paperback;
	}//getIs_It_Paperback
	public void setIs_It_Paperback(String is_It_Paperback) {
		//this will come in as a String of 0 or 1 conversion can be done in here
		if(is_It_Paperback.equals("0")) 
		{
			Is_It_Paperback = false;
		}//if
		else
		{
			Is_It_Paperback = true;
		}//else if
	}//setIs_It_Paperback
}//Book