package remining.farzad119.timeremining;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnviewreminingtime = (Button) findViewById(R.id.btnviewreminingtime);
        Button btnisexpired = (Button) findViewById(R.id.btnisexpired);

        btnviewreminingtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReminingTimeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        btnisexpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IsExpiredActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }
}
