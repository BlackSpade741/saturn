package utoronto.saturn.app.front_end.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


import utoronto.saturn.app.GuiManager;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        button = findViewById(R.id.login_activity_button);
        button.setOnClickListener(this::onButtonClickAction);
    }

    private void onButtonClickAction(View v) {

        AutoCompleteTextView usernameView = findViewById(R.id.txt_username);
        Editable username = usernameView.getText();
        TextInputEditText passwordView = findViewById(R.id.txt_password);
        Editable password = passwordView.getText();

        // If the user does not fill in any of the textboxes prompt an error for 2 sec
        if (username.toString().equals("") || password.toString().equals("")) {
            removeKeyboard();
            Snackbar error_message = Snackbar.make(v , "Please fill in all text boxes.",
                    2000);
            error_message.show();
            return;
        }

        boolean res = GuiManager.getInstance().logIn(username.toString(), password.toString());

        // If the user is not found then prompt user to enter a correct email and password
        if (!res) {
            removeKeyboard();
            Snackbar error_message = Snackbar.make(v , "Invalid login. Please try again.",
                    2000);
            error_message.show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Removes the keyboard to display the error message
    private void removeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
