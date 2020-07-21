/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class let the user edit one's profile
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.app.AppConfig;
import info.androidhive.bsma.app.AppController;
import info.androidhive.bsma.helper.SQLiteHandler;
import info.androidhive.bsma.helper.ManagerSession;
import java.util.HashMap;
import java.util.Map;
import info.androidhive.bsma.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends Activity {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private Button btnLinkToLogin;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputAge;
    private EditText inputPhone;
    private CheckBox inputbtv;
    private CheckBox inputcrew;
    private EditText inputAddress;
    private EditText inputConfirmPassword;
    private EditText inputPassword;
    private Button btnUpdate;
    private Button btnBack;
    private ProgressDialog pDialog;
    private ManagerSession session;
    private static SQLiteHandler db;
    private RadioGroup rg_grade;
    private RadioButton rad_09;
    private RadioButton rad_10;
    private RadioButton rad_11;
    private RadioButton rad_12;
    private RadioGroup rg_path;
    private RadioButton rad_uni;
    private RadioButton rad_college;
    private RadioButton rad_work;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        inputFirstName = (EditText) findViewById(R.id.firstName);
        inputLastName = (EditText) findViewById(R.id.lastName);
        inputAge= (EditText) findViewById(R.id.age);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputAddress = (EditText) findViewById(R.id.address);
        inputbtv = (CheckBox) findViewById(R.id.check_btv);
        inputcrew = (CheckBox) findViewById(R.id.check_crew);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        rg_grade = (RadioGroup) findViewById(R.id.radg_grade);
        rad_09 = (RadioButton) findViewById(R.id.rad_09);
        rad_10 = (RadioButton) findViewById(R.id.rad_10);
        rad_11 = (RadioButton) findViewById(R.id.rad_11);
        rad_12 = (RadioButton) findViewById(R.id.rad_12);
        rg_path = (RadioGroup) findViewById(R.id.radg_path);
        rad_uni = (RadioButton) findViewById(R.id.rad_uni);
        rad_college = (RadioButton) findViewById(R.id.rad_college);
        rad_work = (RadioButton) findViewById(R.id.rad_work);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnBack = (Button) findViewById(R.id.btnback);

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
        final String email = user.get("email");

        // Displaying the user details on the screen
        inputFirstName.setText(firstname);
        inputLastName.setText(lastname);
        inputAge.setText(age);
        inputPhone.setText(phone);
        inputAddress.setText(add);



        if (grade.equals("09")) {rg_grade.check(rad_09.getId());}
        else if (grade.equals("10")) {rg_grade.check(rad_10.getId());}
        else if (grade.equals("11")) {rg_grade.check(rad_11.getId());}
        else if (grade.equals("12")) {rg_grade.check(rad_12.getId());}

        if (path.equals("University")) {rg_path.check(rad_uni.getId());}
        else if (path.equals("College")) {rg_path.check(rad_college.getId());}
        else if (path.equals("Workshop")) {rg_path.check(rad_work.getId());}



        if (sector.equals("Brebeuf TV & Tech Crew")) {
            inputbtv.setChecked(true);
            inputcrew.setChecked(true);
        }
        else if(sector.equals("Brebeuf TV")) {
            inputbtv.setChecked(true);
            inputcrew.setChecked(false);
        }
        else if(sector.equals("Tech Crew")) {
            inputcrew.setChecked(true);
            inputbtv.setChecked(false);
        }
        else {
            inputbtv.setChecked(false);
            inputcrew.setChecked(false);
        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int selectedRadioButtonID = rg_grade.getCheckedRadioButtonId();
                String grade="";
                if (selectedRadioButtonID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                    grade = selectedRadioButton.getText().toString();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Please select your grade!", Toast.LENGTH_LONG)
                            .show();
                }

                int selectedRadioID = rg_path.getCheckedRadioButtonId();
                String path="";
                if (selectedRadioID != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioID);
                    path = selectedRadioButton.getText().toString();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Please select your pathway!", Toast.LENGTH_LONG)
                            .show();
                }


                String firstname = inputFirstName.getText().toString().trim();
                String lastname = inputLastName.getText().toString().trim();
                String age = inputAge.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();
                String phone = PhoneNumberUtils.formatNumber(inputPhone.getText().toString().trim());
                String password = inputPassword.getText().toString().trim();
                String confirm_password = inputConfirmPassword.getText().toString().trim();
                String sector = "";

                if(inputbtv.isChecked()&&inputcrew.isChecked())
                {
                    sector="Brebeuf TV & Tech Crew";
                }else if (inputbtv.isChecked()) {sector="Brebeuf TV";}
                else if (inputcrew.isChecked()) {sector="Tech Crew";}
                else {Toast.makeText(getApplicationContext(),
                        "Please select a sector!", Toast.LENGTH_LONG)
                        .show();}



                if (!firstname.isEmpty() && !lastname.isEmpty() && !address.isEmpty() && !age.isEmpty() && !phone.isEmpty()) {
                if(password.length() ==0){
                         updateUser(firstname, lastname, age, phone, grade, path, address, sector, email);
                    }
                else if (password.length() >= 8) {
                    if (confirm_password.equals(password)) {
                        updateUserNew(firstname, lastname, age, phone, grade, path, address, sector, email, password);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "The passwords do not match!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                else {Toast.makeText(getApplicationContext(),
                        "Enter a password that is more than 8 characters! ", Toast.LENGTH_LONG)
                        .show();}


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });
    }



    private void updateUser(final String firstname, final String lastname, final String age, final String phone,
                              final String grade, final String path, final String address, final String sector, final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_update";

        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("num");

                        JSONObject user = jObj.getJSONObject("user");
                        String firstname = user.getString("first");
                        String lastname = user.getString("last");
                        String age = user.getString("age");
                        String phone = user.getString("phone");
                        String grade = user.getString("grade");
                        String path = user.getString("path");
                        String address = user.getString("add");
                        String sector = user.getString("sector");
                        String email = user.getString("email");


                        // Inserting row in users table
                        db.updateUser(firstname, lastname, age, phone, grade, path, address, sector, email);
                        Toast.makeText(getApplicationContext(), "User successfully updated.", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(
                                ProfileActivity.this,
                                LoginActivity.class);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("age", age);
                params.put("phone", phone);
                params.put("grade", grade);
                params.put("path", path);
                params.put("address", address);
                params.put("sector", sector);
                params.put("email", email);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void updateUserNew(final String firstname, final String lastname, final String age, final String phone,
                               final String grade, final String path, final String address, final String sector,
                               final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_reqe = "reqe_update_password";

        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReqe = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATEPASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Password Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("num");

                        JSONObject user = jObj.getJSONObject("user");
                        String firstname = user.getString("first");
                        String lastname = user.getString("last");
                        String age = user.getString("age");
                        String phone = user.getString("phone");
                        String grade = user.getString("grade");
                        String path = user.getString("path");
                        String address = user.getString("add");
                        String sector = user.getString("sector");
                        String email = user.getString("email");

                        // Inserting row in users table
                        db.updateUser(firstname, lastname, age, phone, grade, path, address, sector, email);
                        Toast.makeText(getApplicationContext(), "User successfully updated. Try login again!", Toast.LENGTH_LONG).show();
                        // Launch login activity

                        session.setLogin(false);

                        db.deleteUsers();

                        // Launching the login activity
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("age", age);
                params.put("phone", phone);
                params.put("grade", grade);
                params.put("path", path);
                params.put("address", address);
                params.put("sector", sector);
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReqe, tag_string_reqe);
    }




    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void back() {
        Intent intent = new Intent(ProfileActivity.this,
                MainActivity.class);
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