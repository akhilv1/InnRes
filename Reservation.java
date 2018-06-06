import java.util.Random;
import java.sql.*;

class Reservation{ 
	public String first; 
	public String last; 
	public String begin; 
	public String end; 
	public int children; 
	public int adults;
	public String room; // User's Input
	public String roomCode; // Generated Room
	public String bed; // User's Input
	public int code;

	public Reservation(String first, String last, String room, 
		String bed, String begin, String end, int children, int adults){
			
		this.first = first;
		this.last = last;
		this.begin = begin;
		this.end = end;
		this.children = children;
		this.adults = adults;
		this.room = room;
		this.bed = bed;				
	}
		
	public Reservation(){
		this.first = "";
		this.last = "";
		this.begin = "";
		this.end = "";
		this.children = 0;
		this.adults = 0;
		this.bed = "";
		this.room = "";
		this.code = -1;
	}
		
	//Returns true on weekend for rate calculation
	private boolean checkWeekend(String date){
		//TODO write function
		return false;
	}
		
	public String getFirst(){
		return this.first;
	}
		
	public String getLast(){
		return this.last;
	}
		
	public String getBegin(){
		return this.begin;
	}
		
	public String getEnd(){
		return this.end;
	}
		
	public int getChildren(){
		return this.children;
	}

	public int getAdults(){
		return this.adults;
	}
		
	public String getRoom(){
		return this.room;
	}
	
	public String getBed(){
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
		
	public int getCost(){
		return this.code;
	}
	
	public ResultSet sql_Query(String sql){
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
							   System.getenv("HP_JDBC_USER"),
							   System.getenv("HP_JDBC_PW"))) {

			try (Statement stmt = conn.createStatement();
				 ResultSet rs = stmt.executeQuery(sql)) {
				return rs;
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
				return null;
			}
		}
		catch(SQLException e){
			System.err.println("SQLException: " + e.getMessage());
			return null;
		}
	}
}