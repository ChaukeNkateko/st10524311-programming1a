# st10524311-programming1a
This is the Chat App Proof of Engagement (PoE) for the module PROG5121. It is a console-based chat application (QuickChat) built in two parts.

# QuickChat – Java Messaging Application

## 1. Project Description

QuickChat is a Java-based messaging application developed to allow users to register, log in and communicate through messages.

The application uses object-oriented programming, arrays, methods, loops, conditional statements and JSON file storage.

The project was developed using NetBeans IDE with Java and Ant.

## 2. Main Features

### Part 1: User Registration and Login

The application allows users to:
- Register a new account.
- Enter their first name and last name.
- Create a valid username.
- Create a password that meets the required complexity.
- Enter and validate a cellphone number.
- Log in using their registered username and password.
- Receive feedback on whether login was successful.

### Part 2: Messaging

The messaging functionality allows users to:
- Enter the number of messages they want to send.
- Enter a recipient's cellphone number.
- Write messages.
- Validate message length.
- Generate a unique message ID.
- Generate a message hash.
- Choose whether to send, disregard or store a message.
- Display message information and confirmation messages.

### Part 3: Message Management and Storage

Part 3 extends the existing application by adding message management features and arrays.

#### 3.1 Arrays

The application must populate the following arrays without hard-coding the information:

**Sent Messages**
- Contains all messages that have been sent.

**Disregarded Messages**
- Contains all messages that were disregarded.

**Stored Messages**
- Reads the stored messages from the JSON file and places them into an array.

**Message Hash**
- Contains all message hashes.

**Message ID**
- Contains all message IDs.

#### 3.2 Fourth Main Menu Option: Stored Messages

A fourth main menu option called **Stored Messages** must be created.

Users must be able to select this option to perform the following operations:

1. Display the sender and recipient of all stored messages.
2. Display the longest stored message.
3. Search for a message using its message ID and display the corresponding recipient and message.
4. Search for all messages stored for a particular recipient.
5. Delete a message using its message hash.
6. Display a report containing the full details of all stored messages.

#### 3.3 Message Storage

The application must use JSON file storage to keep stored messages.

The stored messages must be read from the JSON file and loaded into an array so that they can be accessed and managed through the Stored Messages menu.

#### 3.4 Message Searching and Deletion

The application must allow users to search for messages using:
- Message ID.
- Recipient cellphone number.

Users must also be able to delete a stored message by providing its message hash.

After deletion, the application must confirm that the correct message was successfully deleted.

#### 3.5 Message Report

The application must generate a report showing the full details of all stored messages.

The report must include:
- Message hash.
- Recipient.
- Message ID.
- Message content.
- Other available message details.

## 3. Part 3 Test Data

The following test data must be used to populate the arrays and test the application.

| Message | Recipient / Developer | Message | Flag |
|---|---|---|---|
| Message 1 | +27834557896 | Did you get the cake? | Sent |
| Message 2 | +27838884567 | Where are you? You are late! I have asked you to be on time. | Stored |
| Message 3 | +27834484567 | Yohoooo, I am at your gate. | Disregarded |
| Message 4 | 0823884567 (Developer) | It is dinner time! | Sent |
| Message 5 | +27838884567 | Ok, I am leaving without you. | Stored |

These records are used to test message storage, searching, deletion and reporting.

## 4. Part 3 Unit Testing Requirements

The application must include unit tests using JUnit and the assertEquals method.

The following tests must be created:

1. **Sent Messages Array:** Check that the sent messages array is populated correctly and contains the expected test data.
2. **Longest Message:** Display the longest message from messages 1–4.
3. **Search by Message ID:** Search for message ID `0838884567` and check that the system returns the message "It is dinner time!"
4. **Search by Recipient:** Search for all sent or stored messages belonging to recipient `+27838884567`.
5. **Delete Message:** Delete Message 2 using its message hash and check that the message is successfully deleted.
6. **Display Report:** Check that the report displays the stored message details, including the message hash, recipient and message information.

The tests must compare the expected results with the actual results returned by the application.

## 5. Technologies Used

- Java – Main programming language.
- NetBeans IDE – Development environment.
- Apache Ant – Project build tool.
- JSON-simple 1.1 – JSON file reading and writing.
- JSON – Used for message storage.
- JUnit 5 – Used for unit testing.

## 6. Project Structure

The project contains the following main files:

- `Myprogfile.java` – Main application, menu and program execution.
- `Login.java` – Registration and login validation.
- `Message.java` – Message creation, validation, hashing and management.
- `LoginTest.java` – Tests registration and login functionality.
- `MessageTest.java` – Tests message functionality and Part 3 requirements.
- `messages.json` – Stores message information.
- `build.xml` – Apache Ant build configuration.
- `nbproject/` – NetBeans project configuration.

## 7. How to Run the Application

1. Install the Java Development Kit (JDK).
2. Install NetBeans IDE.
3. Extract the project ZIP file.
4. Open NetBeans.
5. Select File → Open Project.
6. Select the QuickChat project folder.
7. Make sure the JSON-simple 1.1 library is added.
8. Right-click the project and select Clean and Build.
9. Right-click the project and select Run.

## 8. How to Run the Unit Tests

1. Open the project in NetBeans.
2. Locate the test folder.
3. Open `LoginTest.java` or `MessageTest.java`.
4. Right-click the project.
5. Select Test.
6. Check the test results to see whether the expected and actual results match.

## 9. Conclusion

QuickChat demonstrates how Java can be used to develop a messaging application with registration, login, message sending and message management features.

Part 3 improves the application by introducing arrays, JSON file storage, message searching, message deletion and report generation.

JUnit testing is used to check that the application works correctly and produces the expected results.

**Project Name:** QuickChat

**Programming Language:** Java

**IDE:** NetBeans

**Build Tool:** Apache Ant

**Testing Framework:** JUnit 5

**JSON Library:** JSON-simple 1.1



YouTube link https://youtu.be/AM4z25M7zGg
