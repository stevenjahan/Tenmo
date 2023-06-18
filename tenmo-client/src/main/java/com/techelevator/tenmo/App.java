package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);


    private AuthenticatedUser currentUser;

    private static int transferIdNumber;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        Balance balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is:  $" + balance.getBalance());
    }

	private void viewTransferHistory() {
        Transfer[] transfers = transferService.getTransfersFromUserId(currentUser, currentUser.getUser().getId());
        System.out.println("-------------------------------");
        System.out.println("Transfers");
        System.out.println("ID     From/To          Amount");
        System.out.println("-------------------------------");

        int currentUserAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        for(Transfer transfer: transfers) {
            printTransferStubDetails(currentUser, transfer);
        }

        int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to view details (0 to cancel)");
        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if(transferChoice != null) {
            printTransferDetails(currentUser, transferChoice);
        }
	}

	private void viewPendingRequests() {
        Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);
        System.out.println("-------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To          Amount");
        System.out.println("-------------------------------");

        for(Transfer transfer: transfers) {
            printTransferStubDetails(currentUser, transfer);
        }
        // TODO ask to view details
        int transferIdChoice = console.getUserInputInteger("\nPlease enter transfer ID to approve/reject (0 to cancel)");
        Transfer transferChoice = validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if(transferChoice != null) {
            approveOrReject(transferChoice, currentUser);
        }
    }

	private void sendBucks() {
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);

        int userIdChoice = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            createTransfer(userIdChoice, amountChoice, "Send", "Approved");
        }
    }

	private void requestBucks() {
        User[] users = userService.getAllUsers(currentUser);
        printUserOptions(currentUser, users);
        int userIdChoice = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = console.getUserInput("Enter amount");
            createTransfer(userIdChoice, amountChoice, "Request", "Pending");
        }
}
