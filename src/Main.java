import database.BankManager;
import service.BankingService;
import ui.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        BankManager bankManager = new BankManager();

        BankingService bankingService =
                new BankingService(bankManager);

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame(bankingService);

            loginFrame.setVisible(true);
        });
    }
}