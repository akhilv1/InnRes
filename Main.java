import java.util.Scanner;
import java.util.ArrayList;
import java.sql.*;

class Inn_Reservations{
	public static void main(String[] args){
		Scanner reader = new Scanner (System.in);
		int sel = 0;
		while(sel != -1){
			System.out.println("\nInn Reservation Manager: ");
			System.out.println("1: Show Rooms and Rates");
			System.out.println("2: Make a Reservation");
			System.out.println("3: Change a Reservation");
			System.out.println("4: Cancel a Reservation");
			System.out.println("5: About a Reservation");
			System.out.println("6: Inn Revenue");
			System.out.println("7: Exit\n");
			System.out.print("Choose an Option: ");

			sel = reader.nextInt();
			ArrayList<String> inputs;
			String sql = "";
			Reservation res;
			switch (sel){
				case 1:
					System.out.println("Option 1: Show Rooms and Rates");
					req1();
				    break;
				case 2: // TODO handle "Any" case by generating bed and room
					System.out.println("Option 2: Make a Reservation");
					res = req2();
					req2_confirm(res);
					break;
				case 3:
					System.out.println("Option 3: Change a Reservation");
					res = req3();
					sql = contruct_req3_sql_statement(res);
					execSql(sql);
				    break;
				case 4:
					System.out.println("Option 4: Cancel a Reservation");
					sql = req4();
					req4_confirm(sql);
					break;
				case 5:
					System.out.println("Option 5: About a Reservation\n");
					inputs = req5();
					sql = contruct_req5_sql_statement(inputs);
					fetch_res(sql);
				   	break;
				case 6:
					System.out.println("Option 6: Inn Revenue");
					construct_req6_year_stats();
					break;
				case 7:
					System.out.println("Thank you for using the Inn Reservation System");
					sel = -1;
					break;
			}
		}
	}

	// Checks the date to confirm YYYY-MM-DD Format	
	public static boolean checkDate(String date){
		if(date.indexOf('-') == 4 && date.lastIndexOf('-') == 7){
			String temp[] = date.split("-");
			int year = Integer.parseInt(temp[0]);
			int month = Integer.parseInt(temp[1]);
			int day = Integer.parseInt(temp[2]);
			if(year < 0 || year >  3000 || month < 0 || month > 12 || day < 0 || day > 31){
				return false;
			}
			return true;
		}
		return false;
	}
		
