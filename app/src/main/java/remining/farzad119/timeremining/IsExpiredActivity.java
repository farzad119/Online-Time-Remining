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

public class IsExpiredActivity extends Activity {

    private Button btnview;
    private TextView txtreminingtime;
    private IsExpired isExpired= new IsExpired();

    //{"id":"49","now":"2016-07-13","expireddate":"2016-12-13"}
    private String sampleJsonTime = "{\"id\":\"49\",\"now\":\"2016-07-13\",\"expireddate\":\"2016-12-13\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isexpired);

        btnview = (Button) findViewById(R.id.btnview);
        txtreminingtime = (TextView) findViewById(R.id.txtisexpired);

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtreminingtime.setText(isExpired.getIsExpiredWithUntilDate(sampleJsonTime)+ " Day until expire");
            }
        });
    }

}

