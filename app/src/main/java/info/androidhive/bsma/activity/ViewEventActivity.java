/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show all events taken from database
 */
package info.androidhive.bsma.activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import android.os.AsyncTask;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import info.androidhive.bsma.R;
import info.androidhive.bsma.helper.ManagerSession;
import info.androidhive.bsma.helper.SQLiteHandler;

public class ViewEventActivity extends AppCompatActivity {

    ListView SubjectFullFormListView;
    ProgressBar progressBar;
    String HttpUrl = "http://bsma.kiuloper.com/ANDROID_LOGIN_API/event_data.php";
    List<String> IdList = new ArrayList<>();
    private Button btnBack;
    private SQLiteHandler db;
    private ManagerSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_event);

        SubjectFullFormListView = (ListView) findViewById(R.id.listview1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnBack = (Button) findViewById(R.id.btnback);

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        new GetHttpResponse(ViewEventActivity.this).execute();

        //Adding ListView Item click Listener.
        SubjectFullFormListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(ViewEventActivity.this, ViewSingleEventActivity.class);

                // Sending ListView clicked value using intent.
                intent.putExtra("ListViewValue", IdList.get(position).toString());

                startActivity(intent);

                //Finishing current activity after open next activity.
                finish();

            }
        });
    }

    private void back() {

        db = new SQLiteHandler(getApplicationContext());
        session = new ManagerSession(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String role = user.get("role");
        if (role.equals("STUDENT")) {
            Intent intent = new Intent(ViewEventActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
        Intent intent = new Intent(ViewEventActivity.this,
                MainEventsActivity.class);
        startActivity(intent);
        finish();}
    }


    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        String JSonResult;

        List<Subject> SubjectFullFormList;

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
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServiceClass httpServicesClass = new HttpServiceClass(HttpUrl);
            try
            {
                httpServicesClass.ExecutePostRequest();

                    JSonResult = httpServicesClass.getResponse();

                    if(JSonResult != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            Subject subject;

                            SubjectFullFormList = new ArrayList<Subject>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                subject = new Subject();

                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding Student Id TO IdList Array.


                                subject.Subject_id = jsonObject.getString("BSMA_ID").toString();
                                subject.Subject_title = jsonObject.getString("BSMA_TITLE").toString();
                                subject.Subject_des = jsonObject.getString("BSMA_DES").toString();
                                subject.Subject_date = jsonObject.getString("BSMA_DATE").toString();
                                subject.Subject_due = jsonObject.getString("BSMA_DUE").toString();
                                subject.Subject_by = jsonObject.getString("BSMA_AUTHOR").toString();
                                subject.Subject_from = jsonObject.getString("BSMA_TEACHER").toString();
                                subject.Subject_sec = jsonObject.getString("BSMA_SECTOR").toString();
                                IdList.add(subject.Subject_id);
                                SubjectFullFormList.add(subject);

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
            progressBar.setVisibility(View.GONE);

            SubjectFullFormListView.setVisibility(View.VISIBLE);

            ListAdapter adapter = new ListAdapter(SubjectFullFormList, context);

            SubjectFullFormListView.setAdapter(adapter);

        }
    }
}