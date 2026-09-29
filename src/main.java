import java.util.Scanner;
import java.math.BigDecimal;
import java.util.HashMap;
public class main{
		public static void main(String[]args){
				Scanner sc = new Scanner(System.in);
				HashMap<String,Account> usrval = new HashMap<>();

				while(true){
				System.out.println("-------------------");
				System.out.println("Banking Application");
				System.out.println("-------------------");
				System.out.println("what would you like to do?\n 1. create new account\n 2.log into existing account\n 3.Log in as an administrator\n 4.Exit application\n ");
				int choice = sc.nextInt();
				sc.nextLine();

				if(choice==4){
						System.out.println("Thankyou.");
						break;
				}
				switch(choice){
						case 1:
								newacc(sc,usrval);
						break;

						case 2:
								login(sc,usrval);
						break;
						case 3:
								System.out.println("Enter adiministrator username");
								String usrname = sc.nextLine();
								System.out.println("Enter adiministrator password");
								String passwd1 = sc.nextLine();
								if(usrname.equals("admin") && passwd1.equals("password")){
										System.out.println("ACCESS GRANTED");
										adminMenu(sc,usrval);
								}else{
										System.out.println("ACCESS  DENIED");
								}
						break;

						default:
								System.out.println("invalid input");
						}
				}
				sc.close();
		}

		public static void newacc(Scanner sc,HashMap<String,Account> usrval){
				String usrname;
				String passwd1;
				String passwd2;
				System.out.println("Enter your username(A Strong and collection of charecters,larger than 6 characters long.)");
				usrname = sc.nextLine();

				if(usrname.length()<6){
						System.out.println("invalid username");
				}else if(usrval.containsKey(usrname)){
						System.out.println("username alreadytaken");
				}else{
						do{ 
								System.out.println("username available.\n set password");
								passwd1 = sc.nextLine();
								System.out.println("re-enter password");
								passwd2 = sc.nextLine();
								if(!passwd1.equals(passwd2)){
										System.out.println("mismatching passwords, try again.");
								}
						}while(!passwd1.equals(passwd2));
						usrval.put(usrname, new Account(passwd1));
						System.out.println("password confirmed. user created.");
						}
		}


		public static void login(Scanner sc,HashMap<String,Account> usrval){
				String usrname;
				String passwd;
				String passwd1;
				System.out.println("Enter your username");
				usrname = sc.nextLine();
				if(usrname.length()<6){
						System.out.println("invalid username");
				}else if(usrval.containsKey(usrname)){
						System.out.println("Enter password");
						passwd = sc.nextLine();
						Account currentAcc = usrval.get(usrname);
						if (currentAcc.isBlocked()) {
								System.out.println("Account suspended. Contact Admin.");
						} else if (currentAcc.getPassword().equals(passwd)) {
								System.out.println("access granted.");
								mainmenu(sc, currentAcc);
						} else {
								System.out.println("access denied.");
						}
				}else{
						System.out.println("user invalid");
				}	
	
		}

		public static void mainmenu(Scanner sc, Account userAccount) {
						while(true){ 
								System.out.println("#################");
								System.out.println("### Main Menu ###");
								System.out.println("#################");
								System.out.println("Current Balance: $" + userAccount.getBalance());
								System.out.println("\nEnter your choice");
								System.out.println("1.Deposit money to your virtual acc");
								System.out.println("2.Withdraw money from your virtual acc");
								System.out.println("3.Exits to start");
								
								int choice1 = sc.nextInt();
								sc.nextLine();
								
								if(choice1 == 3){
										break;
								}
								
								switch(choice1){
										case 1:
												System.out.println("Enter amount to deposit:");
												String depInput = sc.nextLine(); 
												BigDecimal depositAmount = new BigDecimal(depInput);
												
												if (userAccount.deposit(depositAmount)) {
														System.out.println("Deposit successful.");
												}
												break;
												
										case 2:
												System.out.println("Enter amount to withdraw:");
												String withInput = sc.nextLine();
												BigDecimal withdrawAmount = new BigDecimal(withInput); 
												
												if (userAccount.withdraw(withdrawAmount)) {
														System.out.println("Withdrawal successful.");
												}
												break;
												
										default:
												System.out.println("Invalid option.");
								}
						}			
		}
		public static void adminMenu(Scanner sc, HashMap<String, Account> usrval) {
				while(true) {
						System.out.println("###################");
						System.out.println("### Admin Panel ###");
						System.out.println("###################");
						System.out.println("1. Add Funds to User");
						System.out.println("2. Block User");
						System.out.println("3. Unblock User");
						System.out.println("4. Logout");
						
						int choice = sc.nextInt();
						sc.nextLine(); 
						
						if (choice == 4) {
								System.out.println("Logging out of Admin Panel...");
								break;
						}
						
						// For options 1, 2, and 3, we always need a target username first
						System.out.println("Enter target username:");
						String target = sc.nextLine();
						
						if (!usrval.containsKey(target)) {
								System.out.println("Error: User not found in database.");
								continue; // Skips the rest of the loop and starts over
						}
						
						// Retrieve the user's Account object
						Account targetAcc = usrval.get(target);
						
						switch(choice) {
								case 1:
										System.out.println("Enter amount to deposit for " + target + ":");
										String depInput = sc.nextLine();
										BigDecimal amount = new BigDecimal(depInput);
										
										if (targetAcc.deposit(amount)) {
												System.out.println("Funds added successfully. New balance: $" + targetAcc.getBalance());
										}
										break;
										
								case 2:
										targetAcc.setBlocked(true);
										System.out.println("User '" + target + "' has been BLOCKED.");
										break;
										
								case 3:
										targetAcc.setBlocked(false);
										System.out.println("User '" + target + "' has been UNBLOCKED.");
										break;
										
								default:
										System.out.println("Invalid option.");
						}
				}
		}
	}
