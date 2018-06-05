import java.util.*;

class Reservation{
	public Student student; 
		public String first; 
		public String last; 
		public String begin; 
		public String end; 
		public int children; 
		public int adults;
		
		public String room;
		public String bed;
		public int code;


		public Reservation(String first, String last, String room, 
			String bed, String begin, String end, int children, int adults){
			
			this.first = first;
			this.last = last;
			this.begin = begin;
			this.end = end;
			this.children = children;
			this.adults = adults;
			
			if(this.room = "Any") // TODO assign Empty rooms based on availability
				this.room = Random(0);
			else
				this.room = room;
			if(this.bed = "Any") // TODO assign Bed
				this.bed = Random(0);
			else
				this.bed = bed;			
		}
		
		//Returns true on weekend for rate calculation
		private boolean checkWeekend(String date){
			//TODO write function
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
		
		public String getRoomCode(){
			return this.room;
		}

		public int getCode(){
			if(this.code != null)
				return this.code;
			else{ //TODO Generate random reservation Code
				this.code = Random(0);
				return this.code;
			}
		}
		
		public int getCost(){
			return this.code;
		}
}