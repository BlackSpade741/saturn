package utoronto.saturn;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utoronto.saturn.app.GuiManager;

public class UserDatabase extends Database {
    private static final String table = "users";

    /**
     * Deletes a row from users table with userId (email) and eventId (from events table)
     *
     * @param email the user's email in users table
     * @param eventId the eventId in events table
     * @return true on success
     */
    public static boolean leaveEvent(String email, int eventId) {
        return DatabaseUtilities.deleteRowUser(email, eventId);
    }

    /**
     * Adds a row in users to create an entry to associate the user to event
     *
     * @param eventId the eventId in events table
     * @return true on success
     */
    public static boolean joinEvent(int eventId) {
        User user = GuiManager.getInstance().getCurrentUser();
        return DatabaseUtilities.addRowUser(user.getEmail(), user.getUsername(), user.getPassword(), eventId);
    }

    /**
     * Adds a row in users to create an entry with no eventId
     *
     * @return true on success
     */
    public static boolean openAccount(User user) {
        // New account -> EventID == -1
        if(!checkEmailExists(user.getEmail())) {
            return DatabaseUtilities.addRowUser(user.getEmail(), user.getUsername(), user.getPassword(), -1);
        } else {
            return false;
        }
    }

    /**
     * Returns an arrayList of all emails in users table
     *
     * @return ArrayList<String> of all emails in users table
     */
    public static ArrayList<String> getAllEmail() {
        ArrayList<String> lst = new ArrayList<>();
        ResultSet set = DatabaseUtilities.selectColumn("users", "email");
        try {
            while (set.next()) {
                lst.add(set.getString(1));
            }
        }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }

        return lst;
    }

    /**
     * Returns if input email in the users database
     *
     * @return true if input email is in the database
     */
    public static boolean checkEmailExists(String email) {
        ArrayList<String> lst = getAllEmail();
        return lst.contains(email);
    }

    /**
     * Return info about a particular attribute in users table about user
     *
     * @return ResultSet that contains info about an attribute of user
     */
    public static ResultSet getAttribute(String attribute) {
        User user = GuiManager.getInstance().getCurrentUser();
        return getAttribute(user.getEmail(), attribute);
    }

    private static ResultSet getAttribute(String email, String attribute) {
        return DatabaseUtilities.selectRow(table, attribute, "email", email);
    }

    public static boolean checkUserCredentials(String email, String password){
        ResultSet result = DatabaseUtilities.executeQuery("SELECT email FROM " + table + " WHERE email = '" + email + "' AND password = '" + password + "'");
        try {
            if (result != null && result.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String getUsername(String email) {
        ResultSet username = getAttribute(email, "username");
        try {
            if (username.next()) {
                return username.getString("username");
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
}



