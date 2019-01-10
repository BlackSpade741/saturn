package utoronto.saturn;

import android.annotation.SuppressLint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class DatabaseUtilities extends Database {

    private final static ArrayList<String> tables = new ArrayList<String>(Arrays.asList("events", "users"));
    private final static ArrayList<String> eventsValues = new ArrayList<String>(Arrays.asList("id", "creator", "name", "description", "date", "type", "url", "isglobal", "*"));
    private final static ArrayList<String> usersValues = new ArrayList<String>(Arrays.asList("email", "username", "password", "eventid", "*"));
    private final static String eventsColumn = "(id, creator, name, description, date, type, url, isglobal)";
    private final static String usersColumn = "(email, username, password, eventid)";



    public DatabaseUtilities() {
        super();
    }

    /**
     * Deletes a row from table with Column equal to Value
     *
     * @param table  Table in database
     * @param Column Column in database
     * @param Value  Row in column in database
     * @return Return whether the query successfully executed
     */
    static boolean deleteRow(String table, String Column, String Value) throws IllegalArgumentException {
        if (!tables.contains(table)) {
            throw new IllegalArgumentException("Table is not valid!");
        }

        String st = "DELETE FROM " + table + " WHERE " + Column + "=" + Value;
        SQLBackgroundUpdate sbu = new SQLBackgroundUpdate();
        sbu.execute(st);

        // TODO repair this by removing the return
        return true;
    }

    static boolean deleteRowUser(String email, int eventid) {
        String st = String.format(Locale.getDefault(), "DELETE FROM users WHERE email = '%s' AND eventid = %d", email, eventid);
        SQLBackgroundUpdate sbu = new SQLBackgroundUpdate();
        sbu.execute(st);
        return true;
    }

    /**
     * Add a row in users
     *
     * @param email    User's email
     * @param username User's username
     * @param password User's password
     * @param eventid  A user event's ID
     * @return Return whether the query successfully executed
     */
    static boolean addRowUser(String email, String username, String password, int eventid) {
        // check whether the event has already been added
        String s = String.format(Locale.getDefault(), "SELECT * FROM users WHERE email = '%s' AND eventid = %d", email, eventid);
        SQLBackgroundQuery sbq = new SQLBackgroundQuery();
        sbq.execute(s);
        try {
            ResultSet res = sbq.get();
            if (res.next()) return false;
        } catch (ExecutionException | InterruptedException | SQLException e) {
            e.printStackTrace();
            return false;
        }

        // add if not
        String st = String.format(Locale.getDefault(), "INSERT INTO users %s VALUES ('%s', '%s', '%s', %d)", usersColumn, email, username, password, eventid);

        SQLBackgroundUpdate sbu = new SQLBackgroundUpdate();
        sbu.execute(st);

        return true;
    }

    /**
     * Add a row in events
     *
     * @param creator     Creator of the event
     * @param name        Name of event
     * @param description Description of event
     * @param type        Type of event ie. Anime
     * @param url         Image url
     * @return Return whether the query successfully executed
     */
    static boolean addRowEvent(String creator, String name, String description, String date, String type, String url, boolean isglobal) throws ParseException {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date properDate = dateFormat.parse(date);

        String st = "INSERT INTO events " + eventsColumn +
                " VALUES (NEXTVAL('event_id'), '" + creator + "','" + name + "', '" + description + "', '" + properDate + "', '" + type + "', '" + url + "', '" + isglobal + "')";
        SQLBackgroundUpdate sbu = new SQLBackgroundUpdate();
        sbu.execute(st);

        // TODO repair this by removing the return
        return true;
    }

    /**
     * Add a column in table
     *
     * @param table     Table to add column to
     * @param valueName Name of column
     * @param valueType Type of column
     * @return Return whether the query successfully executed
     */
    private static boolean addColumn(String table, String valueName, String valueType) {

        String st = "ALTER TABLE " + table + " ADD " + valueName + " " + valueType;
        SQLBackgroundUpdate sbu = new SQLBackgroundUpdate();
        sbu.execute(st);

        // TODO repair this by removing the return
        return true;
    }

    /**
     * Remove a column in table
     *
     * @param table     Table to add column to
     * @param valueName Name of column
     * @return Return whether the query successfully executed
     */
    private static boolean removeColumn(String table, String valueName) {

        String st = "ALTER TABLE " + table + " DROP " + valueName;
        SQLBackgroundUpdate sbu = new SQLBackgroundUpdate();
        sbu.execute(st);

        // TODO repair this by removing the return
        return true;
    }

    /**
     * Return the result of executing query on the database
     * @param query A *****VALID***** query
     * @return Result of query
     */
    static ResultSet executeQuery(String query){
        try {
            SQLBackgroundQuery sbq = new SQLBackgroundQuery();
            sbq.execute(query);
            return sbq.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the query after selecting a column
     *
     * @param table  Table to select from
     * @param column Column to select
     * @return Query of selection
     * @throws IllegalArgumentException If values are not valid
     */
    static ResultSet selectColumn(String table, String column) throws IllegalArgumentException {
        if (!tables.contains(table)) {
            throw new IllegalArgumentException("Table is not valid!");
        } else if ((table.equals("events") && !eventsValues.contains(column)) || (table.equals("users") && !usersValues.contains(column))) {
            throw new IllegalArgumentException("Column is not valid!");
        }

        try {
            SQLBackgroundQuery sbq = new SQLBackgroundQuery();
            sbq.execute("SELECT " + column + " FROM " + table);
            return sbq.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the query after selecting columns
     *
     * @param table   Table to select from
     * @param columns Columns to select
     * @return Query of selection
     * @throws IllegalArgumentException If values are not valid
     */
    private static ResultSet selectColumns(String table, List<String> columns) throws IllegalArgumentException {
        if (!tables.contains(table)) {
            throw new IllegalArgumentException("Table is not valid!");
        } else if (table.equals("events")) {
            for (String column : columns) {
                if (!eventsValues.contains(column)) {
                    throw new IllegalArgumentException("Column " + column + " is not valid!");
                }
            }
        } else if (table.equals("users")) {
            for (String column : columns) {
                if (!usersValues.contains(column)) {
                    throw new IllegalArgumentException("Column " + column + " is not valid!");
                }
            }
        }

        try {
            StringBuilder query = new StringBuilder("SELECT ");
            for (String column : columns) {
                query.append(column).append(", ");
            }
            query = new StringBuilder(query.substring(0, query.length() - 2) + " FROM " + table);
            SQLBackgroundQuery sbq = new SQLBackgroundQuery();
            sbq.execute(query.toString());
            return sbq.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the query after selecting a column
     *
     * @param table  Table to select from
     * @param column Column to select from
     * @param value  Value to select in the column
     * @return Query of selection
     * @throws IllegalArgumentException If values are not valid
     */
    static ResultSet selectRow(String table, String column, String conditionColumn, String value) throws IllegalArgumentException {
        if (!tables.contains(table)) {
            throw new IllegalArgumentException("Table is not valid!");
        } else if ((table.equals("events") && !eventsValues.contains(column) && !eventsValues.contains(conditionColumn)) || (table.equals("users") && !usersValues.contains(column) && !usersValues.contains(conditionColumn))) {
            throw new IllegalArgumentException("Column is not valid!");
        } else if (value.contains(" ")) {
            throw new IllegalArgumentException("Value is trying to exploit SQL query!");
        }

        try {
            SQLBackgroundQuery sbq = new SQLBackgroundQuery();
            sbq.execute("SELECT " + column + " FROM " + table + " WHERE " + conditionColumn + "= '" + value + "'");
            return sbq.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    static ResultSet selectRows(String table, List<String> columns, String conditionColumn, String value) throws IllegalArgumentException {
        if (!tables.contains(table)) {
            throw new IllegalArgumentException("Table is not valid!");
        } else if (table.equals("events")) {
            for (String column : columns) {
                if (!eventsValues.contains(column)) {
                    throw new IllegalArgumentException("Column " + column + " is not valid!");
                }
            }
            if (!eventsValues.contains(conditionColumn)) {
                throw new IllegalArgumentException("Column " + conditionColumn + " is not valid!");
            }
        } else if (table.equals("users")) {
            for (String column : columns) {
                if (!usersValues.contains(column)) {
                    throw new IllegalArgumentException("Column " + column + " is not valid!");
                }
            }
            if (!usersValues.contains(conditionColumn)) {
                throw new IllegalArgumentException("Column " + conditionColumn + " is not valid!");
            }
        } else if (value.contains(" ")) {
            throw new IllegalArgumentException("Value is trying to exploit SQL query!");
        }

        try {
            StringBuilder query = new StringBuilder("SELECT ");
            for (String column : columns) {
                query.append(column).append(", ");
            }
            query = new StringBuilder(query.substring(0, query.length() - 2) + " FROM " + table + " WHERE " + conditionColumn + "=" + value);
            SQLBackgroundQuery sbq = new SQLBackgroundQuery();
            sbq.execute(query.toString());
            return sbq.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prints out the entirety of the table
     *
     * @param table Table to print
     * @param rows  Number of rows to print
     */
    private static void printTable(String table, int rows) throws IllegalArgumentException {
        if (!tables.contains(table)) {
            throw new IllegalArgumentException("Table is not valid!");
        }

        // Cycle through everything from select column
        ResultSet set = selectColumn(table, "*");
        if (set == null)
            return;

        ResultSetMetaData metaSet;

        try {
            metaSet = set.getMetaData();

            ArrayList<String> columns = new ArrayList<>();

            for (int column = 1; column <= metaSet.getColumnCount(); column++) {
                columns.add(metaSet.getColumnLabel(column));
            }

            String rowString;
            while (set.next() && rows != 0) {
                rowString = "";
                for (int i = 0; i < columns.size(); i++) {
                    rowString += columns.get(i) + ": " + set.getString(columns.get(i)) + ", ";
                }

                System.out.println(rowString.substring(0, rowString.length() - 2));
                rows -= 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String a[]) {
        printTable("users", 5);
        ResultSet set = DatabaseUtilities.selectColumn("users", "email");
                try {
            while (set.next()) {
                System.out.print(set.getString(1));
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}