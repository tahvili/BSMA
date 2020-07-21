/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will show the users that had their qr codes scanned
 */
package info.androidhive.bsma.activity;
import java.util.ArrayList;
import java.util.Iterator;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import info.androidhive.bsma.R;

public class QrListActivity extends Activity {

    private static final String TAG = QrListActivity.class.getSimpleName();
    String data = "";
    TableLayout tl;
    TableRow tr;
    TextView label;
    String Subject_id;
    private Button btnBack;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_list);

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

        final GetQrFromDB getdb = new GetQrFromDB();
        new Thread(new Runnable() {
            public void run() {
                data = getdb.getQrFromDB();
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
                user.setCheck(json_data.getString("BSMA_TIME"));
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
        label.setText("FIRST NAME");
        label.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        label.setPadding(20, 26, 20, 26);

        LinearLayout Ll = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(label,params);
        tr.addView((View)Ll); // Adding textView to tablerow.

        /** Creating Qty Button **/
        TextView last = new TextView(this);
        last.setText("LAST NAME");
        last.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        last.setPadding(20, 26, 20, 26);

        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(last,params);
        tr.addView((View)Ll); // Adding textview to tablerow.


        /** Creating Qty Button **/
        TextView checkbox = new TextView(this);
        checkbox.setText("TIME STARTED");
        checkbox.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        checkbox.setPadding(20, 26, 20, 26);
        Ll = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 5, 5);
        //Ll.setPadding(10, 5, 5, 5);
        Ll.addView(checkbox,params);
        tr.addView((View)Ll); // Adding textview to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
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
            label.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            label.setPadding(20, 26, 20, 26);
            label.setBackgroundColor(Color.WHITE);
            LinearLayout Ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(label,params);
            tr.addView((View)Ll); // Adding textView to tablerow.


            /** Creating Qty Button **/
            TextView last = new TextView(this);
            last.setText(p.getLast());
            last.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            last.setPadding(20, 26, 5, 26);
            last.setBackgroundColor(Color.WHITE);
            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(last,params);
            tr.addView((View)Ll); // Adding textview to tablerow.

            /** Creating Qty Button **/
            TextView check = new TextView(this);
            check.setText(p.getCheck());
            check.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            check.setPadding(20, 26, 5, 26);
            check.setBackgroundColor(Color.WHITE);
            Ll = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            //Ll.setPadding(10, 5, 5, 5);
            Ll.addView(check,params);
            tr.addView((View)Ll); // Adding textview to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

        }

    }

    private void back() {
        Intent intent = new Intent(QrListActivity.this,
                QrActivity.class);
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