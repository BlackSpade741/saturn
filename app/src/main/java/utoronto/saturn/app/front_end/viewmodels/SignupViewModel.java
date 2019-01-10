package utoronto.saturn.app.front_end.viewmodels;

import android.arch.lifecycle.ViewModel;

import java.sql.SQLException;

import utoronto.saturn.User;
import utoronto.saturn.UserDatabase;
import utoronto.saturn.app.GuiManager;

public class SignupViewModel extends ViewModel {

    /*
        Checks to see if the entered email already exists. Returns True if email exists and False
        if the email doesn't exists in the database
     */
    public boolean signUp(String username, String email, String password) {
        // Checks to see if user is successful in sign up
        return GuiManager.getInstance().signUp(username, email, password);
    }
}
