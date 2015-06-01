package movies.search.app.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import movies.search.app.R;

public class Util {

    public static void showExitDialog(final Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                ((Activity) context).finish();
            }
        });
        builder.create();
        builder.show();
    }
    public static void showInfoDialog(final Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", null);
        builder.create();
        builder.show();
    }

    public static void showWifiDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setMessage("Internet not available, please connect to internet to proceed.");
        builder.setCancelable(false);
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                ((Activity) context).finish();
            }
        });
        builder.create();
        builder.show();
    }

    public static boolean isNetworkAvailable(Context c) {
        try{
            ConnectivityManager connectivityManager= (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String format(String releaseDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat required = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            Date d = format.parse(releaseDate);
            return required.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return releaseDate;
    }
}
