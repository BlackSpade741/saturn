package utoronto.saturn.app.front_end.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import utoronto.saturn.Event;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.adapters.EventItemFullAdapter;
import utoronto.saturn.app.front_end.listeners.OnItemClickListener;
import utoronto.saturn.app.front_end.viewmodels.CategoryViewModel;

public class CategoryFragment extends Fragment {

    private CategoryViewModel mViewModel;
    private OnItemClickListener mListener;
    private String category;
    private List<Event> events;

    private RecyclerView rvEvents;


    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString("CATEGORY", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args){
        category = args.getString("CATEGORY");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        TextView tvTitle = view.findViewById(R.id.text_category_title);
        tvTitle.setText(getString(R.string.category_title, category));
        rvEvents = view.findViewById(R.id.rv_category_events);
        EventItemFullAdapter adapter = new EventItemFullAdapter(events, mListener);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        events = mViewModel.getEventsByCategory(category);

        rvEvents.setAdapter(new EventItemFullAdapter(events, mListener));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickListener){
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
