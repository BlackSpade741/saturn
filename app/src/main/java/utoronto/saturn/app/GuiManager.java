package utoronto.saturn.app;

import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import utoronto.saturn.Event;
import utoronto.saturn.EventDatabase;
import utoronto.saturn.User;
import utoronto.saturn.UserDatabase;

public class GuiManager {
    
    /*
    * This class handles the connection between the front end and the back end.
    * A singleton class accessed by the front end to query for info, etc.
    */
    private static GuiManager instance = new GuiManager();
    private User currentUser;
    private static final String[] categories = new String[]{"Anime", "Concerts", "Movies", "Games"};
    private static String[] types;
    private static Map<String, List<Event>> allEvents;
    private static final Logger log = Logger.getLogger(GuiManager.class.getName());
    private static List<LoadingListener> listeners = new ArrayList<>();
    private static List<Event> userEvents;
    private static boolean userEventsDirty = true;

    private GuiManager() {
        allEvents = new HashMap<>();
        types = new String[]{"anime", "concert", "movie", "game"};
    }

    // Use this to get the current instance of this class
    public static GuiManager getInstance() {
        return instance;
    }

    public static void getAllEventsFromDb(){
        if (!allEvents.keySet().isEmpty()) return;
        notifyLoadingStarted();
        for (String type : types) {
            BackgroundQuery query = new BackgroundQuery();
            // query.execute(type);
            query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, type);
        }
    }

    public static Map<String, List<Event>> getAllEventsInTypes(){
        return allEvents;
    }

    public static Set<Event> getAllEvents(){
        Set<Event> res = new HashSet<>();
        for (String type : types) {
            List<Event> events = allEvents.get(type);
            if (events != null)
                res.addAll(events);
        }
        return res;
    }

    // Return event if it exists in database(which it should)
    public static Event getEvent(int eventId){
        Set<Event> events = getAllEvents();
        for (Event event : events) {
            if (event.getID() == eventId)
                return event;
        }
        return null;
    }

    /*
        Checks to see if the given email, is already taken
        Returns true if the sign up is successful and false otherwise
     */
    public boolean signUp(String username, String email, String password) {
        // Create the new user account and add it to the users database
        User user = new User(username, email, password);
        boolean res = UserDatabase.openAccount(user);
        if (res) {
            setCurrentUser(user);
            return true;
        }
        return false;
    }

    /*
        Checks to see if the given email, and password are valid
        Returns true if the login is successful and false otherwise
     */
    public boolean logIn(String email, String password) {
        // Get the resulting relations after selecting email
        String username;
        if (UserDatabase.checkUserCredentials(email, password) && (username = UserDatabase.getUsername(email)) != null) {
            setCurrentUser(new User(username, email, password));
            return true;
        }
        return false;
    }

    // get event functions
    public List<Event> getEventsByCategory(String category) {
        return allEvents.get(category);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(User user) {
        currentUser = user;
        userEvents = getUserFollowedEventsFromDb();
    }

    /* 
    * Get suggestions based on the current users preferences.
    */
    public List<Event> getSuggested() {
        try {
            return EventDatabase.getSuggested();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Event> getPopular() {
        try {
            return EventDatabase.getPopular();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Event> getTrending() {
        try {
            return EventDatabase.getTrending();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String[] getCategories() {
        return categories;
    }

    private List<Event> getUserFollowedEventsFromDb() {
        try {
            return EventDatabase.getUserFollowedEvents(currentUser.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> getUserFollowedEvents() {
        return userEvents;
    }

    /*
    * Handles joining event with current user in backend.
    */
    public boolean joinEvent(Event event) {
        if (UserDatabase.joinEvent(event.getID())) {
            userEvents.add(event);
            userEventsDirty = true;
            return true;
        }
        return false;
    }

    /*
    * Handles leaving event with current user in backend.
    */
    public boolean leaveEvent(Event event) {
        if (UserDatabase.leaveEvent(currentUser.getEmail(), event.getID())) {
            userEvents.remove(event);
            userEventsDirty = true;
            return true;
        }
        return false;
    }

    public static void addListener(LoadingListener listener) {
        listeners.add(listener);
    }
    
    public static void removeListener(LoadingListener listener) {
        listeners.remove(listener);
    }

    /* 
    * Notifies listeners that event loading has begun.
    */
    private static void notifyLoadingStarted() {
        for (LoadingListener listener : listeners) {
            listener.notifyLoadingStarted();
        }
    }

    /*
    * Tells the UI to move past loading screen.
    */
    private static void notifyLoadingFinished() {
        for (LoadingListener listener : listeners) {
            log.info("Notifying listener");
            listener.notifyLoadingFinished();
        }
    }

    /*
    * Helper class for fetching data upon app launch.
    */
    private static class BackgroundQuery extends AsyncTask<String, Void, ResultSet> {
        Connection conn;
        PreparedStatement st;
        String type;

        private static final String URL = "placeholder";
        private static final String USERNAME = "placeholder";
        private static final String PASSWORD = "placeholder";

        /*
        * Fetch events in background and unsure duplicate events are not present.
        */
        @Override
        protected ResultSet doInBackground(String... strings) {
            ResultSet result;
            try {
                Class.forName("org.postgresql.Driver");
                //STEP 3: Open a connection
                Log.d("database", "Connecting to database...");
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                conn.setAutoCommit(false);
                type = strings[0];
                st = conn.prepareStatement(String.format("SELECT id, name, date, url, description, creator FROM events WHERE type = '%s' ORDER BY date", type), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                result = st.executeQuery();
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                throw new IllegalStateException("Invalid Query!");
            }

            return result;
        }

        /*
        * Adds events from database into users application.
        */
        @Override
        protected void onPostExecute(ResultSet result) {
            log.info("onPostExecute");
            List<Event> events = allEvents.get(type);
            if (events == null) {
                allEvents.put(type, new ArrayList<>());
                events = allEvents.get(type);
            }
            long currentTime = System.currentTimeMillis();
            try {
                while (result != null && result.next()) {
                    assert events != null;
                    Log.d("database", "Fetching next...");
                    int id = result.getInt("ID");
                    String name = result.getString("name");
                    String date = result.getString("date");
                    String url = result.getString("url");
                    String description = result.getString("description");
                    String creator = result.getString("creator");

                    Event newEvent = EventDatabase.createEvent(id, name, date, url, description, creator);
                    events.add(newEvent);
                }
            } catch (SQLException | ParseException | MalformedURLException e) {
                e.printStackTrace();
            }
            notifyLoadingFinished();
        }
    }


}
