package utoronto.saturn;

import android.os.Build;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class Event {
    private int ID;
    private String name;
    private String artist;
    private String description;
    private URL imageURL;
    private long releaseDate;


    Event(int ID, String name, URL imageURL, long releaseDate, String artist, String description) {
        if(name.replaceAll(" ", "").length() == 0) {
            throw new IllegalArgumentException("Name is empty!");
        }

        this.ID = ID;
        this.name = name;
        this.imageURL = imageURL;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public URL getImageURL() {
        return this.imageURL;
    }

    public long getReleaseDate() {
        return this.releaseDate;
    }

    public String getDescription(){
        return this.description;
    }

    public String getArtist(){
        return this.artist;
    }

    /*
        Checks if event is released
     */
    private boolean isReleased() {
        return this.releaseDate < System.currentTimeMillis();
    }

    /*
        Time till release date in system time
     */
    public long timeTillRelease() {
        if(isReleased()) {
            return 0;
        } else {
            // Add a func to turn this into readable time at some point...
            return this.releaseDate - System.currentTimeMillis();
        }
    }

    /*
    Get date by converting milliseconds to month and day.
    */
    public String getShortDate(){
        long milliseconds = releaseDate;
        int month, day;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.of("EST"));
            month = dateTime.getMonth().getValue();
            day = dateTime.getDayOfMonth();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        switch(month) {
            case 1:
                return "Jan " + String.valueOf(day);
            case 2:
                return "Feb " + String.valueOf(day);
            case 3:
                return "Mar " + String.valueOf(day);
            case 4:
                return "Apr " + String.valueOf(day);
            case 5:
                return "May " + String.valueOf(day);
            case 6:
                return "Jun " + String.valueOf(day);
            case 7:
                return "Jul " + String.valueOf(day);
            case 8:
                return "Aug " + String.valueOf(day);
            case 9:
                return "Sep " + String.valueOf(day);
            case 10:
                return "Oct " + String.valueOf(day);
            case 11:
                return "Nov " + String.valueOf(day);
            case 12:
                return "Dec " + String.valueOf(day);
        }

        return String.valueOf(month) + " " + String.valueOf(day);
    }

    /*
    Get date by converting milliseconds to month, day, and year.
    */
    public String getLongDate(){
        long milliseconds = releaseDate;
        int month, day, year;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.of("EST"));
            month = dateTime.getMonth().getValue();
            day = dateTime.getDayOfMonth();
            year = dateTime.getYear();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        switch(month) {
            case 1:
                return "January " + String.valueOf(day);
            case 2:
                return "February " + String.valueOf(day);
            case 3:
                return "March " + String.valueOf(day);
            case 4:
                return "April " + String.valueOf(day);
            case 5:
                return "May " + String.valueOf(day);
            case 6:
                return "June " + String.valueOf(day);
            case 7:
                return "July " + String.valueOf(day);
            case 8:
                return "August " + String.valueOf(day);
            case 9:
                return "September " + String.valueOf(day);
            case 10:
                return "October " + String.valueOf(day);
            case 11:
                return "November " + String.valueOf(day);
            case 12:
                return "December " + String.valueOf(day);
        }
        return "";
    }
}