package remining.farzad119.timeremining;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ReminingTimeActivity extends Activity {

    private boolean isActive = false;
    private Long lastResult;
    private TimeRemining timeRemining = new TimeRemining();
    //{"id":"49","now":"2016-07-13 17:24:26","endtime":"2016-07-13 20:30:50"}
    private String sampleJsonTime = "{\"id\":\"49\",\"now\":\"2016-07-13 17:24:26\",\"endtime\":\"2016-07-13 20:30:50\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remining_time);
        Button btnview = (Button) findViewById(R.id.btnview);
        final TextView txtreminingtime = (TextView) findViewById(R.id.txtreminingtime);

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActive=true;
                lastResult = timeRemining.CalcDiff(sampleJsonTime);
                txtreminingtime.setText(timeRemining.getFormattedMill(lastResult));

                Log.w("diff time ",timeRemining.getReminingTime(sampleJsonTime));
            }
        });

        //timer update textview namayesh khorooji
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (isActive) {
                    lastResult = lastResult - 1000;
                    txtreminingtime.setText(timeRemining.getFormattedMill(lastResult));
                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

    }

}
