package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private RestAccountService accountService = new RestAccountService(API_BASE_URL);
    private RestUserService userService = new RestUserService(API_BASE_URL);
    private RestTransferTypeService transferTypeService = new RestTransferTypeService(API_BASE_URL);
    private RestTransferStatusService transferStatusService = new RestTransferStatusService(API_BASE_URL);
    private RestTransferService transferService = new RestTransferService(API_BASE_URL);


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
    public static void incrementTransferIdNumber() {
        transferIdNumber++;
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
        for (Transfer transfer : transfers) {
            printTransferStubDetails(currentUser, transfer);
        }

        int transferIdChoice = consoleService.promptForInt("\nPlease enter transfer ID to view details (0 to cancel)");
        Transfer transferChoice = transferService.validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if (transferChoice != null) {
            printTransferDetails(currentUser, transferChoice);
        }
    }

    private void viewPendingRequests() {
        Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);
        System.out.println("-------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To          Amount");
        System.out.println("-------------------------------");

        for (Transfer transfer : transfers) {
            printTransferStubDetails(currentUser, transfer);
        }
        // TODO ask to view details
        int transferIdChoice = consoleService.promptForInt("\nPlease enter transfer ID to approve/reject (0 to cancel)");
        Transfer transferChoice = transferService.validateTransferIdChoice(transferIdChoice, transfers, currentUser);
        if (transferChoice != null) {
            approveOrReject(transferChoice, currentUser);
        }
    }

    private void sendBucks() {
        User[] users = userService.getAllUsers(currentUser);
        userService.printUserOptions(currentUser, users);

        int userIdChoice = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel)");
        if (userService.validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = consoleService.promptForString("Enter amount");
            createTransfer(userIdChoice, amountChoice, "Send", "Approved");
        }
    }

    private void requestBucks() {
        User[] users = userService.getAllUsers(currentUser);
        userService.printUserOptions(currentUser, users);
        int userIdChoice = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel)");
        if (userService.validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = consoleService.promptForString("Enter amount");
            createTransfer(userIdChoice, amountChoice, "Request", "Pending");
        }
    }

    private void printTransferDetails (AuthenticatedUser currentUser, Transfer transferChoice){
        int id = transferChoice.getTransferId();
        BigDecimal amount = transferChoice.getAmount();
        int fromAccount = transferChoice.getAccountFrom();
        int toAccount = transferChoice.getAccountTo();
        int transactionTypeId = transferChoice.getTransferTypeId();
        int transactionStatusId = transferChoice.getTransferStatusId();

        int fromUserId = accountService.getAccountById(currentUser, fromAccount).getUserId();
        String fromUserName = userService.getUserByUserId(currentUser, fromUserId).getUsername();
        int toUserId = accountService.getAccountById(currentUser, toAccount).getUserId();
        String toUserName = userService.getUserByUserId(currentUser, toUserId).getUsername();
        String transactionType = transferTypeService.getTransferTypeFromId(currentUser, transactionTypeId).getTransferTypeDesc();
        String transactionStatus = transferStatusService.getTransferStatusById(currentUser, transactionStatusId).getTransferStatusDesc();

        System.out.println(fromUserName + toUserName + transactionType + transactionStatus);
    }
    private void printTransferStubDetails(AuthenticatedUser authenticatedUser, Transfer transfer) {
        String fromOrTo = "";
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        if (accountService.getAccountById(currentUser, accountTo).getUserId() == authenticatedUser.getUser().getId()) {
            int accountFromUserId = accountService.getAccountById(currentUser, accountFrom).getUserId();
            String userFromName = userService.getUserByUserId(currentUser, accountFromUserId).getUsername();
            fromOrTo = "From: " + userFromName;
        } else {
            int accountToUserId = accountService.getAccountById(currentUser, accountTo).getUserId();
            String userToName = userService.getUserByUserId(currentUser, accountToUserId).getUsername();
            fromOrTo = "To: " + userToName;
        }

        System.out.println(transfer.getTransferId() + fromOrTo + transfer.getAmount());
    }

    private void approveOrReject(Transfer pendingTransfer, AuthenticatedUser authenticatedUser) {

        consoleService.printApproveOrRejectOptions();
        int choice = consoleService.promptForInt("Please choose an option");

        if(choice != 0) {
            if(choice == 1) {
                int transferStatusId = transferStatusService.getTransferStatus(currentUser, "Approved").getTransferStatusId();
                pendingTransfer.setTransferStatusId(transferStatusId);
            } else if (choice == 2) {
                int transferStatusId = transferStatusService.getTransferStatus(currentUser, "Rejected").getTransferStatusId();
                pendingTransfer.setTransferStatusId(transferStatusId);
            } else {
                System.out.println("Invalid choice.");
            }
            transferService.updateTransfer(currentUser, pendingTransfer);
        }

    }

    private Transfer createTransfer (int accountChoiceUserId, String amountString, String transferType, String status){

        int transferTypeId = transferTypeService.getTransferType(currentUser, transferType).getTransferTypeId();
        int transferStatusId = transferStatusService.getTransferStatus(currentUser, status).getTransferStatusId();
        int accountToId;
        int accountFromId;
        if(transferType.equals("Send")) {
            accountToId = accountService.getAccountByUserId(currentUser, accountChoiceUserId).getAccountId();
            accountFromId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
        } else {
            accountToId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
            accountFromId = accountService.getAccountByUserId(currentUser, accountChoiceUserId).getAccountId();
        }

        BigDecimal amount = new BigDecimal(amountString);

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountFromId);
        transfer.setAccountTo(accountToId);
        transfer.setAmount(amount);
        transfer.setTransferStatusId(transferStatusId);
        transfer.setTransferTypeId(transferTypeId);
        transfer.setTransferId(transferIdNumber);

        transferService.createTransfer(currentUser, transfer);
        // increment transferIdNumber so it is always unique
        App.incrementTransferIdNumber();
        return transfer;
    }
}
