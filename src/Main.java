import database.BankManager;
import service.BankingService;
import ui.LoginFrame;
import com.formdev.flatlaf.FlatDarkLaf; 

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        FlatDarkLaf.setup(); 

        BankManager bankManager = new BankManager();
        BankingService bankingService = new BankingService(bankManager);

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(bankingService);
            loginFrame.setVisible(true);
        });
    }
}
