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

import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.viewmodels.SignupViewModel;

public class SignupActivity extends AppCompatActivity {
    private SignupViewModel myViewModel;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("before set Content View");
        setContentView(R.layout.activity_signup);
        System.out.println("set Content View");
        myViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        button = findViewById(R.id.signup_activity_button);
        button.setOnClickListener(this::onButtonClickAction);
        System.out.println("on create");
    }

    private void onButtonClickAction(View v) {

        AutoCompleteTextView usernameView = findViewById(R.id.txt_username);
        Editable username = usernameView.getText();
        AutoCompleteTextView emailView = findViewById(R.id.txt_email);
        Editable email = emailView.getText();
        TextInputEditText passwordView = findViewById(R.id.txt_password);
        Editable password = passwordView.getText();

        // If the user does not fill in any of the textboxes prompt an error for 2 sec
        if (username.toString().equals("") || password.toString().equals("") || email.toString().equals("")) {
            removeKeyboard();
            Snackbar error_message = Snackbar.make(v , "Please fill in all text boxes.",
                    2000);
            error_message.show();
            return;
        }

        // Output a message if the username exists is not found
        if (!myViewModel.signUp(username.toString(), email.toString(), password.toString())) {

            removeKeyboard();
            Snackbar error_message = Snackbar.make(v , "Email already exists. Please try again.",
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
