/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class is to create a temp sql database in the phone
 */
package info.androidhive.bsma.helper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "users";

    // Login Table Columns names
    private static final String KEY_ID = "BSMA_ID";
    private static final String KEY_FIRSTNAME = "BSMA_FIRST";
    private static final String KEY_LASTNAME = "BSMA_LAST";
    private static final String KEY_AGE = "BSMA_AGE";
    private static final String KEY_PHONE = "BSMA_PHONE";
    private static final String KEY_GRADE = "BSMA_GRADE";
    private static final String KEY_PATH = "BSMA_PATH";
    private static final String KEY_ADD = "BSMA_ADD";
    private static final String KEY_SECTOR = "BSMA_SECTOR";
    private static final String KEY_EMAIL = "BSMA_EMAIL";
    private static final String KEY_ROLE = "BSMA_ROLE";
    private static final String KEY_EVENTS = "BSMA_EVENTS";
    private static final String KEY_UID = "BSMA_NUM";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT," +
                KEY_AGE + " TEXT," + KEY_GRADE + " TEXT," + KEY_PATH + " TEXT," +
                KEY_ADD + " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_SECTOR +
                " TEXT," + KEY_PHONE + " TEXT," + KEY_ROLE + " TEXT," + KEY_EVENTS + " TEXT," + KEY_UID + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }


    /**
     * Storing user details in database
     * */
    public void addUser(String firstname, String lastname, String age, String phone, String grade,  String path,  String address, String sector, String email, String role, String events, String uid) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, firstname);
        values.put(KEY_LASTNAME, lastname);
        values.put(KEY_AGE, age);
        values.put(KEY_GRADE, grade);
        values.put(KEY_PATH, path);
        values.put(KEY_ADD, address);
        values.put(KEY_SECTOR, sector);
        values.put(KEY_PHONE, phone);
        values.put(KEY_EMAIL, email);
        values.put(KEY_ROLE, role);
        values.put(KEY_EVENTS, events);
        values.put(KEY_UID, uid);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);

    }


    public void updateUser(String firstname, String lastname, String age, String phone, String grade,  String path,  String address, String sector, String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, firstname);
        values.put(KEY_LASTNAME, lastname);
        values.put(KEY_AGE, age);
        values.put(KEY_GRADE, grade);
        values.put(KEY_PATH, path);
        values.put(KEY_ADD, address);
        values.put(KEY_SECTOR, sector);
        values.put(KEY_PHONE, phone);
        values.put(KEY_EMAIL, email);
        // Inserting Row
        long id = db.update(TABLE_USER, values,"BSMA_EMAIL = ?",new String[] { email });
        db.close(); // Closing database connection

        Log.d(TAG, "User updated in sqlite: " + id);

    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("firstname", cursor.getString(1));
            user.put("lastname", cursor.getString(2));
            user.put("age", cursor.getString(3));
            user.put("grade", cursor.getString(4));
            user.put("path", cursor.getString(5));
            user.put("add", cursor.getString(6));
            user.put("email", cursor.getString(7));
            user.put("sector", cursor.getString(8));
            user.put("phone", cursor.getString(9));
            user.put("role", cursor.getString(10));
            user.put("events", cursor.getString(11));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
