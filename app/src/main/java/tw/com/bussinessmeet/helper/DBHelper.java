package tw.com.bussinessmeet.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
    private final static int _DBVersion = 1;
    private final static String _DBName = "Business_Meet.db";
    private final static String[] _TableName = new String[]{"User_Information","Matched"};

    public DBHelper(Context context) {
        super(context, _DBName, null, _DBVersion);
        SQLiteDatabase   db = getWritableDatabase();
        Log.d("add",context.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("add","create");
        String SQL = "CREATE TABLE IF NOT EXISTS "+ _TableName[0] + "( " +
                        "blue_tooth varchar(18) PRIMARY KEY , " +
                        "user_name NVARCHAR(100) NOT NULL, " +
                        "company NVARCHAR(100) NOT NULL, " +
                        "position NVARCHAR(100), " +
                        "email VARCHAR(100)," +
                        "tel VARCHAR(20)," +
                        "avatar NVARCHAR(1000)" +
                    ");";
        SQL += "CREATE TABLE IF NOT EXISTS" + _TableName[1] + "( " +
                    "m_sno INT PRIMARY KEY IDENTITY, " +
                    "blue_tooth VARCHAR(18) REFERENCES " + _TableName[0] +"(blue_tooth), " +
                    "matched_blue_tooth VARCHAR(18) REFERENCES " + _TableName[0] + "(blue_tooth),"+
                    "memorandum VARCHAR(1000)" +
                ");";
        db.execSQL(SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL = "DROP TABLE " + _TableName[1];
        db.execSQL(SQL);
    }
}