	public static boolean execSql(String sql){
		System.out.println("ExecSql " + sql);
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {

		    try (Statement stmt = conn.createStatement()) {
				return stmt.execute(sql);
		    }
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
				return false;
			}
		}
		catch(SQLException e){
			System.err.println("SQLException: " + e.getMessage());
			return false;
		}
	}
	
	/* Requirement 1
	 * Rooms and Rates
	 */

	public static void req1(){
		String sql = "select res.room room, length, beds, bedtype, maxocc, baseprice, pop, latest, length from (select res.room, DATEDIFF(checkout, checkin) length, max.latest from reservations res join (select room, max(checkout) latest from reservations group by Room) max on res.Room = max.room where res.checkout = max.latest) latest join (select room, beds, bedType, maxOcc, basePrice, (SUM(DATEDIFF(checkout, checkin)) / 180) as pop from rooms join reservations res on (rooms.RoomCode = res.room) where DATEDIFF('2010-10-23', checkin) < 180 group by room order by pop desc) res on res.room = latest.room;";
			// TODO Freezes for whatever reason
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {

				try (Statement stmt = conn.createStatement();
					 ResultSet rs = stmt.executeQuery(sql)) {
					while(rs.next()){ // Only returns the last reservation in  a list
						Rooms room = new Rooms();
						System.out.println("Room\tRate\tPopularity\tLatest\tLength\t");
					    room.room = rs.getString("room");
						room.rate = rs.getInt("baseprice");
						room.popularity = rs.getFloat("pop");
						room.latest = rs.getString("latest");
						room.length = rs.getInt("length");	
						room.maxOcc = rs.getInt("maxocc");
						room.bed = rs.getString("bedtype");
						room.numBeds = rs.getInt("beds");
						
						System.out.printf("%s\t%d\t%.2f\t%s\t%d%n", room.room, room.rate, room.popularity, room.latest, room.length);
					}
				}
				catch(SQLException e){
					System.err.println("SQLException: " + e.getMessage());
				}
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
			}
	}
	
	/* Requirement 2
	 * Reservation Creation
	 */
	public static Reservation req2(){
		Reservation res = new Reservation();

		Scanner reader = new Scanner (System.in);
		String userInput; 

		System.out.println("Make a Reservation: ");

		while(true){
			int input = Ask_For_Option2(res);

			switch(input){
				case 1: //First Name, inputs(0)
					System.out.print("Enter a First Name: ");
					res.first = reader.nextLine();
					break;
				case 2: //Last Name, inputs(1)
					System.out.print("Enter a Last Name: ");
					res.last = reader.nextLine();
					break;
				case 3: //Desired Room
					System.out.print("Enter a Desired Room: ");
					res.room = reader.nextLine();
					break;
				case 4: //Desired Bed	
					System.out.print("Enter a Desired Bed: ");
					res.bed = reader.nextLine();
					break;
				case 5: //Date Range, Start Date = inputs(2), End Date = inputs(3)
					System.out.print("Enter a Start Date (YYYY-MM-DD): ");
					res.checkIn = reader.nextLine();

					System.out.print("Enter a End Date (YYYY-MM-DD): ");
					res.checkOut = reader.nextLine();
					
					if(!checkDate(res.checkIn) || !checkDate(res.checkOut)){
						res.checkIn = "";
						res.checkOut = "";
						System.out.println("Please enter the date in YYYY-MM-DD Format");
					}
					break;
				case 6: //Numebr of Children,inputs(4)
					System.out.print("Enter the number of Children: ");
					try {
						res.children = reader.nextInt();
					} catch (Exception InputMismatchException) {
						System.out.println("Invalid Type");
					}
					break;
				case 7: //Numebr of Adults,inputs(5)
				System.out.print("Enter the number of Adults: ");
					try {
						res.adults = reader.nextInt();
					} catch (Exception InputMismatchException) {
						System.out.println("Invalid Type");
					}
					break;
				case 8:
					return res;
				case 9:
					return null;
					
			}
		}
	}
	
	public static int Ask_For_Option2(Reservation res){
		int input = 0;
		Scanner reader = new Scanner (System.in);
		
		System.out.println("Choose a feild to edit:");
		System.out.printf("\t1: First Name: %s\n", res.getFirst());
		System.out.printf("\t2: Last Name: %s\n", res.getLast());
		System.out.printf("\t3: Desired Room: %s\n", res.room);
		System.out.printf("\t4: Desired Bed: %s\n", res.bed);

		if(res.getCheckIn() == ""){
			System.out.printf("\t5: Range of Dates: \n"); 
		}
		else{
			System.out.printf("\t5: Range of Dates: %s - %s\n", res.getCheckIn(), res.getCheckOut());
		}
		
		System.out.printf("\t6: Number of Children: %d\n", res.getChildren());
		System.out.printf("\t7: Number of Adults: %d\n", res.getAdults());
		System.out.println("\t8: Confirm Reservation");
		System.out.println("\t9: Cancel");
		
		System.out.print("\nChoose which option to enter: ");
		try {
			input = reader.nextInt();
		} catch (Exception InputMismatchException) {
			System.out.println("Invalid Type");
		} 

		return input; 
	}
	
	public static boolean req2_confirm(Reservation res){
		Scanner reader = new Scanner (System.in);
		print_res(res);
		System.out.printf("Total Cost: %.2f %nConfirm Reservation (Y/N):", res.getCost());
		
		//res.room
		// TODO assign Room

		
		// Confirm Reservation
		while(true){
			String input = reader.next();
			if(input.charAt(0) == 'Y' || input.charAt(0) == 'y'){
				execSql(contruct_req2_sql_statement(res));
				return true;
			}
			else if(input.charAt(0) == 'N' || input.charAt(0) == 'n'){
				return false;
			}
			else{
				System.out.println("Invalid Input%nConfirm Reservation (Y/N):");
			}
		}
	}

	// TODO handle any case and display options if no matches found (use next available date from 1
	// TODO check date availability
	public static String contruct_req2_sql_statement(Reservation res){
		String statement = "INSERT INTO reservations (Code, Room, CheckIn, CheckOut, Rate, LastName, FirstName, Adults, Kids) VALUES (";
		
		statement += res.getCode() + ", ";
		statement += "'" + res.getRoom() + "', ";
		statement += "'" + res.getCheckIn() + "', ";
		statement += "'" + res.getCheckOut() + "', ";
		statement += res.getRate() + ", ";
		statement += "'" + res.getLast() + "', ";
		statement += "'" + res.getFirst() + "', ";
		statement += res.getAdults() + ", ";
		statement += res.getChildren() + ");";
		
		return statement;
	}
	
	/* Requirement 3
	 * Reservation Change
	 */
	
	public static Reservation req3(){
		Reservation res;

		Scanner reader = new Scanner (System.in);
		String userInput;

		System.out.print("Please enter your reservation code to edit:");
		int code = reader.nextInt();
		res = fetch_res(code);

		while(true){
			int input = Ask_For_Option3(res);

			switch(input){
				case 1: //First Name, inputs(0)
					System.out.print("Enter a First Name: ");
					res.first = reader.nextLine();
					break;
				case 2: //Last Name, inputs(1)
					System.out.print("Enter a Last Name: ");
					res.last = reader.nextLine();
					break;
				case 3: //Date Range, Start Date = inputs(2), End Date = inputs(3)
					// TODO (OPTIONAL)type checking
					System.out.print("Enter a Start Date (YYYY-MM-DD): ");
					res.checkIn = reader.nextLine();

					System.out.print("Enter a End Date (YYYY-MM-DD): ");
					res.checkOut = reader.nextLine();
					
					if(!checkDate(res.checkIn) || !checkDate(res.checkOut)){
						res.checkIn = "";
						res.checkOut = "";
						System.out.println("Please enter the date in YYYY-MM-DD Format");
					}
					break;
				case 4: //Numebr of Children,inputs(4)
					System.out.print("Enter the number of Children: ");
					try {
						res.children = reader.nextInt();
					} catch (Exception InputMismatchException) {
						System.out.println("Invalid Type");
					}
					break;
				case 5: //Numebr of Adults,inputs(5)
					System.out.print("Enter the number of Adults: ");
					try {
						res.adults = reader.nextInt();
					} catch (Exception InputMismatchException) {
						System.out.println("Invalid Type");
					}
					break;
				case 6:
					return res;
				case 7:
					return null;
			}
		}
	}
	
	public static int Ask_For_Option3(Reservation res){
		int input = 0;
		Scanner reader = new Scanner (System.in);
		
		System.out.println("Choose a feild to edit:");
		System.out.printf("\tReservation: %d\n", res.getCode());
		System.out.printf("\t1: First Name: %s\n", res.getFirst());
		System.out.printf("\t2: Last Name: %s\n", res.getLast());

		if(res.getCheckIn() == ""){
			System.out.printf("\t5: Range of Dates: \n"); 
		}
		else{
			System.out.printf("\t5: Range of Dates: %s - %s\n", res.getCheckIn(), res.getCheckOut());
		}

		System.out.printf("\t4: Number of Children: %d\n", res.getChildren());
		System.out.printf("\t5: Number of Adults: %d\n", res.getAdults());

		System.out.println("\t6: Confirm Reservation");
		System.out.println("\t7: Cancel Changes");
		
		System.out.print("\nChoose which option to enter: ");
		try {
			input = reader.nextInt();
		} catch (Exception InputMismatchException) {
			System.out.println("Invalid Type");
		}  

		return input; 
	}

	public static String contruct_req3_sql_statement(Reservation res){
		String statement = "UPDATE reservations SET ";
		
		statement += "FirstName = '" + res.getFirst() + "', ";
		statement += "LastName = '" + res.getLast() + "', ";
		statement += "CheckIn = '" + res.getCheckIn() + "', ";
		statement += "CheckOut = '" + res.getCheckOut() + "', ";
		statement += "Kids = " + res.getChildren() + ", ";
		statement += "Adults = " + res.getAdults();
		statement += " WHERE Code = " + res.getCode();
		
		return statement + ";";
	}
	
	/* Requirement 4
	 * Reservation Cancellation
	 */

	public static String req4(){
		
		System.out.print("Please enter your reservation code to cancel:");
		Scanner reader = new Scanner (System.in);
		int code = 0;
		// Get Res Info
		try{
			code = reader.nextInt();
		} catch (Exception InputMismatchException) {
			System.out.println("Invalid Type");
			return "";
		} 
		Reservation res = fetch_res(code);
		res.code = code; 
		
		String statement = "DELETE FROM reservations ";
		statement += "WHERE Code = " + res.getCode()+ ";";
		print_res(res);
		return statement;
	}
	
	public static boolean req4_confirm(String sql){
		Scanner reader = new Scanner (System.in);
		System.out.print("Confirm Cancellation (Y/N): ");
		
		// Confirm Reservation
		while(true){
			String input = reader.next();
			if(input.charAt(0) == 'Y' || input.charAt(0) == 'y'){
				execSql(sql);
				return true;
			}
			else if(input.charAt(0) == 'N' || input.charAt(0) == 'n'){
				return false;
			}
			else{
				System.out.println("Invalid Input%nConfirm Reservation (Y/N):");
			}
		}
	}
	
	// Fetches a reservation based on the Reservation Code
	// Returns ArrayList of Reservation
	public static Reservation fetch_res(int code){
		Reservation res = new Reservation(); 
		
		String sql = "SELECT * FROM reservations"; 
		sql += " WHERE Code = " + code + ";";
		
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
							   System.getenv("HP_JDBC_USER"),
							   System.getenv("HP_JDBC_PW"))) {

			try (Statement stmt = conn.createStatement();
				 ResultSet rs = stmt.executeQuery(sql)) {
				while(rs.next()){ // Only returns the last reservation in  a list
				    res.code = rs.getInt("CODE");
					res.room = rs.getString("Room");
					res.checkIn = rs.getString("CheckIn");
					res.checkOut = rs.getString("CheckOut");
					res.first = rs.getString("FirstName");
					res.last = rs.getString("LastName");
					res.adults = rs.getInt("Adults");
					res.children = rs.getInt("Kids");
					res.rate = rs.getFloat("Rate");
					
//				    System.out.format("%d %s %s %s %s %s %n", res.code, res.room, res.first, res.last, res.checkIn, res.checkOut);
				}
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
		
		return res;
	}
	
	// Overloaded function that prints the resulting entries of an SQL query
	public static void fetch_res(String sql){
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
							   System.getenv("HP_JDBC_USER"),
							   System.getenv("HP_JDBC_PW"))) {

			try (Statement stmt = conn.createStatement();
				 ResultSet rs = stmt.executeQuery(sql)) {
				while(rs.next()){ // Only returns the last reservation in  a list
					Reservation res = new Reservation(); 
				    res.code = rs.getInt("CODE");
					res.room = rs.getString("Room");
					res.checkIn = rs.getString("CheckIn");
					res.checkOut = rs.getString("CheckOut");
					res.first = rs.getString("FirstName");
					res.last = rs.getString("LastName");
					res.adults = rs.getInt("Adults");
					res.children = rs.getInt("Kids");
					
					print_res(res);
//				    System.out.format("%d %s %s %s %s %s %n", res.code, res.room, res.first, res.last, res.checkIn, res.checkOut);
				}
			}
			catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		catch(SQLException e){
			System.err.println("SQLException: " + e.getMessage());
		}
	}
	
	public static void print_res(Reservation res){
		// Display Res Info
		System.out.printf("\tReservation:\t %d\n", res.getCode());
		System.out.printf("\tFirst Name:\t %s\n", res.getFirst());
		System.out.printf("\tLast Name:\t %s\n", res.getLast());
		System.out.printf("\tRate:\t\t %.2f\n", res.getRate());
		
		if(res.getCheckIn() == ""){
			System.out.printf("\tRange of Dates: \n"); 
		}
		else{
			System.out.printf("\tRange of Dates:\t %s - %s\n", res.getCheckIn(), res.getCheckOut());
		}

		System.out.printf("\tNumber of Children:\t %d\n", res.getChildren());
		System.out.printf("\tNumber of Adults:\t %d\n\n", res.getAdults());
	}
	
	/* Requirement 5
	 * Reservation Info
	 */
	// TODO convert to using Reservation Object
	public static ArrayList<String> req5(){
		ArrayList<String> inputs = new ArrayList<String>(); 

		for(int i = 0; i < 6; i++){
			inputs.add("Empty");
		}

		Scanner reader = new Scanner (System.in);
		String userInput; 

		while(true){
			int input = Ask_For_Option5(inputs);

			switch(input){
				case 1: //First Name, inputs(0)
					System.out.print("Enter a First Name: ");
					userInput = reader.nextLine();
					inputs.set(0, userInput);
					break;
				case 2: //Last Name, inputs(1)
					System.out.print("Enter a Last Name: ");
					userInput = reader.nextLine();
					inputs.set(1, userInput);
					break;
				case 3: //Date Range, Start Date = inputs(2), End Date = inputs(3)
					System.out.print("Enter a Start Date (YYYY-MM-DD): ");
					userInput = reader.nextLine();
					inputs.set(2, userInput);

					System.out.print("Enter a End Date (YYYY-MM-DD): ");
					userInput = reader.nextLine();
					inputs.set(3, userInput);
					
					if(!checkDate(inputs.get(2)) || !checkDate(inputs.get(3))){
						inputs.set(2, "Empty");
						inputs.set(3, "Empty");
						System.out.println("Please enter the date in YYYY-MM-DD Format");
					}
					break;
				case 4: //Room Code,inputs(4)
					System.out.print("Enter a Room Code: ");
					userInput = reader.nextLine();
					inputs.set(4, userInput);
					break;
				case 5: //Reservation Code, inputs(5)
					System.out.print("Enter a Reservation Code: ");
					userInput = reader.nextLine();
					inputs.set(5, userInput);
					break;
				case 6:
					return inputs;
			}
		}
	}
	
	public static int Ask_For_Option5(ArrayList<String> inputs){
		System.out.println("Which Criteria would you like to search by:");
		System.out.print("\t1: First Name:        ");
		System.out.println(inputs.get(0));
		
		System.out.print("\t2: Last Name:         ");
		System.out.println(inputs.get(1));

		System.out.print("\t3: Range of Dates:    ");
		if(inputs.get(3) == "Empty"){
			System.out.println(inputs.get(2));
		}
		else{
			System.out.println(inputs.get(2) + " - " + inputs.get(3));
		}

		System.out.print("\t4: Room Code:         ");
		System.out.println(inputs.get(4));

		System.out.print("\t5: Reservation Code:  ");
		System.out.println(inputs.get(5));

		System.out.println("\t6: Confirm Search");

		System.out.print("\nChoose which option to enter: ");

		int input = 0;
		Scanner reader = new Scanner (System.in);
		try {
			input = reader.nextInt();
		} catch (Exception InputMismatchException) {
			System.out.println("Invalid Type");
		}  

		return input; 
	}

	public static String contruct_req5_sql_statement(ArrayList<String> inputs){
		int numNonempties = 0;

		for(int i = 0; i < 6; i++){
			if(i == 3){
				i++; 
			}

			if(inputs.get(i) != "Empty"){
				numNonempties++;
			}
		} 

		System.out.println(numNonempties);
	
		String statement = "SELECT * FROM reservations"; 


		int i = 0; 

		if(numNonempties > 0){
			statement = statement + " WHERE ";


			while(numNonempties > 0){
				//First Name 
				if(i == 0 && inputs.get(i) != "Empty"){
					//there is a first name 
					statement = statement + "FirstName = '" + inputs.get(i) + "'";
					numNonempties--;

					if(numNonempties > 0){
						statement = statement + " AND ";
					}
				}

				//Last Name
				else if(i == 1 && inputs.get(i) != "Empty"){
					//there is a first name 
					statement = statement + "LastName = '" + inputs.get(i) + "'";
					numNonempties--;

					if(numNonempties > 0){
						statement = statement + " AND ";
					}
				}

				//Date Range 
				else if(i == 2 && inputs.get(i) != "Empty"){
					//the res starts before range ends in date 
					statement = statement + "((CheckIn <= '" + inputs.get(i) + "' AND ";
					statement = statement + "CheckOut >= '" + inputs.get(i) + "') OR ";

					statement = statement + "(CheckIn >= '" + inputs.get(i) + "' AND ";
					statement = statement + "CheckIn <= '" + inputs.get(i + 1) + "') OR";

					statement = statement + "(CheckIn >= '" + inputs.get(i) + "' AND ";
					statement = statement + "CheckOut <= '" + inputs.get(i + 1) + "'))";

					numNonempties--;
					i++;

					if(numNonempties > 0){
						statement = statement + " AND ";
					}
				}

				//Room Code
				else if(i == 4 && inputs.get(i) != "Empty"){
					//there is a first name 
					statement = statement + "Room = '" + inputs.get(i) + "'";
					numNonempties--;

					if(numNonempties > 0){
						statement = statement + " AND ";
					}
				}

				//Reservation Code
				else if(i == 5 && inputs.get(i) != "Empty"){
					//there is a first name 
					statement = statement + "Code = '" + inputs.get(i) + "'";
					numNonempties--;

					if(numNonempties > 0){
						statement = statement + " AND ";
					}
				}

				i++;
			}
		}
		
		return statement + ";";
	}
	
	/* Requirement 6
	 * Revenue Info
	 */
	
	public static int request_year(){
		System.out.print("What year would you like stats for? (YYYY): ");
		Scanner reader = new Scanner (System.in);
		return reader.nextInt();
	}


	public static void construct_req6_year_stats(){
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", 
						   "October", "November", "December"};
		int year = request_year();

		//construct sql statements for each month and the overall year 
		//each sql statement is added to sqlStatements
		ArrayList<String> sqlStatements = new ArrayList<String>();
		for(int i = 0; i < months.length; i++){
			sqlStatements.add(contruct_req6_month_sql_statement(year, months[i]));
		}
		sqlStatements.add(contruct_req6_year_sql_statement(year));

		ArrayList<Rooms> rooms = new ArrayList<Rooms>();
		
		//execute each statement in sqlStatements where index 0-11 are months and 12 is the yearly total 
		for(int i = 0; i < sqlStatements.size(); i++){
			ArrayList<Rooms> tempRooms = pull_req6_month_stats(sqlStatements.get(i));

			if(rooms.size() == 0){
				rooms = tempRooms;
			}

			//get the revenue field of each room and save it in the  
			//currect month of the permanent rooms list 
			for(int j = 0; j < tempRooms.size(); j++){
				//j is the current room index, i is the current month index 
				rooms.get(j).cal.revenue[i] = tempRooms.get(j).revenue;
			}
		}

		print_revenue(rooms);

	}


	public static void print_revenue(ArrayList<Rooms> rooms){
		System.out.println("Room\tJan\tFeb\tMar\tApr\tMay\tJun\tJul\tAug\tSep\tOct\tNov\tDec\tTotal");
		for(int i = 0; i < rooms.size(); i++){
			int[] rev = rooms.get(i).cal.revenue;
			System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d%n", 
				rooms.get(i).room, rev[0], rev[1], rev[2], rev[3], rev[4], rev[5], rev[6], rev[7], 
				rev[8], rev[9], rev[10], rev[11], rev[12]);
		}
	}

	//execute a pull for monthly or yearly stats from the perameter sql statment 
	public static ArrayList<Rooms> pull_req6_month_stats(String sql){
		ArrayList<Rooms> tempRooms = new ArrayList<Rooms>();

		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
								   System.getenv("HP_JDBC_USER"),
								   System.getenv("HP_JDBC_PW"))) {
				try (Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
					while(rs.next()){ // Only returns the last reservation in  a list
						Rooms room = new Rooms();
					    room.room = rs.getString("room");
						room.revenue = rs.getInt("Revenue");
						
						tempRooms.add(room);
					}
				}
				catch(SQLException e){
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		catch(SQLException e){
			System.err.println("SQLException: " + e.getMessage());
		}

		return tempRooms;
	}

	public static String contruct_req6_month_sql_statement(int year, String month){
		String statement = "SELECT Room, round(Sum(Rate * DATEDIFF(checkout, checkin)), 0) Revenue";

		statement = statement + " FROM reservations"; 
		statement = statement + " WHERE date_format(Checkout, '%Y') = '" + year + "'";
		statement = statement + "AND date_format(Checkout, '%M') = '" + month + "'";
		statement = statement + "GROUP BY date_format(Checkout, '%M'), room"; 
		return statement + ";";
	}

	public static String contruct_req6_year_sql_statement(int year){
		String statement = "SELECT Room, round(Sum(Rate * DATEDIFF(checkout, checkin)), 0) Revenue";

		statement = statement + " FROM reservations";
		statement = statement + " WHERE date_format(Checkout, '%Y') = '" + year + "'";
		statement = statement + "GROUP BY date_format(Checkout, '%Y'), room"; 
		return statement + ";";
	}


}