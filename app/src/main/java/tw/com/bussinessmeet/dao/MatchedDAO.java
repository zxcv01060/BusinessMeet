package tw.com.bussinessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.bussinessmeet.bean.FriendBean;
import tw.com.bussinessmeet.helper.DBHelper;

public class MatchedDAO {
    private String whereClause = "m_sno = ?";
    private String tableName = "Matched";
    private String[] column = new String[]{"friend_no","matchmaker_id","friend_id","remark","create_date","modify_date"};
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public MatchedDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(FriendBean friendBean){
        ContentValues values = new ContentValues();
        values.put(column[0],friendBean.getFriendNo());
        values.put(column[1],friendBean.getMatchmakerId());
        values.put(column[2],friendBean.getFriendId());
        values.put(column[3],friendBean.getRemark());
        values.put(column[4],friendBean.getCreateDate());
        values.put(column[5],friendBean.getModifyDate());
        return values;
    }
    public void add(FriendBean friendBean){
        if(friendBean.getRemark() == null || friendBean.getRemark().equals("")) friendBean.setRemark("");
        ContentValues values = putValues(friendBean);
        values.put(column[4],dataFormat.format(new Date()));
        db.insert(tableName,null,values);
    }
    public void update(FriendBean friendBean){
        ContentValues values = putValues(friendBean);
        values.put(column[5],dataFormat.format(new Date()));
        db.update(tableName,values,whereClause,new String[]{String.valueOf(friendBean.getFriendNo())});
        db.close();
    }

    public Cursor search(FriendBean friendBean){
        String matchmakerId = friendBean.getMatchmakerId();
        String friendId = friendBean.getFriendId();
        String[] searchValue = new String[]{matchmakerId,friendId};
        String[] searchColumn = new String[]{column[1],column[2]};
        String where = "";
        ArrayList<String> args = new ArrayList<>();
        for(int i = 0; i < searchColumn.length; i ++){
            if(!searchValue[i].equals("") && searchValue[i] != null){
                if(!where.equals("")) where += " and ";
                where += searchColumn[i] + " = ?";
                args.add(searchValue[i]);
            }
        }
        Cursor cursor = db.query(tableName, column, where, args.toArray(new String[0]),null,null,null);
        if(cursor.moveToFirst()) {
            return cursor;
        }else{
            return null;
        }
    }

    // getById

//    public String getById(String blueTooth) {
//        Cursor cursor = db.query(tableName, null, "blue_tooth = ?", new String[]{blueTooth}, null, null, null);
//        cursor.moveToFirst();
//        int index = cursor.getColumnIndex("blue_tooth");
//        try {
//            return cursor.getString(cursor.getColumnIndex("blue_tooth"));
//        } catch (Exception e) {
//            return null;
//        }
//    }

}
