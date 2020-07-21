package info.androidhive.bsma.activity;
/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will all qr related buttons and the user's qr code
 */
import info.androidhive.bsma.app.AppConfig;
import info.androidhive.bsma.app.AppController;
import info.androidhive.bsma.helper.SQLiteHandler;
import info.androidhive.bsma.helper.ManagerSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import info.androidhive.bsma.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import org.json.JSONException;
import org.json.JSONObject;

public class QrActivity extends Activity {
    private static final String TAG = QrActivity.class.getSimpleName();
    String HttpUrlDeleteRecord = "http://bsma.kiuloper.com/ANDROID_LOGIN_API/create_qr.php";
    HttpParse httpParse = new HttpParse();
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    ProgressDialog progressDialog2;
    private Button btnBack;
    private Button btnCreate;
    private Button btnView;
    private Button btnScan;
    private ImageView image;
    private String text2Qr;
    private ManagerSession session;
    private SQLiteHandler db;
    private String Subject_email, Subject_time;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnBack = (Button) findViewById(R.id.btnback);
        btnCreate = (Button) findViewById(R.id.btncreate);
        btnView = (Button) findViewById(R.id.btnview);
        btnScan = (Button) findViewById(R.id.btnscan);
        image = (ImageView) findViewById(R.id.image);
        final Activity activity = this;
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR code for BSMA");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setCaptureActivity(CaptureActivityPortait.class);
                integrator.initiateScan();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteQr("ALL");
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new ManagerSession(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = db.getUserDetails();

        text2Qr = user.get("email");

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

    }

    private void view() {
        Intent intent = new Intent(QrActivity.this,
                QrListActivity.class);
        startActivity(intent);
        finish();

    }

    private void back() {
        Intent intent = new Intent(QrActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(QrActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void DeleteQr(final String BSMA_ID) {

        class EventDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog2 = ProgressDialog.show(QrActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog2.dismiss();

                Toast.makeText(QrActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Subject_email = result.getContents();
                DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                Date dateobj = new Date();
                Subject_time = df.format(dateobj);
                StudentRecordUpdate(Subject_email, Subject_time);

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void StudentRecordUpdate(final String Subject_email, final String Subject_time){

        String tag_string_req = "req_update";
        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERTQR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString() );
                hideDialog();
                Toast.makeText(QrActivity.this,response.toString() , Toast.LENGTH_LONG).show();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    System.out.print("is there error? " + error);
                    if (!error) {
                        Toast.makeText(QrActivity.this,"User successfully updated.", Toast.LENGTH_LONG).show();
                        // Launch login activity
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Toast.makeText(QrActivity.this,errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("Subject_email", Subject_email);
                params.put("Subject_time", Subject_time);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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