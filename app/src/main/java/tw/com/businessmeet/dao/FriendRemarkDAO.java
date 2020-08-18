package tw.com.businessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import tw.com.businessmeet.bean.FriendRemarkBean;
import tw.com.businessmeet.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FriendRemarkDAO {
    private String tableName = "friend_remark";
    private String[] column = FriendRemarkBean.getColumn();
    private String whereClause = column[0] + " = ?";
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public FriendRemarkDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(FriendRemarkBean friendRemarkBean){
        ContentValues values = new ContentValues();
        values.put(column[0],friendRemarkBean.getFriendRemarksNo());
        values.put(column[1],friendRemarkBean.getFriendLabelNo());
        values.put(column[2],friendRemarkBean.getFriendCustomizationNo());
        values.put(column[3],friendRemarkBean.getCreateDate());
        values.put(column[4],friendRemarkBean.getModifyDate());
        return values;
    }
    public void add(FriendRemarkBean friendRemarkBean){
        ContentValues values = putValues(friendRemarkBean);
        values.put("create_date",dataFormat.format(new Date()));
        db.insert(tableName,null,values);
    }
    public void update(FriendRemarkBean friendRemarkBean){
        ContentValues values = putValues(friendRemarkBean);
        values.put("modify_date",dataFormat.format(new Date()));
        db.update(tableName,values,whereClause,new String[]{String.valueOf(friendRemarkBean.getFriendRemarksNo())});

    }

    public Cursor search(FriendRemarkBean friendRemarkBean){
        Integer friendCustomizationNo = friendRemarkBean.getFriendCustomizationNo();
        Integer[] searchValue = new Integer[]{friendCustomizationNo};
        String[] searchColumn = new String[]{column[2]};
        String where = "";
        ArrayList<Integer> args = new ArrayList<>();
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
