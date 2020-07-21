/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will only show the user's qr code (used for students only)
 */
package info.androidhive.bsma.activity;
import info.androidhive.bsma.helper.SQLiteHandler;
import info.androidhive.bsma.helper.ManagerSession;
import java.util.HashMap;
import info.androidhive.bsma.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrSActivity extends Activity {
    private static final String TAG = QrSActivity.class.getSimpleName();
    private Button btnBack;
    private ImageView image;
    private String text2Qr;
    private ManagerSession session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrs);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnBack = (Button) findViewById(R.id.btnback);
        image = (ImageView) findViewById(R.id.image);
        final Activity activity = this;

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

    private void back() {
        Intent intent = new Intent(QrSActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(QrSActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}