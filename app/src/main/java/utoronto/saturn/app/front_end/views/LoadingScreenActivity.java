package utoronto.saturn.app.front_end.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import utoronto.saturn.app.GuiManager;
import utoronto.saturn.app.LoadingListener;
import utoronto.saturn.app.R;

public class LoadingScreenActivity extends AppCompatActivity implements LoadingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        GuiManager.addListener(this);
        GuiManager.getAllEventsFromDb();
    }

    @Override
    public void notifyLoadingStarted() {

    }

    @Override
    public void notifyLoadingFinished() {
        GuiManager.removeListener(this);
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void notifyProgress(double progress) {

    }
}
