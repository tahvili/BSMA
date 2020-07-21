/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class is the main activity that will load every time the app is opened
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.helper.SQLiteHandler;
import info.androidhive.bsma.helper.ManagerSession;
import java.util.HashMap;
import info.androidhive.bsma.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends Activity {

    private TextView txtFirstname;
    private TextView txtLastname;
    private TextView txtEmail;
    private TextView txtAge;
    private TextView txtPhone;
    private TextView txtGrade;
    private TextView txtPath;
    private TextView txtAdd;
    private TextView txtSector;
    private TextView txtEvents;
    private Button btnLogout;
    private Button btnProfile;
    private Button btnQr;
    private Button btnEventView;
    private Button btnQrS;
    private Button btnManage;

    private SQLiteHandler db;
    private ManagerSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFirstname = (TextView) findViewById(R.id.firstname);
        txtLastname = (TextView) findViewById(R.id.lastname);
        txtAge = (TextView) findViewById(R.id.age);
        txtPhone = (TextView) findViewById(R.id.phone);
        txtGrade = (TextView) findViewById(R.id.grade);
        txtPath = (TextView) findViewById(R.id.path);
        txtAdd = (TextView) findViewById(R.id.add);
        txtSector = (TextView) findViewById(R.id.sector);
        txtEvents = (TextView) findViewById(R.id.events);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnQr = (Button) findViewById(R.id.btnQr);
        btnQrS = (Button) findViewById(R.id.btnQrS);
        btnEventView = (Button) findViewById(R.id.btnEventView);
        btnManage = (Button) findViewById(R.id.btnManage);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new ManagerSession(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String firstname = user.get("firstname");
        String lastname = user.get("lastname");
        String age = user.get("age");
        String phone = user.get("phone");
        String grade = user.get("grade");
        String path = user.get("path");
        String add = user.get("add");
        String sector = user.get("sector");
        String events = user.get("events");
        String email = user.get("email");
        String role = user.get("role");

        if (role.equals("STUDENT")) {
            View b = findViewById(R.id.btnQr);
            b.setVisibility(View.GONE);
            View c = findViewById(R.id.btnManage);
            c.setVisibility(View.GONE);
        }
        else if (role.equals("ADMIN")) {
            View x = findViewById(R.id.btnQrS);
            x.setVisibility(View.GONE);
            View z = findViewById(R.id.btnEventView);
            z.setVisibility(View.GONE);
        }

        // Displaying the user details on the screen
        txtFirstname.setText(firstname);
        txtLastname.setText(lastname);
        txtAge.setText(age);
        txtPhone.setText(phone);
        txtGrade.setText(grade);
        txtPath.setText(path);
        txtAdd.setText(add);
        txtSector.setText(sector);
        txtEmail.setText(email);
        txtEvents.setText(events);


        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Profile();
            }
        });
        btnQr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Qr();
            }
        });
        btnQrS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QrS();
            }
        });
        btnEventView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Events();
            }
        });
        btnManage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Manage();
            }
        });
    }

    // Log users out
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    // Open Profile page
    private void Profile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    // Open Qr tool for admins
    private void Qr() {
        Intent intent = new Intent(MainActivity.this, QrActivity.class);
        startActivity(intent);
        finish();
    }
    // Open Qr tool for students
    private void QrS() {
        Intent intent = new Intent(MainActivity.this, QrSActivity.class);
        startActivity(intent);
        finish();
    }
    // Open events for students
    private void Events() {
        Intent intent = new Intent(MainActivity.this, ViewEventActivity.class);
        startActivity(intent);
        finish();
    }
    // Open user and event manager for admins
    private void Manage() {
        Intent intent = new Intent(MainActivity.this, ManageActivity.class);
        startActivity(intent);
        finish();
    }
}