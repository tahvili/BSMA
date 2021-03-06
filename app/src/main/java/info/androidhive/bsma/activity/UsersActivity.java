/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show the users for each event
 */
package info.androidhive.bsma.activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.   graphics.   Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import info.androidhive.bsma.R;
import info.androidhive.bsma.app.AppConfig;
import info.androidhive.bsma.app.AppController;
import info.androidhive.bsma.helper.ManagerSession;

public class UsersActivity extends Activity {

    private static final String TAG = UsersActivity.class.getSimpleName();
    String data = "";
    TableLayout tl;
    TableRow tr;
    TextView label;
    String Subject_id;
    private Button btnBack;
    private String useroutid;
    private String inOuts;
    private ProgressDialog pDialog;
    private ManagerSession session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_students_list);

        btnBack = (Button) findViewById(R.id.btnback);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        tl = (TableLayout) findViewById(R.id.maintable);

        Subject_id = getIntent().getStringExtra("BSMA_ID");

        final GetDataFromDB getdb = new GetDataFromDB();
        new Thread(new Runnable() {
            public void run() {
                data = getdb.getDataFromDB();
                System.out.println(data);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ArrayList<Users> users = parseJSON(data);
                        addData(users);
                    }
                });

            }
        }).start();
    }

    public ArrayList<Users> parseJSON(String result) {
        ArrayList<Users> users = new ArrayList<Users>();
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Users user = new Users();
                user.setId(json_data.getInt("BSMA_ID"));
                user.setFirst(json_data.getString("BSMA_FIRST"));
                user.setLast(json_data.getString("BSMA_LAST"));
                user.setCheck(json_data.getString("BSMA_EVENTS"));
                users.add(user);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
        return users;
    }

    void addHeader(){
        /** Create a TableRow dynamically **/
        tr = new TableRow(this);


        /** Creating a TextView to add to the row **/
        label = new TextView(this);
            label.setText("");
        label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        label.setPadding(5, 5, 5, 5);

        LinearLayout Ll = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(label,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        /** Creating Qty Button **/
        TextView last = new TextView(this);
        last.setText("");
        last.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        last.setPadding(5, 5, 5, 5);

        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(last,params);
        tr.addView((View)Ll); // Adding textview to tablerow.


        /** Creating Qty Button **/
        TextView checkbox = new TextView(this);
        checkbox.setText("");
        checkbox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        checkbox.setPadding(5, 5, 5, 5);
        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(checkbox,params);
        tr.addView((View)Ll); // Adding textview to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    @SuppressWarnings({ "rawtypes" })
    public void addData(ArrayList<Users> users) {

        addHeader();

        for (Iterator i = users.iterator(); i.hasNext();) {

            final Users p = (Users) i.next();

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);

            /** Creating a TextView to add to the row **/
            label = new TextView(this);
            label.setText(p.getFirst());
            label.setId(p.getId());
            label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            label.setPadding(20, 26, 20, 26);
            label.setBackgroundColor(Color.WHITE);
            LinearLayout Ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(label,params);
            tr.addView((View)Ll); // Adding textView to tablerow.


            /** Creating Qty Button **/
            TextView last = new TextView(this);
            last.setText(p.getLast());
            last.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            last.setPadding(20, 26, 5, 26);
            last.setBackgroundColor(Color.WHITE);
            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(last,params);
            tr.addView((View)Ll); // Adding textview to tablerow.


            /** Creating Qty Button **/
            CheckBox inOut = new CheckBox(this);
            String check = p.getCheck();
            boolean search = check.indexOf("kiu" + Subject_id) >= 0;
            inOut.setChecked(search);
            inOut.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            inOut.setPadding(5, 5, 5, 5);
            inOut.setBackgroundColor(Color.WHITE);
            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(inOut,params);
            tr.addView((View)Ll); // Adding textview to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            inOuts = check;
            inOut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    useroutid = String.valueOf(p.getId());
                    boolean search = inOuts.indexOf("kiu" + Subject_id) >= 0;
                    String value = "kiu" + Subject_id;
                    useroutid = useroutid.toString();
                    if (search) {inOuts=inOuts.replaceAll(value ,"");}
                    else {inOuts=inOuts + "kiu" + Subject_id;}

                    updateUser(useroutid, inOuts);

                }
            });

        }

    }

    private void updateUser(final String id, final String inOuts) {
        // Tag used to cancel the request
        String tag_string_req = "req_update";
        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_USERUPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();
                Toast.makeText(UsersActivity.this,"Updated", Toast.LENGTH_LONG).show();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    System.out.print("is there error? " + error);
                    if (!error) {
                        Toast.makeText(UsersActivity.this,"User successfully updated.", Toast.LENGTH_LONG).show();
                        // Launch login activity
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Toast.makeText(UsersActivity.this,errorMsg, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "error kiu: " + getApplicationContext() );
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG,
                            "something is wrong: " + e);
                    hideDialog();
                }
                finish();
                startActivity(getIntent());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("update error area");
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("inOuts", inOuts);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void back() {
        Intent intent = new Intent(UsersActivity.this,
                ViewEventActivity.class);
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