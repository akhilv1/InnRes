import java.util.*;

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
		this.first = null;
		this.last = null;
		this.begin = null;
		this.end = null;
		this.children = -1;
		this.adults = -1;
		this.bed = null;
		this.room = null;
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
		else{ //TODO Generate random reservation Code
//			this.code = Random(0);
			return this.code;
		}
	}
		
	public int getCost(){
		return this.code;
	}
}