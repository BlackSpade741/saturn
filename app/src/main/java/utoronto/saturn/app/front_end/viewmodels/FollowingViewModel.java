package utoronto.saturn.app.front_end.viewmodels;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import utoronto.saturn.User;

public class FollowingViewModel extends ViewModel {
    private List<User> artists;

    public FollowingViewModel(){
        artists = new ArrayList<>();
    }

    public List<User> getArtists(){
        return artists;
    }
}
