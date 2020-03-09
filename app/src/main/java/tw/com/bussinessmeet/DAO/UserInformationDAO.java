package tw.com.bussinessmeet.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import tw.com.bussinessmeet.Bean.UserInformationBean;
import tw.com.bussinessmeet.helper.DBHelper;

public class UserInformationDAO {
    private String whereClause = "blue_tooth = ?";
    private String tableName = "User_Information";
    private SQLiteDatabase db ;
    public UserInformationDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    public void add (UserInformationBean userInformationBean){
        Log.d("add:","add");

        Log.d("add","dbsuccess");
        ContentValues values = new ContentValues();
        values.put("blue_tooth", userInformationBean.getBlueTooth());
        values.put("user_name", userInformationBean.getUserName());
        values.put("company",userInformationBean.getCompany());
        values.put("position", userInformationBean.getPosition());
        values.put("avatar", userInformationBean.getAvatar());
        db.insert(tableName, null, values);
        db.close();
    }
    public void update(UserInformationBean userInformationBean){

        ContentValues values = new ContentValues();
        values.put("blue_tooth", userInformationBean.getBlueTooth());
        values.put("user_name", userInformationBean.getUserName());
        values.put("company",userInformationBean.getCompany());
        values.put("position", userInformationBean.getPosition());
        values.put("avatar", userInformationBean.getAvatar());
        db.close();
    //範例
//        String[] whereArgs1 = {"#100", b.getStorage_id()};
//        String whereClause1 = DatabaseSchema.TABLE_TALKS.COLUMN_TID + "=? AND " + DatabaseSchema.TABLE_TALKS.COLUMN_STORAGEID + "=?";
//        db.update(DatabaseSchema.TABLE_TALKS.NAME, values1, whereClause1, whereArgs1);
        db.update(tableName, values,whereClause , new String[]{userInformationBean.getBlueTooth()});
    }
    public  void delete(String blueTooth){

        db.delete(tableName, whereClause,new String[]{blueTooth});
        db.close();
    }
    public Cursor searchAll(UserInformationBean userInformationBean){

        String blueTooth = userInformationBean.getBlueTooth();
        String userName = userInformationBean.getUserName();
        String company = userInformationBean.getCompany();
        String position = userInformationBean.getPosition();
        String where = " ";
        String[] args = new String[]{};
        String[] column = new String[]{"blue_tooth", "user_name", "company", "position"};
        if(blueTooth != null && !blueTooth.equals(" ")){
            where += "blue_tooth = ?";
            args[args.length] = userInformationBean.getBlueTooth();
        }
        if(userName != null && !userName.equals((""))){
            if(!where.equals(" "))where += " and ";
            where += "user_name = ?";
            args[args.length] = userInformationBean.getUserName();
        }
        if(company != null && !company.equals(" ")){
            if(!where.equals(" "))where += " and ";
            where += "company = ?";
            args[args.length] = userInformationBean.getCompany();
        }
        if(position != null && !position.equals(" ")){
            if(!where.equals(" "))where += " and ";
            where += "position = ?";
            args[args.length] = userInformationBean.getPosition();
        }
        Cursor cursor = db.query(tableName, column, where,args,null,null,null);
        db.close();
        return cursor;
    }
}
