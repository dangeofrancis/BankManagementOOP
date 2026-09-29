import java.util.HashMap;
		public class BankManager {
				private HashMap<String, Account> database;

				public BankManager() {
						this.database = new HashMap<>();
				}

				public boolean registerUser(String username, String password) {
						if (database.containsKey(username)) {
							return false; 
					}
				database.put(username, new Account(password));
				return true; 
				}

				public Account authenticateUser(String username, String password) {
						if (!database.containsKey(username)) {
							return null; 
						}
						Account currentAccount = database.get(username);
						if (!currentAccount.getPassword().equals(password)) {
								     return null;
						}
						return currentAccount;
				}
				public Account getAccountForAdmin(String username) {
						return database.get(username);
				}
		}
