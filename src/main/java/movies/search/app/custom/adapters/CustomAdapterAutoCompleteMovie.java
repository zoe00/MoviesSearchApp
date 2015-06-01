package movies.search.app.custom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import movies.search.app.R;
import movies.search.app.bo.Movie;

public class CustomAdapterAutoCompleteMovie extends ArrayAdapter<Movie> {

    private int viewResourceId=0;
    private Context context;
    private List<Movie> items, allItems;

    public CustomAdapterAutoCompleteMovie(Context context, int viewResourceId, List<Movie> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.context = context;
        this.viewResourceId = viewResourceId;
        allItems = new ArrayList<Movie>(items.size());
        allItems.addAll(items);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public TextView textViewTitle;
        public TextView textViewOverview;
        public TextView textViewRating;
    }

    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(viewResourceId, null);
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.textViewOverview = (TextView) convertView.findViewById(R.id.textViewOverview);
            holder.textViewRating = (TextView) convertView.findViewById(R.id.textViewRating);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        Movie movie = items.get(position);
        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewOverview.setText(movie.getOverview());
        holder.textViewRating.setText(movie.getVoteAverage()+"");

        return convertView;
    }

    public int getCount() {
        return items.size();
    }

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Movie)resultValue).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Movie> suggestions = new ArrayList<Movie>();
                for (Movie movie : allItems) {
                    if (movie.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(movie);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                addAll((ArrayList<Movie>) results.values);
            } else {
                addAll(allItems);
            }
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return mFilter;
    }

}
