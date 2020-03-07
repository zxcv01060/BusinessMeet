package tw.com.bussinessmeet;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectDataBase {
    public void connect(){
        Log.d("test","connectstart");
        String driverName = "net.sourceforge.jtds.jdbc.Driver";
        String dbURL = "jdbc:jtds:sqlserver://192.168.43.67:1433/business_meet;charset=utf8";
        String userName = "10646012";
        String userPwd = "12345";
        Connection dbConn;
        try{
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
            testConnection(dbConn);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void testConnection(Connection con) throws  Exception{
        try{
            String sql = "select * from dbo.user_information";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                Log.e("&&&&&", rs.getString("name"));
                Log.e("&&&&&", rs.getString("blue_tooth"));
            }
            rs.close();
            stmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
