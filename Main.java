import java.util.Scanner;
import java.util.ArrayList;


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
			switch (sel){
				case 1:
					System.out.println("Option 1: Show Rooms and Rates");
				    break;
				case 2:
					System.out.println("Option 2: Make a Reservation");
					inputs = resInfo();
					break;
				case 3:
					System.out.println("Option 3: Change a Reservation");
					inputs = resInfo();
				    break;
				case 4:
					System.out.println("Option 4: Cancel a Reservation");
					inputs = resInfo();
					break;
				case 5:
					System.out.println("Option 5: About a Reservation\n");
					inputs = resInfo();
					System.out.println(contruct_req5_sql_statement(inputs));
				   	break;
				case 6:
					System.out.println("Option 6: Inn Revenue");
					break;
				case 7:
					System.out.println("Thank you for using the Inn Reservation System");
					sel = -1;
					break;
			}
		}
	}
	
	// General Info function that gets the standard reservation info from a user
	public static ArrayList<String> resInfo(){
		ArrayList<String> inputs = new ArrayList<String>(); 

		for(int i = 0; i < 6; i++){
			inputs.add("Empty");
		}

		Scanner reader = new Scanner (System.in);
		String userInput; 
		boolean loop = true;

		while(loop){
			int input = Ask_For_Option(inputs);

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
					loop = false;
					return inputs;
			}
		}
		return null;
	}

	public static int Ask_For_Option(ArrayList<String> inputs){
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

		int input; 
		Scanner reader = new Scanner (System.in);
		input = reader.nextInt(); 

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
					statement = statement + "CODE = '" + inputs.get(i) + "'";
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
}






