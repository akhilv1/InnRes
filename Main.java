import java.util.Scanner;

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
			System.out.println("7: Exit");
			System.out.print("Choose an Option: ");
			sel = reader.nextInt();
			switch (sel){
				case 1:
					System.out.println("Option 1: Show Rooms and Rates");
				    break;
				case 2:
					System.out.println("Option 2: Make a Reservation");
					break;
				case 3:
					System.out.println("Option 3: Change a Reservation");
				    break;
				case 4:
					System.out.println("Option 4: Cancel a Reservation");
					break;
				case 5:
					System.out.println("Option 5: About a Reservation");
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
}