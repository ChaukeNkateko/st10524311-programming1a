# st10524311-programming1a
This is the Chat App Proof of Engagement (PoE) for the module PROG5121. It is a console-based chat application (QuickChat) built in two parts.

Part 1 – Registration and Login

Create an account by providing a username, password, and South African cell phone number.
Log in using the same credentials.
Part 2 – Sending Messages

After a successful login, the user sees a welcome message and a numeric menu.
The user says how many messages they want to enter, then enters the recipient and message text for each one.
Each message gets a unique 10-digit Message ID, a Message Hash, and a message number.
For each message the user can send it, disregard it, or store it to send later (stored messages are saved to a JSON file).
At the end, the application shows the total number of messages sent and the messages that were sent.
The application validates all inputs using the rules specified in the assignment brief and provides clear feedback to the user. Every part is tested with JUnit 5 and the tests run automatically on GitHub.

Features
Feature	Description
Username Validation	Must contain an underscore (_) and be no more than 5 characters long.
Password Complexity	Must be at least 8 characters, contain a capital letter, a number, and a special character.
Cell Phone Validation	Must start with the South African country code (+27) and be followed by 1–10 digits.
Registration	Stores user details (first name, last name, username, password, cell number).
Login	Verifies credentials and returns a welcome message or error message.
Welcome Message and Menu	Shows "Welcome to QuickChat." and a numeric menu: 1) Send Messages, 2) Show recently sent messages ("Coming Soon."), 3) Quit. The menu runs in a while loop until the user quits.
Number of Messages	The user sets how many messages they will enter once, at the start. A for loop then runs exactly that many times.
Message Number	Comes from the for loop counter, so the first message is number 0.
Message ID	Randomly generated 10-digit number.
Message Hash	First two digits of the Message ID, a colon, the message number, a colon, then the first and last words of the message in capitals (for example 00:0:HITONIGHT).
Message Length Check	A message may not be longer than 250 characters. If it is too long, the app says how many characters over the limit it is.
Recipient Check	Reuses the cell phone check from the Login class (international code followed by the number).
Send, Disregard or Store	The user chooses what to do with each message. Only sent messages count towards the total.
JSON Storage	Stored messages are written to messages.json as a valid JSON array.
Unit Testing	JUnit 5 tests for the Login class (14 tests) and the Message class (13 tests).
Automated Testing	A GitHub Actions workflow (TestJava.yml) runs all the tests on every push and pull request.
Technologies Used
Technology	Purpose
Java (JDK 25)	Core programming language
Maven	Build automation and dependency management
JUnit 5	Unit testing framework
Git	Version control
GitHub	Remote repository hosting
GitHub Actions	Continuous integration (automated tests)
NetBeans IDE	Development environment
Project Structure
The project now also contains the Part 2 files:

PROG5121-POE-ChatApp
├── .github/workflows/TestJava.yml      (GitHub Actions workflow)
├── src
│   ├── main/java/com/mycompany/chatapp/part1
│   │   ├── ChatAppPart1.java           (main program: registration, login, menu, sending messages)
│   │   ├── Login.java                  (Part 1: registration and login checks)
│   │   └── Message.java                (Part 2: message ID, hash, checks, send/store, JSON)
│   └── test/java/com/mycompany/chatapp/part1
│       ├── LoginTest.java              (14 unit tests)
│       └── MessageTest.java            (13 unit tests)
├── pom.xml
└── README.md
Setup Instructions
Prerequisites
Java JDK 25 or later
Maven (optional – if using the command line)
NetBeans IDE (recommended)
Git (for version control)
GitHub account (for submission)
Clone the Repository
git clone https://github.com/ST10476385/PROG5121-POE-ChatApp.git
cd PROG5121-POE-ChatApp
Open in NetBeans
Open NetBeans IDE.
Go to File → Open Project.
Navigate to the project folder and select it.
Click Open Project.
How to Run the Application
Option 1: Run from NetBeans
In the Projects tab, expand ChatApp-Part1.
Expand Source Packages → com.mycompany.chatapp.part1.
Right-click ChatAppPart1.java → Run File (or press Shift + F6).
Option 2: Run from Command Line (with Maven)
mvn compile
mvn exec:java -Dexec.mainClass="com.mycompany.chatapp.part1.ChatAppPart1"
Expected Output – Part 1 (Registration and Login)
The application will guide you through registration and login:

=== Registration ===
Enter a username (must contain _ and be 5 characters or less): kyl_1
Enter a password (8+ characters, a capital letter, a number and a special character): Ch&&sec@ke99!
Enter your cell number (with country code, example +27614880723): +27838968976
Enter your first name: John
Enter your last name: Doe
Username successfully captured. Password successfully captured. Cell phone number successfully added.

=== Login ===
Enter your username: kyl_1
Enter your password: Ch&&sec@ke99!
Welcome John, Doe it is great to see you again.


image
Expected Output – Part 2 (Sending Messages)
After a successful login the QuickChat menu starts. The Message IDs are random, so yours will differ:

Welcome to QuickChat.
How many messages do you wish to enter? 3

--- Menu ---
Option 1) Send Messages
Option 2) Show recently sent messages
Option 3) Quit
Choose an option: 1

--- Message 1 of 3 ---
Enter recipient cell number (with international code, example +27718693002): +27718693002
Cell phone number successfully captured.
Enter your message (max 250 characters): Hi Mike, can you join us for dinner tonight?
Message ready to send.
Message ID generated: 7363025905

--- Message Details ---
Message ID: 7363025905
Message Hash: 73:0:HITONIGHT
Recipient: +27718693002
Message: Hi Mike, can you join us for dinner tonight?

What would you like to do?
1) Send Message
2) Disregard Message
3) Store Message to send later
Choose an option: 1
Message successfully sent.
