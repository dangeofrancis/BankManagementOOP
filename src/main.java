import java.util.Scanner;
import java.util.HashMap;
public class main{
		public static void main(String[]args){
				Scanner sc = new Scanner(System.in);
				HashMap<String,String> usrval = new HashMap<>();

				while(true){
				System.out.println("-------------------");
				System.out.println("Banking Application");
				System.out.println("-------------------");
				System.out.println("what would you like to do?\n 1. create new account\n 2.log into existing account\n 3.Exit application\n ");
				int choice = sc.nextInt();
				sc.nextLine();

				if(choice==3){
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

						default:
								System.out.println("invalid input");
						}
				}
				sc.close();
		}

		public static void newacc(Scanner sc,HashMap<String,String> usrval){
				String usrname;
				String passwd1;
				String passwd2;
				System.out.println("Enter your username(A Strong and collection of charecters,larger than 6 characters long.)");
				usrname = sc.nextLine();

				if(usrname.length()<8){
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
						usrval.put(usrname,passwd1);
						System.out.println("password confirmed.user created.");
				}

		}

		public static void login(Scanner sc,HashMap<String,String> usrval){
				String usrname;
				String passwd;
				String passwd1;
				System.out.println("Enter your username");
				usrname = sc.nextLine();
				if(usrname.length()<8){
						System.out.println("invalid username");
				}else if(usrval.containsKey(usrname)){
						System.out.println("Enter password");
						passwd = sc.nextLine();
						passwd1 = usrval.get(usrname);
						if(passwd1.equals(passwd)){
								System.out.println("access granted.");
								mainmenu(sc,usrval,usrname);
						}else{
								System.out.println("access denied.");
						}
				}else{
						System.out.println("user invalid");
				}	
	
		}

		public static void mainmenu(Scanner sc,HashMap<String,String> userval,String usrname){
				while(true){ 
						System.out.println("#################");
						System.out.println("### Main Menu ###");
						System.out.println("#################");
						System.out.println("");
						System.out.println("Enter your choice");
						System.out.println("1.Deposit money to your virtual acc");
						System.out.println("2.Withdraw money from your virtual acc");
						System.out.println("3.Exits to start");
						int choice1 = sc.nextInt();
						sc.nextLine();
						if(choice1==3){
								break;
						}
				}

					
			
				

		}
		
}			
