package utoronto.saturn.app.front_end.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import utoronto.saturn.User;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.listeners.OnItemClickListener;

public class ArtistItemAdapter extends
        RecyclerView.Adapter<ArtistItemAdapter.ViewHolder>{
    private List<User> mArtists;
    private OnItemClickListener mListener;

    public ArtistItemAdapter(List<User> artists, OnItemClickListener listener) {
        mArtists = artists;
        mListener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView artistNameTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            artistNameTextView = itemView.findViewById(R.id.text_artist_name);
        }

        public void bind(final User user, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }
    }

    @NonNull
    public ArtistItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View artistItemView = inflater.inflate(R.layout.layout_artistitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(artistItemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArtistItemAdapter.ViewHolder viewHolder, int position) {
        if (mArtists == null) {
            TextView artistNameTextView = viewHolder.artistNameTextView;
            artistNameTextView.setText(R.string.artist_noartists_message);
            artistNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        }
        else {

            // Get the data model based on position
            User artist = mArtists.get(position);

            // Set item views based on your views and data model
            TextView artistNameTextView = viewHolder.artistNameTextView;
            artistNameTextView.setText(artist.getUsername());
            artistNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            viewHolder.bind(artist, mListener);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if (mArtists == null) return 1;
        return mArtists.size();
    }
}
