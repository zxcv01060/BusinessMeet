package tw.com.bussinessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.helper.DBHelper;

public class UserInformationDAO {
    private String whereClause = "blue_tooth = ?";
    private String tableName = "User_Information";
    private  String[] column = new String[]{"blue_tooth", "user_name", "company", "position","email","tel","avatar","create_date","modify_date"};
    private SQLiteDatabase db ;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public UserInformationDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(UserInformationBean userInformationBean){
        ContentValues values = new ContentValues();
        values.put("blue_tooth", userInformationBean.getBlueTooth());
        values.put("user_name", userInformationBean.getUserName());
        values.put("company",userInformationBean.getCompany());
        values.put("position", userInformationBean.getPosition());
        values.put("email",userInformationBean.getEmail());
        values.put("tel",userInformationBean.getTel());
        values.put("avatar", userInformationBean.getAvatar());
        return  values;
    }
    public void add (UserInformationBean userInformationBean){
        Log.d("add:","add");

        Log.d("add","dbsuccess");
        ContentValues values = putValues(userInformationBean);

        String createDate = dateFormat.format(new Date());
        values.put("create_date",createDate);
        db.insert(tableName, null, values);
        db.close();
    }

    public void update(UserInformationBean userInformationBean){

        ContentValues values = putValues(userInformationBean);
        String modifyDate = dateFormat.format(new Date());
        values.put("modify_date",modifyDate);
    //範例
//        String[] whereArgs1 = {"#100", b.getStorage_id()};
//        String whereClause1 = DatabaseSchema.TABLE_TALKS.COLUMN_TID + "=? AND " + DatabaseSchema.TABLE_TALKS.COLUMN_STORAGEID + "=?";
//        db.update(DatabaseSchema.TABLE_TALKS.NAME, values1, whereClause1, whereArgs1);
        db.update(tableName, values,whereClause , new String[]{userInformationBean.getBlueTooth()});
        db.close();
    }
    public  void delete(String blueTooth){

        db.delete(tableName, whereClause,new String[]{blueTooth});
        db.close();
    }
    public String getById(String blueTooth) {
        Cursor cursor = db.query(tableName, null, "blue_tooth = ?", new String[]{blueTooth}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("blue_tooth");
        try{
            return cursor.getString(cursor.getColumnIndex("blue_tooth"));
        }catch (Exception e){
            return null;
        }

    }
    public Cursor searchAll(UserInformationBean userInformationBean){

        String blueTooth = userInformationBean.getBlueTooth();
        String userName = userInformationBean.getUserName();
        String company = userInformationBean.getCompany();
        String position = userInformationBean.getPosition();
        String[] searchValue = new String[]{blueTooth,userName,company,position};
        String[] searchColumn = new String[]{column[0],column[1],column[2],column[3]};
        String where = "";
        ArrayList<String> args = new ArrayList<>();

        for(int i = 0; i < searchColumn.length; i ++){
            if(searchValue[i] != null && !searchValue[i].equals("") ){
                if(!where.equals("")) where += " and ";
                where += searchColumn[i] + " = ?";
                args.add(searchValue[i]);
            }
        }
        Cursor cursor = db.query(tableName, column, where,args.toArray(new String[0]),null,null,null);
        if(cursor.moveToFirst()) {
            return cursor;
        }else{
            return null;
        }
    }



}
