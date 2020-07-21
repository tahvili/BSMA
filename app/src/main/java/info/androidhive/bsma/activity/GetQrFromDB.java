/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class to take information from database
 */
package info.androidhive.bsma.activity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


public class GetQrFromDB {

    //This class is used to grab the qr code from the database
    public String getQrFromDB() {
        try {

            HttpPost httppost;
            HttpClient httpclient;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(
                    "http://bsma.kiuloper.com/ANDROID_LOGIN_API/get_qrs.php");
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost,
                    responseHandler);

            return response.trim();

        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
            return "error";
        }
    }
}