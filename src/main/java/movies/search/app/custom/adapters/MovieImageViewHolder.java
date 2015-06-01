package movies.search.app.custom.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import movies.search.app.R;
import movies.search.app.ui.FullScreenImageActivity;

public class MovieImageViewHolder extends RecyclerView.ViewHolder{

    private final Context context;
    public ImageView img;

    public MovieImageViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        img = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
