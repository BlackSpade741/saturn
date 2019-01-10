package utoronto.saturn.app.front_end.viewmodels;

import android.arch.lifecycle.ViewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utoronto.saturn.*;
import utoronto.saturn.app.GuiManager;

public class LoginViewModel extends ViewModel{
    public boolean checkLogin(String email, String password) {
        GuiManager guiManager = GuiManager.getInstance();

        // If the input is valid
        return guiManager.logIn(email, password);
    }

    private void reinitializeUser(User user) {

    }

}
