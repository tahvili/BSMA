/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show the events options
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainEventsActivity extends Activity {

    private Button btnBack;
    private Button btnNewEvent;
    private Button btnViewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_events);


        btnBack = (Button) findViewById(R.id.btnback);
        btnNewEvent = (Button) findViewById(R.id.btnnewevent);
        btnViewEvent = (Button) findViewById(R.id.btnviewevents);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });
        btnNewEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newevent();
            }
        });
        btnViewEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewevent();
            }
        });
    }

    private void back() {
        Intent intent = new Intent(MainEventsActivity.this,
                ManageActivity.class);
        startActivity(intent);
        finish();
    }

    private void newevent() {
        Intent intent = new Intent(MainEventsActivity.this,
                NewEventActivity.class);
        startActivity(intent);
        finish();
    }

    private void viewevent() {
        Intent intent = new Intent(MainEventsActivity.this,
                ViewEventActivity.class);
        startActivity(intent);
        finish();
    }
}