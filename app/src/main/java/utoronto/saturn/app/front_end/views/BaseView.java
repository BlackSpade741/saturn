package utoronto.saturn.app.front_end.views;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

import utoronto.saturn.Event;
import utoronto.saturn.User;
import utoronto.saturn.app.AlarmReceiver;
import utoronto.saturn.app.GuiManager;
import utoronto.saturn.app.NotificationScheduler;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.adapters.EventItemFullAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class BaseView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Referenced from https://awsrh.blogspot.com/2017/10/custom-pop-up-window-with-android-studio.html

    public void eventPopUp(Event event) {
        Dialog myDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        System.out.println("clicked pop up");
        myDialog.setContentView(R.layout.event_description_popup);
        TextView close_popup = myDialog.findViewById(R.id.close_event_popup);
        close_popup.setText("X");
        Button register_event = myDialog.findViewById(R.id.register_button);

        boolean registered = false;
        if (GuiManager.getInstance().getUserFollowedEvents().contains(event)) {
            registered = true;
        }

        close_popup.setOnClickListener(v -> {
            // Closes the pop up if the user clicks the X
            myDialog.dismiss();
        });

        if (registered) {
            register_event.setText("Remove");
            register_event.setOnClickListener(v -> {
                boolean res = GuiManager.getInstance().leaveEvent(event);
                if (res) {
                    Snackbar message = Snackbar.make(v, "Event removed successfully!", 2000);
                    message.show();
                } else {
                    Snackbar message = Snackbar.make(v, "Cannot remove event!", 2000);
                    message.show();
                }
            });
        } else {
            register_event.setOnClickListener(v -> {
                boolean res = GuiManager.getInstance().joinEvent(event);
                if (res) {
                    // Set up the notification;
                    String event_name = event.getName();
                    NotificationScheduler.setReminder(BaseView.this, AlarmReceiver.class, event_name,8, 0);

                    Snackbar message = Snackbar.make(v, "Event added successfully!", 2000);
                    message.show();
                } else {
                    Snackbar message = Snackbar.make(v, "Cannot add event!", 2000);
                    message.show();
                }
            });
        }
        updateEventPopUpInfo(myDialog, event);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void artistPopUp(User user) {
        Dialog myDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        // v.getParent();
        System.out.println("clicked pop up");
        myDialog.setContentView(R.layout.artist_description_popup);
        TextView close_popup = (TextView) myDialog.findViewById(R.id.close_artist_popup);
        close_popup.setText("X");

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Closes the pop up if the user clicks the X
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void updateEventPopUpInfo(Dialog dialog, Event event) {
        // TODO: update text boxes with event info
        TextView textView_description = dialog.findViewById(R.id.event_description);
        textView_description.setText(event.getDescription());

        TextView textView_date = dialog.findViewById(R.id.event_date);
        textView_date.setText(event.getLongDate());

        TextView textView_artist = dialog.findViewById(R.id.event_artist);
        textView_artist.setText(event.getArtist());

        TextView textView_name = dialog.findViewById(R.id.event_name);
        textView_name.setText(event.getName());

        ImageLoaderPackage pkg = new ImageLoaderPackage(event.getImageURL(), dialog);
        ImageLoader loader = new ImageLoader();
        loader.execute(pkg);
    }

    private static class ImageLoader extends AsyncTask<ImageLoaderPackage, Void, Drawable> {
        private ImageLoaderPackage pkg;

        @Override
        protected Drawable doInBackground(ImageLoaderPackage... loaderPackages) {
            try {
                ImageLoaderPackage loaderPackage = loaderPackages[0];
                pkg = loaderPackage;
                URL url = loaderPackage.getUrl();
                InputStream is = (InputStream) url.getContent();
                return Drawable.createFromStream(is, "src name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable image){
            Dialog dialog = pkg.getDialog();
            ImageView event_image = dialog.findViewById(R.id.event_image);
            event_image.setImageDrawable(image);
        }
    }

    static class ImageLoaderPackage{
        private URL url;
        private Dialog dialog;

        ImageLoaderPackage (URL url, Dialog dialog) {
            this.url = url;
            this.dialog = dialog;
        }

        URL getUrl() {
            return url;
        }

        Dialog getDialog() {
            return dialog;
        }
    }
}
