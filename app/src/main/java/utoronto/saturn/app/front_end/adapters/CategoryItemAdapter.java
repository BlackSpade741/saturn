package utoronto.saturn.app.front_end.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import utoronto.saturn.app.R;
import utoronto.saturn.app.front_end.listeners.OnCategoryItemClickListener;
import utoronto.saturn.app.front_end.listeners.OnItemClickListener;

public class CategoryItemAdapter extends
        RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>{
    private String[] mCategories;
    private final OnCategoryItemClickListener listener;

    public CategoryItemAdapter(String[] categories, OnCategoryItemClickListener listener) {
        mCategories = categories;
        this.listener = listener;
    }

    public CategoryItemAdapter(List<String> categories, OnCategoryItemClickListener listener) {
        this((String[]) categories.toArray(), listener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.tv_category_name);
        }

        public void bind(final String category, final OnCategoryItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onCategoryItemClick(category));
        }
    }

    @NonNull
    public CategoryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View categoryItemView = inflater.inflate(R.layout.layout_categoryitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(categoryItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryItemAdapter.ViewHolder viewHolder, int position) {
        String category = mCategories[position];
        TextView categoryNameTextView = viewHolder.categoryNameTextView;
        categoryNameTextView.setText(category);
        viewHolder.bind(category, listener);
    }

    @Override
    public int getItemCount() {
        return mCategories.length;
    }
}
