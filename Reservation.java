import java.sql.*;

class Reservation{ 
	public String first; 
	public String last; 
	public String checkIn; 
	public String checkOut; 
	public int children; 
	public int adults;
	public String room;
	public String bed;
	public int code;
	public float rate;
	public float cost;

	public Reservation(){
		this.first = "";
		this.last = "";
		this.checkIn = "";
		this.checkOut = "";
		this.children = 0;
		this.adults = 0;
		this.bed = "";
		this.room = "";
		this.code = -1;
		this.rate = -1;
		this.cost = -1;
	}
		
	public String getFirst(){
		return this.first;
	}
		
	public String getLast(){
		return this.last;
	}
		
	public String getCheckIn(){
		return this.checkIn;
	}
		
	public String getCheckOut(){
		return this.checkOut;
	}
		
	public int getChildren(){
		return this.children;
	}

	public int getAdults(){
		return this.adults;
	}
	
	public String getRoom(){
		if(this.room == "any" || this.room == "Any" || this.room == "ANY"){
			// TODO Implement 'any' case based on availability and maxocc
			return "AOB";
		}
		return this.room;
	}
	
	public String getBed(){
		if(this.bed == "any" || this.bed == "Any" || this.bed == "ANY"){
			// TODO Implement 'any' case based on room
			return "Queen";
		}
		return this.bed;
	}

	public int getCode(){
		if(this.code != -1)
			return this.code;
		else{ // If unassigned, query the largest reservation number and increment it
			int max = 99999;
			String sql = "select max(code) as max from reservations;";
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {

				try (Statement stmt = conn.createStatement();
					 ResultSet rs = stmt.executeQuery(sql)) {
					while(rs.next()){
					    max = rs.getInt("max");
					    System.out.format("%d %n", max);
					}
				}
				catch(SQLException e){
					System.err.println("SQLException: " + e.getMessage());
					return -1;
				}
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
				return -1;
			}
			
			this.code = ++ max;
			return this.code;
		}
	}
		
	public float getCost(){
		this.cost = 0;
		for(int i = 0; i < this.getLength(this.checkIn, this.checkOut); i ++){
			if(checkWeekend(this.checkIn, i))
				this.cost += 1.1 * this.getRate();
			else
				this.cost += this.getRate();
		}
		this.cost = (float)(this.getRate() * 1.18);
		return this.cost;
	}
	
	//Returns true on weekcheckOut for rate calculation
	private boolean checkWeekend(String date, int offset){
		//TODO write function
		return false;
	}
	
	//Returns difference of two dates
	private int getLength(String checkIn, String checkOut){
		//TODO write function
		return 0;
	}
	
	public float getRate(){
		if(this.rate != -1)
			return this.code;
		else{ // If unassigned, query the largest reservation number and increment it
			String sql = "select baseprice from rooms where roomcode = '" + this.getRoom() + "';";
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {

				try (Statement stmt = conn.createStatement();
					 ResultSet rs = stmt.executeQuery(sql)) {
					while(rs.next()){
					    this.rate = rs.getInt("baseprice");
					}
				}
				catch(SQLException e){
					System.err.println("SQLException: " + e.getMessage());
					return -1;
				}
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
				return -1;
			}
			
			return this.rate;
		}
	}
}