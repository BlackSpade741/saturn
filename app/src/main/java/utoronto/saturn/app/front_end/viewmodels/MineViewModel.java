package utoronto.saturn.app.front_end.viewmodels;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import utoronto.saturn.Event;
import utoronto.saturn.app.GuiManager;

public class MineViewModel extends ViewModel {
    private List<Event> mEvents;

    public MineViewModel(){
        mEvents = GuiManager.getInstance().getUserFollowedEvents();
    }

    public List<Event> getEvents() {
        return mEvents;
    }
}
