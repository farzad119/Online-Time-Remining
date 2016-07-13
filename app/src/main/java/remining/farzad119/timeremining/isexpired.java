package remining.farzad119.timeremining;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class IsExpired {

    public IsExpired() {
    }

    //if is not expire, return until date expire
    public String getIsExpiredWithUntilDate(String JsonTimes) {
        String isExpired = null;
        try {
            JSONObject jObj = new JSONObject(JsonTimes);

            //joda kardan adaad tarikh
            String[] CurrentDate = jObj.getString("now").split("-");
            String[] FinalDate = jObj.getString("expireddate").split("-");

            //set kardan tarikh va zaman dar yek calendar
            Calendar thatDay = Calendar.getInstance();
            thatDay.set(Calendar.DAY_OF_MONTH, Integer.valueOf(FinalDate[2]));
            thatDay.set(Calendar.MONTH, Integer.valueOf(FinalDate[1]) - 1); // 0-11 so 1 less
            thatDay.set(Calendar.YEAR, Integer.valueOf(FinalDate[0]));

            Calendar today = Calendar.getInstance();
            today.set(Calendar.DAY_OF_MONTH, Integer.valueOf(CurrentDate[2]));
            today.set(Calendar.MONTH, Integer.valueOf(CurrentDate[1]) - 1); // 0-11 so 1 less
            today.set(Calendar.YEAR, Integer.valueOf(CurrentDate[0]));

            long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();

            if (diff < 0) {
                isExpired = "تاریخ انقضای حساب کاربری به پایان رسیده است";
            } else {
                isExpired = getDurationBreakdown(diff) ;
            }

            Log.wtf("diff hour", diff + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isExpired;
    }

    //return boolean state
    public boolean getIsExpired(String JsonTimes) {
        boolean isExpired = false;
        try {
            JSONObject jObj = new JSONObject(JsonTimes);

            //joda kardan adaad tarikh
            String[] CurrentDate = jObj.getString("now").split("-");
            String[] FinalDate = jObj.getString("expireddate").split("-");

            //set kardan tarikh va zaman dar yek calendar
            Calendar thatDay = Calendar.getInstance();
            thatDay.set(Calendar.DAY_OF_MONTH, Integer.valueOf(FinalDate[2]));
            thatDay.set(Calendar.MONTH, Integer.valueOf(FinalDate[1]) - 1); // 0-11 so 1 less
            thatDay.set(Calendar.YEAR, Integer.valueOf(FinalDate[0]));

            Calendar today = Calendar.getInstance();
            today.set(Calendar.DAY_OF_MONTH, Integer.valueOf(CurrentDate[2]));
            today.set(Calendar.MONTH, Integer.valueOf(CurrentDate[1]) - 1); // 0-11 so 1 less
            today.set(Calendar.YEAR, Integer.valueOf(CurrentDate[0]));

            long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();

            if (diff < 0) {
                isExpired = true;
            } else {
                isExpired = false;
            }

            Log.wtf("diff hour", diff + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isExpired;
    }

    //class tabdil millisanie be roz
    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            Log.wtf("diff", "Duration must be greater than zero!");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        return (sb.toString());
    }


}

