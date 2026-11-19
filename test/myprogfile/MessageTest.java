/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package myprogfile;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for Part 2 message behavior and Part 3 arrays and operations. */
public class MessageTest {

    @BeforeEach
    public void setUp() {
        Message.resetAllMessageData();
    }

    private Message createMessage(String id, int number, String recipient,
                                  String sender, String text) {
        return new Message(id, number, recipient, sender, text);
    }

    @Test
    public void sentDisregardedAndStoredMessagesGoIntoSeparateArrays() {
        Message sent = createMessage("1111111111", 1, "+27834557896",
                "0838884567", "Did you get the cake?");
        Message disregarded = createMessage("3333333333", 3, "+27834484567",
                "0838884567", "Yohoooo, I am at your gate.");
        Message stored = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Where are you? You are late! I have asked you to be on time.");

        assertEquals("Message successfully sent.", sent.sentMessage(Message.SENT));
        assertEquals("Message disregarded.", disregarded.sentMessage(Message.DISREGARDED));
        assertEquals("Message successfully stored.", stored.sentMessage(Message.STORED));

        assertEquals(1, Message.getSentMessages().length);
        assertEquals(1, Message.getDisregardedMessages().length);
        assertEquals(1, Message.getStoredMessages().length);
        assertEquals(3, Message.getMessageIDs().length);
        assertEquals(3, Message.getMessageHashes().length);
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void findsLongestStoredMessage() {
        Message longest = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Where are you? You are late! I have asked you to be on time.");
        Message shorter = createMessage("5555555555", 5, "+27838884567",
                "0838884567", "Ok, I am leaving without you.");

        longest.sentMessage(Message.STORED);
        shorter.sentMessage(Message.STORED);

        assertSame(longest, Message.findLongestStoredMessage());
    }

    @Test
    public void searchesMessageGroupsByID() {
        Message sent = createMessage("1111111111", 1, "+27834557896",
                "0838884567", "Did you get the cake?");
        Message disregarded = createMessage("3333333333", 3, "+27834484567",
                "0838884567", "Yohoooo, I am at your gate.");
        Message stored = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Where are you?");

        sent.sentMessage(Message.SENT);
        disregarded.sentMessage(Message.DISREGARDED);
        stored.sentMessage(Message.STORED);

        assertSame(sent, Message.searchByMessageID("1111111111"));
        assertSame(disregarded, Message.searchByMessageID("3333333333"));
        assertSame(stored, Message.searchByMessageID("2222222222"));
        assertNull(Message.searchByMessageID("bad-id"));
    }

    @Test
    public void recipientSearchLooksInStoredMessages() {
        Message first = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Where are you?");
        Message second = createMessage("5555555555", 5, "+27838884567",
                "0838884567", "Ok, I am leaving without you.");
        Message other = createMessage("1111111111", 1, "+27834557896",
                "0838884567", "Did you get the cake?");

        first.sentMessage(Message.STORED);
        second.sentMessage(Message.STORED);
        other.sentMessage(Message.SENT);

        assertEquals(2, Message.findMessagesByRecipient("+27838884567").length);
        assertEquals(0, Message.findMessagesByRecipient("12345").length);
    }

    @Test
    public void combinedRecipientSearchReturnsSentAndStoredMessages() {
        Message sent = createMessage("1111111111", 1, "+27838884567",
                "0838884567", "Can you bring the orange folder?");
        Message stored = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Meet me at four.");

        sent.sentMessage(Message.SENT);
        stored.sentMessage(Message.STORED);

        assertEquals(2,
                Message.findMessagesByRecipientSentAndStored("+27838884567").length);
    }

    @Test
    public void deletesStoredMessageByHashOnly() {
        Message stored = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Where are you?");
        Message sent = createMessage("1111111111", 1, "+27834557896",
                "0838884567", "Did you get the cake?");

        stored.sentMessage(Message.STORED);
        sent.sentMessage(Message.SENT);

        assertTrue(Message.deleteMessageByHash(stored.getMessageHash()));
        assertEquals(0, Message.getStoredMessages().length);
        assertNull(Message.searchByMessageID("2222222222"));
        assertNotNull(Message.searchByMessageID("1111111111"));
        assertFalse(Message.deleteMessageByHash("unknown-hash"));
    }

    @Test
    public void readsStoredMessagesFromJSON() throws Exception {
        Path jsonFile = Files.createTempFile("quickchat-messages", ".json");
        String previousFile = "messages.json";

        try {
            Message.setStorageFile(jsonFile.toString());

            Message stored = createMessage("2222222222", 2, "+27838884567",
                    "0838884567", "Stored test message");
            stored.sentMessage(Message.STORED);

            Message.resetAllMessageData();
            Message[] loaded = Message.readStoredMessages(jsonFile.toString());

            assertEquals(1, loaded.length);
            assertEquals("Stored test message", loaded[0].getMessageText());
            assertEquals("0838884567", loaded[0].getSender());
            assertEquals(1, Message.getStoredMessages().length);
        } finally {
            Message.setStorageFile(previousFile);
            Files.deleteIfExists(jsonFile);
        }
    }

    @Test
    public void reportIncludesFullStoredMessageDetails() {
        Message stored = createMessage("2222222222", 2, "+27838884567",
                "0838884567", "Where are you? You are late!");

        stored.sentMessage(Message.STORED);
        String report = Message.displayMessageReport();

        assertTrue(report.contains("2222222222"));
        assertTrue(report.contains(stored.getMessageHash()));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains("Where are you? You are late!"));
        assertTrue(report.contains("Status: Stored"));
    }

    @Test
    public void validatesPartTwoMessageData() {
        Message message = createMessage("1234567890", 0, "+27834557896",
                "0838884567", "Hi there");

        assertTrue(message.checkMessageID());
        assertEquals("Message ready to send.",
                Message.checkMessageLength("short"));
        assertEquals("Message ID generated: 1234567890",
                message.displayMessageID());
        assertTrue(message.getMessageHash().startsWith("12:0:"));
    }
}
