import java.sql.*;

class Rooms{
	String room;
	int rate;
	float popularity;
	String latest;
	int length;
	int maxOcc;
	String bed;
	int numBeds;
	int revenue;
	InnCalandar cal;
	
	public Rooms(String room){
		this.room = room;
		this.rate = 0;
		this.popularity = 0;
		this.latest = "";
		this.length = 0;
		this.maxOcc = 0;
		this.bed = "";
		this.numBeds = 0;
		this.revenue = 0;
		
		this.cal = new InnCalandar(this.room);
	}
	
	public Rooms(String roomcode, int rate, int numBeds, String bed, int maxOcc){
		this.room = roomcode;
		this.rate = rate;
		this.numBeds = numBeds;
		this.bed = bed;
		this.maxOcc = maxOcc;
		this.popularity = 0;
		this.latest = "";
		this.length = 0;
		this.revenue = 0;
	}
	
	public int getRate(){
		return this.rate;
	}
	
	public int getNumBeds(){
		return this.numBeds;
	}
	
	public String getBed(){
		return this.bed;
	}
	
	public int getNaxOcc(){
		return this.maxOcc;
	}
	
	// Returns popularity of current room, if not available, queries SQL
	public float getPopularity(){
		if(this.popularity != 0)
			return this.popularity;
		else{ // Popularity will most likey return 0 due to CURDATE()
			String sql = "select room, (SUM(DATEDIFF(checkout, checkin)) / 180) as pop from rooms join reservations res on (rooms.RoomCode = res.room) where DATEDIFF(CURDATE(), checkin) < 180 and room = '" + this.room + "' group by room;";
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {

				try (Statement stmt = conn.createStatement();
					 ResultSet rs = stmt.executeQuery(sql)) {
					while(rs.next()){
					    this.popularity = rs.getFloat("pop");
					}
				}
				catch(SQLException e){
					System.err.println("SQLException: " + e.getMessage());
					return 0;
				}
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
				return 0;
			}
			
			return this.popularity;
		}
	}
	
	// Returns latest checkout date of current room, if not available, queries SQL
	public String getLatest(){
		if(this.latest != "")
			return this.latest;
		else{
			String sql = "select res.room, DATEDIFF(checkout, checkin) length, max.latest from reservations res join (select room, max(checkout) latest from reservations group by Room) max on res.Room = max.room where res.checkout = max.latest && res.room = '" + this.room + "';";
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {

				try (Statement stmt = conn.createStatement();
					 ResultSet rs = stmt.executeQuery(sql)) {
					while(rs.next()){
					    this.length = rs.getInt("length");
						this.latest = rs.getString("latest");
					}
				}
				catch(SQLException e){
					System.err.println("SQLException: " + e.getMessage());
					return "";
				}
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
				return "";
			}
			return this.latest;
		}
	}
	
	// Returns length of latest stay
	public int getLength(){
		if(this.length != 0)
			return this.length;
		else{
			getLatest();
			return this.length;
		}
	}
	
	// TODO add date finding functions for reservations
}
