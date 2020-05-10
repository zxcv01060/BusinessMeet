package tw.com.bussinessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.helper.DBHelper;

public class MatchedDAO {
    private String whereClause = "m_sno = ?";
    private String tableName = "Matched";
    private String[] column = new String[]{"blue_tooth","matched_blue_tooth","memorandum","create_date","modify_date"};
    private SQLiteDatabase db;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public MatchedDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(MatchedBean matchedBean){
        ContentValues values = new ContentValues();
        values.put(column[0],matchedBean.getBlueTooth());
        values.put(column[1],matchedBean.getMatchedBlueTooth());
        values.put(column[2],matchedBean.getMemorandum());
        return values;
    }
    public void add(MatchedBean matchedBean){
        if(matchedBean.getMemorandum() == null || matchedBean.getMemorandum().equals("")) matchedBean.setMemorandum("");
        ContentValues values = putValues(matchedBean);
        values.put(column[3],dataFormat.format(new Date()));
        db.insert(tableName,null,values);
    }
    public void update(MatchedBean matchedBean){
        ContentValues values = putValues(matchedBean);
        values.put(column[4],dataFormat.format(new Date()));
        db.update(tableName,values,whereClause,new String[]{String.valueOf(matchedBean.getMSno())});
        db.close();
    }

    public Cursor search(MatchedBean matchedBean){
        String blueTooth = matchedBean.getBlueTooth();
        String matchedBlueTooth = matchedBean.getMatchedBlueTooth();
        String[] searchValue = new String[]{blueTooth,matchedBlueTooth};
        String[] searchColumn = new String[]{column[0],column[1]};
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

    public String getById(String blueTooth) {
        Cursor cursor = db.query(tableName, null, "blue_tooth = ?", new String[]{blueTooth}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("blue_tooth");
        try {
            return cursor.getString(cursor.getColumnIndex("blue_tooth"));
        } catch (Exception e) {
            return null;
        }
    }

}
