class InnCalandar {
	
	private boolean[] jan, feb, mar, apr, may, jun, jul, aug, sept, oct, nov, dec;
	
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
		checkDate(start, end);
		boolean month[] = getMonthArr(start);
		if(getMonth(start) != getMonth(end)){
			int days = month.length - getDay(start);
		}
		return true;
	}
	
	// returns true if date range is vacant
	boolean checkDate(String start, String end){
		return true;
	}
	
	// Sets date range to False to indicate vacancy
	boolean removeDate(String start, String end){
		return true;
	}
	
	// Returns the correct month array for the input date in YYYY-MM-DD format
	private boolean[] getMonthArr(String date){
		return null;
	}
	
	private boolean[] getMonthArr(int month){
		return null;
	}
	
	// Returns the MM part of the YYYY-MM-DD format
	private int getMonth(String date){
		return 0;
	}
	
	// Returns the DD part of the YYYY-MM-DD format
	private int getDay(String date){
		return 0;
	}
	
	// returns next vaccant date
	public String nextVaccDate(){
		return "";
	}
	
	// returns next vaccant date after input start
	public String nextVaccDate(String start){
		return "";
	}
}