/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show the options open to click on
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.app.AppConfig;
import info.androidhive.bsma.app.AppController;
import info.androidhive.bsma.helper.SQLiteHandler;
import info.androidhive.bsma.helper.ManagerSession;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.bsma.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class NewEventActivity extends Activity {
    private static final String TAG = NewEventActivity.class.getSimpleName();
    private Button btnback;
    private Button btnsave;
    private String txt_today;
    private ProgressDialog pDialog;
    private EditText txt_title;
    private EditText txt_des;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private EditText txt_teacher;
    private TextView txt_date;
    private CheckBox inputbtv;
    private CheckBox inputcrew;
    private SQLiteHandler db;
    private ManagerSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        dateView = (TextView) findViewById(R.id.textduedate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        txt_today = dateView.getText().toString();
        txt_title = (EditText) findViewById(R.id.txt_title);
        txt_des = (EditText) findViewById(R.id.txt_des);
        txt_date = (TextView) findViewById(R.id.textduedate);
        txt_teacher = (EditText) findViewById(R.id.txt_teacher);
        inputbtv = (CheckBox) findViewById(R.id.check_btv);
        inputcrew = (CheckBox) findViewById(R.id.check_crew);
        btnback = (Button) findViewById(R.id.btnback);
        btnsave = (Button) findViewById(R.id.btnsave);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new ManagerSession(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        final String firstname = user.get("firstname");
        final String lastname = user.get("lastname");

        btnsave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String title = txt_title.getText().toString();
                String des = txt_des.getText().toString();
                String date = txt_date.getText().toString();
                String teacher = txt_teacher.getText().toString();
                String sector = "";

                if(inputbtv.isChecked()&&inputcrew.isChecked())
                {
                    sector="Brebeuf TV & Tech Crew";
                }else if (inputbtv.isChecked()) {sector="Brebeuf TV";}
                else if (inputcrew.isChecked()) {sector="Tech Crew";}
                else {Toast.makeText(getApplicationContext(),
                        "Please select a sector!", Toast.LENGTH_LONG)
                        .show();}

                if (!title.isEmpty() && !des.isEmpty() && !date.isEmpty() && !sector.isEmpty() && !teacher.isEmpty()) {

                        registerEvent(firstname, lastname, title, des, date, teacher, sector, txt_today);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    private void registerEvent(final String firstname, final String lastname, final String title, final String des,
                              final String due, final String teacher, final String sector, final String date) {
        // Tag used to cancel the request
        String tag_string_req = "req_event";

        pDialog.setMessage("Adding new event...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_NEWEVENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Event successfully Added.", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(
                                NewEventActivity.this,
                                MainEventsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Saving Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("des", des);
                params.put("date", date);
                params.put("due", due);
                params.put("teacher", teacher);
                params.put("author", firstname + " " + lastname);
                params.put("sector", sector);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(NewEventActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void back() {
        Intent intent = new Intent(NewEventActivity.this,
                MainEventsActivity.class);
        startActivity(intent);
        finish();
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
