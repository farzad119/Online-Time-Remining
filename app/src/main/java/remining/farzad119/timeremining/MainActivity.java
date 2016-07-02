package remining.farzad119.timeremining;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_settime=(Button)findViewById(R.id.btnset);
        final EditText ethour=(EditText)findViewById(R.id.ethour);
        final EditText etmin=(EditText)findViewById(R.id.etmin);
        final EditText etsec=(EditText)findViewById(R.id.etsec);
        final EditText etyear=(EditText)findViewById(R.id.etyear);
        final EditText etmonth=(EditText)findViewById(R.id.etmonth);
        final EditText etday=(EditText)findViewById(R.id.etday);
        Button btnviewreminingtime=(Button)findViewById(R.id.btnviewreminingtime);
        Button btnisexpired=(Button)findViewById(R.id.btnisexpired);

        btnviewreminingtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ReminingTime.class);
                MainActivity.this.startActivity(intent);
            }
        });

        btnisexpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,isexpired.class);
                MainActivity.this.startActivity(intent);
            }
        });

        btn_settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create http cliient object to send request to server
                //its for test
                String url = "/time_insert.php?endtime="
                        +etyear.getText().toString()+"-"
                        +etmonth.getText().toString()+"-"
                        +etday.getText().toString()+"a"
                        +ethour.getText().toString()+ ":"
                        +etmin.getText().toString() + ":"
                        +etsec.getText().toString();

                new RequestTask().execute(url);}

        });

    }

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
            Log.w("Response",result+"");
            Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG).show();
        }
    }
}
