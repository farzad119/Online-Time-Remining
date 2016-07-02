package remining.farzad119.timeremining;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ReminingTime extends ActionBarActivity {

    Boolean isActive = false;
    Long lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remining_time);
        Button btnview = (Button) findViewById(R.id.btnview);
        final EditText etid = (EditText) findViewById(R.id.etid);
        final TextView txtreminingtime = (TextView) findViewById(R.id.txtreminingtime);

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://farzad119.ir/Files/timeremining/time.php?id=" + etid.getText().toString();
                //ejraye class daryaft etelaat
                new RequestTask().execute(url);
            }
        });

        //timer update textview namayesh khorooji
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (isActive) {
                    lastResult = lastResult - 1000;
                    txtreminingtime.setText(getDurationBreakdown(lastResult));
                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

    }

    //Thread daryaft ettelat az database
    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            Log.w("Response", result + "");
            decodeJson(result);
        }
    }

    //class tabdil millisanie be roz va daghighe
    private static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            Log.wtf("diff", "Duration must be greater than zero!");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(hours + days * 24);
        sb.append(" Hours ");
        sb.append(minutes + hours);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return (sb.toString());
    }

    private void decodeJson(String json) {

        try {
            JSONObject jObj = new JSONObject(json);

            Toast.makeText(ReminingTime.this, "End time : " + jObj.getString("endtime") + "\n now :" + jObj.getString("now"), Toast.LENGTH_LONG).show();

            //joda kardan zaman va tarikh
            //example current : 2016-1-30 19:20:25
            //example endtime : 2016-1-30 09:20:00
            String[] CurrentDateArrayHelper = jObj.getString("now").split(" ");
            String[] FinalDateArrayHelper = jObj.getString("endtime").split(" ");

            //joda kardan adaad tarikh
            String[] CurrentDate = CurrentDateArrayHelper[0].split("-");
            String[] FinalDate = FinalDateArrayHelper[0].split("-");

            //joda karda adaad zaman
            String[] CurrentTime = CurrentDateArrayHelper[1].split(":");
            String[] FinalTime = FinalDateArrayHelper[1].split(":");

            //tabdil tarikh be calendar
            Calendar calDatenow = Calendar.getInstance();
            calDatenow.set(Integer.valueOf(CurrentDate[0]), Integer.valueOf(CurrentDate[1]), Integer.valueOf(CurrentDate[2]));

            Calendar calDatenowFinal = Calendar.getInstance();
            calDatenowFinal.set(Integer.valueOf(FinalDate[0]), Integer.valueOf(FinalDate[1]), Integer.valueOf(FinalDate[2]));

            //tabdil zaman be calendar
            Calendar calTimeenow = Calendar.getInstance();
            calTimeenow.set(Integer.valueOf(CurrentTime[0]), Integer.valueOf(CurrentTime[1]), Integer.valueOf(CurrentTime[2]));

            Calendar calTimeFinal = Calendar.getInstance();
            calTimeFinal.set(Integer.valueOf(FinalTime[0]), Integer.valueOf(FinalTime[1]), Integer.valueOf(FinalTime[2]));

            //set kardan tarikh va zaman dar yek calendar
            Calendar thatDay = Calendar.getInstance();
            thatDay.set(Calendar.SECOND, Integer.valueOf(FinalTime[2]));
            thatDay.set(Calendar.MINUTE, Integer.valueOf(FinalTime[1]));
            thatDay.set(Calendar.HOUR, Integer.valueOf(FinalTime[0]));
            thatDay.set(Calendar.DAY_OF_MONTH, Integer.valueOf(FinalDate[2]));
            thatDay.set(Calendar.MONTH, Integer.valueOf(FinalDate[1]) - 1); // 0-11 so 1 less
            thatDay.set(Calendar.YEAR, Integer.valueOf(FinalDate[0]));

            Log.wtf("SECOND", Integer.valueOf(FinalTime[2]) + "");
            Log.wtf("MINUTE", Integer.valueOf(FinalTime[1]) + "");
            Log.wtf("HOUR", Integer.valueOf(FinalTime[0]) + "");
            Log.wtf("DAY_OF_MONTH", Integer.valueOf(FinalDate[2]) + "");
            Log.wtf("MONTH", Integer.valueOf(FinalDate[1]) + "");
            Log.wtf("YEAR", Integer.valueOf(FinalDate[0]) + "");

            Calendar today = Calendar.getInstance();
            today.set(Calendar.SECOND, Integer.valueOf(CurrentTime[2]));
            today.set(Calendar.MINUTE, Integer.valueOf(CurrentTime[1]));
            today.set(Calendar.HOUR, Integer.valueOf(CurrentTime[0]));
            today.set(Calendar.DAY_OF_MONTH, Integer.valueOf(CurrentDate[2]));
            today.set(Calendar.MONTH, Integer.valueOf(CurrentDate[1]) - 1); // 0-11 so 1 less
            today.set(Calendar.YEAR, Integer.valueOf(CurrentDate[0]));

            long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
            Log.wtf("diff hour", getDurationBreakdown(diff));
            lastResult = diff;
            isActive = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
