/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will update the information for single events
 */
package info.androidhive.bsma.activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import info.androidhive.bsma.R;
import java.util.Calendar;
import java.util.HashMap;

public class
UpdateActivity extends AppCompatActivity {

    String HttpURL = "http://bsma.kiuloper.com/ANDROID_LOGIN_API/update_event.php";
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    Button UpdateStudent;
    String Subject_id, Subject_title, Subject_des, Subject_date, Subject_due, Subject_by, Subject_from, Subject_sec;

    private Button btnback;
    private String txt_today;
    private ProgressDialog pDialog;
    private EditText txt_title;
    private EditText txt_des;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView, wasDateView;
    private int year, month, day;
    private EditText txt_teacher;
    private TextView txt_date;
    private CheckBox inputbtv;
    private CheckBox inputcrew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        dateView = (TextView) findViewById(R.id.textduedate);
        wasDateView = (TextView) findViewById(R.id.textwasduedate);
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

        UpdateStudent = (Button)findViewById(R.id.UpdateButton);

        // Receive Student ID, Name , Phone Number , Class Send by previous ShowSingleRecordActivity.



        Subject_id = getIntent().getStringExtra("BSMA_ID");
        Subject_title = getIntent().getStringExtra("BSMA_TITLE");
        Subject_des = getIntent().getStringExtra("BSMA_DES");
        Subject_date = getIntent().getStringExtra("BSMA_DATE");
        Subject_due = getIntent().getStringExtra("BSMA_DUE");
        Subject_by = getIntent().getStringExtra("BSMA_AUTHOR");
        Subject_from = getIntent().getStringExtra("BSMA_TEACHER");
        Subject_sec = getIntent().getStringExtra("BSMA_SECTOR");

        // Setting Received Student Name, Phone Number, Class into EditText.
        wasDateView.setText(Subject_due);
        txt_title.setText(Subject_title);
        txt_des.setText(Subject_des);
        txt_teacher.setText(Subject_from);
        if (Subject_sec.equals("Brebeuf TV & Tech Crew")) {
            inputbtv.setChecked(true);
            inputcrew.setChecked(true);
        }
        else if(Subject_sec.equals("Brebeuf TV")) {
            inputbtv.setChecked(true);
            inputcrew.setChecked(false);
        }
        else if(Subject_sec.equals("Tech Crew")) {
            inputcrew.setChecked(true);
            inputbtv.setChecked(false);
        }
        else {
            inputbtv.setChecked(false);
            inputcrew.setChecked(false);
        }

        // Adding click listener to update button .
        UpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting data from EditText after button click.
                GetDataFromEditText();

                // Sending Student Name, Phone Number, Class to method to update on server.
                StudentRecordUpdate(Subject_id, Subject_title, Subject_des, Subject_due, Subject_sec, Subject_from);

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

    // Method to get existing data from EditText.
    public void GetDataFromEditText(){

        Subject_title = txt_title.getText().toString();
        Subject_des = txt_des.getText().toString();
        Subject_from = txt_teacher.getText().toString();
        Subject_due = txt_date.getText().toString();
        Subject_sec = "";

        if(inputbtv.isChecked()&&inputcrew.isChecked())
        {
            Subject_sec="Brebeuf TV & Tech Crew";
        }else if (inputbtv.isChecked()) {Subject_sec="Brebeuf TV";}
        else if (inputcrew.isChecked()) {Subject_sec="Tech Crew";}
        else {Toast.makeText(getApplicationContext(),
                "Please select a sector!", Toast.LENGTH_LONG)
                .show();}


    }

    // Method to Update Student Record.
    public void StudentRecordUpdate(final String BSMA_ID, final String BSMA_TITLE, final String BSMA_DES, final String BSMA_DUE, final String BSMA_SEC, final String BSMA_FROM){

        class StudentRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("BSMA_ID",params[0]);

                hashMap.put("BSMA_TITLE",params[1]);

                hashMap.put("BSMA_DES",params[2]);

                hashMap.put("BSMA_DUE",params[3]);

                hashMap.put("BSMA_SEC",params[4]);

                hashMap.put("BSMA_TEACHER",params[5]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(Subject_id, Subject_title, Subject_des, Subject_due, Subject_sec, Subject_from);
    }

    private void back() {
        Intent intent = new Intent(UpdateActivity.this,
                ViewEventActivity.class);
        startActivity(intent);
        finish();
    }
}
