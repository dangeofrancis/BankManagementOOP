import java.util.Scanner;
import java.math.BigDecimal;
public class main{
		public static void main(String[]args){
				Scanner sc = new Scanner(System.in);
				BankManager bank = new BankManager();
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
								newacc(sc,bank);
						break;

						case 2:
								login(sc,bank);
						break;
						case 3:
								System.out.println("Enter adiministrator username");
								String usrname = sc.nextLine();
								System.out.println("Enter adiministrator password");
								String passwd1 = sc.nextLine();
								if(usrname.equals("admin") && passwd1.equals("password")){
										System.out.println("ACCESS GRANTED");
										adminMenu(sc,bank);
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

		public static void newacc(Scanner sc,BankManager bank){
				String usrname;
				System.out.println("Enter your username(A Strong and collection of charecters,larger than 6 characters long.)");
				usrname = sc.nextLine();

				if(usrname.length()<6){
						System.out.println("invalid username");
						return;
				}
				String passwd1, passwd2;
						do { System.out.println("Set password:");
						passwd1 = sc.nextLine();
					   	System.out.println("Re-enter password:");
						passwd2 = sc.nextLine();
					   	if (!passwd1.equals(passwd2)) {
									System.out.println("Mismatching passwords, try again."); 
								}
						} while (!passwd1.equals(passwd2));
						if (bank.registerUser(usrname, passwd1)) {
							System.out.println("Password confirmed. User created."); 
						}else {
							System.out.println("Username already taken."); 
						}
		}


		public static void login(Scanner sc, BankManager bank) {
				System.out.println("Enter your username:");
				String usrname = sc.nextLine();
				System.out.println("Enter your password:");
				String passwd = sc.nextLine();
		
				Account currentAcc = bank.authenticateUser(usrname, passwd);
		
				if (currentAcc == null) {
				System.out.println("Access denied: Invalid username or password.");
				} else if (currentAcc.isBlocked()) {
				System.out.println("Account suspended. Contact Admin.");
				} else {
				System.out.println("Access granted.");
				mainmenu(sc, currentAcc);
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
		public static void adminMenu(Scanner sc, BankManager bank) {
        while (true) {
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
            
            System.out.println("Enter target username:");
            String target = sc.nextLine();
            
            Account targetAcc = bank.getAccountForAdmin(target);
            
            if (targetAcc == null) {
                System.out.println("Error: User not found in database.");
                continue; // Skips the switch statement and loops back to the top
            }
            
            switch (choice) {
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
