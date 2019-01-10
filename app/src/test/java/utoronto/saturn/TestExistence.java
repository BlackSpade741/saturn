package utoronto.saturn;

import org.junit.Assert;
import org.junit.Test;

public class TestExistence {
    private static final String PATH = "utoronto.saturn.";

    private void assertClassExists(String path) {
        try {
            Class.forName(path);
        } catch (ClassNotFoundException e) {
            Assert.fail(path + " does not exist!");
        }
    }

    // Ensure Event Class Exists
    @Test
    public void checkEventExists() {
        assertClassExists(PATH + "Event");
    }

    // Ensure User Class Exists
    @Test
    public void checkUserExists() {
        assertClassExists(PATH + "User");
    }

    // Ensure Eventmanager Class Exists
    @Test
    public void checkEventManagerExists() {
        assertClassExists(PATH + "EventManager");
    }

    // Ensure LocalEventManager Class Exists
    @Test
    public void checkLocalEventManagerExists() {
        assertClassExists(PATH + "LocalEventManager");
    }

    // Ensure GlobalEventManager Class Exists
    @Test
    public void checkGlobalEventManagerExists() {
        assertClassExists(PATH + "GlobalEventManager");
    }

    // Ensure Database Class Exists
    @Test
    public void checkDatabaseClassExists() {
        assertClassExists(PATH + "Database");
    }

    // Ensure EventDatabase Class Exists
    @Test
    public void checkEventDatabaseClassExists() {
        assertClassExists(PATH + "EventDatabase");
    }

    // Ensure UserDatabase Class Exists
    @Test
    public void checkUserDatabaseClassExists() {
        assertClassExists(PATH + "UserDatabase");
    }

    // Ensure DatabaseUtilities Class Exists
    @Test
    public void checkDatabaseUtilitiesClassExists() {
        assertClassExists(PATH + "DatabaseUtilities");
    }
}
