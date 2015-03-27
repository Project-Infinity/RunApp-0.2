package infinity.runapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ADC on 3/6/2015.
 */
public class InfinityDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "infinityDB.db";

    private static final String TABLE_USER = "User";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FNAME = "fname";
    public static final String COLUMN_LNAME = "lname";


    // constructor
    public InfinityDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + " (" +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_FNAME + " TEXT, " +
                COLUMN_LNAME + " TEXT);";

        db.execSQL(CREATE_USER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
    }

    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_FNAME, user.getFname());
        values.put(COLUMN_LNAME, user.getLname());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USER, null, values);

        db.close();
    }

    public boolean checkUser(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery("SELECT count(*) FROM " + TABLE_USER + ";", null);


        if(cur != null)
        {
            cur.moveToFirst();
            if(cur.getInt(0) == 0){
                return false;
            }
        }
        return true;
    }

    public User setUser(){
        String sql_query = "SELECT * FROM " + TABLE_USER + " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor myCursor = db.rawQuery(sql_query, null);

        User myUser = new User();

        if(myCursor.moveToFirst()){
            myUser.setEmail(myCursor.getString(1));
            myUser.setFname(myCursor.getString(2));
            myUser.setLname(myCursor.getString(3));
            myCursor.close();
        }
        else
            myUser = null;

        db.close();

        return myUser;
    }

    public void restartUserTable(){
        String DROP_USER_TABLE = "DROP TABLE " + TABLE_USER;

        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + " (" +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_FNAME + " TEXT, " +
                COLUMN_LNAME + " TEXT);";

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(DROP_USER_TABLE);
        db.execSQL(CREATE_USER_TABLE);

        }
}
