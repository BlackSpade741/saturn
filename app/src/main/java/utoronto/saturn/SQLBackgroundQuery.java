package utoronto.saturn;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLBackgroundQuery extends AsyncTask<String, Void, ResultSet> {
    Connection conn;
    PreparedStatement st;
    private static final String URL = "placeholder";
    private static final String USERNAME = "placeholder";
    private static final String PASSWORD = "placeholder";

    public SQLBackgroundQuery() {
        super();
    }

    @Override
    protected ResultSet doInBackground(String... strings) {
        // check if username is good
        try {
            Class.forName("org.postgresql.Driver");
            //STEP 3: Open a connection
            Log.d("myTag", "Connecting to database...");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            st = conn.prepareStatement(strings[0], ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet s = st.executeQuery();
            conn.close();
            return s;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Invalid Query!");
        }
    }
}
