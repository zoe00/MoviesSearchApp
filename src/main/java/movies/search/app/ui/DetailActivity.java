package movies.search.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import movies.search.app.R;
import movies.search.app.bo.ImageConfiguration;
import movies.search.app.bo.Movie;
import movies.search.app.bo.MovieImages;
import movies.search.app.custom.adapters.ImageGalleryRecyclerAdapter;
import movies.search.app.globals.AppData;
import movies.search.app.helper.RecyclerItemClickListener;
import movies.search.app.helper.Util;
import movies.search.app.network.VolleyRequestQueue;


public class DetailActivity extends Activity implements Response.ErrorListener, Response.Listener<JSONObject>, ImageLoadingListener {

    private Context context;
    private TextView tvTitle, tvReleaseDate, tvVotes, tvOverview;
    private Toolbar toolbar;
    private String TAG = "DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        context = this;
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .showImageOnLoading(null).build();
        Movie movie = (Movie) getIntent().getSerializableExtra("data");
        if (movie != null) {
            tvOverview = ((TextView) findViewById(R.id.textViewOverview));
            if (movie.getOverview() != null && movie.getOverview().length() != 0)
                tvOverview.setText(movie.getOverview());
            else
                tvOverview.setText("No Overview Available");
            tvTitle = ((TextView) findViewById(R.id.textViewTitle));
            tvTitle.setText(movie.getTitle());
            tvReleaseDate = ((TextView) findViewById(R.id.textViewReleaseDate));
            tvReleaseDate.setText("Release date: \n" + Util.format(movie.getReleaseDate()));
            tvVotes = ((TextView) findViewById(R.id.textViewVotes));
            tvVotes.setText(movie.getVoteAverage() + "\nrating");
            ImageConfiguration imageConfig = AppData.getInstance().getImageConfig();
            String imageUrl = imageConfig.getBaseUrl() + imageConfig.getPosterSizes()[imageConfig.getPosterSizes().length / 2]
                    + movie.getPosterPath();
            Log.e("image_url", imageUrl);
            imageLoader.displayImage(imageUrl, (ImageView) findViewById(R.id.img), options, (ImageLoadingListener) context);
            getImages(movie.getId());
        }
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search Movies");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void refreshToolbar() {
        toolbar.setBackgroundColor(android.R.color.transparent);
        toolbar.setTitle("");
    }

    private void getImages(final long id) {
        Properties props = new Properties();
        try {
            InputStream inputStream = getAssets().open("project.properties");
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("base_url") + props.getProperty("gallery");
        url = url.replace("id", id + "");
        String params = "api_key=" + props.getProperty("api_key");
        url = url + "?" + params;
        RequestQueue queue = VolleyRequestQueue.getInstance(context).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                this, this);
        jsObjRequest.setTag(TAG);
        queue.add(jsObjRequest);
    }

    private void initGallery(final MovieImages images) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.Adapter adapter = new ImageGalleryRecyclerAdapter(context, images.getPosters());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManger = new LinearLayoutManager(this);
        layoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, FullScreenImageActivity.class);
                        intent.putExtra("path", images.getPosters().get(position).getFilePath());
                        startActivity(intent);
                    }
                })
        );
    }

    private MovieImages parseResponse(JSONObject response) {
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(response.toString(), MovieImages.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        Palette.generateAsync(bitmap,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrant =
                                palette.getVibrantSwatch();
                        Palette.Swatch lightVibrant =
                                palette.getLightVibrantSwatch();
                        if (vibrant != null) {
                            tvTitle.setBackgroundColor(
                                    vibrant.getRgb());
                            tvTitle.setTextColor(
                                    vibrant.getTitleTextColor());
                        }
                        if (lightVibrant != null) {
                            tvVotes.setBackgroundColor(
                                    lightVibrant.getRgb());
                            tvVotes.setTextColor(
                                    lightVibrant.getTitleTextColor());
                            tvReleaseDate.setBackgroundColor(
                                    lightVibrant.getRgb());
                            tvReleaseDate.setTextColor(
                                    lightVibrant.getTitleTextColor());
                            tvOverview.setTextColor(
                                    lightVibrant.getTitleTextColor());
                        }
                        refreshToolbar();
                    }
                });
    }

    @Override
    public void onLoadingCancelled(String s, View view) {
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            MovieImages images = parseResponse(response);
            if (images != null && images.getPosters() != null)
                initGallery(images);
        }
    }

    @Override
    public void onBackPressed() {
        VolleyRequestQueue.getInstance(context).getRequestQueue().cancelAll(TAG);
        super.onBackPressed();
    }
}
