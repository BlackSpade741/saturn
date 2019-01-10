package utoronto.saturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import utoronto.saturn.Event;

public class User {

    private String username;
    private String email;
    private String password;
    private List<User> followedCreatorList;
    private List<Event> events;
    private Set<Event> localEvents;
    private Set<Event> globalEvents;
    private Set<Event> interestedEvents;
    private Set<Event> attendingEvents;

    public User(String username, String email, String password) {

        // Zero Lengths
        if (username.length() == 0 || email.length() == 0 || password.length() == 0) {
            throw new IllegalArgumentException("You passed an Argument of invalid length!");
        }
        // No @ in email
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Emails must contain @!");
        }

        // ID
        this.username = username;
        this.email = email;
        this.password = password;
        this.followedCreatorList = new ArrayList<User>();
        this.events = new ArrayList<Event>();
    }

    public void addToFollowedCreators(User creator) {
        // Already Following Creator
        if (followedCreatorList.contains(creator)) {
            throw new IllegalStateException("Already following that creator!");
        }

        this.followedCreatorList.add(creator);
    }

    public List<User> getFollowedCreatorList() {
        return this.followedCreatorList;
    }

    public void followCreator(User creator) {
        this.addToFollowedCreators(creator);
    }

    public List<Event> getAllEvents() {
        return this.events;
    }

    // Returns name in format first:last
    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(followedCreatorList, user.followedCreatorList) &&
                Objects.equals(events, user.events) &&
                Objects.equals(localEvents, user.localEvents) &&
                Objects.equals(globalEvents, user.globalEvents) &&
                Objects.equals(interestedEvents, user.interestedEvents) &&
                Objects.equals(attendingEvents, user.attendingEvents);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, email, password, followedCreatorList, events, localEvents, globalEvents, interestedEvents, attendingEvents);
    }

}
