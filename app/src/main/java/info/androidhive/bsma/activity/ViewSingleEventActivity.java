/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show every single event taken from database
 */
package info.androidhive.bsma.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import info.androidhive.bsma.R;
import info.androidhive.bsma.helper.ManagerSession;
import info.androidhive.bsma.helper.SQLiteHandler;

public class ViewSingleEventActivity extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Student Data from Id Sent from previous activity.
    String HttpURL = "http://bsma.kiuloper.com/ANDROID_LOGIN_API/filter_event_data.php";

    // Http URL for delete Already Open Student Record.
    String HttpUrlDeleteRecord = "http://bsma.kiuloper.com/ANDROID_LOGIN_API/delete_event.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;
    TextView id, title, des, date, due, by, from, sec;
    String Subject_id, Subject_title, Subject_des, Subject_date, Subject_due, Subject_by, Subject_from, Subject_sec;
    Button UpdateButton, DeleteButton, BackButton, UsersButton;
    String TempItem;
    ProgressDialog progressDialog2;
    private SQLiteHandler db;
    private ManagerSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listvieweventlayout);
        id = (TextView)findViewById(R.id.id);
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        des = (TextView)findViewById(R.id.des);
        due = (TextView)findViewById(R.id.due);
        by = (TextView)findViewById(R.id.author);
        from = (TextView)findViewById(R.id.teacher);
        sec = (TextView)findViewById(R.id.sector);

        UsersButton = (Button)findViewById(R.id.buttonUsers);
        UpdateButton = (Button)findViewById(R.id.buttonUpdate);
        DeleteButton = (Button)findViewById(R.id.buttonDelete);
        BackButton = (Button)findViewById(R.id.buttonBack);

        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");

        //Calling method to filter Student Record and open selected record.
        HttpWebCall(TempItem);

        db = new SQLiteHandler(getApplicationContext());
        session = new ManagerSession(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String role = user.get("role");
        if (role.equals("STUDENT")) {
            View b = findViewById(R.id.buttonUsers);
            b.setVisibility(View.GONE);
            View c = findViewById(R.id.buttonUpdate);
            c.setVisibility(View.GONE);
            View x = findViewById(R.id.buttonDelete);
            x.setVisibility(View.GONE);
        }


        UsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ViewSingleEventActivity.this,UsersActivity.class);
                intent.putExtra("BSMA_ID", TempItem);
                startActivity(intent);
                finish();
            }
        });

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewSingleEventActivity.this,UpdateActivity.class);

                // Sending Student Id, Name, Number and Class to next UpdateActivity.
                intent.putExtra("BSMA_ID", TempItem);
                intent.putExtra("BSMA_TITLE",Subject_title);
                intent.putExtra("BSMA_DES", Subject_des);
                intent.putExtra("BBSMA_DATE", Subject_date);
                intent.putExtra("BSMA_DUE", Subject_due);
                intent.putExtra("BSMA_AUTHOR", Subject_by);
                intent.putExtra("BSMA_TEACHER", Subject_from);
                intent.putExtra("BSMA_SECTOR", Subject_sec);

                startActivity(intent);

                // Finishing current activity after opening next activity.
                finish();

            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        // Add Click listener on Delete button.
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling Student delete method to delete current record using Student ID.
                EventDelete(TempItem);

            }
        });

    }

    private void back() {
        Intent intent = new Intent(ViewSingleEventActivity.this,
                ViewEventActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to Delete Student Record
    public void EventDelete(final String BSMA_ID) {

        class EventDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(ViewSingleEventActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(ViewSingleEventActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewSingleEventActivity.this,
                        ViewEventActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            protected String doInBackground(String... params) {

                // Sending STUDENT id.
                hashMap.put("BSMA_ID", params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpUrlDeleteRecord);

                return finalResult;
            }
        }

        EventDeleteClass eventDeleteClass = new EventDeleteClass();

        eventDeleteClass.execute(BSMA_ID);
    }


    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ViewSingleEventActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ViewSingleEventActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("BSMA_ID",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;



                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Student Name, Phone Number, Class into Variables.
                            Subject_id = jsonObject.getString("BSMA_ID").toString();
                            Subject_title = jsonObject.getString("BSMA_TITLE").toString();
                            Subject_des = jsonObject.getString("BSMA_DES").toString();
                            Subject_date = jsonObject.getString("BSMA_DATE").toString();
                            Subject_due = jsonObject.getString("BSMA_DUE").toString();
                            Subject_by = jsonObject.getString("BSMA_AUTHOR").toString();
                            Subject_from = jsonObject.getString("BSMA_TEACHER").toString();
                            Subject_sec = jsonObject.getString("BSMA_SECTOR").toString();

                        }

                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block

                        e.printStackTrace();

                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {


    // Setting Student Name, Phone Number, Class into TextView after done all process .
    id.setText(Subject_id);
    title.setText(Subject_title);
    des.setText(Subject_des);
    date.setText(Subject_date);
    due.setText(Subject_due);
    by.setText(Subject_by);
    from.setText(Subject_from);
    sec.setText(Subject_sec);


        }
    }

}
