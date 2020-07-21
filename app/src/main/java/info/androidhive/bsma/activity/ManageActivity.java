/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show the options open to click on
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class ManageActivity extends Activity {

    private Button btnBack;
    private Button btnEvents;
    private Button btnUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnEvents = (Button) findViewById(R.id.btnEvents);
        btnUsers = (Button) findViewById(R.id.btnUsers);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });
        btnEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                events();
            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                users();
            }
        });
    }

    private void back() {
        Intent intent = new Intent(ManageActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void events() {
        Intent intent = new Intent(ManageActivity.this,
                MainEventsActivity.class);
        startActivity(intent);
        finish();
    }

    private void users() {
        Intent intent = new Intent(ManageActivity.this,
                UsersViewActivity.class);
        startActivity(intent);
        finish();
    }
}