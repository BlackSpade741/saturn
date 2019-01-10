package utoronto.saturn.app.front_end.views;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import utoronto.saturn.app.GuiManager;
import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.adapters.CategoryItemAdapter;
import utoronto.saturn.app.front_end.listeners.OnCategoryItemClickListener;
import utoronto.saturn.app.front_end.listeners.OnItemClickListener;


public class DiscoverFragment extends Fragment {
    private OnCategoryItemClickListener mListener;

    public DiscoverFragment() {
    // Required empty public constructor
    }

     /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        RecyclerView rvCategories = view.findViewById(R.id.rv_categories);
        CategoryItemAdapter adapter = new CategoryItemAdapter(GuiManager.getCategories(), mListener);
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoryItemClickListener) {
            mListener = (OnCategoryItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnCategoryItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
