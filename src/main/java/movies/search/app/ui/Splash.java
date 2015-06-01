package movies.search.app.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import movies.search.app.R;
import movies.search.app.bo.ImageConfig;
import movies.search.app.globals.AppData;
import movies.search.app.globals.Globals;
import movies.search.app.helper.Util;
import movies.search.app.network.VolleyRequestQueue;

public class Splash extends Activity implements Response.ErrorListener, Response.Listener<JSONObject> {

    private Context context;
    private ProgressDialog progressDialog;
    private String TAG = "SPLASH";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!Util.isNetworkAvailable(context))
            Util.showWifiDialog(context);
        else if(Globals.getConfig(context).length()==0) {
            progressDialog = ProgressDialog.show(Splash.this, "", "Configuring App for the first time, please wait...");
            configApp();
        }
        else
            proceed();
    }

    private void init() {
        context = this;
    }

    private void proceed(){
        prepareAppData();
        finish();
        startActivity(new Intent(context, SearchActivity.class));
    }

    private void prepareAppData() {
        Gson gson = new Gson();
        AppData appData = AppData.getInstance();
        appData.setImageConfig(gson.fromJson(Globals.getConfig(context), ImageConfig.class).getImages());
    }

    private void configApp(){
        Properties props = new Properties();
        try {
            InputStream inputStream = getAssets().open("project.properties");
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("base_url")+props.getProperty("config");
        String params = "api_key="+props.getProperty("api_key");
        url = url+"?"+params;
        RequestQueue queue = VolleyRequestQueue.getInstance(context).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                this, this);
        jsObjRequest.setTag(TAG);
        queue.add(jsObjRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Util.showExitDialog(context, "Something went wrong, please try again later.");
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e(TAG, response+";;");
        progressDialog.dismiss();
        Globals.setConfig(context, response.toString());
        proceed();
    }

    @Override
    public void onBackPressed() {
        VolleyRequestQueue.getInstance(context).getRequestQueue().cancelAll(TAG);
        super.onBackPressed();
    }
}