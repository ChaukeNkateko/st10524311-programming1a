/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myprogfile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Represents one QuickChat message and manages the arrays required for Part 3.
 */
public class Message {

    public static final int SENT = 1;
    public static final int DISREGARDED = 2;
    public static final int STORED = 3;

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String sender;
    private String messageText;
    private String messageHash;
    private int status;

    // The five arrays required for Part 3.
    private static Message[] sentMessages = new Message[0];
    private static Message[] disregardedMessages = new Message[0];
    private static Message[] storedMessages = new Message[0];
    private static String[] messageIDs = new String[0];
    private static String[] messageHashes = new String[0];

    private static int totalMessagesSent;
    private static String storageFile = "messages.json";

    /** Constructor used by the existing Part 2 code. */
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, "", messageText);
    }

    /** Constructor that accepts a sender as well as a recipient. */
    public Message(int messageNumber, String recipient, String sender, String messageText) {
        this(generateMessageID(), messageNumber, recipient, sender, messageText);
    }

    /** Constructor with a fixed ID, useful for tests and JSON loading. */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this(messageID, messageNumber, recipient, "", messageText);
    }

    /** Constructor with fixed values for tests or messages loaded from JSON. */
    public Message(String messageID, int messageNumber, String recipient,
                   String sender, String messageText) {
        this.messageID = messageID == null ? "" : messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient;
        this.sender = sender == null ? "" : sender;
        this.messageText = messageText == null ? "" : messageText;
        this.messageHash = createMessageHash();
    }

    /** Creates a random ten-digit ID. */
    public static String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }

        return id.toString();
    }

    /** Lets tests use a temporary JSON file instead of the project's archive. */
    public static void setStorageFile(String fileName) {
        if (fileName != null && !fileName.trim().isEmpty()) {
            storageFile = fileName;
        }
    }

    /** Clears in-memory data. It does not delete the JSON file. */
    public static void resetAllMessageData() {
        sentMessages = new Message[0];
        disregardedMessages = new Message[0];
        storedMessages = new Message[0];
        messageIDs = new String[0];
        messageHashes = new String[0];
        totalMessagesSent = 0;
    }

    /**
     * Populates the arrays from supplied messages.
     * Messages without an outcome are treated as sent.
     */
    public static void populateMessageArray(Message[] newMessages) {
        resetAllMessageData();

        if (newMessages == null) {
            return;
        }

        for (Message message : newMessages) {
            if (message == null) {
                continue;
            }

            if (message.status == DISREGARDED) {
                addToDisregarded(message);
            } else if (message.status == STORED) {
                addToStored(message);
            } else {
                addToSent(message);
            }
        }
    }

    public static Message[] getSentMessages() {
        return sentMessages.clone();
    }

    public static Message[] getDisregardedMessages() {
        return disregardedMessages.clone();
    }

    public static Message[] getStoredMessages() {
        return storedMessages.clone();
    }

    public static String[] getMessageIDs() {
        return messageIDs.clone();
    }

    public static String[] getMessageHashes() {
        return messageHashes.clone();
    }

    /** Returns all active messages from the three message groups. */
    public static Message[] getMessageArray() {
        Message[] all = new Message[0];

        for (Message message : sentMessages) {
            all = appendMessage(all, message);
        }
        for (Message message : disregardedMessages) {
            all = appendMessage(all, message);
        }
        for (Message message : storedMessages) {
            all = appendMessage(all, message);
        }

        return all;
    }

    /** Validates that an ID consists of exactly ten digits. */
    public boolean checkMessageID() {
        return messageID.matches("[0-9]{10}");
    }

    public String displayMessageID() {
        return "Message ID generated: " + messageID;
    }

    /** Checks the 250-character message limit. */
    public static String checkMessageLength(String text) {
        if (text == null) {
            return "Message is required.";
        }

        if (text.length() > 250) {
            int extraCharacters = text.length() - 250;
            return "Message exceeds 250 characters by " + extraCharacters
                    + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    /** Reuses the South African phone-number validation in Login. */
    public String checkRecipientCell() {
        Login login = new Login();

        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /** Creates a hash from the first two ID digits, message number, and first/last words. */
    public String createMessageHash() {
        if (messageID.length() < 2 || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");

        return (messageID.substring(0, 2) + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    /**
     * Records the user's choice:
     * 1 = sent, 2 = disregarded, 3 = stored.
     */
    public String sentMessage(int choice) {
        if (choice == SENT) {
            addToSent(this);
            return "Message successfully sent.";
        }

        if (choice == DISREGARDED) {
            addToDisregarded(this);
            return "Message disregarded.";
        }

        if (choice == STORED) {
            addToStored(this);
            storeMessagesToFile(storageFile);
            return "Message successfully stored.";
        }

        return "Invalid option.";
    }

    private static void addToSent(Message message) {
        if (containsMessage(sentMessages, message)) {
            return;
        }

        message.status = SENT;
        sentMessages = appendMessage(sentMessages, message);
        addIndexes(message);
        totalMessagesSent++;
    }

    private static void addToDisregarded(Message message) {
        if (containsMessage(disregardedMessages, message)) {
            return;
        }

        message.status = DISREGARDED;
        disregardedMessages = appendMessage(disregardedMessages, message);
        addIndexes(message);
    }

    private static void addToStored(Message message) {
        if (containsMessage(storedMessages, message)) {
            return;
        }

        message.status = STORED;
        storedMessages = appendMessage(storedMessages, message);
        addIndexes(message);
    }

    private static boolean containsMessage(Message[] array, Message target) {
        for (Message message : array) {
            if (message == target) {
                return true;
            }
        }
        return false;
    }

    private static void addIndexes(Message message) {
        messageIDs = appendString(messageIDs, message.messageID);
        messageHashes = appendString(messageHashes, message.messageHash);
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /** Keeps the existing Part 2 sent-message output method. */
    public static String printMessages() {
        if (sentMessages.length == 0) {
            return "No sent messages are available.";
        }

        StringBuilder output = new StringBuilder();
        for (Message message : sentMessages) {
            output.append(message.displayMessageDetails()).append("\n---\n");
        }
        return output.toString();
    }

    /** Stored Messages option 1: show sender and recipient for each stored message. */
    public static String printSenderRecipientReport() {
        if (storedMessages.length == 0) {
            return "No stored messages are available.";
        }

        StringBuilder output = new StringBuilder();
        for (Message message : storedMessages) {
            output.append("Sender: ").append(displaySender(message))
                    .append(" | Recipient: ").append(message.recipient)
                    .append('\n');
        }
        return output.toString();
    }

    private static String displaySender(Message message) {
        return message.sender.isEmpty() ? "Unknown" : message.sender;
    }

    /** Stored Messages option 2: find the longest stored message. */
    public static Message findLongestStoredMessage() {
        Message longest = null;

        for (Message message : storedMessages) {
            if (longest == null
                    || message.messageText.length() > longest.messageText.length()) {
                longest = message;
            }
        }

        return longest;
    }

    public static String displayLongestStoredMessage() {
        Message longest = findLongestStoredMessage();

        if (longest == null) {
            return "No stored messages are available.";
        }

        return longest.displayMessageDetails();
    }

    // Compatibility method used in earlier implementation drafts.
    public static Message findLongestMessage() {
        return findLongestStoredMessage();
    }

    public static String displayLongestMessage() {
        return displayLongestStoredMessage();
    }

    /** Stored Messages option 3: search all message groups by ID. */
    public static Message searchByMessageID(String id) {
        if (id == null) {
            return null;
        }

        id = id.trim();

        Message found = findByID(sentMessages, id);
        if (found != null) {
            return found;
        }

        found = findByID(disregardedMessages, id);
        if (found != null) {
            return found;
        }

        return findByID(storedMessages, id);
    }

    public static Message findMessageByID(String id) {
        return searchByMessageID(id);
    }

    private static Message findByID(Message[] array, String id) {
        for (Message message : array) {
            if (message.messageID.equals(id)) {
                return message;
            }
        }
        return null;
    }

    /** Stored Messages option 4: search stored messages only. */
    public static Message[] findMessagesByRecipient(String targetRecipient) {
        return findRecipientIn(targetRecipient, storedMessages);
    }

    /** Stored Messages option 7: search sent and stored messages. */
    public static Message[] findMessagesByRecipientSentAndStored(String targetRecipient) {
        Message[] matches = findRecipientIn(targetRecipient, sentMessages);
        Message[] storedMatches = findRecipientIn(targetRecipient, storedMessages);

        for (Message message : storedMatches) {
            matches = appendMessage(matches, message);
        }

        return matches;
    }

    private static Message[] findRecipientIn(String targetRecipient, Message[] array) {
        Message[] matches = new Message[0];

        if (targetRecipient == null) {
            return matches;
        }

        // Deliberately use an exact match, as requested in the instructions.
        String search = targetRecipient.trim();

        for (Message message : array) {
            if (message.recipient.equals(search)) {
                matches = appendMessage(matches, message);
            }
        }

        return matches;
    }

    public static Message[] searchMessagesByRecipient(String targetRecipient) {
        return findMessagesByRecipient(targetRecipient);
    }

    /** Stored Messages option 5: delete a stored message by its complete hash. */
    public static boolean deleteMessageByHash(String hash) {
        if (hash == null) {
            return false;
        }

        String searchHash = hash.trim();

        for (int i = 0; i < storedMessages.length; i++) {
            Message message = storedMessages[i];

            if (message.messageHash.equalsIgnoreCase(searchHash)) {
                storedMessages = removeMessageAt(storedMessages, i);
                messageIDs = removeFirst(messageIDs, message.messageID);
                messageHashes = removeFirst(messageHashes, message.messageHash);
                storeMessagesToFile(storageFile);
                return true;
            }
        }

        return false;
    }

    public static boolean deleteStoredMessageByHash(String hash) {
        return deleteMessageByHash(hash);
    }

    /** Stored Messages option 6: report all stored-message details. */
    public static String displayMessageReport() {
        if (storedMessages.length == 0) {
            return "No stored messages are available.";
        }

        StringBuilder report = new StringBuilder("Full stored message report\n\n");

        for (Message message : storedMessages) {
            report.append(message.displayMessageDetails()).append('\n');
            report.append("----------------------------------------\n");
        }

        return report.toString();
    }

    public static String displayStoredMessageReport() {
        return displayMessageReport();
    }

    /** Writes the stored-message array as a JSON array using JSON-simple. */
    public static void storeMessagesToFile(String fileName) {
        JSONArray jsonMessages = new JSONArray();

        for (Message message : storedMessages) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("MessageID", message.messageID);
            jsonMessage.put("MessageNumber", message.messageNumber);
            jsonMessage.put("Sender", message.sender);
            jsonMessage.put("Recipient", message.recipient);
            jsonMessage.put("Message", message.messageText);
            jsonMessage.put("Status", "Stored");
            jsonMessage.put("MessageHash", message.messageHash);
            jsonMessages.add(jsonMessage);
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(jsonMessages.toJSONString());
        } catch (IOException e) {
            System.out.println("Could not save stored messages: " + e.getMessage());
        }
    }

    /**
     * Loads the JSON archive into the stored-message, ID, and hash arrays.
     * A missing archive is treated as an empty stored-message list.
     */
    public static Message[] readStoredMessages(String fileName) {
        storedMessages = new Message[0];

        File file = new File(fileName);
        if (!file.exists()) {
            return new Message[0];
        }

        try (FileReader reader = new FileReader(file)) {
            Object parsed = new JSONParser().parse(reader);

            if (!(parsed instanceof JSONArray)) {
                System.out.println("Stored message file is not a JSON array.");
                return new Message[0];
            }

            JSONArray jsonMessages = (JSONArray) parsed;

            for (Object item : jsonMessages) {
                if (!(item instanceof JSONObject)) {
                    continue;
                }

                JSONObject jsonMessage = (JSONObject) item;
                String id = getJsonString(jsonMessage, "MessageID");
                String recipient = getJsonString(jsonMessage, "Recipient");
                String text = getJsonString(jsonMessage, "Message");
                String sender = getJsonString(jsonMessage, "Sender");
                int number = getJsonNumber(jsonMessage, "MessageNumber");

                if (id == null || recipient == null || text == null) {
                    continue;
                }

                Message message = new Message(id, number, recipient,
                        sender == null ? "" : sender, text);
                message.status = STORED;
                storedMessages = appendMessage(storedMessages, message);
            }

            rebuildIndexes();
        } catch (IOException | ParseException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }

        return storedMessages.clone();
    }

    private static String getJsonString(JSONObject object, String key) {
        Object value = object.get(key);
        return value == null ? null : value.toString();
    }

    private static int getJsonNumber(JSONObject object, String key) {
        Object value = object.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        try {
            return Integer.parseInt(value == null ? "0" : value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Rebuilds the ordered ID and hash arrays from active message groups. */
    private static void rebuildIndexes() {
        messageIDs = new String[0];
        messageHashes = new String[0];

        for (Message message : sentMessages) {
            addIndexes(message);
        }
        for (Message message : disregardedMessages) {
            addIndexes(message);
        }
        for (Message message : storedMessages) {
            addIndexes(message);
        }
    }

    private static Message[] appendMessage(Message[] array, Message message) {
        Message[] updated = new Message[array.length + 1];
        System.arraycopy(array, 0, updated, 0, array.length);
        updated[array.length] = message;
        return updated;
    }

    private static String[] appendString(String[] array, String value) {
        String[] updated = new String[array.length + 1];
        System.arraycopy(array, 0, updated, 0, array.length);
        updated[array.length] = value;
        return updated;
    }

    private static Message[] removeMessageAt(Message[] array, int index) {
        Message[] updated = new Message[array.length - 1];
        System.arraycopy(array, 0, updated, 0, index);
        System.arraycopy(array, index + 1, updated, index, array.length - index - 1);
        return updated;
    }

    private static String[] removeFirst(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                String[] updated = new String[array.length - 1];
                System.arraycopy(array, 0, updated, 0, i);
                System.arraycopy(array, i + 1, updated, i, array.length - i - 1);
                return updated;
            }
        }
        return array;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String displayMessageDetails() {
        String displayedSender = sender.isEmpty() ? "Unknown" : sender;
        String displayedStatus = status == SENT ? "Sent"
                : status == DISREGARDED ? "Disregarded"
                : status == STORED ? "Stored" : "Not processed";

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText
                + "\nSender: " + displayedSender
                + "\nMessage Number: " + messageNumber
                + "\nStatus: " + displayedStatus;
    }
}
