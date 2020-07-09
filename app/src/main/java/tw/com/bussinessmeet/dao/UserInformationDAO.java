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
    private String whereClause = "user_id = ?";
    private String tableName = "user_information";
    private  String[] column = new String[]{ "user_id","password","name", "gender","mail","profession","bluetooth","avatar","tel","create_date","modify_date"};
    private SQLiteDatabase db ;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public UserInformationDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(UserInformationBean userInformationBean){
        ContentValues values = new ContentValues();
        String userId = userInformationBean.getUserId();
        String password = userInformationBean.getPassword();
        String name = userInformationBean.getName();
        String gender = userInformationBean.getGender();
        String mail = userInformationBean.getMail();
        String profession = userInformationBean.getProfession();
        String tel = userInformationBean.getTel();
        String avatar = userInformationBean.getAvatar();
        String bluetooth = userInformationBean.getBluetooth();
        if(userId != null && !userId.equals("")){
            values.put("user_id", userInformationBean.getUserId());
        }
        if(password != null && !password.equals("")){
            values.put("password", userInformationBean.getPassword());
        }
        if(name != null && !name.equals("")){
            values.put("name", userInformationBean.getName());
        }
        if(mail != null && !mail.equals("")){
            values.put("mail",userInformationBean.getMail());
        }
        if(gender != null && !gender.equals("")){
            values.put("gender",userInformationBean.getGender());
        }
        if(profession != null && !profession.equals("")){
            values.put("profession", userInformationBean.getProfession());
        }
        if(tel != null && !tel.equals("")){
            values.put("tel",userInformationBean.getTel());
        }
        if(avatar != null && !avatar.equals("")) {
            values.put("avatar", userInformationBean.getAvatar());
        }
        if(bluetooth != null && !bluetooth.equals("")){
            values.put("bluetooth", userInformationBean.getBluetooth());
        }
       
        return  values;
    }
    public void add (UserInformationBean userInformationBean){
        Log.d("add:","add");

        Log.d("add","dbsuccess");
        ContentValues values = putValues(userInformationBean);

        String createDate = dateFormat.format(new Date());
        values.put("create_date",createDate);
        db.insert(tableName, null, values);

    }

    public void update(UserInformationBean userInformationBean){

        ContentValues values = putValues(userInformationBean);
        String modifyDate = dateFormat.format(new Date());
        values.put("modify_date",modifyDate);
    //範例
//        String[] whereArgs1 = {"#100", b.getStorage_id()};
//        String whereClause1 = DatabaseSchema.TABLE_TALKS.COLUMN_TID + "=? AND " + DatabaseSchema.TABLE_TALKS.COLUMN_STORAGEID + "=?";
//        db.update(DatabaseSchema.TABLE_TALKS.NAME, values1, whereClause1, whereArgs1);
        System.out.println("====================="+userInformationBean.getUserId());
        System.out.println(values);
        db.update(tableName, values,whereClause , new String[]{userInformationBean.getUserId()});
        db.close();
    }
    public  void delete(String blueTooth){

        db.delete(tableName, whereClause,new String[]{blueTooth});
        db.close();
    }
    public Cursor getById(String userId) {
        Cursor cursor = db.query(tableName, null, "user_id = ?", new String[]{userId}, null, null, null);
        cursor.moveToFirst();
        try{
            if(cursor.moveToFirst())
                return cursor;
        }catch (Exception e){
           e.printStackTrace();
        }
        return null;
    } 
    public String getId(String blueTooth) {
        Cursor cursor = db.query(tableName, null, "bluetooth = ?", new String[]{blueTooth}, null, null, null);
        cursor.moveToFirst();
        try{
            return cursor.getString(cursor.getColumnIndex("user_id"));
        }catch (Exception e){
            return null;
        }

    }
    public Cursor searchAll(UserInformationBean userInformationBean){

      
        String userId = userInformationBean.getUserId();
        String password = userInformationBean.getPassword();
        String name = userInformationBean.getName();
        String gender = userInformationBean.getGender();
        String mail = userInformationBean.getMail();
        String profession = userInformationBean.getProfession();
        String blueTooth = userInformationBean.getBluetooth();
        String avatar = userInformationBean.getAvatar();
        String tel = userInformationBean.getTel();
        String[] searchValue = new String[]{userId,password,name,gender,mail,profession,blueTooth,avatar,tel};
        String[] searchColumn = new String[]{column[0],column[1],column[2],column[3],column[4],column[5],column[6],column[7],column[8]};
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
