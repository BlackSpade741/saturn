package utoronto.saturn.app.front_end.viewmodels;

import android.arch.lifecycle.ViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utoronto.saturn.Event;
import utoronto.saturn.app.GuiManager;

public class HomeViewModel extends ViewModel {
    private List<Event> myEvents;
    private List<Event> popularEvents;
    private List<Event> suggestedEvents;

    public HomeViewModel(){
        myEvents = new ArrayList<>();
        popularEvents = GuiManager.getInstance().getPopular();
        popularEvents = popularEvents.subList(0, Math.min(3, popularEvents.size()));
        suggestedEvents = GuiManager.getInstance().getSuggested();
    }

    public List<Event> getMyEvents() {
        myEvents = GuiManager.getInstance().getUserFollowedEvents();
        return myEvents;
    }

    public List<Event> getPopularEvents() {
        return popularEvents;
    }

    public List<Event> getSuggestedEvents() {
        return new ArrayList<>(suggestedEvents);
    }
}
