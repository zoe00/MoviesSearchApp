package movies.search.app.custom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import movies.search.app.R;
import movies.search.app.bo.ImageConfiguration;
import movies.search.app.bo.Poster;
import movies.search.app.globals.AppData;

public class ImageGalleryRecyclerAdapter extends RecyclerView.Adapter<MovieImageViewHolder> {

    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;
    private final ImageConfiguration imageConfig;
    private Context context;
    private ArrayList<Poster> items;

    public ImageGalleryRecyclerAdapter(Context context, ArrayList<Poster> items) {
        this.context = context;
        this.items = items;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .showImageOnLoading(null).build();
        imageConfig = AppData.getInstance().getImageConfig();
    }

    @Override
    public MovieImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowview_image_tile, parent, false);
        return new MovieImageViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(MovieImageViewHolder viewHolder, int position) {
        String imageUrl = imageConfig.getBaseUrl()+imageConfig.getLogoSizes()[imageConfig.getLogoSizes().length/2]
                +items.get(position).getFilePath();
        imageLoader.displayImage(imageUrl, viewHolder.img, options);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

