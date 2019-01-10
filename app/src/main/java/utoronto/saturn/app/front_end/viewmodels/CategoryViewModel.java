package utoronto.saturn.app.front_end.viewmodels;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import utoronto.saturn.Event;
import utoronto.saturn.app.GuiManager;

public class CategoryViewModel extends ViewModel {
    private List<Event> animeEvents, movieEvents, gameEvents, concertEvents;

    public CategoryViewModel() {
        animeEvents = GuiManager.getInstance().getEventsByCategory("anime");
        movieEvents = GuiManager.getInstance().getEventsByCategory("movie");
        gameEvents = GuiManager.getInstance().getEventsByCategory("game");
        concertEvents = GuiManager.getInstance().getEventsByCategory("concert");
    }

    public List<Event> getEventsByCategory(String category) {
        switch (category) {
            case "Anime":
                return animeEvents;
            case "Movies":
                return movieEvents;
            case "Games":
                return gameEvents;
            case "Concerts":
                return concertEvents;
        }
        return null;
    }
}
