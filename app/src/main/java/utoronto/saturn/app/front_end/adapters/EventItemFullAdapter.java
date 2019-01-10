package utoronto.saturn.app.front_end.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import utoronto.saturn.Event;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.listeners.OnItemClickListener;

public class EventItemFullAdapter extends
        RecyclerView.Adapter<EventItemFullAdapter.ViewHolder>{
    private List<Event> mEvents;
    private final OnItemClickListener listener;

    public EventItemFullAdapter(List<Event> events, OnItemClickListener listener) {
        mEvents = events;
        this.listener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView dateTextView;
        public TextView eventNameTextView;
        public TextView locationTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            dateTextView = itemView.findViewById(R.id.text_date);
            eventNameTextView = itemView.findViewById(R.id.text_event_name);
            locationTextView = itemView.findViewById(R.id.text_location);
        }

        public void bind(final Event event, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(event));
        }
    }

    @NonNull
    public EventItemFullAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View eventItemView = inflater.inflate(R.layout.layout_eventitem_full, parent, false);

        return new ViewHolder(eventItemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull EventItemFullAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        if (mEvents == null || mEvents.size() == 0) {
            TextView dateTextView = viewHolder.dateTextView;
            dateTextView.setText("");
            TextView eventNameTextView = viewHolder.eventNameTextView;
            eventNameTextView.setText(R.string.event_noevents_message);
            TextView locationTextView = viewHolder.locationTextView;
            locationTextView.setText("");
        }
        else {
            Event event = mEvents.get(position);

            // Set item views based on your views and data model
            TextView dateTextView = viewHolder.dateTextView;

            dateTextView.setText(event.getShortDate());
            TextView eventNameTextView = viewHolder.eventNameTextView;
            eventNameTextView.setText(event.getName());
            TextView locationTextView = viewHolder.locationTextView;

            locationTextView.setText(event.getArtist());
            viewHolder.bind(event, listener);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if (mEvents == null || mEvents.size() == 0) return 1;
        return mEvents.size();
    }
}
