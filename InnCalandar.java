class InnCalandar {
	
	private boolean[] jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dec;
	public int numRes = 0;
	
	public InnCalandar(){
		jan = new boolean[31];
		feb = new boolean[28];
		mar = new boolean[31];
		apr = new boolean[30];
		may = new boolean[31];
		jun = new boolean[30];
		jul = new boolean[31];
		aug = new boolean[31];
		sept = new boolean[30];
		oct = new boolean[31];
		nov = new boolean[30];
		dec = new boolean[31];
	}
	
	// Reserves date range and returns false on failure
	boolean addDateRange(String start, String end){
		if(!checkDate(start, end)){ // Requested full days
			return false;
		}
		
		boolean month[] = getMonthArr(start);
		if(getMonth(start) != getMonth(end)){
			int day = getDay(start) - 1;
			while(day < month.length){ // Books dates
				month[day] = true;
			}
			day = 0; // Reset to beginning of month
			month = getMonthArr(end); // get next month
			while(day < (getDay(end) - 1)){
				month[day] = true;
			}
			return true;
		}
		else{
			int day = getDay(start) - 1;
			month = getMonthArr(start);
			while(day < (getDay(end) - 1)){
				month[day] = true;
			}
			return true;
		}
	}
	
	// returns true if date range is vacant
	boolean checkDate(String start, String end){
		boolean month[] = getMonthArr(start);
		if(getMonth(start) != getMonth(end)){
			boolean ret = false;
			int day = getDay(start) - 1;
			while(day < month.length){ // Books dates
				ret = ret & month[day]; // Ret will be true if there are any booked days
			}
			day = 0; // Reset to beginning of month
			month = getMonthArr(end); // get next month
			while(day < (getDay(end) - 1)){
				ret = ret & month[day]; 
			}
			return !ret; // if vacant, returns true
		}
		else{
			int day = getDay(start) - 1;
			month = getMonthArr(start);
			while(day < (getDay(end) - 1)){
				month[day] = true;
			}
			return true;
		}
	}
	
	// Sets date range to False to indicate vacancy
	boolean removeDate(String start, String end){
		if(checkDate(start, end)){ // Removed empty days
			return false;
		}
		
		boolean month[] = getMonthArr(start);
		if(getMonth(start) != getMonth(end)){
			int day = getDay(start) - 1;
			while(day < month.length){ // removes dates
				month[day] = false;
			}
			day = 0; // Reset to beginning of month
			month = getMonthArr(end); // get next month
			while(day < (getDay(end) - 1)){
				month[day] = false;
			}
			return true;
		}
		else{
			int day = getDay(start) - 1;
			month = getMonthArr(start);
			while(day < (getDay(end) - 1)){
				month[day] = true;
			}
			return true;
		}
	}
	
	// Returns the correct month array for the input date in YYYY-MM-DD format
	private boolean[] getMonthArr(String date){
		int month = getMonth(date);
		switch(month){
			case 1:
				return jan;
			case 2:
				return feb;
			case 3:
				return mar;
			case 4:
				return apr;
			case 5:
				return may;
			case 6:
				return jun;
			case 7:
				return jul;
			case 8:
				return aug;
			case 9:
				return sept;
			case 10:
				return oct;
			case 11:
				return nov;
			case 12:
				return dec;
		}
		return null;
	}
	
	private boolean[] getMonthArr(int month){
		switch(month){
			case 1:
				return jan;
			case 2:
				return feb;
			case 3:
				return mar;
			case 4:
				return apr;
			case 5:
				return may;
			case 6:
				return jun;
			case 7:
				return jul;
			case 8:
				return aug;
			case 9:
				return sept;
			case 10:
				return oct;
			case 11:
				return nov;
			case 12:
				return dec;
		}
		return null;
	}
	
	// Returns the YYYY part of the YYYY-MM-DD format
	private int getYear(String date){
		String temp[] = date.split("-");
		return Integer.parseInt(temp[0]);
	}
	
	// Returns the MM part of the YYYY-MM-DD format
	private int getMonth(String date){
		String temp[] = date.split("-");
		return Integer.parseInt(temp[1]);
	}
	
	// Returns the DD part of the YYYY-MM-DD format
	private int getDay(String date){
		String temp[] = date.split("-");
		return Integer.parseInt(temp[2]);
	}
	
	// returns next vaccant date
	public String nextVaccDate(){
		return "";
	}
	
	// returns next vaccant date after input start
	public String nextVaccDate(String start){
		boolean month[] = getMonthArr(start);
		int currMonth = getMonth(start);
		int currYear = getYear(start);
		int day = getDay(start) - 1;
		
		while(true){
			while(day < month.length){
				if(month[day] == false)
					return getYear(start)
				day ++;
			}
			if(currMonth == 12){
				currYear ++;
				currMonth = 1;
			}
			month = getMonthArr(currMonth);
		}
		return "";
	}
}