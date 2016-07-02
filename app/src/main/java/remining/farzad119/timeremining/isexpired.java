package remining.farzad119.timeremining;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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

public class isexpired extends AppCompatActivity {

    Button btnview;
    EditText etid;
    TextView txtreminingtime;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_isexpired);

            btnview=(Button)findViewById(R.id.btnview);
            etid=(EditText)findViewById(R.id.etid);
            txtreminingtime=(TextView)findViewById(R.id.txtisexpired);

            btnview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ferestadan id karbar be samt server
                    String url = "http://farzad119.ir/Files/timeremining/signupexpired/isexpired.php?id=" + etid.getText().toString();

                    //ejraye class daryaft etelaat
                    new RequestTask().execute(url);
                }
            });

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
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        responseString = out.toString();
                        out.close();
                    } else{
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


                try {
                    JSONObject jObj = new JSONObject(result);

                    Toast.makeText(isexpired.this, "End time : " + jObj.getString("expireddate") + "\n now :" + jObj.getString("now"), Toast.LENGTH_LONG).show();

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

                    if (diff<0){
                        txtreminingtime.setText("تاریخ انقضای حساب کاربری به پایان رسیده است");
                    }else {
                        txtreminingtime.setText(getDurationBreakdown(diff)+"روز دیگر باقی مانده است");
                    }

                    Log.wtf("diff hour",diff+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    //class tabdil millisanie be roz
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0) {
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
        sb.append(days);
        return(sb.toString());
    }
}

