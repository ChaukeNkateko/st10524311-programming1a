/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package myprogfile;

import java.util.Scanner;

/** QuickChat application with the Part 3 Stored Messages . */
public class Myprogfile {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== Registration ===");

        System.out.print("Enter a username (must contain _ and be 5 characters or less): ");
        String username = input.nextLine();

        System.out.print("Enter a password (8+ characters, a capital letter, a number and a special character): ");
        String password = input.nextLine();

        System.out.print("Enter your cell number (with country code, example +27614880723): ");
        String cellNumber = input.nextLine();

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        String result = login.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println(result);

        if (!result.startsWith("Username successfully captured")) {
            input.close();
            return;
        }

        System.out.println("\n=== Login ===");
        System.out.print("Enter your username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Enter your password: ");
        String enteredPassword = input.nextLine();

        login.loginUser(enteredUsername, enteredPassword);
        System.out.println(login.returnLoginStatus());

        if (!login.isLoggedIn()) {
            input.close();
            return;
        }

        // The signed-in user's phone number is used as the sender.
        String sender = cellNumber;

        // Load saved messages from the previous session.
        Message.readStoredMessages("messages.json");

        System.out.println("\nWelcome to QuickChat.");
        int numberOfMessages = readPositiveInteger(
                input, "How many messages do you wish to enter? ");

        boolean messagesEntered = false;
        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            int choice = readInteger(input);

            if (choice == 1) {
                if (messagesEntered) {
                    System.out.println("You have already entered all "
                            + numberOfMessages + " messages.");
                } else {
                    sendMessages(input, numberOfMessages, sender);
                    messagesEntered = true;
                }
            } else if (choice == 2) {
                System.out.println(Message.printMessages());
            } else if (choice == 3) {
                System.out.println("Thank you for using QuickChat.");
                running = false;
            } else if (choice == 4) {
                storedMessagesMenu(input);
            } else {
                System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }

        input.close();
    }

    private static int readPositiveInteger(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            int number = readInteger(input);

            if (number > 0) {
                return number;
            }

            System.out.println("Please enter a number greater than zero.");
        }
    }

    private static int readInteger(Scanner input) {
        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void sendMessages(Scanner input, int numberOfMessages, String sender) {
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\n--- Message " + (i + 1)
                    + " of " + numberOfMessages + " ---");

            String recipient;
            while (true) {
                System.out.print("Enter recipient cell number (with international code): ");
                recipient = input.nextLine();

                Message check = new Message(i, recipient, "test");
                String checkResult = check.checkRecipientCell();
                System.out.println(checkResult);

                if (checkResult.equals("Cell phone number successfully captured.")) {
                    break;
                }
            }

            String text;
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                text = input.nextLine();

                String lengthResult = Message.checkMessageLength(text);
                System.out.println(lengthResult);

                if (lengthResult.equals("Message ready to send.")) {
                    break;
                }
            }

            Message message = new Message(i, recipient, sender, text);

            System.out.println(message.displayMessageID());
            System.out.println("\n--- Message Details ---");
            System.out.println(message.displayMessageDetails());

            System.out.println("\n1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choose an option: ");

            String response = message.sentMessage(readInteger(input));
            System.out.println(response);
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        System.out.println("\n--- Sent Messages ---");
        System.out.println(Message.printMessages());
    }

    /** Part 3 main-menu option 4. */
    private static void storedMessagesMenu(Scanner input) {
        boolean returnToMainMenu = false;

        while (!returnToMainMenu) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search all message groups by Message ID");
            System.out.println("4) Search stored messages by recipient");
            System.out.println("5) Delete stored message using Message Hash");
            System.out.println("6) Display full stored message report");
            System.out.println("7) Search sent and stored messages by recipient");
            System.out.println("8) Return to main menu");
            System.out.print("Choose an option: ");

            int choice = readInteger(input);

            if (choice == 1) {
                System.out.println(Message.printSenderRecipientReport());
            } else if (choice == 2) {
                System.out.println(Message.displayLongestStoredMessage());
            } else if (choice == 3) {
                System.out.print("Enter the Message ID: ");
                Message found = Message.searchByMessageID(input.nextLine());

                if (found == null) {
                    System.out.println("No message found.");
                } else {
                    System.out.println("Recipient: " + found.getRecipient());
                    System.out.println("Message: " + found.getMessageText());
                }
            } else if (choice == 4) {
                System.out.print("Enter the recipient's cellphone number: ");
                Message[] matches = Message.findMessagesByRecipient(input.nextLine());
                printMessages(matches);
            } else if (choice == 5) {
                System.out.print("Enter the message hash: ");
                boolean deleted = Message.deleteMessageByHash(input.nextLine());

                if (deleted) {
                    System.out.println("Stored message successfully deleted.");
                } else {
                    System.out.println("No stored message has that hash.");
                }
            } else if (choice == 6) {
                System.out.println(Message.displayMessageReport());
            } else if (choice == 7) {
                System.out.print("Enter the recipient's cellphone number: ");
                Message[] matches =
                        Message.findMessagesByRecipientSentAndStored(input.nextLine());
                printMessages(matches);
            } else if (choice == 8) {
                returnToMainMenu = true;
            } else {
                System.out.println("Invalid option. Choose a number from 1 to 8.");
            }
        }
    }

    private static void printMessages(Message[] messages) {
        if (messages.length == 0) {
            System.out.println("No messages found for that recipient.");
            return;
        }

        for (Message message : messages) {
            System.out.println("Message: " + message.getMessageText());
        }
    }
}
