package utoronto.saturn;

import android.annotation.SuppressLint;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import utoronto.saturn.app.GuiManager;


public class EventDatabase extends Database {

    private static final String table = "events";

    protected static Logger log = Logger.getLogger(EventDatabase.class.getName());

    EventDatabase() {
        super();
        log.setLevel(Level.FINE);
    }

    /**
     * Add an entry to the database
     *
     * @param creator the creator of the event
     * @param name of the event
     * @param description of the event
     * @param date of the event
     * @param type of the event (anime, movie, concert, or game)
     * @param url picture of the event
     * @param isglobal of the event
     * @return success
     * @throws ParseException If values are not valid
     */
    boolean addEvent(String creator, String name, String description, String date, String type, String url, boolean isglobal) throws ParseException{
        return DatabaseUtilities.addRowEvent(creator, name, description, date, type, url, isglobal);
    }

    /**
     * Delete an entry from the database
     *
     * @param id  of the event in the database
     * @return success
     */
    boolean deleteEvent(int id) {
        return DatabaseUtilities.deleteRow(table, "id", Integer.toString(id));
    }

    /**
     * Get a list of user's events from the database
     *
     * @param email of the user
     * @return List of events of the user
     * *@throws SQLException  if problem arises from executing query
     */
    public static List<Event> getUserFollowedEvents(String email) throws SQLException{
        ResultSet rs = DatabaseUtilities.executeQuery(String.format("SELECT eventID FROM users WHERE email = '%s' AND eventId != -1", email));
        List<Event> result = new ArrayList<>();

        while (rs != null && rs.next()) {
            int id = rs.getInt("eventID");
            Event event = GuiManager.getEvent(id);

            if (event != null){
                result.add(event);
            }
        }
        return result;
    }

    /**
     * Get a list of popular events in the database (by counting how many users added the events)
     *
     * @return List of popular events
     * *@throws SQLException  if problem arises from executing query
     */
    public static List<Event> getPopular() throws SQLException {
        // TODO: this is a hack, improve this by refactoring to use EventManager when necessary
        ResultSet rs = DatabaseUtilities.executeQuery("SELECT eventID, COUNT(*) AS count FROM users GROUP BY eventId ORDER BY count(*) DESC");
        ArrayList<Event> eventList = new ArrayList<>();
        while (rs != null && rs.next() && eventList.size() <= 5) {
            Event popEvent = GuiManager.getEvent(rs.getInt("eventID"));
            if (popEvent != null) {
                eventList.add(popEvent);
            }
        }

        return eventList;
    }

    /**
     * Get a list of trending events in the database (by counting date of the event)
     *
     * @return List of trending events
     * *@throws SQLException  if problem arises from executing query
     */
    public static List<Event> getTrending() throws SQLException {
        ResultSet rs = DatabaseUtilities.executeQuery("SELECT id FROM events ORDER BY date ORDER BY count(*) DESC");
        ArrayList<Event> eventList = new ArrayList<>();
        while (rs != null && rs.next() && eventList.size() <= 5) {
            Event popEvent = GuiManager.getEvent(rs.getInt("eventID"));
            if (popEvent != null) {
                eventList.add(popEvent);
            }
        }
        return eventList;
    }

    /**
     * Get a list of suggested events in the database (by counting by type)
     *
     * @return List of suggested events
     * *@throws SQLException  if problem arises from executing query
     */
    public static List<Event> getSuggested() throws SQLException {
        ResultSet rs = DatabaseUtilities.executeQuery("SELECT type, COUNT(*) FROM events GROUP BY type ORDER BY count(*) DESC");
        String type = "";
        while (rs != null && rs.next()) {
            type = rs.getString(1);
        }
        rs = DatabaseUtilities.selectRow(table, "id", "type", type);
        ArrayList<Event> eventList = new ArrayList<>();
        while (rs != null && rs.next() && eventList.size() <= 5) {
            Event popEvent = GuiManager.getEvent(rs.getInt("id"));
            if (popEvent != null) {
                eventList.add(popEvent);
            }
        }

        return eventList;

    }

    /**
     *  Create an event by the inputted information
     *
     * @param id the id of the event in the database
     * @param creator the creator of the event
     * @param name of the event
     * @param description of the event
     * @param date of the event
     * @param url picture of the event
     * @return Event object with inputted information
     * @throws ParseException, MalformedURLException  for the date
     */
    public static Event createEvent(int id, String name, String date, String url, String description, String creator) throws ParseException, MalformedURLException{
        @SuppressLint("SimpleDateFormat") SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date parseDate = f.parse(date);
        long milliseconds = parseDate.getTime();
        URL u = new URL(url);
        return createEvent(id, name, milliseconds, u, description, creator);
    }

    /**
     *  Create an event by the inputted information
     *
     * @param id the id of the event in the database
     * @param creator the creator of the event
     * @param name of the event
     * @param description of the event
     * @param date of the event
     * @param url picture of the event
     * @return Event object with inputted information
     */
    public static Event createEvent(int id, String name, long date, URL url, String description, String creator) {
        return new Event(id, name, url, date, creator, description);
    }
}
