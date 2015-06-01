package movies.search.app.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import movies.search.app.R;
import movies.search.app.bo.ImageConfiguration;
import movies.search.app.globals.AppData;

public class FullScreenImageActivity extends Activity implements ImageLoadingListener {

    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_preview);
        init();
    }

    private void init() {
        Context context = this;
        img =  (ImageView) findViewById(R.id.imageView);
        String path = getIntent().getStringExtra("path");
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .showImageOnLoading(null).build();
        ImageConfiguration imageConfig = AppData.getInstance().getImageConfig();
        String imageUrl = imageConfig.getBaseUrl()+imageConfig.getPosterSizes()[imageConfig.getPosterSizes().length-2]
                +path;
        imageLoader.displayImage(imageUrl, img, options, (ImageLoadingListener) context);
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        try{
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width, height, true);
            img.setImageBitmap(scaled);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadingCancelled(String s, View view) {}
}