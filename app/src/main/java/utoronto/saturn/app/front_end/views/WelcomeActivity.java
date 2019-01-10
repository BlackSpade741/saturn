package utoronto.saturn.app.front_end.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.viewmodels.WelcomeViewModel;

public class WelcomeActivity extends AppCompatActivity {
    private WelcomeViewModel mViewModel;
    private Button loginButton;
    private Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mViewModel = ViewModelProviders.of(this).get(WelcomeViewModel.class);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this::onLoginButtonClickAction);

        signupButton = findViewById(R.id.signUpButton);
        signupButton.setOnClickListener(this::onSignupButtonClickAction);
    }

    private void onLoginButtonClickAction(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void onSignupButtonClickAction(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
