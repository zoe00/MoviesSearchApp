package movies.search.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Properties;

import movies.search.app.R;
import movies.search.app.bo.Movie;
import movies.search.app.custom.adapters.CustomAdapterAutoCompleteMovie;
import movies.search.app.network.VolleyRequestQueue;


public class SearchActivity extends Activity implements Response.ErrorListener, Response.Listener<JSONObject> {

    private Context context;
    private AutoCompleteTextView searchBar;
    private String TAG = "SEARCH";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {
        context = this;
        progressBar = (ProgressBar)findViewById(R.id.toolbarProgressBar);
        progressBar.setVisibility(View.GONE);
        searchBar = (AutoCompleteTextView)findViewById(R.id.editTextSearch);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchBar.getText().toString().length()!=0) {
                    VolleyRequestQueue.getInstance(context).getRequestQueue().cancelAll(TAG);
                    progressBar.setVisibility(View.VISIBLE);
                    searchMovie(searchBar.getText().toString());
                }
                else
                    progressBar.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailActivity.class);
                Movie movie = (Movie) parent.getItemAtPosition(position);
                intent.putExtra("data", movie);
                startActivity(intent);
            }
        });
        initToolBar();
    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private void searchMovie(final String key){
        final Properties props = new Properties();
        try {
            InputStream inputStream = getAssets().open("project.properties");
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("base_url")+props.getProperty("search");
        String params = "api_key="+props.getProperty("api_key")+"&query="+key;
        url = url+"?"+params;
        RequestQueue queue = VolleyRequestQueue.getInstance(context).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                this, this);
        jsObjRequest.setTag(TAG);
        queue.add(jsObjRequest);
    }

    private void refreshAdapter(ArrayList<Movie> list) {
        CustomAdapterAutoCompleteMovie adapter = new CustomAdapterAutoCompleteMovie(context, R.layout.rowview_search_autocomplete, list);
        searchBar.setAdapter(adapter);
        if(searchBar.getText().toString().length()!=0)
            searchBar.showDropDown();
    }

    private ArrayList<Movie> parseResponse(JSONObject response) {
        Gson gson = new GsonBuilder().create();
        try {
            Type listOfTestObject = new TypeToken<ArrayList<Movie>>(){}.getType();
            return gson.fromJson(response.getJSONArray("results").toString(), listOfTestObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        VolleyRequestQueue.getInstance(context).getRequestQueue().cancelAll(TAG);
        super.onBackPressed();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Toast.makeText(context, "Search failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e(TAG, response+";;");
        progressBar.setVisibility(View.GONE);
        if(response!=null) {
            ArrayList<Movie> list = parseResponse(response);
            if (list != null && !list.isEmpty()) {
                refreshAdapter(list);
            }
        }
    }
}
