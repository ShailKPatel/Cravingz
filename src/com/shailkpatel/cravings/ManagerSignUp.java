package com.shailkpatel.cravings;

import com.shailkpatel.cravings.manager.LoginManager;
import com.shailkpatel.cravings.manager.RegisterManager;
import com.shailkpatel.cravings.util.InputValidator;

public class ManagerSignUp {

    private static final InputValidator iv = new InputValidator();

    public static void main(String[] args) {
        while (true) {
            System.out.println();
            System.out.println("Welcome! Please choose an option:");
            System.out.println("1. Register as a new manager");
            System.out.println("2. Login as an existing manager");
            System.out.println("3. Exit");

            int choice = iv.getIntInput("Enter 1, 2, or 3: ", 1, 3);

            if (choice == 1) {
                RegisterManager registerManager = new RegisterManager();
                registerManager.register();
                return;
            } else if (choice == 2) {
                LoginManager loginManager = new LoginManager();
                loginManager.login();
                return;
            } else if (choice == 3) {
                System.out.println("Exiting the Manager Sign-Up menu.");
                return;
            }
        }
    }
}
