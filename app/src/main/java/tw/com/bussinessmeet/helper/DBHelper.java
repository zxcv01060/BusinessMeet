package tw.com.bussinessmeet.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
    private final static int _DBVersion = 1;
    private final static String _DBName = "BeMet.db";
    private final static String[] _TableName = new String[]{"user_information","user_customization","friend","groups","friend_group","friend_customization","friend_label","friend_remark","timeline_properties","timeline","activity_label","activity_remind","activity_invite","problem_report"};

    public DBHelper(Context context) {
            super(context, _DBName, null, _DBVersion);
            SQLiteDatabase   db = getWritableDatabase();
            Log.d("add",context.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("add","create");
        String SQL = "create table if not exists"+ _TableName[0] + "("+
                "user_no int AUTOINCREMENT primary key,"+
                "account varchar(100) not null,"+
                "password varchar(64) not null,"+
                "name nvarchar(100) not null,"+
                "gender  char(2) not null,"+
                "mail    varchar(100) not null,"+
                "profession  nvarchar(100) not null,"+
                "bluetooth   varchar(17) not null,"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[1] + "("+
                "user_customization_no   int AUTOINCREMENT primary key,"+
                "user_no int  not null references"+ _TableName[0] + "(user_no)  ,"+
                "column_name nvarchar(100) not null,"+
                "content nvarchar(1000) not null,"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[2] + "("+
                "friend_no   int AUTOINCREMENT primary key,"+
                "matchmaker  int not null references"+ _TableName[0] + "(user_no),  "+
                "friend  int not null references"+ _TableName[0] + "(user_no),"+
                "remark nvarchar(2500),"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[3] + "("+
                "group_no    int AUTOINCREMENT   primary key,"+
                "name    nvarchar(100) not null,     "+
                "user_no int not null references"+ _TableName[0] + "(user_no),"+
                "create_date datetime not null,"+
                "modify_date datetime    "+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[4] + "("+
                "friendGroup_no  int AUTOINCREMENT primary key,"+
                "group_no    int not null references"+ _TableName[3] + "(group_no),   "+
                "friend_no   int not null references"+ _TableName[2] + "(friend_no),"+
                "create_date datetime not null,"+
                "modify_date datetime    "+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[5] + "("+
                "friend_customization_no int AUTOINCREMENT   primary key,"+
                "name    nvarchar(100) not null,"+
                "friend_no   int not null references"+ _TableName[2] + "(friend_no),"+
                "user_no int not null references"+ _TableName[0] + "(user_no),"+
                "create_date datetime not null,"+
                "modify_date datetime    "+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[6] + "("+
                "friend_label_no int AUTOINCREMENT primary key,"+
                "content nvarchar(50) not null,"+
                "friend_customization_no not null int    references"+ _TableName[5] + "(friend_customization_no),"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[7] + "("+
                "friendRemarks_no    int AUTOINCREMENT primary key,"+
                "friendLabel_no  int not null references"+ _TableName[6] + "(friend_label_no),"+
                "friend_customization_no int not null references"+ _TableName[5] + "(friend_customization_no),  "+
                "create_date datetime not null,"+
                "modify_date datetime    "+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[8] + "("+
                "timeline_properties_no  int AUTOINCREMENT primary key,"+
                "name    nvarchar(100) not null,"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[9] + "("+
                "timeline_no int AUTOINCREMENT primary key,"+
                "place   nvarchar(100) not null,"+
                "title   nvarchar(100) not null,     "+
                "remark  nvarchar(2500),     "+
                "timeline_properties_no  int not null    references"+ _TableName[8] + "(timeline_properties_no),"+
                "color varchar(7),"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
                SQL = "create table if not exists"+ _TableName[10] + "("+
                "activityLabel_no    int AUTOINCREMENT   primary key,"+
                "activity_no int not null references"+ _TableName[9] + "(timeline_no),  "+
                "content nvarchar(100) not null,"+
                "create_date datetime not null,"+
                "modify_date datetime        "+
                ");";
        db.execSQL(SQL);
                SQL = "create table if not exists"+ _TableName[11] + "("+
                "activity_remind_no  int  AUTOINCREMENT primary key,"+
                "time datetime not null,     "+
                "activity_no int not null references"+ _TableName[9] + "(timeline_no),"+
                "create_date datetime not null ,"+
                "modify_date datetime    "+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[12] + "("+
                "activityInvite_no   int AUTOINCREMENT primary key,"+
                "user_no int not null references"+ _TableName[0] + "(user_no),  "+
                "activity_no int not null references"+ _TableName[9] + "(timeline_no),"+
                "create_date datetime not null ,"+
                "modify_date datetime    "+
                ");";
        db.execSQL(SQL);
        SQL = "create table if not exists"+ _TableName[13] + "("+
                "problem_report_no   int AUTOINCREMENT primary key,"+
                "content nvarchar(3000) not null ,"+
                "user_no int not null references"+ _TableName[0] + "(user_no),"+
                "create_date datetime not null,"+
                "modify_date datetime"+
                ");";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL = "DROP TABLE " + _TableName[1];
        db.execSQL(SQL);
    }
}
